package com.verdantartifice.primalmagick.common.mana.network;

import org.jetbrains.annotations.NotNull;

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
    protected final Map<IManaNetworkNode, Set<Route>> routes = new ConcurrentHashMap<>();

    public boolean addRoute(@NotNull Route route) {
        return this.routes.computeIfAbsent(route.getTerminus(), k -> ConcurrentHashMap.newKeySet()).add(route);
    }

    public boolean removeRoute(@NotNull Route route) {
        return this.routes.containsKey(route.getTerminus()) && this.routes.get(route.getTerminus()).remove(route);
    }

    public Optional<Route> getRoute(long nodeId) {
        if (this.routes.containsKey(nodeId)) {
            return this.routes.get(nodeId).stream().max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
        } else {
            return Optional.empty();
        }
    }

    protected void mergeRoutes(@NotNull RouteTable other) {
        other.routes.entrySet().stream().flatMap(e -> e.getValue().stream()).forEach(this::addRoute);
    }

    public void propagateRoutes(@NotNull Set<IManaNetworkNode> processedNodes) {
        Set<IManaNetworkNode> targetNodes = this.routes.keySet().stream().filter(Predicate.not(processedNodes::contains)).collect(Collectors.toSet());
        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
        newProcessedNodes.addAll(targetNodes);
        targetNodes.forEach(node -> {
            node.getRouteTable().mergeRoutes(this);
            node.getRouteTable().propagateRoutes(newProcessedNodes);
        });
    }
}
