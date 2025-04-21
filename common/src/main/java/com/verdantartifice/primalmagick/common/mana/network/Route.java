package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A unidirectional path through a mana distribution network, consisting of a head, a tail, and zero or more relays.
 *
 * @author Daedalus4096
 */
@Immutable
public class Route {
    protected final BlockPos headPosition;
    protected final List<BlockPos> relayPositions;  // Ordered from head to tail
    protected final BlockPos tailPosition;

    // Cached data suppliers
    protected final Function<Level, List<IManaNetworkNode>> nodeSupplier = Util.memoize(this::getNodesInner);
    protected final Function<Level, List<Hop>> hopSupplier = Util.memoize(this::getHopsInner);
    protected final Function<Level, Double> scoreSupplier = Util.memoize(this::getScoreInner);
    protected final Function<Level, Boolean> validSupplier = Util.memoize(this::isValidInner);
    protected final Function<Level, Double> distanceSupplier = Util.memoize(this::getDistanceInner);

//    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail) {
//        this(head, tail, List.of());
//    }
//
//    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail, @NotNull IManaRelay... relays) {
//        this(head, tail, ImmutableList.copyOf(relays));
//    }

//    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail, @NotNull List<IManaRelay> relays) {
//        this.headPosition = Objects.requireNonNull(head).getBlockPos();
//        this.tailPosition = Objects.requireNonNull(tail).getBlockPos();
//        this.relayPositions = ImmutableList.copyOf(relays.stream().filter(Objects::nonNull).map(IManaRelay::getBlockPos).toList());
//    }

    protected Route(@NotNull BlockPos headPosition, @NotNull BlockPos tailPosition, @NotNull List<BlockPos> relayPositions) {
        this.headPosition = Objects.requireNonNull(headPosition);
        this.tailPosition = Objects.requireNonNull(tailPosition);
        this.relayPositions = ImmutableList.copyOf(relayPositions.stream().filter(Objects::nonNull).toList());
    }

    public Route(@NotNull List<BlockPos> nodePositions) {
        int size = nodePositions.size();
        if (size < 2) {
            throw new IllegalArgumentException("nodePositions must contain at least 2 nodes");
        }
        this.headPosition = Objects.requireNonNull(nodePositions.getFirst());
        this.tailPosition = Objects.requireNonNull(nodePositions.getLast());
        this.relayPositions = size >= 3 ? ImmutableList.copyOf(nodePositions.subList(1, size - 1)) : ImmutableList.of();
    }

    public @NotNull BlockPos getHeadPosition() {
        return this.headPosition;
    }

    public @Nullable IManaSupplier getHead(@NotNull Level level) {
        return level.getBlockEntity(this.headPosition) instanceof IManaSupplier supplier ? supplier : null;
    }

    public @NotNull BlockPos getTailPosition() {
        return this.tailPosition;
    }

    public @Nullable IManaConsumer getTail(@NotNull Level level) {
        return level.getBlockEntity(this.tailPosition) instanceof IManaConsumer consumer ? consumer : null;
    }

    public @NotNull List<BlockPos> getRelayPositions() {
        return this.relayPositions;
    }

    public @Nullable List<IManaRelay> getRelays(@NotNull Level level) {
        List<IManaRelay> retVal = this.relayPositions.stream().map(pos -> level.getBlockEntity(pos) instanceof IManaRelay relay ? relay : null).toList();
        return retVal.stream().anyMatch(Objects::isNull) ? null : retVal;
    }

    public double getScore(@NotNull Level level) {
        return this.scoreSupplier.apply(level);
    }

    protected double getScoreInner(@NotNull Level level) {
        // Calculate a score for the route based on throughput, number of hops, and total distance
        List<Hop> hops = this.getHops(level);
        if (hops == null) {
            return 0D;
        } else {
            int throughput = this.getMaxThroughput(level);
            return throughput * throughput * (1D / hops.size()) * (1D / hops.stream().mapToDouble(Hop::getDistanceSqr).sum());
        }
    }

    public int getMaxThroughput(@NotNull Level level) {
        List<Hop> hops = this.getHops(level);
        return hops == null ? 0 : hops.stream().mapToInt(Hop::getManaThroughput).min().orElse(0);
    }

    protected @Nullable List<IManaNetworkNode> getNodes(@NotNull Level level) {
        return this.nodeSupplier.apply(level);
    }

    protected @Nullable List<IManaNetworkNode> getNodesInner(@NotNull Level level) {
        IManaSupplier head = this.getHead(level);
        IManaConsumer tail = this.getTail(level);
        List<IManaRelay> relays = this.getRelays(level);
        if (head != null && tail != null && relays != null) {
            return ImmutableList.<IManaNetworkNode>builder()
                    .add(head)
                    .addAll(relays)
                    .add(tail)
                    .build();
        } else {
            return null;
        }
    }

    protected @NotNull List<BlockPos> getNodePositions() {
        return ImmutableList.<BlockPos>builder()
                .add(this.headPosition)
                .addAll(this.relayPositions)
                .add(this.tailPosition)
                .build();
    }

    public @Nullable List<Hop> getHops(@NotNull Level level) {
        return this.hopSupplier.apply(level);
    }

    protected @Nullable List<Hop> getHopsInner(@NotNull Level level) {
        IManaSupplier head = this.getHead(level);
        IManaConsumer tail = this.getTail(level);
        List<IManaRelay> relays = this.getRelays(level);
        if (head != null && tail != null && relays != null) {
            List<Hop> retVal = new ArrayList<>();
            IManaSupplier supplier = head;

            for (IManaRelay relay : relays) {
                retVal.add(new Hop(supplier, relay));
                supplier = relay;
            }
            retVal.add(new Hop(supplier, tail));

            return retVal;
        } else {
            return null;
        }
    }

    /**
     * Returns a new route with the given supplier at the head of the node list.
     *
     * @param supplier the desired new first node
     * @param level the level in which the route resides
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> pushHead(@Nullable IManaSupplier supplier, @NotNull Level level) {
        if (this.getHead(level) instanceof IManaRelay relay && supplier != null) {
            // If this route starts in a relay, then push the old head to the front of the relay list
            Route retVal = new Route(supplier.getBlockPos(), this.tailPosition, ImmutableList.<BlockPos>builder().add(relay.getBlockPos()).addAll(this.relayPositions).build());
            return retVal.isValid(level) ? Optional.of(retVal) : Optional.empty();
        } else {
            // If this route's existing head is not a relay, then the given supplier cannot be made a new head
            return Optional.empty();
        }
    }

    /**
     * Returns a new route with the given consumer at the tail of the node list.
     *
     * @param consumer the desired new final node
     * @param level the level in which the route resides
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> pushTail(@Nullable IManaConsumer consumer, @NotNull Level level) {
        if (this.getTail(level) instanceof IManaRelay relay && consumer != null) {
            // If this route ends in a relay, then push the old tail to the end of the relay list
            Route retVal = new Route(this.headPosition, consumer.getBlockPos(), ImmutableList.<BlockPos>builder().addAll(this.relayPositions).add(relay.getBlockPos()).build());
            return retVal.isValid(level) ? Optional.of(retVal) : Optional.empty();
        } else {
            // If this route's existing tail is not a relay, then the given consumer cannot be made a new tail
            return Optional.empty();
        }
    }

    /**
     * Returns a new route that connects this route and the given one. The given route is appended to this route, if
     * and only if the terminus of this route is the same as the origin of the given route. The overlapping node is
     * de-duplicated.
     *
     * @param other the route to be appended to this one
     * @param level the level in which the route resides
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> connect(@NotNull Route other, @NotNull Level level) {
        Optional<Route> retVal = Optional.empty();

        level.getProfiler().push("overlapCheck");
        boolean overlaps = this.tailPosition.equals(other.headPosition);
        level.getProfiler().popPush("getTail");
        IManaConsumer tail = this.getTail(level);

        level.getProfiler().popPush("connectCheck");
        if (overlaps && tail instanceof IManaRelay relay) {
            level.getProfiler().popPush("assembleRoute");
            List<BlockPos> newRelays = ImmutableList.<BlockPos>builder()
                    .addAll(this.relayPositions)
                    .add(relay.getBlockPos())
                    .addAll(other.relayPositions)
                    .build();
            Route newRoute = new Route(this.headPosition, other.tailPosition, newRelays);
            level.getProfiler().popPush("isValidCheck");
            if (newRoute.isValid(level)) {
                retVal = Optional.of(newRoute);
            }
        }

        level.getProfiler().pop();
        return retVal;
    }

    /**
     * Tests whether this route is topologically valid, i.e. whether it represents a possibly acceptable path from
     * origin to terminus through a mana network. Does not test whether the route is <strong>active</strong>, in that
     * all nodes are currently extant and loaded in the world.
     *
     * @return whether this route is topologically valid
     */
    public boolean isValid(@NotNull Level level) {
        return this.validSupplier.apply(level);
    }

    protected boolean isValidInner(@NotNull Level level) {
        if (this.headPosition.equals(this.tailPosition)) {
            // A zero-hop route is invalid, as is a circular route going through one or more relays
            return false;
        }

        IManaSupplier head = this.getHead(level);
        IManaConsumer tail = this.getTail(level);
        List<IManaRelay> relays = this.getRelays(level);

        // Test whether the corresponding block entities for each node still exist in the world
        if (head == null || tail == null || relays == null) {
            return false;
        }

        // Disallow any routes between nodes that are both origin and terminus to prevent feedback loops
        if (head.isOrigin() && head instanceof IManaConsumer consumer && consumer.isTerminus() &&
                tail.isTerminus() && tail instanceof IManaSupplier supplier && supplier.isOrigin()) {
            return false;
        }

        // Confirm that there are no cycles in the route by checking for duplicate nodes
        List<IManaNetworkNode> nodes = this.getNodes(level);
        if (nodes == null) {
            return false;
        }
        List<Long> idList = nodes.stream().map(IManaNetworkNode::getNodeId).toList();
        Set<Long> deduplicatedIdSet = new HashSet<>(idList);
        if (idList.size() > deduplicatedIdSet.size()) {
            return false;
        }

        // Test whether all nodes are in transmission range of each other
        List<Hop> hops = this.getHops(level);
        return hops != null && hops.stream().allMatch(Hop::inRange);
    }

    /**
     * Tests whether this route is active and can currently be used to successfully transport mana.
     *
     * @param level the level of the network
     * @return whether this route is active
     */
    public boolean isActive(Level level) {
        // First test that the route is valid at all
        if (!this.isValid(level)) {
            return false;
        }

        // Confirm that all nodes in the route are chunk-loaded
        return level.isLoaded(this.headPosition) && level.isLoaded(this.tailPosition) && this.relayPositions.stream().allMatch(level::isLoaded);
    }

    public boolean canRoute(@NotNull Source source, @NotNull Level level) {
        IManaSupplier head = this.getHead(level);
        IManaConsumer tail = this.getTail(level);
        List<IManaRelay> relays = this.getRelays(level);
        return head != null && head.canSupply(source) &&
                tail != null && tail.canConsume(source) &&
                relays != null && relays.stream().allMatch(r -> r.canRelay(source));
    }

    public double getDistance(@NotNull Level level) {
        return this.distanceSupplier.apply(level);
    }

    protected double getDistanceInner(@NotNull Level level) {
        List<Hop> hops = this.getHops(level);
        return hops == null ? Double.POSITIVE_INFINITY : hops.stream().mapToDouble(Route.Hop::getDistanceSqr).map(Math::sqrt).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Route route)) return false;
        return Objects.equals(headPosition, route.headPosition) && Objects.equals(relayPositions, route.relayPositions) && Objects.equals(tailPosition, route.tailPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headPosition, relayPositions, tailPosition);
    }

    @Override
    public String toString() {
        return "Route{" + this.getNodePositions().stream().map(pos -> "[" + pos.toShortString() + "]").collect(Collectors.joining("->")) + "}";
    }

    public record Hop(@NotNull IManaSupplier supplier, @NotNull IManaConsumer consumer) {
        public double getDistanceSqr() {
            return this.supplier.getBlockPos().distSqr(this.consumer.getBlockPos());
        }

        public boolean inRange() {
            double distanceSqr = this.getDistanceSqr();
            int supplierRange = this.supplier.getNetworkRange();
            int consumerRange = this.consumer.getNetworkRange();
            return ((supplierRange * supplierRange) >= distanceSqr) && ((consumerRange * consumerRange) >= distanceSqr);
        }

        public int getManaThroughput() {
            return Math.min(this.supplier.getManaThroughput(), this.consumer.getManaThroughput());
        }
    }
}
