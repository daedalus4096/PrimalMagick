package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NetworkGraph {
    private final Map<BlockPos, List<Edge>> edges = new ConcurrentHashMap<>();

    private void addNode(@NotNull final BlockPos pos) {
        if (!this.edges.containsKey(pos)) {
            this.edges.put(pos, new LinkedList<>());
        }
    }

    public void addEdge(@NotNull final BlockPos from, @NotNull final BlockPos to) {
        this.addNode(from);
        this.addNode(to);
        this.edges.get(from).add(new Edge(from, to));
    }

    public boolean removeIf(@NotNull final Predicate<BlockPos> predicate) {
        MutableBoolean removed = new MutableBoolean(false);
        this.edges.forEach((key, value) -> {
            if (value.removeIf(edge -> predicate.test(edge.to()))) {
                removed.setTrue();
            }
        });
        if (this.edges.keySet().removeIf(predicate)) {
            removed.setTrue();
        }
        return removed.getValue();
    }

    public void clear() {
        this.edges.clear();
    }

    public Optional<Route> findRoute(@NotNull final BlockPos start, @NotNull final BlockPos end,
                                     @NotNull final Optional<Source> sourceOpt, @NotNull final Level level) {
        return assemblePath(findPreviousEdges(start, sourceOpt, level), start, end).toRoute(level);
    }

    public Set<Route> findAllRoutes(@NotNull final BlockPos start, @NotNull final Optional<Source> sourceOpt, @NotNull final Level level) {
        Map<BlockPos, Edge> previousSteps = findPreviousEdges(start, sourceOpt, level);
        return this.edges.keySet().stream()
                .map(endPos -> assemblePath(previousSteps, start, endPos).toRoute(level))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private static EdgeList assemblePath(@NotNull final Map<BlockPos, Edge> previousSteps, @NotNull final BlockPos from, @NotNull final BlockPos to) {
        EdgeList path = new EdgeList();
        BlockPos current = to;
        if (previousSteps.containsKey(current) || current.equals(from)) {
            while (current != null) {
                Edge edge = previousSteps.get(current);
                if (edge != null) {
                    path.addFirst(edge);
                    current = edge.from();
                } else {
                    current = null;
                }
            }
        }
        return path;
    }

    private Map<BlockPos, Edge> findPreviousEdges(@NotNull final BlockPos start, @NotNull final Optional<Source> sourceOpt, @NotNull final Level level) {
        if (!this.edges.containsKey(start)) {
            return Map.of();
        }

        // Initialize search parameters
        final Map<BlockPos, Double> distances = new HashMap<>();
        final Map<BlockPos, Edge> previousSteps = new HashMap<>();
        final Set<BlockPos> vertices = new HashSet<>();
        this.edges.keySet().forEach(pos -> {
            distances.put(pos, Double.POSITIVE_INFINITY);
            vertices.add(pos);
        });
        distances.put(start, 0D);

        // Search through each vertex in the set
        while (!vertices.isEmpty()) {
            BlockPos nextVertex = findNextSearchVertex(vertices, distances);
            if (nextVertex == null) {
                break;
            }
            vertices.remove(nextVertex);

            // Iterate through vertex neighbors
            List<Edge> neighbors = this.edges.getOrDefault(nextVertex, List.of());
            double currentDistance = distances.getOrDefault(nextVertex, Double.POSITIVE_INFINITY);
            neighbors.stream().filter(e -> vertices.contains(e.to())).forEach(neighbor -> {
                double alt = currentDistance == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : currentDistance + neighbor.getWeight(sourceOpt, level);

                // If the combined distance is less than the previously known best distance, record the edge as the current best
                if (alt < distances.getOrDefault(neighbor.to(), Double.POSITIVE_INFINITY)) {
                    distances.put(neighbor.to(), alt);
                    previousSteps.put(neighbor.to(), neighbor);
                }
            });
        }

        return previousSteps;
    }

    private static @Nullable BlockPos findNextSearchVertex(@NotNull final Set<BlockPos> vertices, @NotNull final Map<BlockPos, Double> distances) {
        BlockPos retVal = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (final BlockPos pos : vertices) {
            if (retVal == null || distances.get(pos) < minDistance) {
                retVal = pos;
                minDistance = distances.get(pos);
            }
        }
        return retVal;
    }

    private record Edge(@NotNull BlockPos from, @NotNull BlockPos to) {
        public double getDistanceSqr() {
            return this.from.distSqr(this.to);
        }

        public boolean inRange(@NotNull final Level level) {
            double distanceSqr = this.getDistanceSqr();
            IManaNetworkNode fromNode = level.getBlockEntity(this.from) instanceof IManaNetworkNode n1 ? n1 : null;
            IManaNetworkNode toNode = level.getBlockEntity(this.to) instanceof IManaNetworkNode n2 ? n2 : null;
            if (fromNode == null || toNode == null) {
                return false;
            } else {
                int fromRange = fromNode.getNetworkRange();
                int toRange = toNode.getNetworkRange();
                return ((fromRange * fromRange) >= distanceSqr) && ((toRange * toRange) >= distanceSqr);
            }
        }

        public int getManaThroughput(@NotNull final Level level) {
            IManaNetworkNode fromNode = level.getBlockEntity(this.from) instanceof IManaNetworkNode n1 ? n1 : null;
            IManaNetworkNode toNode = level.getBlockEntity(this.to) instanceof IManaNetworkNode n2 ? n2 : null;
            if (fromNode == null || toNode == null) {
                return 0;
            } else {
                return Math.min(fromNode.getManaThroughput(), toNode.getManaThroughput());
            }
        }

        public boolean canRoute(@NotNull final Optional<Source> sourceOpt, @NotNull final Level level) {
            return sourceOpt.map(source -> {
                IManaConsumer fromNode = level.getBlockEntity(this.from) instanceof IManaConsumer n1 ? n1 : null;
                IManaSupplier toNode = level.getBlockEntity(this.to) instanceof IManaSupplier n2 ? n2 : null;
                if (fromNode == null || toNode == null) {
                    return false;
                } else {
                    return fromNode.canConsume(source) && toNode.canSupply(source);
                }
            }).orElse(true);
        }

        public double getWeight(@NotNull final Optional<Source> sourceOpt, @NotNull final Level level) {
            if (!this.inRange(level) || !this.canRoute(sourceOpt, level)) {
                return Double.POSITIVE_INFINITY;
            } else {
                int throughput = this.getManaThroughput(level);
                return throughput == 0 ? Double.POSITIVE_INFINITY : this.getDistanceSqr() * (1D / (double)(throughput * throughput));
            }
        }
    }

    private static class EdgeList extends LinkedList<Edge> {
        public Optional<Route> toRoute(@NotNull final Level level) {
            if (this.isEmpty()) {
                return Optional.empty();
            } else {
                List<BlockPos> positions = new LinkedList<>();
                BlockPos lastTo = this.getFirst().from();
                positions.add(lastTo);
                for (Edge edge : this) {
                    // If the edges don't form a continuous path, abort
                    if (!lastTo.equals(edge.from())) {
                        return Optional.empty();
                    }
                    lastTo = edge.to();
                    positions.add(lastTo);
                }

                // Edge lists are ordered from consumer to supplier, but routes are expected to be from supplier to consumer
                Collections.reverse(positions);

                // Ensure the route is valid before returning it
                Route retVal = new Route(positions);
                return retVal.isValid(level) ? Optional.of(retVal) : Optional.empty();
            }
        }
    }
}
