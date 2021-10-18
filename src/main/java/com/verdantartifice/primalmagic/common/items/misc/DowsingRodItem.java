package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagic.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Definition of an item for checking ritual altar and prop status.
 * 
 * @author Daedalus4096
 */
public class DowsingRodItem extends Item {
    protected static final float THRESHOLD_HIGH = 0.15F;
    protected static final float THRESHOLD_LOW = 0.025F;
    
    public DowsingRodItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            BlockPos targetPos = context.getClickedPos();
            Block block = level.getBlockState(targetPos).getBlock();
            BlockEntity blockEntity = level.getBlockEntity(targetPos);
            Player player = context.getPlayer();
            if (blockEntity instanceof RitualAltarTileEntity altarEntity) {
                this.doStabilityCheck(altarEntity, player);
                this.damageRod(player, stack);
                return InteractionResult.SUCCESS;
            } else if (block instanceof OfferingPedestalBlock pedestalBlock && blockEntity instanceof OfferingPedestalTileEntity pedestalTile) {
                this.doPropSaltCheck(level, pedestalBlock, targetPos, player);
                this.doPropSymmetryCheck(level, pedestalBlock, targetPos, pedestalTile.getAltarPos(), player);
                this.damageRod(player, stack);
                return InteractionResult.SUCCESS;
            } else if (block instanceof IRitualPropBlock propBlock && blockEntity instanceof IRitualPropTileEntity propTile) {
                this.doPropSaltCheck(level, propBlock, targetPos, player);
                this.doPropSymmetryCheck(level, propBlock, targetPos, propTile.getAltarPos(), player);
                this.damageRod(player, stack);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
    
    protected void doStabilityCheck(RitualAltarTileEntity altarEntity, Player player) {
        float delta = altarEntity.calculateStabilityDelta();
        if (delta >= THRESHOLD_HIGH) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability.very_good"), Util.NIL_UUID);
        } else if (delta >= THRESHOLD_LOW) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability.good"), Util.NIL_UUID);
        } else if (delta <= -THRESHOLD_HIGH) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability.very_poor"), Util.NIL_UUID);
        } else if (delta <= -THRESHOLD_LOW) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability.poor"), Util.NIL_UUID);
        } else {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability.neutral"), Util.NIL_UUID);
        }
    }
    
    protected void doPropSaltCheck(Level level, ISaltPowered block, BlockPos blockPos, Player player) {
        if (block.isBlockSaltPowered(level, blockPos)) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.salt_connection.active"), Util.NIL_UUID);
        } else {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.salt_connection.inactive"), Util.NIL_UUID);
        }
    }
    
    protected void doPropSymmetryCheck(Level level, IRitualStabilizer block, BlockPos blockPos, BlockPos altarPos, Player player) {
        BlockPos symPos = RitualAltarTileEntity.getSymmetricPosition(altarPos, blockPos);
        if (symPos == null || block.hasSymmetryPenalty(level, blockPos, symPos)) {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.symmetry.not_found"), Util.NIL_UUID);
        } else {
            player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.symmetry.found"), Util.NIL_UUID);
        }
    }
    
    protected void damageRod(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, p -> {
                p.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
    }
}
