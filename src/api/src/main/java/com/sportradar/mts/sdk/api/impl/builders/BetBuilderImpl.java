/*
 * Copyright (C) Sportradar AG. See LICENSE for full license governing this code
 */

package com.sportradar.mts.sdk.api.impl.builders;

import com.google.common.collect.Lists;
import com.sportradar.mts.sdk.api.*;
import com.sportradar.mts.sdk.api.builders.BetBuilder;
import com.sportradar.mts.sdk.api.enums.*;
import com.sportradar.mts.sdk.api.impl.BetBonusImpl;
import com.sportradar.mts.sdk.api.impl.BetFreeStakeImpl;
import com.sportradar.mts.sdk.api.impl.BetImpl;
import com.sportradar.mts.sdk.api.impl.StakeImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BetBuilderImpl implements BetBuilder {
    private String betId;
    private BetBonus betBonus;
    private BetFreeStake freeStake;
    private Stake stake;
    private Stake entireStake;
    private List<Integer> selectedSystems;
    private String reofferId;
    private Long sum;
    private List<Selection> selections;
    private Boolean customBet;
    private Integer calculationOdds;

    @Override
    public BetBuilder setBetBonus(long value, BetBonusMode betBonusMode, BetBonusType betBonusType, BetBonusDescription betBonusDescription, BetBonusPaidAs betBonusPaidAs) {
        betBonus = new BetBonusImpl(value, betBonusMode, betBonusType, betBonusDescription, betBonusPaidAs);
        return this;
    }

    @Override
    public BetBuilder setBetBonus(long value) {
        betBonus = new BetBonusImpl(value, BetBonusMode.ALL, BetBonusType.TOTAL, BetBonusDescription.ACCUMULATOR_BONUS, BetBonusPaidAs.CASH);
        return this;
    }

    @Override
    public BetBuilder setBetFreeStake(long value, BetFreeStakeType betFreeStakeType, BetFreeStakeDescription betFreeStakeDescription, BetFreeStakePaidAs betFreeStakePaidAs) {
        freeStake = new BetFreeStakeImpl(value, betFreeStakeType, betFreeStakeDescription, betFreeStakePaidAs);
        return this;
    }

    @Override
    public BetBuilder setStake(long value, StakeType stakeType) {
        stake = new StakeImpl(value, stakeType);
        return this;
    }

    @Override
    public BetBuilder setEntireStake(long value, StakeType stakeType) {
        entireStake = new StakeImpl(value, stakeType);
        return this;
    }

    @Override
    public BetBuilder setBetId(String id) {
        betId = id;
        return this;
    }

    @Override
    public BetBuilder addSelectedSystem(int systemId) {
        if(selectedSystems == null)
        {
            selectedSystems = Lists.newArrayList();
        }
        selectedSystems.add(systemId);
        return this;
    }

    @Override
    public List<Integer> getSelectedSystems() {
        return selectedSystems;
    }

    @Override
    public BetBuilder setReofferId(String reofferId) {
        this.reofferId = reofferId;
        return this;
    }

    @Override
    public BetBuilder setSumOfWins(Long sum) {
       this.sum = sum;
       return this;
    }

    @Override
    public BetBuilder addSelection(Selection selection) {
        if(selections == null)
        {
            selections = Lists.newArrayList();
        }
        Optional<Selection> similarSel =
                selections.stream()
                        .filter(f -> f.getEventId().equals(selection.getEventId()) && f.getId().equals(selection.getId()))
                        .findFirst();
        if (similarSel.isPresent())
        {
            if (similarSel.get().getOdds().equals(selection.getOdds()) &&
                    Objects.equals(similarSel.get().getBoostedOdds(), selection.getBoostedOdds()) &&
                    similarSel.get().getIsBanker() == selection.getIsBanker())
            {
                return this;
            }
            throw new IllegalArgumentException("Bet can not have selections with the same eventId, id and different odds or different banker value.");
        }
        selections.add(selection);
        return this;
    }

    @Override
    public List<Selection> getSelections() {
        return selections;
    }

    @Override
    public BetBuilder setCustomBet(Boolean customBet) {
        this.customBet = customBet;
        return this;
    }

    @Override
    public BetBuilder setCalculationOdds(Integer calculationOdds) {
        this.calculationOdds = calculationOdds;
        return this;
    }

    /**
     * Builds bet
     * @return {@link Bet}
     */
    @Override
    public Bet build() {
        return new BetImpl(betId, betBonus, freeStake, stake, entireStake, selectedSystems, selections, reofferId, sum, customBet, calculationOdds);
    }
}
