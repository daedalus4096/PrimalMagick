package com.verdantartifice.primalmagick.common.tiles.mana;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.mana.network.IManaSupplier;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.tiles.base.ITieredDeviceBlockEntity;
import com.verdantartifice.primalmagick.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagick.common.wands.IWand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;

/**
 * Base definition of a mana font tile entity.  Provides the recharge and wand interaction
 * functionality for the corresponding block.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractManaFontTileEntity extends AbstractTilePM implements IInteractWithWand, IManaSupplier, ITieredDeviceBlockEntity {
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final RouteTable routeTable = new RouteTable();
    protected int ticksExisted = 0;
    protected int mana;
    
    public AbstractManaFontTileEntity(BlockEntityType<? extends AbstractManaFontTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.mana = this.getInitialMana();
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.mana = compound.getInt("mana");
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("mana", this.mana);
    }

    public int getMana() {
        return this.mana;
    }

    /**
     * Sets the font's current mana level, to a max of its capacity.
     *
     * @param mana the new mana total for the font
     * @return true if the font's mana total was changed, false otherwise
     */
    public boolean setMana(int mana) {
        int startMana = this.getMana();
        this.mana = Math.min(mana, this.getManaCapacity());
        return this.getMana() != startMana;
    }
    
    public int getManaCapacity() {
        return this.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock ? fontBlock.getManaCapacity() : 0;
    }

    public int getManaRechargedPerTick() {
        return switch (this.getDeviceTier()) {
            case ENCHANTED -> 1;
            case BASIC, FORBIDDEN -> 5;
            case HEAVENLY, CREATIVE -> 25;
        };
    }
    
    protected abstract int getInitialMana();

    @Override
    public InteractionResult onWandRightClick(ItemStack wandStack, Level world, Player player, BlockPos pos, Direction direction) {
        if (wandStack.getItem() instanceof IWand wand) {
            // On initial interaction, save this tile's position into the wand's NBT for use during future ticks
            wand.setPositionInUse(wandStack, this.getBlockPos());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, Level level, Player player, Vec3 targetPos, int count) {
        if (count % 5 == 0) {
            this.doSiphon(wandStack, level, player, targetPos);
        }
    }
    
    public void doSiphon(ItemStack wandStack, Level level, Player player, Vec3 targetPos) {
        if (wandStack.getItem() instanceof IWand wand) {
            if (this.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock) {
                Source source = fontBlock.getSource();
                if (source != null) {
                    // Transfer mana from the font to the wand
                    int tap = Math.min(this.mana, wand.getSiphonAmount(wandStack));
                    int leftover = wand.addMana(wandStack, source, tap);
                    if (leftover < tap) {
                        this.mana -= (tap - leftover);
                        StatsManager.incrementValue(player, StatsPM.MANA_SIPHONED, (tap - leftover) / 100); // Record whole mana siphoned for stats
                        this.setChanged();
                        this.syncTile(true);
                        
                        // Show fancy sparkles
                        if (level instanceof ServerLevel serverLevel) {
                            PacketHandler.sendToAllAround(
                                    new ManaSparklePacket(this.worldPosition, targetPos.x, targetPos.y, targetPos.z, 20, source.getColor()),
                                    serverLevel,
                                    this.worldPosition, 
                                    32.0D);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public int extractMana(@NotNull Source source, int maxExtract, boolean simulate) {
        if (this.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock) {
            Source fontSource = fontBlock.getSource();
            if (source.equals(fontSource) && !simulate) {
                int tap = Math.min(this.mana, maxExtract);
                this.mana -= tap;
                this.setChanged();
                this.syncTile(true);
                return tap;
            }
        }
        return 0;
    }

    @VisibleForTesting
    public void doRecharge() {
        // Recharge the font over time
        if (this.setMana(this.getMana() + this.getManaRechargedPerTick())) {
            // Sync the tile if its mana total changed
            this.setChanged();
            this.syncTile(true);
        }
    }

    @Override
    public int getNetworkRange() {
        return 5;
    }

    @Override
    public boolean canSupply(@NotNull Source source) {
        return this.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock && source.equals(fontBlock.getSource());
    }

    @Override
    public int getManaThroughput() {
        return this.getManaCapacity();
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return this.routeTable;
    }
}
