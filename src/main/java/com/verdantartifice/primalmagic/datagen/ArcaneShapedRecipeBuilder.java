package com.verdantartifice.primalmagic.datagen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneShapedRecipeBuilder {
    protected final Item result;
    protected final int count;
    protected final List<String> pattern = new ArrayList<>();
    protected final Map<Character, Ingredient> key = new LinkedHashMap<>();
    protected String group;
    protected SimpleResearchKey research;
    protected SourceList manaCosts;
    
    protected ArcaneShapedRecipeBuilder(IItemProvider result, int count) {
        this.result = result.asItem();
        this.count = count;
    }
    
    public static ArcaneShapedRecipeBuilder arcaneShapedRecipe(IItemProvider result, int count) {
        return new ArcaneShapedRecipeBuilder(result, count);
    }
    
    public static ArcaneShapedRecipeBuilder arcaneShapedRecipe(IItemProvider result) {
        return arcaneShapedRecipe(result, 1);
    }
    
    public ArcaneShapedRecipeBuilder key(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }
    
    public ArcaneShapedRecipeBuilder key(Character symbol, IItemProvider item) {
        return key(symbol, Ingredient.fromItems(item));
    }
    
    public ArcaneShapedRecipeBuilder key(Character symbol, Tag<Item> tag) {
        return key(symbol, Ingredient.fromTag(tag));
    }
    
    public ArcaneShapedRecipeBuilder patternLine(String pattern) {
        if (!this.pattern.isEmpty() && pattern.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(pattern);
            return this;
        }
    }
    
    public ArcaneShapedRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }
    
    public ArcaneShapedRecipeBuilder research(SimpleResearchKey research) {
        this.research = research.copy();
        return this;
    }
    
    public ArcaneShapedRecipeBuilder manaCost(SourceList mana) {
        this.manaCosts = mana.copy();
        return this;
    }
    
    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ArcaneShapedRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.pattern, this.key, this.research, this.manaCosts));
    }
    
    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation saveLoc = new ResourceLocation(save);
        if (saveLoc.equals(id)) {
            throw new IllegalStateException("Arcane Shaped Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumer, saveLoc);
        }
    }
    
    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result));
    }

    protected void validate(ResourceLocation id) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for arcane shaped recipe " + id + "!");
        }
        if (this.research == null) {
            throw new IllegalStateException("No research is defined for arcane shaped recipe " + id + "!");
        }
        if (this.manaCosts == null || this.manaCosts.isEmpty()) {
            throw new IllegalStateException("No mana cost is defined for arcane shaped recipe " + id + "!");
        }
        
        Set<Character> set = new HashSet<>(this.key.keySet());
        set.remove(' ');
        for (String patternStr : this.pattern) {
            for (int index = 0; index < patternStr.length(); index++) {
                char c = patternStr.charAt(index);
                if (!this.key.containsKey(c) && c != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c + "'");
                }
                set.remove(c);
            }
        }
        
        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        } else if (this.pattern.size() == 1 && this.pattern.get(0).length() == 1) {
            throw new IllegalStateException("Arcane shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
        }
    }
    
    public static class Result implements IFinishedRecipe {
        protected final ResourceLocation id;
        protected final Item result;
        protected final int count;
        protected final String group;
        protected final List<String> pattern;
        protected final Map<Character, Ingredient> key;
        protected final SimpleResearchKey research;
        protected final SourceList manaCosts;
        
        public Result(ResourceLocation id, Item result, int count, String group, List<String> pattern, Map<Character, Ingredient> key, SimpleResearchKey research, SourceList manaCosts) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.pattern = pattern;
            this.key = key;
            this.research = research;
            this.manaCosts = manaCosts;
        }

        @Override
        public void serialize(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            if (this.research != null) {
                json.addProperty("research", this.research.toString());
            }
            
            JsonObject manaJson = new JsonObject();
            for (Source source : this.manaCosts.getSourcesSorted()) {
                manaJson.addProperty(source.getTag(), this.manaCosts.getAmount(source));
            }
            json.add("mana", manaJson);
            
            JsonArray patternJson = new JsonArray();
            for (String str : this.pattern) {
                patternJson.add(str);
            }
            json.add("pattern", patternJson);
            
            JsonObject keyJson = new JsonObject();
            for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
                keyJson.add(String.valueOf(entry.getKey()), entry.getValue().serialize());
            }
            json.add("key", keyJson);
            
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeSerializersPM.ARCANE_CRAFTING_SHAPED;
        }

        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return new ResourceLocation("");
        }
    }
}
