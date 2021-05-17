package com.verdantartifice.primalmagic.common.entities.misc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Definition of an inner demon entity, a boss monster summoned via a sanguine crucible.
 * 
 * @author Daedalus4096
 */
@OnlyIn(value = Dist.CLIENT, _interface = IChargeableMob.class)
public class InnerDemonEntity extends MonsterEntity implements IRangedAttackMob, IChargeableMob {
    protected final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public InnerDemonEntity(EntityType<? extends InnerDemonEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 50;
    }
    
    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MAX_HEALTH, 400.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D).createMutableAttribute(Attributes.FOLLOW_RANGE, 40.0D).createMutableAttribute(Attributes.ARMOR, 4.0D);
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCharged() {
        return true;
    }

}
