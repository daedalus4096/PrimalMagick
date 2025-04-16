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

    public Set<Route> getAllRoutes() {
        return this.routes.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public boolean addRoute(@NotNull Route route) {
        Set<Route> target;
        if (this.routes.contains(route.getHead().getNodeId(), route.getTail().getNodeId())) {
            target = this.routes.get(route.getHead().getNodeId(), route.getTail().getNodeId());
        } else {
            target = new HashSet<>();
            this.routes.put(route.getHead().getNodeId(), route.getTail().getNodeId(), target);
        }
        return target != null && route.isValid() && target.add(route);
    }

    public boolean removeRoute(@NotNull Route route) {
        Set<Route> target = this.routes.get(route.getHead().getNodeId(), route.getTail().getNodeId());
        return target != null && target.remove(route);
    }

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus, @NotNull IManaNetworkNode owner) {
        if (this.routes.contains(origin.getNodeId(), terminus.getNodeId())) {
            level.getProfiler().push("getRouteSet");
            Set<Route> routeSet = this.routes.get(origin.getNodeId(), terminus.getNodeId());
            Set<Route> toForget = new HashSet<>();
            level.getProfiler().popPush("scoreRoutes");
            Optional<Route> retVal = routeSet == null ?
                    Optional.empty() :
                    routeSet.stream()
                            .filter(r -> sourceOpt.isEmpty() || r.canRoute(sourceOpt.get()))
                            .filter(r -> {
                                if (r.isActive(level)) {
                                    return true;
                                } else {
                                    toForget.add(r);
                                    return false;
                                }
                            })
                            .max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
            level.getProfiler().popPush("forgetRoutes");
            this.forgetRoutes(toForget, Set.of(owner));
            level.getProfiler().pop();
            return retVal;
        } else {
            return Optional.empty();
        }
    }

    public Set<Route> getRoutesForHead(@NotNull IManaSupplier head) {
        return this.routes.row(head.getNodeId()).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    public Set<IManaConsumer> getLinkedTerminuses(@NotNull IManaSupplier origin) {
        return this.getRoutesForHead(origin).stream().map(Route::getTail).filter(IManaConsumer::isTerminus).collect(Collectors.toSet());
    }

    public Set<Route> getRoutesForTail(@NotNull IManaConsumer tail) {
        return this.routes.column(tail.getNodeId()).entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toSet());
    }

    public Set<IManaSupplier> getLinkedOrigins(@NotNull IManaConsumer terminus) {
        return this.getRoutesForTail(terminus).stream().map(Route::getHead).filter(IManaSupplier::isOrigin).collect(Collectors.toSet());
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
        if (!toForget.isEmpty()) {
            Set<IManaNetworkNode> knownNodes = this.getKnownNodes(processedNodes);
            Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
            newProcessedNodes.addAll(knownNodes);
            knownNodes.forEach(node -> {
                toForget.forEach(node.getRouteTable()::removeRoute);
                node.getRouteTable().forgetRoutes(toForget, newProcessedNodes);
            });
        }
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
