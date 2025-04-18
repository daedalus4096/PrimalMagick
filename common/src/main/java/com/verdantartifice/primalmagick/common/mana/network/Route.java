package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A unidirectional path through a mana distribution network, consisting of a head, a tail, and zero or more relays.
 *
 * @author Daedalus4096
 */
@Immutable
public class Route {
    protected final IManaSupplier head;
    protected final List<IManaRelay> relays;    // Ordered from origin to terminus
    protected final IManaConsumer tail;

    // Cached data suppliers
    protected final Supplier<List<IManaNetworkNode>> nodeSupplier = Suppliers.memoize(this::getNodesInner);
    protected final Supplier<List<Hop>> hopSupplier = Suppliers.memoize(this::getHopsInner);
    protected final Supplier<Double> scoreSupplier = Suppliers.memoize(this::getScoreInner);
    protected final Supplier<Boolean> validSupplier = Suppliers.memoize(this::isValidInner);
    protected final Supplier<Boolean> completeSupplier = Suppliers.memoize(this::isCompleteInner);
    protected final Supplier<Double> distanceSupplier = Suppliers.memoize(this::getDistanceInner);

    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail) {
        this(head, tail, List.of());
    }

    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail, @NotNull IManaRelay... relays) {
        this(head, tail, ImmutableList.copyOf(relays));
    }

    public Route(@NotNull IManaSupplier head, @NotNull IManaConsumer tail, @NotNull List<IManaRelay> relays) {
        this.head = Objects.requireNonNull(head);
        this.tail = Objects.requireNonNull(tail);
        this.relays = ImmutableList.copyOf(relays.stream().filter(Objects::nonNull).toList());
    }

    public IManaSupplier getHead() {
        return this.head;
    }

    public IManaConsumer getTail() {
        return this.tail;
    }

    public List<IManaRelay> getRelays() {
        return this.relays;
    }

    public double getScore() {
        return this.scoreSupplier.get();
    }

    protected double getScoreInner() {
        // Calculate a score for the route based on throughput, number of hops, and total distance
        return this.getMaxThroughput() * this.getMaxThroughput() * (1D / this.getHops().size()) * (1D / this.getHops().stream().mapToDouble(Hop::getDistanceSqr).sum());
    }

    public int getMaxThroughput() {
        return this.getHops().stream().mapToInt(Hop::getManaThroughput).min().orElse(0);
    }

    protected List<IManaNetworkNode> getNodes() {
        return this.nodeSupplier.get();
    }

    protected List<IManaNetworkNode> getNodesInner() {
        return ImmutableList.<IManaNetworkNode>builder()
                .add(this.head)
                .addAll(this.relays)
                .add(this.tail)
                .build();
    }

    public List<Hop> getHops() {
        return this.hopSupplier.get();
    }

    protected List<Hop> getHopsInner() {
        List<Hop> retVal = new ArrayList<>();
        IManaSupplier supplier = this.head;

        for (IManaRelay relay : this.relays) {
            retVal.add(new Hop(supplier, relay));
            supplier = relay;
        }
        retVal.add(new Hop(supplier, this.tail));

        return retVal;
    }

    /**
     * Returns a new route with the given supplier at the head of the node list.
     *
     * @param supplier the desired new first node
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> pushHead(IManaSupplier supplier) {
        if (this.head instanceof IManaRelay relay) {
            // If this route starts in a relay, then push the old head to the front of the relay list
            Route retVal = new Route(supplier, this.tail, ImmutableList.<IManaRelay>builder().add(relay).addAll(this.relays).build());
            return retVal.isValid() ? Optional.of(retVal) : Optional.empty();
        } else {
            // If this route's existing head is not a relay, then the given supplier cannot be made a new head
            return Optional.empty();
        }
    }

    /**
     * Returns a new route with the given consumer at the tail of the node list.
     *
     * @param consumer the desired new final node
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> pushTail(IManaConsumer consumer) {
        if (this.tail instanceof IManaRelay relay) {
            // If this route ends in a relay, then push the old tail to the end of the relay list
            Route retVal = new Route(this.head, consumer, ImmutableList.<IManaRelay>builder().addAll(this.relays).add(relay).build());
            return retVal.isValid() ? Optional.of(retVal) : Optional.empty();
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
     * @return an optional containing the new route, or empty if such a route is not valid
     */
    public Optional<Route> connect(Route other) {
        if (this.tail.getNodeId() == other.getHead().getNodeId() && this.tail instanceof IManaRelay relay) {
            List<IManaRelay> newRelays = ImmutableList.<IManaRelay>builder()
                    .addAll(this.relays)
                    .add(relay)
                    .addAll(other.relays)
                    .build();
            Route retVal = new Route(this.head, other.tail, newRelays);
            return retVal.isValid() ? Optional.of(retVal) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    /**
     * Tests whether this route is topologically valid, i.e. whether it represents a possibly acceptable path from
     * origin to terminus through a mana network. Does not test whether the route is <strong>active</strong>, in that
     * all nodes are currently extant and loaded in the world.
     *
     * @return whether this route is topologically valid
     */
    public boolean isValid() {
        return this.validSupplier.get();
    }

    protected boolean isValidInner() {
        if (this.head.getNodeId() == this.tail.getNodeId()) {
            // A zero-hop route is invalid, as is a circular route going through one or more relays
            return false;
        }

        // Disallow any routes between nodes that are both origin and terminus to prevent feedback loops
        if (this.head.isOrigin() && this.head instanceof IManaConsumer consumer && consumer.isTerminus() &&
                this.tail.isTerminus() && this.tail instanceof IManaSupplier supplier && supplier.isOrigin()) {
            return false;
        }

        // Confirm that there are no cycles in the route by checking for duplicate nodes
        List<Long> idList = this.getNodes().stream().map(IManaNetworkNode::getNodeId).toList();
        Set<Long> deduplicatedIdSet = new HashSet<>(idList);
        if (idList.size() > deduplicatedIdSet.size()) {
            return false;
        }

        // Test whether all nodes are in transmission range of each other
        return this.getHops().stream().allMatch(Hop::inRange);
    }

    /**
     * Tests whether this route is complete, in that it's valid, its start node is a proper origin, and its end node is
     * a proper terminus.
     *
     * @return whether this route is complete
     */
    public boolean isComplete() {
        return this.completeSupplier.get();
    }

    protected boolean isCompleteInner() {
        return this.isValid() && this.head.isOrigin() && this.tail.isTerminus();
    }

    /**
     * Tests whether this route is active and can currently be used to successfully transport mana.
     *
     * @param level the level of the network
     * @return whether this route is active
     */
    public boolean isActive(Level level) {
        // First test that the route is valid at all
        if (!this.isValid()) {
            return false;
        }

        // Test whether the corresponding block entities for each node still exist in the world
        if (!(level.getBlockEntity(this.head.getBlockPos()) instanceof IManaSupplier) ||
                !(level.getBlockEntity(this.tail.getBlockPos()) instanceof IManaConsumer) ||
                !this.relays.stream().map(IManaRelay::getBlockPos).allMatch(pos -> level.getBlockEntity(pos) instanceof IManaRelay)) {
            return false;
        }

        // Confirm that all nodes in the route are chunk-loaded
        return this.getNodes().stream().map(IManaNetworkNode::getBlockPos).allMatch(level::isLoaded);
    }

    public boolean canRoute(Source source) {
        return this.head.canSupply(source) && this.tail.canConsume(source) && this.relays.stream().allMatch(r -> r.canRelay(source));
    }

    public double getDistance() {
        return this.distanceSupplier.get();
    }

    protected double getDistanceInner() {
        return this.getHops().stream().mapToDouble(Route.Hop::getDistanceSqr).map(Math::sqrt).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Route route)) return false;
        return Objects.equals(head, route.head) && Objects.equals(relays, route.relays) && Objects.equals(tail, route.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, relays, tail);
    }

    @Override
    public String toString() {
        String routeStr = String.join("->", this.getNodes().stream()
                .map(node -> "[" + node.getBlockPos().toShortString() + "]")
                .toList());
        return "Route{" + routeStr + "}";
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
