package com.verdantartifice.primalmagick.common.mana.network;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.FunctionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;
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

    private TriFunction<Level, Optional<Source>, BlockPos, Set<Route>> allRoutesCache = FunctionUtils.memoize(this::getAllRoutesInner);

    public void invalidate() {
        this.allRoutesCache = FunctionUtils.memoize(this::getAllRoutesInner);
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
        // Network graphs are consumer-first, so search by terminus
        return this.allRoutesCache.apply(level, sourceOpt, terminus.getBlockPos()).stream()
                .filter(route -> route.getTailPosition().equals(origin.getBlockPos()))
                .findFirst();
    }

    public Set<Route> getAllRoutes(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaNetworkNode terminus) {
        return this.allRoutesCache.apply(level, sourceOpt, terminus.getBlockPos());
    }

    private Set<Route> getAllRoutesInner(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull BlockPos terminusPos) {
        synchronized (this.graph) {
            return this.graph.findAllRoutes(terminusPos, sourceOpt, level);
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
