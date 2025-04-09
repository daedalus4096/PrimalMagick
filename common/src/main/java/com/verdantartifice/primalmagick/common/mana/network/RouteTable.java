package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    public RouteTable() {}

    public RouteTable(List<Route> routes) {
        routes.stream().filter(Objects::nonNull).forEach(this::addRoute);
    }

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

    public Optional<Route> getRoute(@NotNull Level level, @NotNull Source source, @NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
        if (this.routes.contains(origin.getNodeId(), terminus.getNodeId())) {
            Set<Route> routeSet = this.routes.get(origin.getNodeId(), terminus.getNodeId());
            return routeSet == null ?
                    Optional.empty() :
                    routeSet.stream()
                            .filter(r -> r.canRoute(source) && r.isActive(level))
                            .max(Comparator.comparing(Route::getScore).thenComparing(Route::hashCode));
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

    public @NotNull Optional<Tag> serializeNBT(HolderLookup.Provider pRegistries) {
        RegistryOps<Tag> registryOps = pRegistries.createSerializationContext(NbtOps.INSTANCE);
        return RouteTable.Serialized.CODEC.encodeStart(registryOps, this.serialize()).resultOrPartial(LOGGER::error);
    }

    public void deserializeNBT(HolderLookup.Provider pRegistries, Tag pTag, @NotNull Level pLevel) {
        RegistryOps<Tag> registryOps = pRegistries.createSerializationContext(NbtOps.INSTANCE);
        RouteTable.Serialized.CODEC.parse(registryOps, pTag)
                .resultOrPartial(LOGGER::error)
                .map(ser -> ser.deserialize(pLevel))
                .ifPresent(this::copyFrom);
    }

    protected RouteTable.Serialized serialize() {
        return new RouteTable.Serialized(this);
    }

    public static class Serialized {
        public static final Codec<RouteTable.Serialized> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Route.Serialized.CODEC.listOf().fieldOf("serializedRoutes").forGetter(s -> s.serializedRoutes)
            ).apply(instance, RouteTable.Serialized::new));

        protected final List<Route.Serialized> serializedRoutes;

        protected Serialized(final RouteTable routeTable) {
            this.serializedRoutes = routeTable.routes.values().stream()
                    .flatMap(Collection::stream)
                    .map(Route::serialize)
                    .toList();
        }

        protected Serialized(List<Route.Serialized> serializedRoutes) {
            this.serializedRoutes = ImmutableList.copyOf(serializedRoutes);
        }

        public RouteTable deserialize(@NotNull Level level) {
            return new RouteTable(this.serializedRoutes.stream()
                    .map(ser -> ser.deserialize(level))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList());
        }
    }
}
