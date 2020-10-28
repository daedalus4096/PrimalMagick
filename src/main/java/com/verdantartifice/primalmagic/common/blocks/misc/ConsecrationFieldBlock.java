package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Random;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
        super(Block.Properties.create(Material.AIR).hardnessAndResistance(-1, 3600000).setLightLevel((state) -> { return 15; }).noDrops());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show glittering particles
        super.animateTick(stateIn, worldIn, pos, rand);
        FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Source.HALLOWED.getColor());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        // Don't show a selection highlight when mousing over the field
        return VoxelShapes.empty();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        // The field should only have a collision box for living, non-player entities; everything else can pass through it
        if (context.getEntity() instanceof PlayerEntity || !(context.getEntity() instanceof LivingEntity)) {
            return VoxelShapes.empty();
        } else {
            return VoxelShapes.fullCube();
        }
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
    
    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        // Don't work with the creative pick-block feature, as this block has no corresponding item block
        return ItemStack.EMPTY;
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        // While a player is in the field, give them regeneration and hunger recovery for a few seconds
        if (entityIn instanceof PlayerEntity && entityIn.ticksExisted % 5 == 0) {
            PlayerEntity player = (PlayerEntity)entityIn;
            player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 110));
            player.addPotionEffect(new EffectInstance(Effects.SATURATION, 110));
        }
    }
}
