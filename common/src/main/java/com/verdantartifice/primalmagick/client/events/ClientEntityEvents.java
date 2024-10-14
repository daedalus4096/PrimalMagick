package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Respond to client-side Forge entity events.
 * 
 * @author Daedalus4096
 */
public class ClientEntityEvents {
    public static void onEntityJoinLevel(Entity entity, Level level) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && entity instanceof Player player && player.getId() == mc.player.getId()) {
            // Update client lexicons with tag-delineated book data
            LexiconLoader.getInstance().updateWithTagData(level.registryAccess());
        }
    }
}
