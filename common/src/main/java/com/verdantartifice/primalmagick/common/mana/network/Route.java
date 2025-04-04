package com.verdantartifice.primalmagick.common.mana.network;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Objects;

/**
 * A unidirectional path through a mana distribution network, consisting of an origin, a terminus, and zero or more
 * relays.
 *
 * @author Daedalus4096
 */
@Immutable
public class Route {
    protected final IManaSupplier origin;
    protected final List<IManaRelay> relays;    // Ordered from origin to terminus
    protected final IManaConsumer terminus;

    public Route(@NotNull IManaSupplier origin, @NotNull IManaConsumer terminus) {
        this(origin, terminus, List.of());
    }

    public Route(@NotNull IManaSupplier origin, @NotNull IManaConsumer terminus, @NotNull IManaRelay... relays) {
        this(origin, terminus, ImmutableList.copyOf(relays));
    }

    public Route(@NotNull IManaSupplier origin, @NotNull IManaConsumer terminus, @NotNull List<IManaRelay> relays) {
        this.origin = Objects.requireNonNull(origin);
        this.terminus = Objects.requireNonNull(terminus);
        this.relays = ImmutableList.copyOf(relays.stream().filter(Objects::nonNull).toList());
    }

    public IManaSupplier getOrigin() {
        return this.origin;
    }

    public IManaConsumer getTerminus() {
        return this.terminus;
    }

    public List<IManaRelay> getRelays() {
        return this.relays;
    }

    public int getScore() {
        // TODO Calculate a score for the route based on throughput and number of hops
        return 0;
    }

    /**
     * Tests whether this route is topologically valid, i.e. whether it represents a possibly acceptable path from
     * origin to terminus through a mana network. Does not test whether the route is <strong>active</strong>, in that
     * all nodes are currently extant and loaded in the world.
     *
     * @return whether this route is topologically valid
     */
    public boolean isValid() {
        if (!this.origin.isOrigin() || !this.terminus.isTerminus()) {
            // Confirm that the origin and terminus of the route are correctly populated
            return false;
        }
        if (this.origin.getNodeId() == this.terminus.getNodeId()) {
            // A zero-hop route is valid, but not a circular one going through one or more relays
            return this.relays.isEmpty();
        }
        // TODO Confirm that there are no cycles in the route

        return true;
    }

    /**
     * Tests whether this route is active and can currently be used to successfully transport mana.
     *
     * @param level the level of the network
     * @return whether this route is active
     */
    public boolean isActive(Level level) {
        // TODO Determine whether the route is valid and whether all nodes are extant and loaded in the world
        // TODO Test whether all nodes are in transmission range of each other

        return false;
    }

    public boolean canRoute(Source source) {
        return this.origin.canSupply(source) && this.terminus.canConsume(source) && this.relays.stream().allMatch(r -> r.canRelay(source));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Route route)) return false;
        return Objects.equals(origin, route.origin) && Objects.equals(relays, route.relays) && Objects.equals(terminus, route.terminus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, relays, terminus);
    }
}
