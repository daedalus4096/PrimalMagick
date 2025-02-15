package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IHasWandComponents {
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
