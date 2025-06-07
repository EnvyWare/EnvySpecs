package com.envyful.specs.forge.spec;

import com.envyful.specs.forge.Flags;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class GuaranteedCatchRequirement extends AbstractBooleanPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet(
            "guaranteedcatch", "forcecatch"
    );

    public GuaranteedCatchRequirement() {
        super(KEYS);
    }

    public GuaranteedCatchRequirement(boolean value) {
        super(KEYS, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Boolean>> createInstance(Boolean aBoolean) {
        return ParseAttempt.success(new GuaranteedCatchRequirement(aBoolean));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return this.value && pokemon.hasFlag(Flags.GUARANTEED_CATCH);
    }

    @Override
    public void applyData(Pokemon pokemon) {
        if (this.value) {
            pokemon.addFlag(Flags.GUARANTEED_CATCH);
        }
    }
}
