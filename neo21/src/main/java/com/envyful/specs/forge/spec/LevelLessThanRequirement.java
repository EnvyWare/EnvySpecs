package com.envyful.specs.forge.spec;

import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class LevelLessThanRequirement extends AbstractIntegerPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet(
            "lvllt", "lvllessthan"
    );

    public LevelLessThanRequirement() {
        super(KEYS, 10);
    }

    public LevelLessThanRequirement(int value) {
        super(KEYS, 10, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Integer>> createInstance(Integer integer) {
        return ParseAttempt.success(new LevelLessThanRequirement(integer));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return pokemon.getPokemonLevel() < this.value;
    }

    @Override
    public void applyData(Pokemon pokemon) {

    }
}
