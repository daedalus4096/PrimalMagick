package com.verdantartifice.primalmagic.common.entities.companions.golems;

import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.world.World;

/**
 * Definition for a hallowsteel golem.  Greatest of the enchanted golems.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemEntity extends AbstractEnchantedGolemEntity {
    public HallowsteelGolemEntity(EntityType<? extends HallowsteelGolemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 200.0D).createMutableAttribute(Attributes.ARMOR, 8.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 25.0D);
    }

    @Override
    protected ITag<Item> getRepairMaterialTag() {
        return ItemTagsPM.INGOTS_HALLOWSTEEL;
    }

    @Override
    protected float getRepairHealAmount() {
        return 50.0F;
    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() == Effects.WITHER ? false : super.isPotionApplicable(potioneffectIn);
    }
}
