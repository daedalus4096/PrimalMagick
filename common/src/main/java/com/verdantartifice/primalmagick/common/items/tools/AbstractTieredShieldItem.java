package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ToolMaterial;

/**
 * Base definition of a shield item made of a magickal metal.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTieredShieldItem extends ShieldItem {
    protected final ToolMaterial material;
    
    public AbstractTieredShieldItem(ToolMaterial material, Item.Properties properties) {
        super(properties
                .durability(material.durability())
                .enchantable(material.enchantmentValue())
                .repairable(material.repairItems())
        );
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }

    /**
     * Return true if a banner can be applied to this item to decorate it.
     * 
     * @return whether a banner can be applied to this item to decorate it
     */
    public boolean canDecorate() {
        return true;
    }
}
