package com.verdantartifice.primalmagick.common.entities.companions.golems;

import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * Definition for a hexium golem entity.  Middle of the enchanted golem children.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemEntity extends AbstractEnchantedGolemEntity {
    public HexiumGolemEntity(EntityType<? extends HexiumGolemEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 20.0D);
    }

    @Override
    protected TagKey<Item> getRepairMaterialTag() {
        return ItemTagsPM.INGOTS_HEXIUM;
    }

    @Override
    protected float getRepairHealAmount() {
        return 37.5F;
    }
}
