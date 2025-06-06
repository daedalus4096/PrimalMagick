package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.tags.RecipeSerializerTagsExt;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all of the mod's recipe serializer tags, both original tags and modifications to other mod tags.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializerTagsProviderPMForge extends TagsProvider<RecipeSerializer<?>> {
    public RecipeSerializerTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.RECIPE_SERIALIZER, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(RecipeSerializerTagsExt.CREATE_AUTOMATION_IGNORE)
                .addOptional(RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.getId())
                .addOptional(RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS.getId())
                .addOptional(RecipeSerializersPM.RITUAL.getId())
                .addOptional(RecipeSerializersPM.RUNECARVING.getId())
                .addOptional(RecipeSerializersPM.CONCOCTING.getId())
                .addOptional(RecipeSerializersPM.DISSOLUTION.getId());
    }
}
