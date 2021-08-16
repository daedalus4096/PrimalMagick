package com.verdantartifice.primalmagic.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Definition for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipe implements IRitualRecipe {
    public static final int MIN_INSTABILITY = 0;
    public static final int MAX_INSTABILITY = 10;
    
    protected final ResourceLocation id;
    protected final String group;
    protected final CompoundResearchKey research;
    protected final SourceList manaCosts;
    protected final int instability;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final NonNullList<BlockIngredient> recipeProps;
    protected final boolean isSimple;

    public RitualRecipe(ResourceLocation id, String group, CompoundResearchKey research, SourceList manaCosts, int instability, ItemStack output, NonNullList<Ingredient> items, NonNullList<BlockIngredient> props) {
        this.id = id;
        this.group = group;
        this.research = research;
        this.manaCosts = manaCosts;
        this.instability = instability;
        this.recipeOutput = output;
        this.recipeItems = items;
        this.recipeProps = props;
        this.isSimple = items.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        // Ritual recipe matching only considers the ingredients, not the props
        StackedContents helper = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
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
    public ItemStack assemble(CraftingContainer inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        // Ritual recipes aren't space-limited
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RITUAL.get();
    }

    @Override
    public CompoundResearchKey getRequiredResearch() {
        return this.research;
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public NonNullList<BlockIngredient> getProps() {
        return this.recipeProps;
    }
    
    @Override
    public int getInstability() {
        return this.instability;
    }
    
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RitualRecipe> {
        @Override
        public RitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            CompoundResearchKey research = CompoundResearchKey.parse(GsonHelper.getAsString(json, "research", ""));
            SourceList manaCosts = JsonUtils.toSourceList(GsonHelper.getAsJsonObject(json, "mana", new JsonObject()));
            int instability = GsonHelper.getAsInt(json, "instability", 0);
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            NonNullList<Ingredient> ingredients = this.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            NonNullList<BlockIngredient> props = this.readProps(GsonHelper.getAsJsonArray(json, "props", new JsonArray()));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for ritual recipe");
            } else {
                return new RitualRecipe(recipeId, group, research, manaCosts, instability, result, ingredients, props);
            }
        }
        
        protected NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> retVal = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    retVal.add(ingredient);
                }
            }
            return retVal;
        }
        
        protected NonNullList<BlockIngredient> readProps(JsonArray jsonArray) {
            NonNullList<BlockIngredient> retVal = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                BlockIngredient ingredient = BlockIngredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingBlocks()) {
                    retVal.add(ingredient);
                }
            }
            return retVal;
        }

        @Override
        public RitualRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            CompoundResearchKey research = CompoundResearchKey.parse(buffer.readUtf(32767));
            int instability = buffer.readVarInt();
            
            SourceList manaCosts = new SourceList();
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.fromNetwork(buffer));
            }
            
            int propCount = buffer.readVarInt();
            NonNullList<BlockIngredient> props = NonNullList.withSize(propCount, BlockIngredient.EMPTY);
            for (int index = 0; index < props.size(); index++) {
                props.set(index, BlockIngredient.read(buffer));
            }
            
            ItemStack result = buffer.readItem();
            return new RitualRecipe(recipeId, group, research, manaCosts, instability, result, ingredients, props);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.research.toString());
            buffer.writeVarInt(recipe.instability);
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeVarInt(recipe.recipeProps.size());
            for (BlockIngredient prop : recipe.recipeProps) {
                prop.write(buffer);
            }
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}
