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
 * Definition for a primalite golem entity.  Weakest of the enchanted golems, but still quite strong.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemEntity extends AbstractEnchantedGolemEntity {
    public PrimaliteGolemEntity(EntityType<? extends PrimaliteGolemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 100.0D).createMutableAttribute(Attributes.ARMOR, 2.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    protected ITag<Item> getRepairMaterialTag() {
        return ItemTagsPM.INGOTS_PRIMALITE;
    }
    
    @Override
    protected float getRepairHealAmount() {
        return 25.0F;
    }
}
