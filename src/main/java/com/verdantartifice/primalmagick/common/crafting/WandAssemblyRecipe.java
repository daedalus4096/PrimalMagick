package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special definition for a wand assembly recipe.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyRecipe extends CustomRecipe {
    public WandAssemblyRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack coreStack = inv.getItem(0);
        ItemStack gemStack = inv.getItem(1);
        ItemStack capStack1 = inv.getItem(2);
        ItemStack capStack2 = inv.getItem(3);
        
        // Make sure the crafting inventory has a core, a gem, and two identical caps
        return !coreStack.isEmpty() && ((coreStack.getItem() instanceof WandCoreItem) || (coreStack.getItem() instanceof StaffCoreItem)) &&
               !gemStack.isEmpty() && (gemStack.getItem() instanceof WandGemItem) &&
               !capStack1.isEmpty() && (capStack1.getItem() instanceof WandCapItem) &&
               !capStack2.isEmpty() && ItemStack.isSameItem(capStack1, capStack2);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack coreStack = inv.getItem(0);
        ItemStack gemStack = inv.getItem(1);
        ItemStack capStack = inv.getItem(2);
        
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
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL.get();
    }
}
