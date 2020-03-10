package com.verdantartifice.primalmagic.common.theorycrafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.util.InventoryUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a project material that requires an item stack from a given tag, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemTagProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "tag";
    
    protected ResourceLocation tagName;
    protected int quantity;
    protected boolean consumed;
    
    public ItemTagProjectMaterial() {
        super();
        this.tagName = null;
        this.quantity = -1;
        this.consumed = false;
    }
    
    public ItemTagProjectMaterial(@Nonnull ResourceLocation tagName, int quantity, boolean consumed) {
        super();
        this.tagName = tagName;
        this.quantity = quantity;
        this.consumed = consumed;
    }
    
    public ItemTagProjectMaterial(@Nonnull ResourceLocation tagName, boolean consumed) {
        this(tagName, 1, consumed);
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        if (this.tagName != null) {
            tag.putString("TagName", this.tagName.toString());
        }
        tag.putBoolean("Consumed", this.consumed);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("TagName")) {
            this.tagName = new ResourceLocation(nbt.getString("TagName"));
        } else {
            this.tagName = null;
        }
        this.consumed = nbt.getBoolean("Consumed");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(PlayerEntity player) {
        return InventoryUtils.isPlayerCarrying(player, this.tagName, this.quantity);
    }

    @Override
    public boolean consume(PlayerEntity player) {
        // Remove items matching this material's tag from the player's inventory if it's supposed to be consumed
        if (this.consumed) {
            return InventoryUtils.consumeItem(player, this.tagName, this.quantity);
        } else {
            return true;
        }
    }
    
    @Nullable
    public ResourceLocation getTagName() {
        return this.tagName;
    }
    
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean isConsumed() {
        return this.consumed;
    }

    @Override
    public AbstractProjectMaterial copy() {
        ItemTagProjectMaterial material = new ItemTagProjectMaterial();
        material.tagName = new ResourceLocation(this.tagName.toString());
        material.quantity = this.quantity;
        material.consumed = this.consumed;
        return material;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (consumed ? 1231 : 1237);
        result = prime * result + quantity;
        result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
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
        ItemTagProjectMaterial other = (ItemTagProjectMaterial) obj;
        if (consumed != other.consumed) {
            return false;
        }
        if (quantity != other.quantity) {
            return false;
        }
        if (tagName == null) {
            if (other.tagName != null) {
                return false;
            }
        } else if (!tagName.equals(other.tagName)) {
            return false;
        }
        return true;
    }
}
