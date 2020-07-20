package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Special definition for a wand assembly recipe.
 * 
 * @author Daedalus4096
 */
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
        
        // Make sure the crafting inventory has a core, a gem, and two identical caps
        return !coreStack.isEmpty() && ((coreStack.getItem() instanceof WandCoreItem) || (coreStack.getItem() instanceof StaffCoreItem)) &&
               !gemStack.isEmpty() && (gemStack.getItem() instanceof WandGemItem) &&
               !capStack1.isEmpty() && (capStack1.getItem() instanceof WandCapItem) &&
               !capStack2.isEmpty() && capStack1.isItemEqual(capStack2);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack coreStack = inv.getStackInSlot(0);
        ItemStack gemStack = inv.getStackInSlot(1);
        ItemStack capStack = inv.getStackInSlot(2);
        
        ItemStack outputStack = (coreStack.getItem() instanceof StaffCoreItem) ? 
                new ItemStack(ItemsPM.MODULAR_STAFF.get()) : 
                new ItemStack(ItemsPM.MODULAR_WAND.get());
        ModularWandItem outputItem = (ModularWandItem)outputStack.getItem();
        
        // Set the components of the modular wand/staff
        if (coreStack.getItem() instanceof StaffCoreItem) {
            outputItem.setWandCore(outputStack, ((StaffCoreItem)coreStack.getItem()).getWandCore());
        } else {
            outputItem.setWandCore(outputStack, ((WandCoreItem)coreStack.getItem()).getWandCore());
        }
        outputItem.setWandGem(outputStack, ((WandGemItem)gemStack.getItem()).getWandGem());
        outputItem.setWandCap(outputStack, ((WandCapItem)capStack.getItem()).getWandCap());
        
        return outputStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL.get();
    }
}
