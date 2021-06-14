package com.verdantartifice.primalmagic.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
    protected final SimpleResearchKey research;
    protected final SourceList manaCosts;
    protected final int instability;
    protected final ItemStack recipeOutput;
    protected final NonNullList<Ingredient> recipeItems;
    protected final NonNullList<BlockIngredient> recipeProps;
    protected final boolean isSimple;

    public RitualRecipe(ResourceLocation id, String group, SimpleResearchKey research, SourceList manaCosts, int instability, ItemStack output, NonNullList<Ingredient> items, NonNullList<BlockIngredient> props) {
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
    public boolean matches(CraftingInventory inv, World worldIn) {
        // Ritual recipe matching only considers the ingredients, not the props
        RecipeItemHelper helper = new RecipeItemHelper();
        List<ItemStack> inputs = new ArrayList<>();
        int count = 0;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack stack = inv.getStackInSlot(index);
            if (!stack.isEmpty()) {
                count++;
                if (this.isSimple) {
                    helper.func_221264_a(stack, 1);
                } else {
                    inputs.add(stack);
                }
            }
        }
        
        return (count == this.recipeItems.size()) && (this.isSimple ? helper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        // Ritual recipes aren't space-limited
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
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
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.RITUAL.get();
    }

    @Override
    public SimpleResearchKey getRequiredResearch() {
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
    
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RitualRecipe> {
        @Override
        public RitualRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            SimpleResearchKey research = SimpleResearchKey.parse(JSONUtils.getString(json, "research", ""));
            SourceList manaCosts = JsonUtils.toSourceList(JSONUtils.getJsonObject(json, "mana", new JsonObject()));
            int instability = JSONUtils.getInt(json, "instability", 0);
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
            NonNullList<Ingredient> ingredients = this.readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            NonNullList<BlockIngredient> props = this.readProps(JSONUtils.getJsonArray(json, "props", new JsonArray()));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for ritual recipe");
            } else {
                return new RitualRecipe(recipeId, group, research, manaCosts, instability, result, ingredients, props);
            }
        }
        
        protected NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> retVal = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
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
        public RitualRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            SimpleResearchKey research = SimpleResearchKey.parse(buffer.readString(32767));
            int instability = buffer.readVarInt();
            
            SourceList manaCosts = new SourceList();
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            for (int index = 0; index < ingredients.size(); index++) {
                ingredients.set(index, Ingredient.read(buffer));
            }
            
            int propCount = buffer.readVarInt();
            NonNullList<BlockIngredient> props = NonNullList.withSize(propCount, BlockIngredient.EMPTY);
            for (int index = 0; index < props.size(); index++) {
                props.set(index, BlockIngredient.read(buffer));
            }
            
            ItemStack result = buffer.readItemStack();
            return new RitualRecipe(recipeId, group, research, manaCosts, instability, result, ingredients, props);
        }

        @Override
        public void write(PacketBuffer buffer, RitualRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeString(recipe.research.toString());
            buffer.writeVarInt(recipe.instability);
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }
            buffer.writeVarInt(recipe.recipeProps.size());
            for (BlockIngredient prop : recipe.recipeProps) {
                prop.write(buffer);
            }
            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}
