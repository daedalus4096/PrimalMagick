package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.altar_stability.very_good"));
        } else if (delta >= THRESHOLD_LOW) {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.altar_stability.good"));
        } else if (delta <= -THRESHOLD_HIGH) {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.altar_stability.very_poor"));
        } else if (delta <= -THRESHOLD_LOW) {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.altar_stability.poor"));
        } else {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.altar_stability.neutral"));
        }
    }
    
    protected void doPropSaltCheck(Level level, ISaltPowered block, BlockPos blockPos, Player player) {
        if (block.isBlockSaltPowered(level, blockPos)) {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.salt_connection.active"));
        } else {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.salt_connection.inactive"));
        }
    }
    
    protected void doPropSymmetryCheck(Level level, IRitualStabilizer block, BlockPos blockPos, BlockPos altarPos, Player player) {
        BlockPos symPos = RitualAltarTileEntity.getSymmetricPosition(altarPos, blockPos);
        if (symPos == null || block.hasSymmetryPenalty(level, blockPos, symPos)) {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.symmetry.not_found"));
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.symmetry.marking_pos"));
                PacketHandler.sendToPlayer(new PropMarkerPacket(symPos, 200), serverPlayer);
            }
        } else {
            player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.symmetry.found"));
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
