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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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

    protected final Table<Long, Long, Set<Route>> routes = HashBasedTable.create();
    protected final Set<IManaNetworkNode> knownNodes = new HashSet<>();
    protected int ticksExisted = 0;
    protected int nextCheck = calculateNextCheck(this.ticksExisted);
    protected boolean active = false;

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }

    public void tick(@NotNull final Level level) {
        if (this.ticksExisted >= this.nextCheck) {
            if (this.isActive()) {
                // Only cull inactive routes if the route table has been fully bootstrapped and activated
                this.cullInactiveRoutes(level);
            }
            this.nextCheck = calculateNextCheck(this.ticksExisted);
        }
        this.ticksExisted++;
    }

    protected static int calculateNextCheck(int currentValue) {
        return currentValue + CHECK_INTERVAL + RANDOM.nextInt(JITTER_AMOUNT);
    }

    public void clear() {
        this.routes.clear();
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

        boolean retVal = target != null && route.isValid() && target.add(route);
        if (retVal) {
            this.knownNodes.addAll(route.getNodes());
        }
        return retVal;
    }

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Optional<Source> sourceOpt, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus, @NotNull IManaNetworkNode owner) {
        if (this.routes.contains(origin.getNodeId(), terminus.getNodeId())) {
            level.getProfiler().push("getRouteSet");
            Set<Route> routeSet = this.routes.get(origin.getNodeId(), terminus.getNodeId());
            level.getProfiler().popPush("scoreRoutes");
            Optional<Route> retVal = routeSet == null ?
                    Optional.empty() :
                    routeSet.stream()
                            .filter(r -> sourceOpt.isEmpty() || r.canRoute(sourceOpt.get()))
                            .filter(r -> r.isActive(level))
                            .max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
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

    protected void mergeRoutes(@NotNull Collection<Route> otherRoutes) {
        otherRoutes.forEach(this::addRoute);
    }

    public void propagateRoutes(@NotNull Level level, @NotNull Set<IManaNetworkNode> processedNodes) {
        // Update the route tables of every known node with this table's routes
        level.getProfiler().push("getKnownNodes");
        Set<IManaNetworkNode> knownNodes = this.getKnownNodes(processedNodes);
        level.getProfiler().popPush("newProcessedNodes");
        Set<IManaNetworkNode> newProcessedNodes = new HashSet<>(processedNodes);
        newProcessedNodes.addAll(knownNodes);
        level.getProfiler().popPush("collectSelfRoutes");
        List<Route> selfRoutes = this.routes.values().stream().flatMap(Collection::stream).toList();
        level.getProfiler().pop();
        knownNodes.forEach(node -> {
            level.getProfiler().push("mergeRoutes");
            node.getRouteTable().mergeRoutes(selfRoutes);
            level.getProfiler().pop();
            node.getRouteTable().propagateRoutes(level, newProcessedNodes);
        });
    }

    protected Set<IManaNetworkNode> getKnownNodes(@NotNull Set<IManaNetworkNode> ignore) {
        Set<Long> ignoreIds = ignore.stream().map(IManaNetworkNode::getNodeId).collect(Collectors.toSet());
        return this.knownNodes.stream().filter(node -> !ignoreIds.contains(node.getNodeId())).collect(Collectors.toSet());
    }

    protected void cullInactiveRoutes(@NotNull Level level) {
        level.getProfiler().push("cullInactiveRoutes");
        this.routes.values().forEach(set -> set.removeIf(route -> !route.isActive(level)));
        level.getProfiler().pop();
    }
}
