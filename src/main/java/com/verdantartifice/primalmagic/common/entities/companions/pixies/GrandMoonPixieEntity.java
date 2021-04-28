package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.PixieItem;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;

/**
 * Definition of a grand moon pixie.  Middle of the moon pixies.
 * 
 * @author Daedalus4096
 */
public class GrandMoonPixieEntity extends AbstractMoonPixieEntity implements IGrandPixie {
    public GrandMoonPixieEntity(EntityType<? extends AbstractPixieEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 9.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }
    
    @Override
    protected PixieItem getSpawnItem() {
        return ItemsPM.GRAND_MOON_PIXIE.get();
    }
}
