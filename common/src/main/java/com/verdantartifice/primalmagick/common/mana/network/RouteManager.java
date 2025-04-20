package com.verdantartifice.primalmagick.common.mana.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Centralized repository of mana networking route tables for all levels in the instance.
 *
 * @author Daedalus4096
 */
public class RouteManager {
    private static final Map<ResourceKey<Level>, RouteTable> ROUTE_TABLES = new HashMap<>();

    public static RouteTable getRouteTable(@NotNull Level level) {
        Objects.requireNonNull(level);
        return ROUTE_TABLES.computeIfAbsent(level.dimension(), k -> new RouteTable());
    }
}
