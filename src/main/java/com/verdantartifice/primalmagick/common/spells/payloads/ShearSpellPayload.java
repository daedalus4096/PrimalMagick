package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.tags.BlockTagsForgeExt;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

/**
 * Definition for a shearing spell.  Acts as if the player used shears on the target, either
 * right- or left-click behavior.  Has no effect when cast by non-players.
 * 
 * @author Daedalus4096
 */
public class ShearSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "shear";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_SHEAR.get().compoundKey();

    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        ItemStack fakeShears = new ItemStack(Items.SHEARS);
        RandomSource rand = world.random;
        int treasureLevel = spellSource.getEnchantmentLevel(EnchantmentsPM.TREASURE.get());
        if (caster instanceof Player player) {
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult)target;
                Entity entity = entityHitResult.getEntity();
                if (entity instanceof IForgeShearable shearableEntity && shearableEntity.isShearable(fakeShears, world, entity.blockPosition())) {
                    List<ItemStack> drops = shearableEntity.onSheared(player, ItemStack.EMPTY, world, entity.blockPosition(), treasureLevel);
                    drops.forEach(d -> {
                        ItemEntity ent = entity.spawnAtLocation(d, 1F);
                        ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                    });
                }
            } else if (target.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockTarget = (BlockHitResult)target;
                BlockPos pos = blockTarget.getBlockPos();
                BlockState state = world.getBlockState(pos);
                UseOnContext fakeContext = new UseOnContext(world, player, InteractionHand.MAIN_HAND, fakeShears, blockTarget);
                for (ToolAction action : ToolActions.DEFAULT_SHEARS_ACTIONS) {
                    // If the block state responds to a right-click from shears, then do that
                    BlockState modifiedState = state.getToolModifiedState(fakeContext, action, true);
                    if (modifiedState != null) {
                        world.setBlock(pos, modifiedState, Block.UPDATE_ALL_IMMEDIATE);
                        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, modifiedState));
                        return;
                    }
                }
                if (state.getBlock() instanceof TripWireBlock tripwire) {
                    // Handle special tripwire case
                    FluidState fluid = world.getFluidState(pos);
                    world.setBlock(pos, state.setValue(TripWireBlock.DISARMED, Boolean.TRUE), Block.UPDATE_INVISIBLE);
                    world.gameEvent(player, GameEvent.SHEAR, pos);
                    if (world.setBlock(pos, fluid.createLegacyBlock(), world.isClientSide ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL)) {
                        state.getBlock().destroy(world, pos, state);
                    }
                }
                if (state.getBlock() instanceof BeehiveBlock beehive && state.getValue(BeehiveBlock.HONEY_LEVEL) >= BeehiveBlock.MAX_HONEY_LEVELS) {
                    // Handle special beehive case
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                    BeehiveBlock.dropHoneycomb(world, pos);
                    world.gameEvent(player, GameEvent.SHEAR, pos);
                    if (!CampfireBlock.isSmokeyPos(world, pos)) {
                        if (beehive.hiveContainsBees(world, pos)) {
                            beehive.angerNearbyBees(world, pos);
                        }
                        beehive.releaseBeesAndResetHoneyLevel(world, state, pos, player, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                    } else {
                        beehive.resetHoneyLevel(world, state, pos);
                    }
                }
                
                // Re-fetch block state in case it was changed by this function
                if (world.getBlockState(pos).is(BlockTagsForgeExt.MINEABLE_WITH_SHEARS)) {
                    // If no applicable right-click action is found, but the block is mineable with shears, then break it quickly
                    BlockBreaker breaker = new BlockBreaker.Builder().power(5).target(pos, state).durability(1F).player(player).tool(fakeShears).fortune(treasureLevel).alwaysDrop().build();
                    BlockBreaker.schedule(world, 1, breaker);
                }
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.SKY;
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
