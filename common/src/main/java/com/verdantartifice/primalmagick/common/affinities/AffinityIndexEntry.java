package com.verdantartifice.primalmagick.common.affinities;

import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public record AffinityIndexEntry(@NotNull ItemStack stack, @NotNull CompletableFuture<SourceList> affinities) {
}
