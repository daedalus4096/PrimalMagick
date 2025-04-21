package com.verdantartifice.primalmagick.common.mana.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkGraph {
    private final Map<BlockPos, List<Edge>> edges = new ConcurrentHashMap<>();

    public void addNode(@NotNull final IManaNetworkNode node) {
        if (!this.edges.containsKey(node.getBlockPos())) {
            this.edges.put(node.getBlockPos(), new LinkedList<>());
        }
    }

    public void addEdge(@NotNull final IManaNetworkNode from, @NotNull final IManaNetworkNode to) {
        this.addNode(from);
        this.addNode(to);
        this.edges.get(from.getBlockPos()).add(new Edge(from, to));
    }

    private Map<BlockPos, Edge> findPreviousEdges(@NotNull final IManaNetworkNode start) {
        if (!this.edges.containsKey(start.getBlockPos())) {
            return Map.of();
        }

        // Initialize search parameters
        final Map<BlockPos, Double> distances = new HashMap<>();
        final Map<BlockPos, Edge> previousSteps = new HashMap<>();
        final Set<BlockPos> vertices = new HashSet<>();
        this.edges.keySet().forEach(pos -> {
            distances.put(pos, Double.POSITIVE_INFINITY);
            previousSteps.put(pos, null);
            vertices.add(pos);
        });
        distances.put(start.getBlockPos(), 0D);

        // Search through each vertex in the set
        while (!vertices.isEmpty()) {
            BlockPos nextVertex = findNextSearchVertex(vertices, distances);
            if (nextVertex == null) {
                break;
            }
            vertices.remove(nextVertex);

            // Iterate through vertex neighbors
            List<Edge> neighbors = this.edges.getOrDefault(nextVertex, List.of());
            neighbors.stream().filter(e -> vertices.contains(e.to())).forEach(neighbor -> {
                double currentDistance = distances.getOrDefault(nextVertex, Double.POSITIVE_INFINITY);
                double alt = currentDistance == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : currentDistance + neighbor.getDistanceSqr();

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
        public Edge(@NotNull final IManaNetworkNode fromNode, @NotNull final IManaNetworkNode toNode) {
            this(fromNode.getBlockPos(), toNode.getBlockPos());
        }

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

        public double getWeight(@NotNull final Level level) {
            if (!this.inRange(level)) {
                return Double.POSITIVE_INFINITY;
            } else {
                int throughput = this.getManaThroughput(level);
                return throughput == 0 ? Double.POSITIVE_INFINITY : this.getDistanceSqr() * (1D / (double)(throughput * throughput));
            }
        }
    }

    private static class EdgeList extends LinkedList<Edge> {

    }
}
