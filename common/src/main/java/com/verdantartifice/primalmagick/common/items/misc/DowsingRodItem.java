package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.mana.network.IManaConsumer;
import com.verdantartifice.primalmagick.common.mana.network.IManaNetworkNode;
import com.verdantartifice.primalmagick.common.mana.network.IManaSupplier;
import com.verdantartifice.primalmagick.common.mana.network.Route;
import com.verdantartifice.primalmagick.common.mana.network.RouteManager;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        BlockPos targetPos = context.getClickedPos();
        this.recordDowsingPosition(stack, player, targetPos);
        if (!level.isClientSide) {
            Block block = level.getBlockState(targetPos).getBlock();
            BlockEntity blockEntity = level.getBlockEntity(targetPos);
            if (blockEntity instanceof RitualAltarTileEntity altarEntity) {
                this.doStabilityCheck(altarEntity, player);
                this.damageRod(player, stack);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (block instanceof OfferingPedestalBlock pedestalBlock && blockEntity instanceof OfferingPedestalTileEntity pedestalTile) {
                this.doPropSaltCheck(level, pedestalBlock, targetPos, player);
                this.doPropSymmetryCheck(level, pedestalBlock, targetPos, pedestalTile.getAltarPos(), player);
                this.damageRod(player, stack);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (block instanceof IRitualPropBlock propBlock && blockEntity instanceof IRitualPropTileEntity propTile) {
                this.doPropSaltCheck(level, propBlock, targetPos, player);
                this.doPropSymmetryCheck(level, propBlock, targetPos, propTile.getAltarPos(), player);
                this.damageRod(player, stack);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (blockEntity instanceof IManaNetworkNode node) {
                this.doRouteTableCheck(level, node, player, stack);
                this.damageRod(player, stack);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        // If using the dowsing rod on empty air, then clear any recorded dowsing positions
        InteractionResultHolder<ItemStack> retVal = super.use(pLevel, pPlayer, pUsedHand);
        this.recordDowsingPosition(pPlayer.getItemInHand(pUsedHand), pPlayer, null);
        return retVal;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pIsSelected) {
            // Only show network route highlights if the dowsing rod is currently selected
            BlockPos primaryPos = pStack.has(DataComponentsPM.DOWSING_PRIMARY_POSITION.get()) ? pStack.get(DataComponentsPM.DOWSING_PRIMARY_POSITION.get()) : null;
            if (primaryPos != null && pLevel.getBlockEntity(primaryPos) instanceof IManaNetworkNode primaryNode) {
                RouteTable routeTable = RouteManager.getRouteTable(pLevel);
                Set<Route.Hop> connectedHops = routeTable.getAllRoutes(pLevel, Optional.empty(), primaryNode).stream()
//                Set<Route.Hop> connectedHops = Stream.concat(routeTable.getRoutesForHead(primaryPos).stream(), routeTable.getRoutesForTail(primaryPos).stream())
                        .map(r -> r.getHops(pLevel))
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                Set<Route.Hop> highlightedHops = new HashSet<>();
                BlockPos secondaryPos = pStack.has(DataComponentsPM.DOWSING_SECONDARY_POSITION.get()) ? pStack.get(DataComponentsPM.DOWSING_SECONDARY_POSITION.get()) : null;
                if (secondaryPos != null && primaryNode instanceof IManaConsumer consumer && pLevel.getBlockEntity(secondaryPos) instanceof IManaSupplier supplier) {
                    // If a supplier and then a consumer were dowsed, highlight the best route between them
                    Optional<Route> routeOpt = routeTable.getRoute(pLevel, Optional.empty(), supplier, consumer);
                    routeOpt.ifPresent(route -> {
                        List<Route.Hop> toHighlight = route.getHops(pLevel);
                        if (toHighlight != null && !toHighlight.isEmpty()) {
                            highlightedHops.addAll(toHighlight);
                            toHighlight.forEach(connectedHops::remove);
                        }
                    });
                }

                // Show particles for each hop to be displayed
                if (pEntity instanceof ServerPlayer serverPlayer) {
                    connectedHops.forEach(hop -> this.showParticlesForRouteHop(serverPlayer, hop, Color.WHITE.getRGB()));
                    highlightedHops.forEach(hop -> this.showParticlesForRouteHop(serverPlayer, hop, Color.RED.getRGB()));
                }
            }
        }
    }

    protected void showParticlesForRouteHop(ServerPlayer serverPlayer, Route.Hop hop, int color) {
        PacketHandler.sendToPlayer(
                new ManaSparklePacket(hop.supplier().getBlockPos().getCenter(), hop.consumer().getBlockPos().getCenter(), 20, color, 0D),
                serverPlayer);
    }

    protected void recordDowsingPosition(@NotNull ItemStack stack, @Nullable Player player, @Nullable BlockPos targetPos) {
        boolean sendStatusMessage = player != null && !player.level().isClientSide;
        if (targetPos == null && player != null && player.isShiftKeyDown()) {
            // Clear all set positions if using the rod on empty air while sneaking
            stack.remove(DataComponentsPM.DOWSING_PRIMARY_POSITION.get());
            stack.remove(DataComponentsPM.DOWSING_SECONDARY_POSITION.get());
            if (sendStatusMessage) {
                player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.position.clear"));
            }
        } else if (targetPos != null) {
            if (stack.has(DataComponentsPM.DOWSING_PRIMARY_POSITION.get())) {
                // If a primary position has already been recorded, roll it down to secondary
                stack.set(DataComponentsPM.DOWSING_SECONDARY_POSITION.get(), stack.get(DataComponentsPM.DOWSING_PRIMARY_POSITION.get()));
            }
            stack.set(DataComponentsPM.DOWSING_PRIMARY_POSITION.get(), targetPos);
            if (sendStatusMessage) {
                Component posText = ComponentUtils.wrapInSquareBrackets(Component.literal(targetPos.toShortString()));
                player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.position.record", posText));
            }
        }
    }

    protected void doRouteTableCheck(@NotNull Level level, @NotNull IManaNetworkNode primaryNode, @NotNull Player player, @NotNull ItemStack stack) {
        BlockPos secondaryPos = stack.has(DataComponentsPM.DOWSING_SECONDARY_POSITION.get()) ? stack.get(DataComponentsPM.DOWSING_SECONDARY_POSITION.get()) : null;
        player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.mana_network.routes",
                ComponentUtils.wrapInSquareBrackets(Component.literal(primaryNode.getBlockPos().toShortString()))));
        if (secondaryPos != null && level.getBlockEntity(secondaryPos) instanceof IManaNetworkNode secondaryNode) {
            if (secondaryNode instanceof IManaSupplier supplier && primaryNode instanceof IManaConsumer consumer &&
                    consumer.getRouteTable().getRoute(level, Optional.empty(), supplier, consumer).isPresent()) {
                player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.mana_network.route_highlight",
                        ComponentUtils.wrapInSquareBrackets(Component.literal(secondaryPos.toShortString())),
                        ComponentUtils.wrapInSquareBrackets(Component.literal(primaryNode.getBlockPos().toShortString()))));
            } else {
                player.sendSystemMessage(Component.translatable("event.primalmagick.dowsing_rod.mana_network.no_route",
                        ComponentUtils.wrapInSquareBrackets(Component.literal(secondaryPos.toShortString())),
                        ComponentUtils.wrapInSquareBrackets(Component.literal(primaryNode.getBlockPos().toShortString()))));
            }
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
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }
}
