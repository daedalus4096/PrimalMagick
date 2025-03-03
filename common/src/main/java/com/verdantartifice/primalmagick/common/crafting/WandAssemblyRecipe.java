package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
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

    private static ItemStack getItem(CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack1 = getItem(inv, 2);
        ItemStack capStack2 = getItem(inv, 3);
        
        // Make sure the crafting inventory has a core, a gem, and two identical caps
        return !coreStack.isEmpty() && ((coreStack.getItem() instanceof WandCoreItem) || (coreStack.getItem() instanceof StaffCoreItem)) &&
               !gemStack.isEmpty() && (gemStack.getItem() instanceof WandGemItem) &&
               !capStack1.isEmpty() && (capStack1.getItem() instanceof WandCapItem) &&
               !capStack2.isEmpty() && ItemStack.isSameItem(capStack1, capStack2);
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        
        ItemStack outputStack = (coreStack.getItem() instanceof StaffCoreItem) ? 
                new ItemStack(ItemsPM.MODULAR_STAFF.get()) :
                new ItemStack(ItemsPM.MODULAR_WAND.get());
        IHasWandComponents outputItem = (IHasWandComponents)outputStack.getItem();
        
        // Set the components of the modular wand/staff
        if (coreStack.getItem() instanceof StaffCoreItem) {
            outputItem.setWandCore(outputStack, ((StaffCoreItem)coreStack.getItem()).getWandCore());
        } else {
            outputItem.setWandCore(outputStack, ((WandCoreItem)coreStack.getItem()).getWandCore());
        }
        outputItem.setWandCap(outputStack, ((WandCapItem)capStack.getItem()).getWandCap());
        WandGem gem = ((WandGemItem)gemStack.getItem()).getWandGem();
        outputItem.setWandGem(outputStack, gem);
        outputStack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(gem.getCapacity()));

        return outputStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_ASSEMBLY_SPECIAL.get();
    }
}
