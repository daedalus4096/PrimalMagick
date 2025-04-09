package com.verdantartifice.primalmagick.common.tiles.mana;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.mana.network.IManaRelay;
import com.verdantartifice.primalmagick.common.mana.network.RouteTable;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
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
