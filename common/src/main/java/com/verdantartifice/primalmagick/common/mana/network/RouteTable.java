package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A data object which tracks all the network routes currently known to a given mana network node.
 *
 * @author Daedalus4096
 */
public class RouteTable {
    protected final Table<IManaSupplier, IManaConsumer, Set<Route>> routes = HashBasedTable.create();

    public boolean addRoute(@NotNull Route route) {
        if (!this.routes.contains(route.getOrigin(), route.getTerminus())) {
            this.routes.put(route.getOrigin(), route.getTerminus(), new HashSet<>());
        }
        return this.routes.get(route.getOrigin(), route.getTerminus()).add(route);
    }

    public boolean removeRoute(@NotNull Route route) {
        return this.routes.contains(route.getOrigin(), route.getTerminus()) && this.routes.get(route.getOrigin(), route.getTerminus()).remove(route);
    }

//    public Optional<Route> getRoute(long nodeId) {
//        if (this.routes.containsKey(nodeId)) {
//            return this.routes.get(nodeId).stream().max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
//        } else {
//            return Optional.empty();
//        }
//    }

    public Set<Route> getRoutesForOrigin(@NotNull IManaSupplier origin) {
        return this.routes.row(origin).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    public Set<Route> getRoutesForTerminus(@NotNull IManaConsumer terminus) {
        return this.routes.column(terminus).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    protected void mergeRoutes(@NotNull RouteTable other) {
        other.routes.values().stream().flatMap(Collection::stream).forEach(this::addRoute);
    }

    public void propagateRoutes(@NotNull Set<IManaNetworkNode> processedNodes) {
        // TODO Convert to table structure
//        Set<IManaNetworkNode> targetNodes = this.routes.keySet().stream().filter(Predicate.not(processedNodes::contains)).collect(Collectors.toSet());
//        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
//        newProcessedNodes.addAll(targetNodes);
//        targetNodes.forEach(node -> {
//            node.getRouteTable().mergeRoutes(this);
//            node.getRouteTable().propagateRoutes(newProcessedNodes);
//        });
    }
}
