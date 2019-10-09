package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WandAssemblyRecipe extends SpecialRecipe {
    public WandAssemblyRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack coreStack = inv.getStackInSlot(0);
        ItemStack gemStack = inv.getStackInSlot(1);
        ItemStack capStack1 = inv.getStackInSlot(2);
        ItemStack capStack2 = inv.getStackInSlot(3);
        
        return !coreStack.isEmpty() && (coreStack.getItem() instanceof WandCoreItem) &&
               !gemStack.isEmpty() && (gemStack.getItem() instanceof WandGemItem) &&
               !capStack1.isEmpty() && (capStack1.getItem() instanceof WandCapItem) &&
               !capStack2.isEmpty() && capStack1.isItemEqual(capStack2);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack coreStack = inv.getStackInSlot(0);
        ItemStack gemStack = inv.getStackInSlot(1);
        ItemStack capStack = inv.getStackInSlot(2);
        ItemStack wandStack = new ItemStack(ItemsPM.MODULAR_WAND);
        ModularWandItem wandItem = (ModularWandItem)wandStack.getItem();
        
        wandItem.setWandCore(wandStack, ((WandCoreItem)coreStack.getItem()).getWandCore());
        wandItem.setWandGem(wandStack, ((WandGemItem)gemStack.getItem()).getWandGem());
        wandItem.setWandCap(wandStack, ((WandCapItem)capStack.getItem()).getWandCap());
        
        return wandStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL;
    }
}
