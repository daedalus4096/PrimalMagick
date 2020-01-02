package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ConjureWaterSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "conjure_water";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_CONJURE_WATER"));

    public ConjureWaterSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, LivingEntity caster) {
        // TODO Auto-generated method stub

    }

    @Override
    public Source getSource() {
        return Source.SEA;
    }

    @Override
    public SourceList getManaCost() {
        return new SourceList().add(this.getSource(), 10);
    }

    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
