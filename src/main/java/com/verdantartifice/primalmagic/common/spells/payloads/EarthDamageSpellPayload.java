package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "earth_damage";
    
    public EarthDamageSpellPayload() {
        super();
    }
    
    public EarthDamageSpellPayload(int power) {
        super(power);
    }
    
    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    @Override
    public Source getSource() {
        return Source.EARTH;
    }
    
    @Override
    protected float getTotalDamage() {
        return 3.0F + this.power;
    }
    
    @Override
    public void playSounds(World world, PlayerEntity caster) {
        world.playSound(null, caster.getPosition(), SoundsPM.ROCKSLIDE, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }
}
