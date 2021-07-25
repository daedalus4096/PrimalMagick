package com.verdantartifice.primalmagic.common.blocks.golems;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Definition for the "head" block of an enchanted golem.  Place it on top of a T shape of
 * enchanted metal blocks, then use a wand on it, and a golem entity will be created.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemControllerBlock<T extends AbstractEnchantedGolemEntity> extends Block implements IInteractWithWand {
    protected static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Nullable
    private BlockPattern golemPattern;

    public AbstractEnchantedGolemControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    
    protected abstract SimpleResearchKey getRequiredResearch();
    
    protected abstract EntityType<T> getEntityType();
    
    protected abstract Block getBaseBlock();
    
    protected abstract Block getControllerBlock();

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public InteractionResult onWandRightClick(ItemStack wandStack, Level world, Player player, BlockPos pos, Direction direction) {
        if (!world.isClientSide && wandStack.getItem() instanceof IWand && this.getRequiredResearch().isKnownByStrict(player)) {
            BlockPattern.BlockPatternMatch helper = this.getGolemPattern().find(world, pos);
            if (helper != null) {
                for (int i = 0; i < this.getGolemPattern().getWidth(); i++) {
                    for (int j = 0; j < this.getGolemPattern().getHeight(); j++) {
                        BlockInWorld info = helper.getBlock(i, j, 0);
                        world.setBlock(info.getPos(), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                        world.levelEvent(2001, info.getPos(), Block.getId(info.getState()));
                    }
                }
                
                BlockPos blockpos = helper.getBlock(1, 2, 0).getPos();
                AbstractEnchantedGolemEntity golem = this.getEntityType().create(world);
                CompanionManager.addCompanion(player, golem);
                golem.moveTo((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addFreshEntity(golem);

                for (int i = 0; i < this.getGolemPattern().getWidth(); i++) {
                    for (int j = 0; j < this.getGolemPattern().getHeight(); j++) {
                        BlockInWorld info = helper.getBlock(i, j, 0);
                        world.blockUpdated(info.getPos(), Blocks.AIR);
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, Player player, int count) {
        // Do nothing; golem controllers don't support wand channeling
    }
    
    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(this.getControllerBlock())))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(this.getBaseBlock())))
                    .where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR)))
                    .build();
        }
        return this.golemPattern;
    }
}
