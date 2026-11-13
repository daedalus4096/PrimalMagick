package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IHasWandComponents {
    static ItemStackTemplate createTemplate(@Nonnull Item item, @Nonnull WandCore core, @Nonnull WandCap cap, @Nonnull WandGem gem) {
        DataComponentPatch componentPatch = DataComponentPatch.builder()
                .set(DataComponentsPM.WAND_CORE.get(), core)
                .set(DataComponentsPM.WAND_CAP.get(), cap)
                .set(DataComponentsPM.WAND_GEM.get(), gem)
                .set(DataComponents.RARITY, calculateRarity(core, cap, gem))
                .set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(gem.getCapacity()))
                .build();
        return new ItemStackTemplate(item, componentPatch);
    }

    static Rarity calculateRarity(@Nullable WandCore core, @Nullable WandCap cap, @Nullable WandGem gem) {
        // A modular wand's unenchanted rarity is the highest of those of its components
        Rarity retVal = Rarity.COMMON;
        if (core != null && core.getRarity().compareTo(retVal) > 0) {
            retVal = core.getRarity();
        }
        if (cap != null && cap.getRarity().compareTo(retVal) > 0) {
            retVal = cap.getRarity();
        }
        if (gem != null && gem.getRarity().compareTo(retVal) > 0) {
            retVal = gem.getRarity();
        }
        return retVal;
    }

    @Nullable
    WandCore getWandCore(@Nonnull ItemStack stack);

    void setWandCore(@Nonnull ItemStack stack, @Nonnull WandCore core);

    @Nullable
    WandCore getWandCoreAppearance(@Nonnull ItemStack stack);

    void setWandCoreAppearance(@Nonnull ItemStack stack, @Nullable WandCore core);

    @Nullable
    WandCap getWandCap(@Nonnull ItemStack stack);

    void setWandCap(@Nonnull ItemStack stack, @Nonnull WandCap cap);

    @Nullable
    WandCap getWandCapAppearance(@Nonnull ItemStack stack);

    void setWandCapAppearance(@Nonnull ItemStack stack, @Nullable WandCap cap);

    @Nullable
    WandGem getWandGem(@Nonnull ItemStack stack);

    void setWandGem(@Nonnull ItemStack stack, @Nonnull WandGem gem);

    @Nullable
    WandGem getWandGemAppearance(@Nonnull ItemStack stack);

    void setWandGemAppearance(@Nonnull ItemStack stack, @Nullable WandGem gem);
}
