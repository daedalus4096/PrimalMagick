package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.Map;

import com.verdantartifice.primalmagic.common.misc.BlockBreaker;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BreakSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "break";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_BREAK"));

    public BreakSpellPayload() {
        super();
    }
    
    public BreakSpellPayload(int power) {
        super();
        this.getProperty("power").setValue(power);
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("power", new SpellProperty("power", "primalmagic.spell.property.power", 1, 5));
        return propMap;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, LivingEntity caster) {
        if (target != null && target.getType() == RayTraceResult.Type.BLOCK && caster instanceof PlayerEntity) {
            BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
            PlayerEntity player = (PlayerEntity)caster;
            BlockPos pos = blockTarget.getPos();
            BlockState state = world.getBlockState(pos);
            float durability = (float)Math.sqrt(100.0F * state.getBlockHardness(world, pos));
            BlockBreaker.enqueue(world, new BlockBreaker(this.getModdedPropertyValue("power", spell), pos, state, durability, durability, player));
        }
    }

    @Override
    public Source getSource() {
        return Source.EARTH;
    }

    @Override
    public SourceList getManaCost() {
        return new SourceList().add(this.getSource(), this.getPropertyValue("power"));
    }

    @Override
    public void playSounds(World world, LivingEntity caster) {
        world.playSound(null, caster.getPosition(), SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
