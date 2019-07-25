/*
 * Copyright (C) Sportradar AG. See LICENSE for full license governing this code
 */

package com.sportradar.mts.sdk.impl.libs.receivers;

import com.sportradar.mts.sdk.api.TicketNonSrSettleResponse;
import com.sportradar.mts.sdk.api.impl.mtsdto.ticketnonsrsettle.TicketNonSrSettleResponseSchema;
import com.sportradar.mts.sdk.api.utils.JsonUtils;
import com.sportradar.mts.sdk.api.utils.MtsDtoMapper;
import com.sportradar.mts.sdk.impl.libs.adapters.amqp.AmqpConsumer;
import com.sportradar.mts.sdk.impl.libs.adapters.amqp.AmqpMessageReceiver;
import com.sportradar.mts.sdk.impl.libs.adapters.amqp.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class AmqpTicketNonSrSettleResponseReceiverImpl implements AmqpMessageReceiver {
    private static final Logger logger = LoggerFactory.getLogger(AmqpTicketCashoutResponseReceiverImpl.class);
    private final Object stateLock = new Object();
    private final AmqpConsumer consumer;
    private final TicketNonSrSettleResponseReceiver ticketNonSrSettleResponseReceiver;
    private boolean opened;


    public AmqpTicketNonSrSettleResponseReceiverImpl(AmqpConsumer consumer,
                                                 TicketNonSrSettleResponseReceiver ticketNonSrSettleResponseReceiver) {
        checkNotNull(consumer, "consumer cannot be null");
        checkNotNull(ticketNonSrSettleResponseReceiver, "ticketCashoutResponseReceiver cannot be null");

        this.consumer = consumer;
        this.ticketNonSrSettleResponseReceiver = ticketNonSrSettleResponseReceiver;

        consumer.setMessageReceivedHandler(this);
    }

    @Override
    public MessageStatus consume(byte[] msg,
                                 String routingKey,
                                 String correlationId,
                                 Map<String, Object> messageHeaders) {

        String msgBody = new String(msg);
        if(logger.isDebugEnabled())
        {
            logger.debug("received ticket response with correlation id: {}, msg: {}, headers: {}", correlationId, msgBody, messageHeaders);
        }
        else {
            logger.info("received ticket response with correlation id: {}", correlationId);
        }

        TicketNonSrSettleResponse ticketNonSrSettleResponse = null;
        try {
            TicketNonSrSettleResponseSchema ticketSchema = JsonUtils.deserialize(msg, TicketNonSrSettleResponseSchema.class);
            ticketNonSrSettleResponse = MtsDtoMapper.map(ticketSchema, correlationId, messageHeaders, msgBody);
        }
        catch (IOException e) {
            logger.error("failed to deserialize ticket non-sportaradar settle response! msg: {}", msgBody, e);
        }
        catch(Exception e)
        {
            logger.error("failed to map ticket non-sportaradar settle response! msg: {}", msgBody, e);
        }

        if (ticketNonSrSettleResponse != null) {
            ticketNonSrSettleResponseReceiver.setTicketNonSrSettleResponse(ticketNonSrSettleResponse);
        }

        return MessageStatus.CONSUMED_SUCCESSFULLY;
    }

    @Override
    public void afterLimitReached(byte[] msg, String routingKey, String correlationId) {
        logger.error("ticket non-sportaradar settle response consume retry reached! msg : {}", new String(msg, StandardCharsets.UTF_8));
    }

    @Override
    public void open() {
        synchronized (stateLock) {
            if (!opened) {
                consumer.open();
                opened = true;
            }
        }
    }

    @Override
    public void close() {
        synchronized (stateLock) {
            if (opened) {
                consumer.close();
                opened = false;
            }
        }
    }

    @Override
    public boolean isOpen() {
        return opened;
    }
}