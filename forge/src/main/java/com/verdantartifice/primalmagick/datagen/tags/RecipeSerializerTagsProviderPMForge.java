package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagick.common.tags.RecipeSerializerTagsExt;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's recipe serializer tags, both original tags and modifications to other mod tags.
 * 
 * @author Daedalus4096
 */
public class RecipeSerializerTagsProviderPMForge extends IntrinsicHolderTagsProvider<RecipeSerializer<?>> {
    public RecipeSerializerTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.RECIPE_SERIALIZER, future, serializer -> BuiltInRegistries.RECIPE_SERIALIZER.getResourceKey(serializer).get(), Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(RecipeSerializerTagsExt.CREATE_AUTOMATION_IGNORE)
                .addOptional(RecipeSerializersPM.ARCANE_CRAFTING_SHAPED.getId())
                .addOptional(RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS.getId())
                .addOptional(RecipeSerializersPM.RITUAL.getId())
                .addOptional(RecipeSerializersPM.RUNECARVING.getId())
                .addOptional(RecipeSerializersPM.CONCOCTING.getId())
                .addOptional(RecipeSerializersPM.DISSOLUTION.getId());
    }
}
