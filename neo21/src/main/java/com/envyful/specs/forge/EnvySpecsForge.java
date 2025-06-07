package com.envyful.specs.forge;

import com.envyful.specs.forge.listener.GuaranteedCatchListener;
import com.envyful.specs.forge.listener.UndeletableListener;
import com.envyful.specs.forge.spec.*;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.init.PixelmonInitEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod("envyspecs")
public class EnvySpecsForge {

    public EnvySpecsForge() {
        Pixelmon.EVENT_BUS.register(new UndeletableListener());
        Pixelmon.EVENT_BUS.register(new GuaranteedCatchListener());
        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSetupEvent(PixelmonInitEvent event) {
        PokemonSpecificationProxy.register(new AmIBossRequirement());
        PokemonSpecificationProxy.register(new MaxIVsWithMinPercentRequirement());
        PokemonSpecificationProxy.register(new MinIVsRequirement());
        PokemonSpecificationProxy.register(new NumIVsRequirement());
        PokemonSpecificationProxy.register(new UndeletableRequirement());
        PokemonSpecificationProxy.register(new UncatchableRequirement());
        PokemonSpecificationProxy.register(new GuaranteedCatchRequirement());
        PokemonSpecificationProxy.register(new LevelHigherThanRequirement());
        PokemonSpecificationProxy.register(new LevelHigherThanEqualToRequirement());
        PokemonSpecificationProxy.register(new LevelLessThanRequirement());
        PokemonSpecificationProxy.register(new LevelLessThanEqualToRequirement());
        PokemonSpecificationProxy.register(new AnySpeciesRequirement());
        PokemonSpecificationProxy.register(new RandomMegaCapableSpeciesRequirement());
    }
}