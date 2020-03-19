package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a ritual altar tile entity.  Provides the core functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock}
 */
public class RitualAltarTileEntity extends TileInventoryPM implements ITickableTileEntity, IInteractWithWand {
    protected boolean active = false;
    protected int activeCount = 0;
    protected UUID activePlayerId = null;
    protected PlayerEntity activePlayerCache = null;
    
    public RitualAltarTileEntity() {
        super(TileEntityTypesPM.RITUAL_ALTAR.get(), 1);
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public int getActiveCount() {
        return this.activeCount;
    }
    
    @Nullable
    public PlayerEntity getActivePlayer() {
        if (this.activePlayerCache == null && this.activePlayerId != null && this.world instanceof ServerWorld) {
            // If the active player cache is empty, find the entity matching the caster's unique ID
            ServerPlayerEntity player = ((ServerWorld)this.world).getServer().getPlayerList().getPlayerByUUID(this.activePlayerId);
            if (player != null) {
                this.activePlayerCache = player;
            } else {
                this.activePlayerId = null;
            }
        }
        return this.activePlayerCache;
    }
    
    public void setActivePlayer(@Nullable PlayerEntity player) {
        if (player == null) {
            this.activePlayerCache = null;
            this.activePlayerId = null;
        } else {
            this.activePlayerCache = player;
            this.activePlayerId = player.getUniqueID();
        }
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the altar's stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.active = compound.getBoolean("Active");
        this.activeCount = compound.getInt("ActiveCount");
        
        this.activePlayerCache = null;
        if (compound.contains("ActivePlayer", Constants.NBT.TAG_COMPOUND)) {
            this.activePlayerId = NBTUtil.readUniqueId(compound.getCompound("ActivePlayer"));
        } else {
            this.activePlayerId = null;
        }
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Active", this.active);
        compound.putInt("ActiveCount", this.activeCount);
        if (this.activePlayerId != null) {
            compound.put("ActivePlayer", NBTUtil.writeUniqueId(this.activePlayerId));
        }
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && this.active) {
            this.activeCount++;
            if (this.activeCount >= 100) {
                if (this.getActivePlayer() != null) {
                    this.getActivePlayer().sendStatusMessage(new StringTextComponent("Ritual complete!"), false);
                }
                this.active = false;
                this.activeCount = 0;
                this.setActivePlayer(null);
                this.markDirty();
                this.syncTile(false);
            }
        }
    }

    @Override
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction) {
        if (!this.world.isRemote && wandStack.getItem() instanceof IWand) {
            if (this.active) {
                this.active = false;
                player.sendStatusMessage(new StringTextComponent("Ritual canceled"), false);
            } else {
                this.active = true;
                player.sendStatusMessage(new StringTextComponent("Ritual started!"), false);
            }
            this.activeCount = 0;
            this.setActivePlayer(player);
            this.markDirty();
            this.syncTile(false);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, PlayerEntity player, int count) {
        // Do nothing; ritual altars don't support wand channeling
    }
}
