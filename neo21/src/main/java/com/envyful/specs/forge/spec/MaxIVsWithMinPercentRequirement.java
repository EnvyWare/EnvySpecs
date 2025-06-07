package com.envyful.specs.forge.spec;

import com.envyful.api.type.Pair;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaxIVsWithMinPercentRequirement extends AbstractPokemonRequirement<Pair<Integer, Double>> {

    private static final Set<String> KEYS = Sets.newHashSet(
            "maxivandpercent", "maxiv%percent", "iv31&percent"
    );

    protected int maxIVCount;
    protected double minPercent;

    public MaxIVsWithMinPercentRequirement() {
        super(KEYS);
    }

    public MaxIVsWithMinPercentRequirement(int maxIVCount, double minPercent) {
        this();

        this.maxIVCount = maxIVCount;
        this.minPercent = minPercent;
    }

    @Override
    public ParseAttempt<List<Requirement<Pokemon, PixelmonEntity, ?>>> create(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return ParseAttempt.error("No key found for " + key  + " in spec " + spec);
        }

        spec = spec.replace(key + ":", "");
        String[] args = spec.split("-");

        if (args.length != 2) {
            return ParseAttempt.error("Not enough data supplied for random iv generation");
        }

        int maxIVCount = UtilParse.parseInteger(args[0]).orElse(-1);

        if (maxIVCount < 0 || maxIVCount > 6) {
            return ParseAttempt.error("Invalid max IV count. Must be between 0 and 6");
        }

        double minPercent = UtilParse.parseDouble(args[1]).orElse(-1.0);

        if (minPercent < 0 || minPercent > 100 || !this.isValidPercentage(maxIVCount, minPercent)) {
            return ParseAttempt.error("Invalid percentage given");
        }

        return this.createInstance(Pair.of(maxIVCount, minPercent))
                .map(Collections::singletonList);
    }

    private boolean isValidPercentage(int maxIVCount, double minPercent) {
        switch (maxIVCount) {
            case 0:
                return minPercent <= 95;
            case 1:
                return minPercent >= 16 && minPercent <= 96;
            case 2:
                return minPercent >= 33 && minPercent <= 96;
            case 4:
                return minPercent >= 50 && minPercent <= 97;
            case 5:
                return minPercent >= 66 && minPercent <= 97;
            case 6:
                return minPercent >= 83 && minPercent <= 98;
        }

        return false;
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Pair<Integer, Double>>> createInstance(Pair<Integer, Double> pair) {
        return ParseAttempt.success(new MaxIVsWithMinPercentRequirement(pair.getKey(), pair.getValue()));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        int maxIVCount = this.getMaxIVCount(pokemon);

        if (maxIVCount < this.maxIVCount) {
            return false;
        }

        return pokemon.getIVs().getPercentage(2) > this.minPercent;
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
        this.calculateIVs().whenCompleteAsync((ivs, throwable) -> {
            pokemon.getIVs().fillFromArray(ivs);
        }, ServerLifecycleHooks.getCurrentServer());
    }

    private CompletableFuture<int[]> calculateIVs() {
        return CompletableFuture.supplyAsync(() -> {
            int[] ivs = new int[6];
            int maxIVCount = 0;
            List<Integer> slots = IntStream.range(0, 6).boxed().collect(Collectors.toList());

            for (int i = 0; i < 6; i++) {
                int slot = RandomHelper.getRandomElementFromCollection(slots);
                slots.remove(slot);

                if (maxIVCount < this.maxIVCount) {
                    ivs[slot] = 31;
                    ++maxIVCount;
                    continue;
                }

                ivs[slot] = RandomHelper.getRandomNumberBetween(0, 30);
            }

            while (this.calculateIVPercentage(ivs) < this.minPercent) {
                slots = IntStream.range(0, 6).boxed().collect(Collectors.toList());

                for (int i = 0; i < 6; i++) {
                    int slot = RandomHelper.getRandomElementFromCollection(slots);
                    slots.remove(slot);

                    if (ivs[slot] == 31) {
                        continue;
                    }

                    ivs[slot] = RandomHelper.getRandomNumberBetween(0, 30);
                }
            }

            return ivs;
        });
    }

    private int calculateIVPercentage(int[] ivs) {
        return Math.round(((ivs[0] + ivs[1] + ivs[2] + ivs[3] + ivs[4] + ivs[5]) / 186.00F) * 100.00F);
    }

    @Override
    public Pair<Integer, Double> getValue() {
        return Pair.of(this.maxIVCount, this.minPercent);
    }
}
