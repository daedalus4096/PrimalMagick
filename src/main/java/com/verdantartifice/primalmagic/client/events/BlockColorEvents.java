package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only block color events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockColorEvents {
    @SubscribeEvent
    public static void onBlockColorInit(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, lightReader, pos, dummy) -> {
            return SaltTrailBlock.colorMultiplier(state.get(SaltTrailBlock.POWER));
        }, BlocksPM.SALT_TRAIL.get());
    }
}
