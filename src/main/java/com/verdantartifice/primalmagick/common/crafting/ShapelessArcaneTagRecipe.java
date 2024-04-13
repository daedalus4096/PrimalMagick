package com.verdantartifice.primalmagick.common.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

/**
 * Definition for a shapeless arcane tag recipe.  Like a normal shapeless arcane recipe, except that
 * it outputs a tag rather than a specific item stack.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.ShapelessRecipe}
 */
public class ShapelessArcaneTagRecipe implements IArcaneRecipe {
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    protected final TagKey<Item> recipeOutputTag;
    protected final int recipeOutputAmount;
    protected final NonNullList<Ingredient> recipeItems;
    protected final boolean isSimple;
    
    public ShapelessArcaneTagRecipe(String group, CompoundResearchKey research, SourceList manaCosts, TagKey<Item> outputTag, int outputAmount, NonNullList<Ingredient> items) {
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.recipeOutputTag = outputTag;
        this.recipeOutputAmount = outputAmount;
        this.recipeItems = items;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        StackedContents helper = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < pContainer.getContainerSize(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                count++;
                if (this.isSimple) {
                    helper.accountStack(stack, 1);
                } else {
                    inputs.add(stack);
                }
            }
        }
        
        return (count == this.recipeItems.size()) && (this.isSimple ? helper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return (pWidth * pHeight) >= this.recipeItems.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.getResultItemFromTag(pRegistryAccess);
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
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.ARCANE_CRAFTING_SHAPELESS_TAG.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
    }

    @Override
    public String getGroup() {
        return this.group;
    }
    
    public static class Serializer implements RecipeSerializer<ShapelessArcaneTagRecipe> {
        protected static final Codec<ShapelessArcaneTagRecipe> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(sar -> sar.group),
                    CompoundResearchKey.CODEC.fieldOf("research").forGetter(sar -> sar.research),
                    SourceList.CODEC.optionalFieldOf("mana", SourceList.EMPTY).forGetter(sar -> sar.manaCosts),
                    TagKey.codec(Registries.ITEM).fieldOf("recipeOutputTag").forGetter(sar -> sar.recipeOutputTag),
                    Codec.INT.fieldOf("recipeOutputAmount").forGetter(sar -> sar.recipeOutputAmount),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingArray = ingredients.stream().filter(Predicate.not(Ingredient::isEmpty)).toArray(Ingredient[]::new);
                        if (ingArray.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless arcane recipe");
                        } else if (ingArray.length > ShapedArcaneRecipe.MAX_WIDTH * ShapedArcaneRecipe.MAX_HEIGHT) {
                            return DataResult.error(() -> "Too many ingredients for shapeless arcane recipe");
                        } else {
                            return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingArray));
                        }
                    }, DataResult::success).forGetter(sar -> sar.recipeItems)
                ).apply(instance, ShapelessArcaneTagRecipe::new);
        });
        
        @Override
        public Codec<ShapelessArcaneTagRecipe> codec() {
            return CODEC;
        }

        @Override
        public ShapelessArcaneTagRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(pBuffer.readUtf(32767));
            
            SourceList manaCosts = SourceList.fromNetwork(pBuffer);
            
            int count = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(pBuffer));
            }
            
            TagKey<Item> resultTag = TagKey.create(Registries.ITEM, pBuffer.readResourceLocation());
            int resultAmount = pBuffer.readVarInt();
            
            return new ShapelessArcaneTagRecipe(group, research, manaCosts, resultTag, resultAmount, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShapelessArcaneTagRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pBuffer.writeUtf(pRecipe.research.toString());
            SourceList.toNetwork(pBuffer, pRecipe.manaCosts);
            pBuffer.writeVarInt(pRecipe.recipeItems.size());
            for (Ingredient ingredient : pRecipe.recipeItems) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeResourceLocation(pRecipe.recipeOutputTag.location());
            pBuffer.writeVarInt(pRecipe.recipeOutputAmount);
        }
    }
}
