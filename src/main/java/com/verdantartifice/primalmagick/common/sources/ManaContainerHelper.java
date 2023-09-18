package com.verdantartifice.primalmagick.common.sources;

import java.util.List;

import com.verdantartifice.primalmagick.client.util.ClientUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        CompoundTag nbt = stack.getTagElement("ManaContainerTag");
        if (nbt != null) {
            SourceList mana = SourceList.deserializeNBT(nbt);
            Source.SORTED_SOURCES.stream().filter(source -> source.isDiscovered(player) && mana.getAmount(source) > 0).map(source ->
                Component.translatable("tooltip.primalmagick.source.mana_container", source.getNameText(), (mana.getAmount(source) / 100.0D))
            ).forEach(tooltip::add);
        }
    }
    
    public static void setManaOnPlace(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof IManaContainer manaTile) {
            CompoundTag nbt = stack.getTagElement("ManaContainerTag");
            if (nbt != null) {
                manaTile.setMana(SourceList.deserializeNBT(nbt));
            }
        }
    }
}
