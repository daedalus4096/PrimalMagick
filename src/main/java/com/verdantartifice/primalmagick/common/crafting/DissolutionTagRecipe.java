package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Definition for a dissolution tag recipe.  Similar to a normal dissolution recipe, but it outputs
 * a given tag rather than a specific stack.
 * 
 * @author Daedalus4096
 */
public class DissolutionTagRecipe implements IDissolutionRecipe {
    protected final String group;
    protected final Ingredient ingredient;
    protected final TagKey<Item> recipeOutputTag;
    protected final int recipeOutputAmount;
    protected final SourceList manaCosts;
    
    public DissolutionTagRecipe(String group, Ingredient ingredient, TagKey<Item> recipeOutputTag, int recipeOutputAmount, SourceList manaCosts) {
        this.group = group;
        this.ingredient = ingredient;
        this.recipeOutputTag = recipeOutputTag;
        this.recipeOutputAmount = recipeOutputAmount;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.getResultItemFromTag(registryAccess);
    }

    protected ItemStack getResultItemFromTag(RegistryAccess pRegistryAccess) {
        // Retrieve the first item in this recipe's tag that was defined in Primal Magick or, failing that, the first item in the tag.
        // TODO Memoize this function
        Optional<HolderSet.Named<Item>> tagOpt = pRegistryAccess.registryOrThrow(Registries.ITEM).getTag(this.recipeOutputTag);
        Optional<Holder<Item>> modItemOpt = tagOpt.flatMap(tag -> tag.stream().filter(h -> h.is(key -> key.location().getNamespace().equals(PrimalMagick.MODID))).findFirst());
        if (modItemOpt.isPresent()) {
            return new ItemStack(modItemOpt.get().get(), this.recipeOutputAmount);
        } else {
            Optional<Holder<Item>> fallbackItemOpt = tagOpt.flatMap(tag -> tag.stream().findFirst());
            if (fallbackItemOpt.isPresent()) {
                return new ItemStack(fallbackItemOpt.get().get(), this.recipeOutputAmount);
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.DISSOLUTION_TAG.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<DissolutionTagRecipe> {
        protected static final Codec<DissolutionTagRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(dr -> dr.group),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(dr -> dr.ingredient),
                    TagKey.codec(Registries.ITEM).fieldOf("recipeOutputTag").forGetter(dr -> dr.recipeOutputTag),
                    Codec.INT.fieldOf("recipeOutputAmount").forGetter(dr -> dr.recipeOutputAmount),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(dr -> dr.manaCosts)
                ).apply(instance, DissolutionTagRecipe::new);
        });
        
        @Override
        public Codec<DissolutionTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public DissolutionTagRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            SourceList manaCosts = SourceList.fromNetwork(buffer);
            Ingredient ing = Ingredient.fromNetwork(buffer);
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, buffer.readResourceLocation());
            int resultAmount = buffer.readVarInt();
            return new DissolutionTagRecipe(group, ing, resultTag, resultAmount, manaCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DissolutionTagRecipe recipe) {
            buffer.writeUtf(recipe.group);
            SourceList.toNetwork(buffer, recipe.manaCosts);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeResourceLocation(recipe.recipeOutputTag.location());
            buffer.writeVarInt(recipe.recipeOutputAmount);
        }
    }
}
