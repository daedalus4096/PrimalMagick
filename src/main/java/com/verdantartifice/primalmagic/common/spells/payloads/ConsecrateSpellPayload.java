package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (target != null) {
            if (target.getType() == HitResult.Type.BLOCK) {
                // If the target is a block, place fields in the two blocks adjacent to the target in
                // the direction determined by the raytrace result
                BlockHitResult blockTarget = (BlockHitResult)target;
                if (world.getBlockState(blockTarget.getBlockPos()).canOcclude()) {
                    for (int offset = 1; offset <= 2; offset++) {
                        BlockPos targetPos = blockTarget.getBlockPos().relative(blockTarget.getDirection(), offset);
                        this.placeField(world, targetPos);
                    }
                } else {
                    this.placeField(world, blockTarget.getBlockPos());
                    this.placeField(world, blockTarget.getBlockPos().relative(blockTarget.getDirection()));
                }
            } else if (target.getType() == HitResult.Type.ENTITY) {
                // If the target is an entity, place fields at the entity's position and the position above that
                BlockPos hitPos = new BlockPos(target.getLocation());
                this.placeField(world, hitPos);
                this.placeField(world, hitPos.above());
            }
        }
    }
    
    protected void placeField(@Nonnull Level world, @Nonnull BlockPos pos) {
        if (world.isEmptyBlock(pos) && world.getBlockState(pos) != BlocksPM.CONSECRATION_FIELD.get().defaultBlockState()) {
            world.setBlock(pos, BlocksPM.CONSECRATION_FIELD.get().defaultBlockState(), Constants.BlockFlags.DEFAULT);
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
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
