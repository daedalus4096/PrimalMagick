package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Definition for a consecration spell.  Creates a two-high field of holy energy at the target location
 * which prevents entry by non-player mobs.  It also heals and restores hunger to players.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.misc.ConsecrationFieldBlock}
 */
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
    public void execute(RayTraceResult target, Vector3d burstPoint, SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (target != null) {
            if (target.getType() == RayTraceResult.Type.BLOCK) {
                // If the target is a block, place fields in the two blocks adjacent to the target in
                // the direction determined by the raytrace result
                BlockRayTraceResult blockTarget = (BlockRayTraceResult)target;
                if (world.getBlockState(blockTarget.getPos()).isSolid()) {
                    for (int offset = 1; offset <= 2; offset++) {
                        BlockPos targetPos = blockTarget.getPos().offset(blockTarget.getFace(), offset);
                        this.placeField(world, targetPos);
                    }
                } else {
                    this.placeField(world, blockTarget.getPos());
                    this.placeField(world, blockTarget.getPos().offset(blockTarget.getFace()));
                }
            } else if (target.getType() == RayTraceResult.Type.ENTITY) {
                // If the target is an entity, place fields at the entity's position and the position above that
                BlockPos hitPos = new BlockPos(target.getHitVec());
                this.placeField(world, hitPos);
                this.placeField(world, hitPos.up());
            }
        }
    }
    
    protected void placeField(@Nonnull World world, @Nonnull BlockPos pos) {
        if (world.isAirBlock(pos) && world.getBlockState(pos) != BlocksPM.CONSECRATION_FIELD.get().getDefaultState()) {
            world.setBlockState(pos, BlocksPM.CONSECRATION_FIELD.get().getDefaultState(), Constants.BlockFlags.DEFAULT);
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
