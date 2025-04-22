package com.verdantartifice.primalmagick.common.mana.network;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * A data object which tracks all the network routes currently known to a given mana network node.
 *
 * @author Daedalus4096
 */
public class RouteTable {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final Random RANDOM = new Random();
    protected static final int CHECK_INTERVAL = 100;
    protected static final int JITTER_AMOUNT = 20;

    protected final NetworkGraph graph = new NetworkGraph();

    protected int ticksExisted = 0;
    protected int nextCheck = calculateNextCheck(this.ticksExisted);

    protected void invalidate() {
    }

    public void tick(@NotNull final Level level) {
        if (this.ticksExisted >= this.nextCheck) {
            this.cullInactiveNodes(level);
            this.nextCheck = calculateNextCheck(this.ticksExisted);
        }
        this.ticksExisted++;
    }

    protected static int calculateNextCheck(int currentValue) {
        return currentValue + CHECK_INTERVAL + RANDOM.nextInt(JITTER_AMOUNT);
    }

    public void clear() {
        synchronized (this.graph) {
            this.graph.clear();
        }
        this.invalidate();
    }

    public void add(@NotNull IManaSupplier supplier, @NotNull IManaConsumer consumer) {
        boolean added;
        synchronized (this.graph) {
            // Network graphs are consumer-first, so add the edge in reverse order
            added = this.graph.addEdge(consumer.getBlockPos(), supplier.getBlockPos());
        }
        if (added) {
            this.invalidate();
        }
    }

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
        synchronized (this.graph) {
            // Network graphs are consumer-first, so find in reverse order
            return this.graph.findRoute(terminus.getBlockPos(), origin.getBlockPos(), sourceOpt, level);
        }
    }

    public Set<Route> getAllRoutes(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaNetworkNode terminus) {
        synchronized (this.graph) {
            return this.graph.findAllRoutes(terminus.getBlockPos(), sourceOpt, level);
        }
    }

    protected void cullInactiveNodes(@NotNull Level level) {
        level.getProfiler().push("cullInactiveRoutes");
        boolean anyRemoved;
        synchronized (this.graph) {
            anyRemoved = this.graph.removeIf(pos -> !level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof IManaNetworkNode));
        }
        if (anyRemoved) {
            this.invalidate();
        }
        level.getProfiler().pop();
    }
}
