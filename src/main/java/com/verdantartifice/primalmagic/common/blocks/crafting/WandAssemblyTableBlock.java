package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.WandAssemblyTableContainer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Block definition for the wand assembly table.  A wand assembly table allows a player to construct a
 * modular wand from component cores, caps, and gems.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableBlock extends Block {
    public WandAssemblyTableBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
        this.setRegistryName(PrimalMagic.MODID, "wand_assembly_table");
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && player instanceof ServerPlayerEntity) {
            // Open the GUI for the wand assembly table
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                @Override
                public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
                    return new WandAssemblyTableContainer(windowId, inv, IWorldPosCallable.of(worldIn, pos));
                }

                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent(WandAssemblyTableBlock.this.getTranslationKey());
                }
            });
        }
        return ActionResultType.SUCCESS;
    }
}
