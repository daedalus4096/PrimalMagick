package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ConsecrateSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "consecrate";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_CONSECRATE"));

    public ConsecrateSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster) {
        if (target != null) {
            if (target.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
                if (world.getBlockState(blockTarget.getPos()).isSolid()) {
                    for (int offset = 1; offset <= 2; offset++) {
                        BlockPos targetPos = blockTarget.getPos().offset(blockTarget.getFace(), offset);
                        this.placeField(world, targetPos);
                    }
                }
            } else if (target.getType() == RayTraceResult.Type.ENTITY) {
                BlockPos hitPos = new BlockPos(target.getHitVec());
                this.placeField(world, hitPos);
                this.placeField(world, hitPos.up());
            }
        }
    }
    
    protected void placeField(@Nonnull World world, @Nonnull BlockPos pos) {
        if (world.isAirBlock(pos) && world.getBlockState(pos) != BlocksPM.CONSECRATION_FIELD.getDefaultState()) {
            world.setBlockState(pos, BlocksPM.CONSECRATION_FIELD.getDefaultState(), 0x3);
        }
    }

    @Override
    public Source getSource() {
        return Source.HALLOWED;
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
