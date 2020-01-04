package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConjureLavaSpellPayload extends AbstractConjureFluidSpellPayload {
    public static final String TYPE = "conjure_lava";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_CONJURE_LAVA"));
    protected static final int MANA_COST = 200;

    public ConjureLavaSpellPayload() {
        super(Fluids.LAVA);
    }

    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    public Source getSource() {
        return Source.INFERNAL;
    }

    @Override
    public SourceList getManaCost() {
        return new SourceList().add(this.getSource(), MANA_COST);
    }

    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
