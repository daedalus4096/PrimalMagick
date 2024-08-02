package com.verdantartifice.primalmagick.common.sources;

import java.util.List;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Static class containing helper methods to assist with common mana container tasks.
 * 
 * @author Daedalus4096
 */
public class ManaContainerHelper {
    public static void appendHoverText(ItemStack stack, List<Component> tooltip) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        ManaStorage cap = stack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        if (cap != null) {
            Sources.getAllSorted().stream().filter(source -> source.isDiscovered(player) && cap.getManaStored(source) > 0).map(source -> {
                return Component.translatable("tooltip.primalmagick.source.mana_container", source.getNameText(), (cap.getManaStored(source) / 100.0D));
            }).forEach(tooltip::add);
        }
    }
}
