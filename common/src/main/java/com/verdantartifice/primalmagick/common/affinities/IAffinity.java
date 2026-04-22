package com.verdantartifice.primalmagick.common.affinities;

import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull CompletableFuture<SourceList> getTotalAsync(@Nullable RecipeManager recipeManager, @NotNull RegistryAccess registryAccess, @NotNull List<Identifier> history);
}
