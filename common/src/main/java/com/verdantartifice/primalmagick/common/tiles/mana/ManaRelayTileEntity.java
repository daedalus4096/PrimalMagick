package com.verdantartifice.primalmagick.common.tiles.mana;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.mana.network.IManaRelay;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public abstract class ManaRelayTileEntity extends AbstractTilePM implements IManaRelay {
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final RouteTable routeTable = new RouteTable();

    public ManaRelayTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.MANA_RELAY.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        // Deserialize the tile's mana networking route table
        RegistryOps<Tag> registryOps = pRegistries.createSerializationContext(NbtOps.INSTANCE);
        RouteTable.Serialized.CODEC.parse(registryOps, pTag.get("RouteTable"))
                .resultOrPartial(LOGGER::error)
                .ifPresent(ser -> {
                    if (this.getLevel() != null) {
                        this.routeTable.copyFrom(ser.deserialize(this.getLevel()));
                    } else {
                        LOGGER.warn("Unable to deserialize route table, no level present");
                    }
                });
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        // Serialize the tile's mana networking route table
        RegistryOps<Tag> registryOps = pRegistries.createSerializationContext(NbtOps.INSTANCE);
        RouteTable.Serialized.CODEC.encodeStart(registryOps, this.routeTable.serialize())
                .resultOrPartial(LOGGER::error)
                .ifPresent(tag -> pTag.put("RouteTable", tag));
    }

    @Override
    public boolean canRelay(Source source) {
        return true;
    }

    @Override
    public int getNetworkRange() {
        return 16;
    }

    @Override
    public int getManaThroughput() {
        return 3200;
    }

    @Override
    public @NotNull RouteTable getRouteTable() {
        return this.routeTable;
    }
}
