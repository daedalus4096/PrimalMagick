package com.verdantartifice.primalmagick.common.sources;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Static class containing helper methods to assist with common mana container tasks.
 * 
 * @author Daedalus4096
 */
public class ManaContainerHelper {
    public static void appendHoverText(ItemStack stack, List<Component> tooltip) {
        CompoundTag nbt = stack.getTagElement("ManaContainerTag");
        if (nbt != null) {
            SourceList mana = new SourceList();
            mana.deserializeNBT(nbt);
            Source.SORTED_SOURCES.stream().filter(source -> mana.getAmount(source) > 0).forEach(source ->
                tooltip.add(Component.translatable("tooltip.primalmagick.source.mana_container", source.getNameText(), (mana.getAmount(source) / 100.0D)))
            );
        }
    }
    
    public static void setManaOnPlace(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof IManaContainer manaTile) {
            CompoundTag nbt = stack.getTagElement("ManaContainerTag");
            if (nbt != null) {
                SourceList mana = new SourceList();
                mana.deserializeNBT(nbt);
                manaTile.setMana(mana);
            }
        }
    }
}
