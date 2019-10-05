package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.ArcaneWorkbenchContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ArcaneWorkbenchBlock extends Block {
    protected static final VoxelShape PART_UPPER = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape PART_POST1 = Block.makeCuboidShape(11.0D, 4.0D, 1.0D, 15.0D, 8.0D, 5.0D);
    protected static final VoxelShape PART_POST2 = Block.makeCuboidShape(11.0D, 4.0D, 11.0D, 15.0D, 8.0D, 15.0D);
    protected static final VoxelShape PART_POST3 = Block.makeCuboidShape(1.0D, 4.0D, 11.0D, 5.0D, 8.0D, 15.0D);
    protected static final VoxelShape PART_POST4 = Block.makeCuboidShape(1.0D, 4.0D, 1.0D, 5.0D, 8.0D, 5.0D);
    protected static final VoxelShape PART_LOWER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_UPPER, PART_POST1, PART_POST2, PART_POST3, PART_POST4, PART_LOWER);

    public ArcaneWorkbenchBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.WOOD));
        this.setRegistryName(PrimalMagic.MODID, "arcane_workbench");
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                @Override
                public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
                    return new ArcaneWorkbenchContainer(windowId, inv, IWorldPosCallable.of(worldIn, pos));
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent(ArcaneWorkbenchBlock.this.getTranslationKey());
                }
            });
        }
        return true;
    }
}
