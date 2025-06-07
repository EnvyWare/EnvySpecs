package com.envyful.specs.forge.spec;

import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class AmIBossRequirement extends AbstractBooleanPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet("amiboss");

    public AmIBossRequirement() {
        super(KEYS);
    }

    public AmIBossRequirement(boolean value) {
        super(KEYS, value);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Boolean>> createInstance(Boolean aBoolean) {
        return ParseAttempt.success(new AmIBossRequirement(aBoolean));
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        return false;
    }

    @Override
    public boolean isMinecraftMatch(PixelmonEntity entityPixelmon) {
        return entityPixelmon.isBossPokemon();
    }

    @Override
    public void applyData(Pokemon pokemon) {

    }

    @Override
    public void applyMinecraft(PixelmonEntity entityPixelmon) {
        entityPixelmon.setBossTier(BossTierRegistry.getRandomBossTier());
    }
}
