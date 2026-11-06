package com.verdantartifice.primalmagick.common.affinities;

import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Primary interface for a data-defined affinity entry.
 * 
 * @author Daedalus4096
 */
public interface IAffinity {
    @NotNull AffinityType<?> getType();
    @NotNull Identifier getTarget();
    @NotNull CompletableFuture<SourceList> getTotalAsync(@NotNull Level level, @NotNull List<Identifier> history);
}
