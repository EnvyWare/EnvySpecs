package com.envyful.specs.forge.listener;

import com.envyful.specs.forge.Flags;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class GuaranteedCatchListener {

    @SubscribeEvent
    public void onCaptureAttempt(CaptureEvent.StartCapture event) {
        if (!event.getPokemon().hasFlag(Flags.GUARANTEED_CATCH)) {
            return;
        }

        event.getCaptureValues().setCatchRate(255);
    }

}
