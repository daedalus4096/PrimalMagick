package com.verdantartifice.primalmagic.common.theorycrafting;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.util.InventoryUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;

/**
 * Definition of a project material that requires a specific item stack, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "item";
    
    protected ItemStack stack;
    protected boolean consumed;
    protected boolean matchNBT;
    
    public ItemProjectMaterial() {
        super();
        this.stack = ItemStack.EMPTY;
        this.consumed = false;
        this.matchNBT = false;
    }
    
    public ItemProjectMaterial(@Nonnull ItemStack stack, boolean consumed, boolean matchNBT) {
        super();
        this.stack = stack;
        this.consumed = consumed;
        this.matchNBT = matchNBT;
    }
    
    public ItemProjectMaterial(@Nonnull ItemStack stack, boolean consumed) {
        this(stack, consumed, false);
    }
    
    public ItemProjectMaterial(@Nonnull IItemProvider item, boolean consumed, boolean matchNBT) {
        this(new ItemStack(item), consumed, matchNBT);
    }
    
    public ItemProjectMaterial(@Nonnull IItemProvider item, boolean consumed) {
        this(item, consumed, false);
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.put("Stack", this.stack.write(new CompoundNBT()));
        tag.putBoolean("Consumed", this.consumed);
        tag.putBoolean("MatchNBT", this.matchNBT);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.stack = ItemStack.read(nbt.getCompound("Stack"));
        this.consumed = nbt.getBoolean("Consumed");
        this.matchNBT = nbt.getBoolean("MatchNBT");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        return InventoryUtils.isPlayerCarrying(player, this.stack, this.matchNBT);
    }

    @Override
    public boolean consume(PlayerEntity player) {
        // Remove this material's item from the player's inventory if it's supposed to be consumed
        if (this.consumed) {
            return InventoryUtils.consumeItem(player, this.stack, this.matchNBT);
        } else {
            return true;
        }
    }
    
    @Nonnull
    public ItemStack getItemStack() {
        return this.stack;
    }
    
    @Override
    public boolean isConsumed() {
        return this.consumed;
    }
    
    @Override
    public AbstractProjectMaterial copy() {
        ItemProjectMaterial material = new ItemProjectMaterial();
        material.stack = this.stack.copy();
        material.consumed = this.consumed;
        material.selected = this.selected;
        material.matchNBT = this.matchNBT;
        return material;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.consumed ? 1231 : 1237);
        result = prime * result + (this.selected ? 1231 : 1237);
        result = prime * result + (this.matchNBT ? 1231 : 1237);
        result = prime * result + ((this.stack == null) ? 0 : this.stack.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemProjectMaterial other = (ItemProjectMaterial) obj;
        if (this.consumed != other.consumed) {
            return false;
        }
        if (this.selected != other.selected) {
            return false;
        }
        if (this.matchNBT != other.matchNBT) {
            return false;
        }
        if (this.stack == null) {
            if (other.stack != null) {
                return false;
            }
        } else if (!ItemStack.areItemStacksEqual(this.stack, other.stack)) {
            return false;
        }
        return true;
    }
}
