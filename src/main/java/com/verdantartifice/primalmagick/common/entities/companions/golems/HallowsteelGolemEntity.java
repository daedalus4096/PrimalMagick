package com.verdantartifice.primalmagick.common.entities.companions.golems;

import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * Definition for a hallowsteel golem.  Greatest of the enchanted golems.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemEntity extends AbstractEnchantedGolemEntity {
    public HallowsteelGolemEntity(EntityType<? extends HallowsteelGolemEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.ARMOR, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 25.0D);
    }

    @Override
    protected TagKey<Item> getRepairMaterialTag() {
        return ItemTagsPM.INGOTS_HALLOWSTEEL;
    }

    @Override
    protected float getRepairHealAmount() {
        return 50.0F;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() == MobEffects.WITHER ? false : super.canBeAffected(potioneffectIn);
    }
}
