package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class FrostDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "frost_damage";
    
    protected int duration;
    
    public FrostDamageSpellPayload() {
        super();
    }
    
    public FrostDamageSpellPayload(int power, int duration) {
        super(power);
        this.duration = duration;
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("Duration", this.duration);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.duration = nbt.getInt("Duration");
    }

    @Override
    public Source getSource() {
        return Source.SEA;
    }

    @Override
    public void playSounds(World world, PlayerEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.ICE, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected float getTotalDamage() {
        return 3.0F + this.power;
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected void applySecondaryEffects(RayTraceResult target, ISpellPackage spell, World world, PlayerEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityTarget = (EntityRayTraceResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                int duration = 20 * this.duration;
                int potency = (int)((1.0F + this.power) / 3.0F);
                ((LivingEntity)entityTarget.getEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration, potency));
            }
        }
    }
}
