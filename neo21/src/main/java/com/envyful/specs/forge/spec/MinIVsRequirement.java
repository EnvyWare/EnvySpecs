package com.envyful.specs.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MinIVsRequirement extends AbstractIntegerPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet(
            "miniv", "minivpercent"
    );

    public MinIVsRequirement() {
        super(KEYS, 50);
    }

    public MinIVsRequirement(int value) {
        super(KEYS, 50, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Integer>> createInstance(Integer integer) {
        return ParseAttempt.success(new MinIVsRequirement(integer));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return pokemon.getIVs().getPercentage(1) >= this.value;
    }

    @Override
    public void applyData(Pokemon pokemon) {
        this.calculateIVs().whenComplete((ivs, error) -> pokemon.getIVs().fillFromArray(ivs));
    }

    private CompletableFuture<int[]> calculateIVs() {
        if (this.value == 100) {
            return CompletableFuture.completedFuture(new int[] {31, 31, 31, 31, 31, 31});
        }

        return CompletableFuture.supplyAsync(() -> {
            int[] ivs = new int[6];

            for (int i = 0; i < 5; i++) {
                ivs[i] = (int) (31 * (this.value / 100.00));
            }

            while (this.calculateIVPercentage(ivs) <= this.value) {
                int slot = UtilRandom.randomInteger(0, 5);

                if (ivs[slot] == 31) {
                    continue;
                }

                ivs[slot]++;
            }

            return ivs;
        });
    }

    private int calculateIVPercentage(int[] ivs) {
        return Math.round(((ivs[0] + ivs[1] + ivs[2] + ivs[3] + ivs[4] + ivs[5]) / 186.00F) * 100.00F);
    }
}
