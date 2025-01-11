package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.tags.StructureTagsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all of the mod's structure tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class StructureTagsProviderPM extends TagsProvider<Structure> {
    public StructureTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.STRUCTURE, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(StructureTagsPM.SHRINE).add(StructuresPM.EARTH_SHRINE, StructuresPM.SEA_SHRINE, StructuresPM.SKY_SHRINE, StructuresPM.SUN_SHRINE, StructuresPM.MOON_SHRINE);
        this.tag(StructureTagsPM.LIBRARY).add(StructuresPM.EARTH_LIBRARY, StructuresPM.SEA_LIBRARY, StructuresPM.SKY_LIBRARY, StructuresPM.SUN_LIBRARY, StructuresPM.MOON_LIBRARY, StructuresPM.FORBIDDEN_LIBRARY);
    }
}
