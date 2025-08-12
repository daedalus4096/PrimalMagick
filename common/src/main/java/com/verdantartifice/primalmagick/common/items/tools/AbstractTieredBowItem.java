package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import javax.annotation.Nonnull;

/**
 * Definition of a repairable bow item made of a magickal metal.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTieredBowItem extends BowItem {
    protected final ToolMaterial material;
    
    public AbstractTieredBowItem(ToolMaterial material, Item.Properties properties) {
        super(properties
                .durability(material.durability())
                .enchantable(material.enchantmentValue())
                .repairable(material.repairItems())
        );
        this.material = material;
    }

    protected AbstractArrow boostArrowDamage(@Nonnull AbstractArrow arrow) {
        double damageBonus = 0.0D;
        if (ToolMaterialsPM.PRIMALITE.equals(this.material)) {
            damageBonus = 0.5D;
        } else if (ToolMaterialsPM.HEXIUM.equals(this.material)) {
            damageBonus = 1.0D;
        } else if (ToolMaterialsPM.HALLOWSTEEL.equals(this.material)) {
            damageBonus = 1.5D;
        }
        arrow.setBaseDamage(arrow.baseDamage + damageBonus);
        return arrow;
    }
    
    public ToolMaterial getMaterial() {
        return this.material;
    }
}
