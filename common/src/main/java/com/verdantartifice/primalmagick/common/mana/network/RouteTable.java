package com.verdantartifice.primalmagick.common.mana.network;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A data object which tracks all the network routes currently known to a given mana network node.
 *
 * @author Daedalus4096
 */
public class RouteTable {
    protected final Map<Long, Set<Route>> routes = new ConcurrentHashMap<>();

    public boolean addRoute(@NotNull Route route) {
        return this.routes.computeIfAbsent(route.getTerminus().getNodeId(), k -> ConcurrentHashMap.newKeySet()).add(route);
    }

    public boolean removeRoute(@NotNull Route route) {
        return this.routes.containsKey(route.getTerminus().getNodeId()) && this.routes.get(route.getTerminus().getNodeId()).remove(route);
    }

    public Optional<Route> getRoute(long nodeId) {
        if (this.routes.containsKey(nodeId)) {
            return this.routes.get(nodeId).stream().max(Comparator.comparing(Route::getScore));
        } else {
            return Optional.empty();
        }
    }
}
