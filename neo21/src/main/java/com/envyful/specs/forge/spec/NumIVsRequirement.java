package com.envyful.specs.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class NumIVsRequirement extends AbstractIntegerPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet(
            "numivs", "perfectivs"
    );

    public NumIVsRequirement() {
        super(KEYS, 2);
    }

    public NumIVsRequirement(int value) {
        super(KEYS, 2, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Integer>> createInstance(Integer integer) {
        return ParseAttempt.success(new NumIVsRequirement(integer));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return this.getMaxIVCount(pokemon) == this.value;
    }

    private int getMaxIVCount(Pokemon pokemon) {
        int counter = 0;
        for (int i : pokemon.getIVs().getArray()) {
            if (i >= 31) {
                ++counter;
            }
        }

        return counter;
    }

    @Override
    public void applyData(Pokemon pokemon) {
        if (this.value < 1) {
            this.value = 1;
        } else if (this.value > 6) {
            this.value = 6;
        }

        Set<BattleStatsType> givenTypes = Sets.newHashSet();

        for (int i = 0; i < this.value; i++) {
            BattleStatsType type = UtilRandom.getRandomElementExcluding(BattleStatsType.EV_IV_STATS, givenTypes.toArray(new BattleStatsType[0]));
            pokemon.getIVs().setStat(type, 31);
            givenTypes.add(type);
        }
    }
}
