A MTS SDK Java library

CHANGE LOG:
2019-05-30 2.3.1.0
Support for custom bet
Added getCustomBetManager to MtsSdkApi
Exposed custom bet fields on Ticket

2019-05-10 2.3.0.0
Support for ticket version 2.3
Support for non-Sportradar ticket settlement
Added lastMatchEndTime to Ticket and TicketBuilder
Added TicketNonSrSettle and TicketNonSrSettleBuilder

2019-02-27 1.8.0.0
Added support for Client API - added method getClientApi on MtsSdkApi
Added configuration for ticket, ticket cancellation and ticket cashout message timeouts

2019-02-07 1.7.0.0
Adding acking on consumers message processed
Added AutoAcceptedOdds to ITicketResponse
Added AdditionalInfo to all ticket responses
Fix: settings corrected for sending ticket cancel and reoffer cancel message
Fix: default langId in EndConsumer to null

2018-11-28 1.6.0.0
Support for ticket version 2.2
Added AutoAcceptedOdd to TicketResponse
Added TotalCombinations to Ticket and TicketBuilder
Added BetCashout to TicketCashout - support for partial cashout
Added BetCancel to TicketCancel - support for partial cancellation
Removed deletion of consumer queues on close
Reviewed and updated documentation and properties files

2018-10-05 1.5.0.0
Added 'exclusiveConsumer' property to the configuration, indicating should the rabbit consumer channel be exclusive (default is true)
Added YAML config support
Added Ticket response timeout callback (Ticket, TicketCancel, TicketCashout) to notify user if the ticket response did not arrive in timely fashion (when sending in non-blocking mode)
Added timeout when fetching MarketDescriptions from API fails (30s)
Improved handling and logging for market description (for UF markets)
Fix: BetBonus value condition - if set, must be greater then zero
Minor fixes and improvements

2018-03-26 1.4.0.0
SenderBuilder.confidence changed from long to Long
RabbitMQ TLS validation
Exposure of ticket JSON payloads trough SdkTicket.getJsonValue
Various small fixes and improvements

2018-01-17 1.3.0.0
support for ticket and ticket response v2.1:
- selection id max length increased to 1000
- bet bonus decreased to min 0
- deprecated response internal message
- added rejection info to ticket response selection
- added response additional info
- bet id is now nullable
- specific sender channel validation removed
- end customer can now have all properties nullable
- sum of wins is now nullable

2017-12-21 1.2.1.0
Fix: TicketBuilder for multi-bet tickets with same selections but different odds or different banker value
Fix: TicketReofferBuilder

2017-11-16 1.2.0.0
Added support for setting port to SdkConfigurationBuilder

2017-10-19 1.1.5.0
Added SdkConfigurationBuilder for building SdkConfiguration

2017-10-16 1.1.4.0
Sender.Currency property updated to support also 4-letter sign (i.e. mBTC)

2017-09-13 1.1.3.0
Added new settings property 'provideAdditionalMarketSpecifiers'
Fix: building selection id with UF specifiers
Fix: removed precondition check for selectionDetails in ticket response
Fix: removed requirement for EndCustomer.Id for Terminal and Retail sender channel

2017-08-28 1.1.2.0
Exposed property CorrelationId in all tickets and ticket responses
Refined logging within sdk (for feed and rest traffic)
Property SelectionDetails on ticket response changed to optional
Updated example with new BuilderFactory (available on MtsSdk instance)
Internal: 'selectionRef:[]' removed from json when empty
Internal: added ConsumerTag to consumer channels

2017-07-10 1.1.1.1
Sender requirements - "terminal" channel endCustomer NPE fix

2017-07-06 1.1.1.0
Logback logging framework removed from the SDK, so any logging framework can now be used
Sender requirements updated
Sender channel phone removed
Selection event id type changed from long to String
AMQP message delivery_mode changed from 1 to 2
Minor bug fixes

2017-05-15 1.1.0.0
Added ticket reoffer support
Added cashout ticket support
Minor bug fixes

2017-04-06 1.0.0.0-beta
Initial release (supports MTS tickets version 2.0)
