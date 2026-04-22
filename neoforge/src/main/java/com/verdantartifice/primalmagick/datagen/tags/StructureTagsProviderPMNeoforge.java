package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.tags.StructureTagsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's structure tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class StructureTagsProviderPMNeoforge extends KeyTagProvider<Structure> {
    public StructureTagsProviderPMNeoforge(PackOutput output, CompletableFuture<Provider> future) {
        super(output, Registries.STRUCTURE, future, Constants.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(StructureTagsPM.SHRINE).add(StructuresPM.EARTH_SHRINE).add(StructuresPM.SEA_SHRINE).add(StructuresPM.SKY_SHRINE).add(StructuresPM.SUN_SHRINE).add(StructuresPM.MOON_SHRINE);
        this.tag(StructureTagsPM.LIBRARY).add(StructuresPM.EARTH_LIBRARY).add(StructuresPM.SEA_LIBRARY).add(StructuresPM.SKY_LIBRARY).add(StructuresPM.SUN_LIBRARY).add(StructuresPM.MOON_LIBRARY).add(StructuresPM.FORBIDDEN_LIBRARY);
    }
}
