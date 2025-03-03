package com.verdantartifice.primalmagick.common.tiles.mana;

import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
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
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Base definition of a mana font tile entity.  Provides the recharge and wand interaction
 * functionality for the corresponding block.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractManaFontTileEntity extends AbstractTilePM implements IInteractWithWand {
    public static final int CENTIMANA_RECHARGED_PER_TICK = 5;
    
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
    
    public void doSiphon(ManaStorage manaCap, Level level, Player player, Vec3 targetPos, int maxTransferCentimana) {
        if (this.getBlockState().getBlock() instanceof AbstractManaFontBlock fontBlock && manaCap != null) {
            Source source = fontBlock.getSource();
            if (source != null) {
                // Transfer mana from the font to the container
                int tap = Math.min(this.mana, maxTransferCentimana);
                int manaTransfered = manaCap.receiveMana(source, tap, false);
                if (manaTransfered > 0) {
                    this.mana -= manaTransfered;
                    StatsManager.incrementValue(player, StatsPM.MANA_SIPHONED, manaTransfered / 100);
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

    @VisibleForTesting
    public void doRecharge() {
        // Recharge the font over time
        if (this.setMana(this.getMana() + CENTIMANA_RECHARGED_PER_TICK)) {
            // Sync the tile if its mana total changed
            this.setChanged();
            this.syncTile(true);
        }
    }
}
