package com.verdantartifice.primalmagick.mixin;

import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepairItemRecipe.class)
public abstract class RepairItemRecipeMixin extends CustomRecipe {
    private RepairItemRecipeMixin(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Inject(method = "getItemsToCombine", at = @At("RETURN"), cancellable = true)
    protected void onGetItemsToCombine(CallbackInfoReturnable<Pair<ItemStack, ItemStack>> cir) {
        // If the discovered items are irreparable, then override the method output to null to disable repairs
        Pair<ItemStack, ItemStack> pair = cir.getReturnValue();
        if (pair != null && (pair.getFirst().is(ItemTagsPM.NO_REPAIR) || pair.getSecond().is(ItemTagsPM.NO_REPAIR))) {
            cir.setReturnValue(null);
        }
    }
}
