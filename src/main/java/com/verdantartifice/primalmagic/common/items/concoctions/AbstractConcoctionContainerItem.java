package com.verdantartifice.primalmagic.common.items.concoctions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Base definition of a concoction container.  Interacts with water sources to fill the container
 * in prepration for concocting.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConcoctionContainerItem extends Item {
    public AbstractConcoctionContainerItem(Item.Properties properties) {
        super(properties);
    }
    
    protected abstract ItemStack getConcoctionContainerItem();

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemStack);
        } else {
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockRayTraceResult)rayTraceResult).getPos();
                if (!worldIn.isBlockModifiable(playerIn, blockPos)) {
                    return ActionResult.resultPass(itemStack);
                }
                if (worldIn.getFluidState(blockPos).isTagged(FluidTags.WATER)) {
                    worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    return ActionResult.func_233538_a_(this.turnFlaskIntoItem(itemStack, playerIn, this.getConcoctionContainerItem()), worldIn.isRemote());
                }
            }
            return ActionResult.resultPass(itemStack);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.CAULDRON) {
            int waterLevel = blockState.get(CauldronBlock.LEVEL);
            if (waterLevel > 0 && !world.isRemote) {
                PlayerEntity player = context.getPlayer();
                if (!player.abilities.isCreativeMode) {
                    ItemStack flaskStack = context.getItem();
                    ItemStack waterStack = this.getConcoctionContainerItem();
                    flaskStack.shrink(1);
                    if (flaskStack.isEmpty()) {
                        player.setHeldItem(context.getHand(), waterStack);
                    } else if (!player.inventory.addItemStackToInventory(waterStack)) {
                        player.dropItem(waterStack, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                    }
                }
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                ((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, blockState, waterLevel - 1);                
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return super.onItemUse(context);
    }
    
    protected ItemStack turnFlaskIntoItem(ItemStack flaskStack, PlayerEntity player, ItemStack stack) {
        return DrinkHelper.fill(flaskStack, player, stack);
    }
}
