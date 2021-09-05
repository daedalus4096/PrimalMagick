package com.verdantartifice.primalmagic.common.tiles.mana;

import com.verdantartifice.primalmagic.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Base definition of a mana font tile entity.  Provides the recharge and wand interaction
 * functionality for the corresponding block.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractManaFontTileEntity extends TilePM implements IInteractWithWand {
    protected static final int RECHARGE_TICKS = 20;
    
    protected int ticksExisted = 0;
    protected int mana = 0;
    
    public AbstractManaFontTileEntity(BlockEntityType<? extends AbstractManaFontTileEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.mana = compound.getShort("mana");
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putShort("mana", (short)this.mana);
        return super.save(compound);
    }
    
    public int getMana() {
        return this.mana;
    }
    
    public int getManaCapacity() {
        return this.getBlockState().getBlock() instanceof AbstractManaFontBlock ? ((AbstractManaFontBlock)this.getBlockState().getBlock()).getManaCapacity() : 0;
    }

    @Override
    public InteractionResult onWandRightClick(ItemStack wandStack, Level world, Player player, BlockPos pos, Direction direction) {
        if (wandStack.getItem() instanceof IWand) {
            // On initial interaction, save this tile into the wand's NBT for use during future ticks
            IWand wand = (IWand)wandStack.getItem();
            wand.setTileInUse(wandStack, this);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, Player player, int count) {
        if (count % 5 == 0 && wandStack.getItem() instanceof IWand) {
            IWand wand = (IWand)wandStack.getItem();
            if (this.getBlockState().getBlock() instanceof AbstractManaFontBlock) {
                Source source = ((AbstractManaFontBlock)this.getBlockState().getBlock()).getSource();
                if (source != null) {
                    // Transfer mana from the font to the wand
                    int tap = Math.min(this.mana, wand.getSiphonAmount(wandStack));
                    int leftover = wand.addRealMana(wandStack, source, tap);
                    if (leftover < tap) {
                        this.mana -= (tap - leftover);
                        StatsManager.incrementValue(player, StatsPM.MANA_SIPHONED, tap - leftover);
                        this.setChanged();
                        this.syncTile(true);
                        
                        // Show fancy sparkles
                        if (!player.level.isClientSide) {
                            Vec3 playerPos = player.position();
                            double targetY = playerPos.y + (player.getEyeHeight() / 2.0D);
                            PacketHandler.sendToAllAround(
                                    new ManaSparklePacket(this.worldPosition, playerPos.x, targetY, playerPos.z, 20, source.getColor()), 
                                    this.level.dimension(), 
                                    this.worldPosition, 
                                    32.0D);
                        }
                    }
                }
            }
        }
    }
    
    protected void doRecharge() {
        // Recharge the font over time
        this.mana++;
        if (this.mana > this.getManaCapacity()) {
            this.mana = this.getManaCapacity();
        } else {
            // Sync the tile if its mana total changed
            this.setChanged();
            this.syncTile(true);
        }
    }
}
