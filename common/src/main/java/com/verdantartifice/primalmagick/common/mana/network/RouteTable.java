package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.base.Suppliers;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

//    protected final Table<BlockPos, BlockPos, Set<Route>> routes = HashBasedTable.create();
    protected final NetworkGraph graph = new NetworkGraph();

    protected int ticksExisted = 0;
    protected int nextCheck = calculateNextCheck(this.ticksExisted);

//    protected Supplier<Set<Route>> allRoutesSupplier = Suppliers.memoize(this::getAllRoutesInner);

    protected void invalidate() {
//        this.allRoutesSupplier = Suppliers.memoize(this::getAllRoutesInner);
    }

    public void tick(@NotNull final Level level) {
        if (this.ticksExisted >= this.nextCheck) {
//            this.cullInactiveRoutes(level);
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
        // Network graphs are consumer-first, so add the edge in reverse order
        synchronized (this.graph) {
            this.graph.addEdge(consumer, supplier);
        }
    }

//    public boolean add(@NotNull Route route) {
//        boolean retVal = this.addInner(route);
//        if (retVal) {
//            this.invalidate();
//        }
//        return retVal;
//    }
//
//    private boolean addInner(@NotNull Route route) {
//        Set<Route> target;
//        synchronized (this.routes) {
//            if (this.routes.contains(route.getHeadPosition(), route.getTailPosition())) {
//                target = this.routes.get(route.getHeadPosition(), route.getTailPosition());
//            } else {
//                target = ConcurrentHashMap.newKeySet();
//                this.routes.put(route.getHeadPosition(), route.getTailPosition(), target);
//            }
//        }
//
//        return target != null && target.add(route);
//    }
//
//    public boolean addAll(@NotNull Collection<Route> routes) {
//        boolean modified = false;
//        for (Route route : routes) {
//            if (this.addInner(route)) {
//                modified = true;
//            }
//        }
//        if (modified) {
//            this.invalidate();
//        }
//        return modified;
//    }

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
        synchronized (this.graph) {
            // Network graphs are consumer-first, so find in reverse order
            return this.graph.findRoute(terminus, origin, sourceOpt, level);
        }
    }

    public Set<Route> getAllRoutes(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaConsumer terminus) {
        synchronized (this.graph) {
            return this.graph.findAllRoutes(terminus, sourceOpt, level);
        }
    }

//    public Optional<Route> getRoute(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
//        boolean contains;
//        synchronized (this.routes) {
//            contains = this.routes.contains(origin.getBlockPos(), terminus.getBlockPos());
//        }
//        if (contains) {
//            level.getProfiler().push("getRouteSet");
//            Set<Route> routeSet;
//            synchronized (this.routes) {
//                routeSet = this.routes.get(origin.getBlockPos(), terminus.getBlockPos());
//            }
//            level.getProfiler().popPush("scoreRoutes");
//            Optional<Route> retVal = routeSet == null ?
//                    Optional.empty() :
//                    routeSet.stream()
//                            .filter(r -> sourceOpt.isEmpty() || r.canRoute(sourceOpt.get(), level))
//                            .filter(r -> r.isActive(level))
//                            .max(Comparator.<Route, Double>comparing(route -> route.getScore(level)).thenComparing(Route::hashCode));
//            level.getProfiler().pop();
//            return retVal;
//        } else {
//            return Optional.empty();
//        }
//    }

//    public Set<Route> getRoutesForHead(@NotNull IManaSupplier head) {
//        return this.getRoutesForHead(head.getBlockPos());
//    }
//
//    public Set<Route> getRoutesForHead(@NotNull BlockPos headPos) {
//        synchronized (this.routes) {
//            return this.routes.row(headPos).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
//        }
//    }
//
//    public Set<IManaConsumer> getLinkedTerminuses(@NotNull IManaSupplier origin, @NotNull Level level) {
//        return this.getRoutesForHead(origin).stream().map(route -> route.getTail(level)).filter(Objects::nonNull).filter(IManaConsumer::isTerminus).collect(Collectors.toSet());
//    }
//
//    public Set<Route> getRoutesForTail(@NotNull IManaConsumer tail) {
//        return this.getRoutesForTail(tail.getBlockPos());
//    }
//
//    public Set<Route> getRoutesForTail(@NotNull BlockPos tailPos) {
//        synchronized (this.routes) {
//            return this.routes.column(tailPos).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
//        }
//    }
//
//    public Set<IManaSupplier> getLinkedOrigins(@NotNull IManaConsumer terminus, @NotNull Level level) {
//        return this.getRoutesForTail(terminus).stream().map(route -> route.getHead(level)).filter(Objects::nonNull).filter(IManaSupplier::isOrigin).collect(Collectors.toSet());
//    }

//    protected void cullInactiveRoutes(@NotNull Level level) {
//        level.getProfiler().push("cullInactiveRoutes");
//        boolean anyRemoved = false;
//        synchronized (this.routes) {
//            for (Set<Route> set : this.routes.values()) {
//                if (set.removeIf(r -> !r.isActive(level))) {
//                    anyRemoved = true;
//                }
//            }
//        }
//        if (anyRemoved) {
//            this.invalidate();
//        }
//        level.getProfiler().pop();
//    }
}
