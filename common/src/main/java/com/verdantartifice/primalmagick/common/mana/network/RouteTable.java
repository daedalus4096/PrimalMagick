package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A data object which tracks all the network routes currently known to a given mana network node.
 *
 * @author Daedalus4096
 */
public class RouteTable {
    protected final Table<Long, Long, Set<Route>> routes = HashBasedTable.create();

    public boolean addRoute(@NotNull Route route) {
        Set<Route> target;
        if (this.routes.contains(route.getOrigin().getNodeId(), route.getTerminus().getNodeId())) {
            target = this.routes.get(route.getOrigin().getNodeId(), route.getTerminus().getNodeId());
        } else {
            target = new HashSet<>();
            this.routes.put(route.getOrigin().getNodeId(), route.getTerminus().getNodeId(), target);
        }
        return target != null && route.isValid() && target.add(route);
    }

    public boolean removeRoute(@NotNull Route route) {
        Set<Route> target = this.routes.get(route.getOrigin().getNodeId(), route.getTerminus().getNodeId());
        return target != null && target.remove(route);
    }

    public Optional<Route> getRoute(@NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
        if (this.routes.contains(origin.getNodeId(), terminus.getNodeId())) {
            Set<Route> routeSet = this.routes.get(origin.getNodeId(), terminus.getNodeId());
            return routeSet == null ? Optional.empty() : routeSet.stream().max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
        } else {
            return Optional.empty();
        }
    }

    public Set<Route> getRoutesForOrigin(@NotNull IManaSupplier origin) {
        return this.routes.row(origin.getNodeId()).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    public Set<Route> getRoutesForTerminus(@NotNull IManaConsumer terminus) {
        return this.routes.column(terminus.getNodeId()).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    protected void mergeRoutes(@NotNull RouteTable other) {
        other.routes.values().stream().flatMap(Collection::stream).forEach(this::addRoute);
    }

    public void propagateRoutes(@NotNull Set<IManaNetworkNode> processedNodes) {
        // Update the route tables of every known node with this table's routes
        Set<Long> processedIds = processedNodes.stream().map(IManaNetworkNode::getNodeId).collect(Collectors.toSet());
        Set<IManaNetworkNode> knownNodes = this.routes.values().stream()
                .flatMap(Collection::stream)
                .flatMap(r -> r.getNodes().stream())
                .filter(node -> !processedIds.contains(node.getNodeId()))
                .collect(Collectors.toSet());
        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
        newProcessedNodes.addAll(knownNodes);
        knownNodes.forEach(node -> {
            node.getRouteTable().mergeRoutes(this);
            node.getRouteTable().propagateRoutes(newProcessedNodes);
        });
    }
}
