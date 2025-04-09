package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final Table<Long, Long, Set<Route>> routes = HashBasedTable.create();

    public void clear() {
        this.routes.clear();
    }

    public void copyFrom(final RouteTable other) {
        this.clear();
        other.routes.values().stream().flatMap(Collection::stream).forEach(this::addRoute);
    }

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

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Source source, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus, @NotNull IManaNetworkNode owner) {
        if (this.routes.contains(origin.getNodeId(), terminus.getNodeId())) {
            Set<Route> routeSet = this.routes.get(origin.getNodeId(), terminus.getNodeId());
            Set<Route> toForget = new HashSet<>();
            Optional<Route> retVal = routeSet == null ?
                    Optional.empty() :
                    routeSet.stream()
                            .filter(r -> r.canRoute(source))
                            .filter(r -> {
                                if (r.isActive(level)) {
                                    return true;
                                } else {
                                    toForget.add(r);
                                    return false;
                                }
                            })
                            .max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
            this.forgetRoutes(toForget, Set.of(owner));
            return retVal;
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

    public Set<IManaSupplier> getLinkedOrigins(@NotNull IManaConsumer terminus) {
        return this.getRoutesForTerminus(terminus).stream().map(Route::getOrigin).collect(Collectors.toSet());
    }

    protected void mergeRoutes(@NotNull RouteTable other) {
        other.routes.values().stream().flatMap(Collection::stream).forEach(this::addRoute);
    }

    public void propagateRoutes(@NotNull Set<IManaNetworkNode> processedNodes) {
        // Update the route tables of every known node with this table's routes
        Set<IManaNetworkNode> knownNodes = this.getKnownNodes(processedNodes);
        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
        newProcessedNodes.addAll(knownNodes);
        knownNodes.forEach(node -> {
            node.getRouteTable().mergeRoutes(this);
            node.getRouteTable().propagateRoutes(newProcessedNodes);
        });
    }

    public void forgetRoutes(@NotNull Set<Route> toForget, @NotNull Set<IManaNetworkNode> processedNodes) {
        // Update the route tables of every known node to remove the given routes
        Set<IManaNetworkNode> knownNodes = this.getKnownNodes(processedNodes);
        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
        newProcessedNodes.addAll(knownNodes);
        knownNodes.forEach(node -> {
            toForget.forEach(node.getRouteTable()::removeRoute);
            node.getRouteTable().forgetRoutes(toForget, newProcessedNodes);
        });
    }

    protected Set<IManaNetworkNode> getKnownNodes(@NotNull Set<IManaNetworkNode> ignore) {
        Set<Long> ignoreIds = ignore.stream().map(IManaNetworkNode::getNodeId).collect(Collectors.toSet());
        return this.routes.values().stream()
                .flatMap(Collection::stream)
                .flatMap(r -> r.getNodes().stream())
                .filter(node -> !ignoreIds.contains(node.getNodeId()))
                .collect(Collectors.toSet());
    }
}
