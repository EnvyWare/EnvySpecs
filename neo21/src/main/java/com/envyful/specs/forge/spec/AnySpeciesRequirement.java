package com.envyful.specs.forge.spec;

import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class AnySpeciesRequirement extends AbstractBooleanPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet("any");

    public AnySpeciesRequirement() {
        super(KEYS);
    }

    public AnySpeciesRequirement(boolean value) {
        super(KEYS, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Boolean>> createInstance(Boolean aBoolean) {
        return ParseAttempt.success(new AnySpeciesRequirement(aBoolean));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return true;
    }

    @Override
    public boolean isMinecraftMatch(PixelmonEntity entityPixelmon) {
        return true;
    }

    @Override
    public void applyData(Pokemon pokemon) {

    }

    @Override
    public void applyMinecraft(PixelmonEntity entityPixelmon) {

    }
}
