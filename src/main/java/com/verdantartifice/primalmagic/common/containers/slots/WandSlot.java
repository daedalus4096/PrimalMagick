package com.verdantartifice.primalmagic.common.containers.slots;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.IStaff;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Custom GUI slot for wand inputs.
 * 
 * @author Daedalus4096
 */
public class WandSlot extends Slot {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_slot");
    
    protected final boolean allowStaves;
    
    @SuppressWarnings("deprecation")
    public WandSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean allowStaves) {
        super(inventoryIn, index, xPosition, yPosition);
        this.setBackground(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE);
        this.allowStaves = allowStaves;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        // Only allow wands or, if allowed, staves to be dropped into the slot
        return (stack.getItem() instanceof IWand) && (this.allowStaves || !(stack.getItem() instanceof IStaff));
    }
}
