package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-side Forge entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=Constants.MOD_ID, value=Dist.CLIENT)
public class ClientEntityEvents {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && event.getEntity() instanceof Player player && player.getId() == mc.player.getId()) {
            // Update client lexicons with tag-delineated book data
            LexiconLoader.getInstance().updateWithTagData(event.getLevel().registryAccess());
        }
    }
}
