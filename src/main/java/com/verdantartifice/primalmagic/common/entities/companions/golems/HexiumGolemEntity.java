package com.verdantartifice.primalmagic.common.entities.companions.golems;

import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.world.World;

/**
 * Definition for a hexium golem entity.  Middle of the enchanted golem children.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemEntity extends AbstractEnchantedGolemEntity {
    public HexiumGolemEntity(EntityType<? extends HexiumGolemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 150.0D).createMutableAttribute(Attributes.ARMOR, 4.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 20.0D);
    }

    @Override
    protected ITag<Item> getRepairMaterialTag() {
        return ItemTagsPM.INGOTS_HEXIUM;
    }

    @Override
    protected float getRepairHealAmount() {
        return 37.5F;
    }
}
