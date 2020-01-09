package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
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

public class ConsecrationFieldBlock extends Block {
    protected static final Material MATERIAL = new Material(MaterialColor.AIR, false, false, false, false, true, false, true, PushReaction.NORMAL);

    public ConsecrationFieldBlock() {
        super(Block.Properties.create(MATERIAL).hardnessAndResistance(-1, 3600000).lightValue(15).noDrops());
        this.setRegistryName(PrimalMagic.MODID, "consecration_field");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Source.HALLOWED.getColor());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
        return ItemStack.EMPTY;
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof PlayerEntity && entityIn.ticksExisted % 5 == 0) {
            PlayerEntity player = (PlayerEntity)entityIn;
            player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100));
            player.addPotionEffect(new EffectInstance(Effects.SATURATION, 100));
        }
    }
}
