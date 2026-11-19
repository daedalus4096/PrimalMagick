package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;

import java.util.List;
import java.util.Optional;

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
                .equippableUnswappable(EquipmentSlot.OFFHAND)
                .delayedComponent(DataComponents.BLOCKS_ATTACKS, (context) -> new BlocksAttacks(
                        0.25F,
                        1.0F,
                        List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                        new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                        Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                        Optional.of(SoundEvents.SHIELD_BLOCK),
                        Optional.of(SoundEvents.SHIELD_BREAK)))
                .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
        );
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }
}
