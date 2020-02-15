package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Block definition for the arcane workbench.  An arcane workbench is like a normal workbench, but can
 * be used to craft arcane recipes requiring mana.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchBlock extends Block {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/arcane_workbench"));

    public ArcaneWorkbenchBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.WOOD));
        this.setRegistryName(PrimalMagic.MODID, "arcane_workbench");
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player instanceof ServerPlayerEntity) {
            // Open the GUI for the arcane workbench
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
        return ActionResultType.SUCCESS;
    }
}
