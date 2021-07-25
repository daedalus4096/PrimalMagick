package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Random;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Block definition for the consecration field.  Consecration fields prevent entry by non-player mobs
 * and grant players health and hunger recovery while standing inside.
 * 
 * @author Daedalus4096
 */
public class ConsecrationFieldBlock extends Block {
    public ConsecrationFieldBlock() {
        super(Block.Properties.of(Material.AIR).strength(-1, 3600000).lightLevel((state) -> { return 15; }).noDrops());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        // Show glittering particles
        super.animateTick(stateIn, worldIn, pos, rand);
        FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Source.HALLOWED.getColor());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // Don't show a selection highlight when mousing over the field
        return Shapes.empty();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // The field should only have a collision box for living, non-player entities; everything else can pass through it
        if (context.getEntity() instanceof Player || !(context.getEntity() instanceof LivingEntity)) {
            return Shapes.empty();
        } else {
            return Shapes.block();
        }
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        // Don't work with the creative pick-block feature, as this block has no corresponding item block
        return ItemStack.EMPTY;
    }
    
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        // While a player is in the field, give them regeneration and hunger recovery for a few seconds
        if (entityIn instanceof Player && entityIn.tickCount % 5 == 0) {
            Player player = (Player)entityIn;
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 110));
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 110));
        }
    }
}
