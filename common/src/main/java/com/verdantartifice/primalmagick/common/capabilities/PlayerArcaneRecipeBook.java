package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncArcaneRecipeBookPacket;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Default implementation for the player arcane recipe book capability.
 * 
 * @author Daedalus4096
 */
public class PlayerArcaneRecipeBook implements IPlayerArcaneRecipeBook {
    private final ArcaneRecipeBook book = new ArcaneRecipeBook();
    private long syncTimestamp = 0L;    // Last timestamp at which this capability received a sync from the server

    @Override
    public ArcaneRecipeBook get() {
        return this.book;
    }

    @Override
    public void sync(ServerPlayer player) {
        if (player != null) {
            PacketHandler.sendToPlayer(new SyncArcaneRecipeBookPacket(player), player);
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag retVal = new CompoundTag();
        retVal.put("Book", this.book.toNbt());
        retVal.putLong("SyncTimestamp", System.currentTimeMillis());
        return retVal;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt, RecipeManager recipeManager) {
        if (nbt == null || nbt.getLong("SyncTimestamp") <= this.syncTimestamp) {
            return;
        }
        this.syncTimestamp = nbt.getLong("SyncTimestamp");
        this.book.fromNbt(nbt.getCompound("Book"), recipeManager);
    }
}
