package com.verdantartifice.primalmagick.common.crafting;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * Definition for a dissolution recipe.  Similar to a smelting recipe, but used by the dissolution chamber
 * instead of a furnace.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipe implements IDissolutionRecipe {
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final SourceList manaCosts;
    
    public DissolutionRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, SourceList manaCosts) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.manaCosts = manaCosts;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.result.copy();
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
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.DISSOLUTION.get();
    }

    @Override
    public SourceList getManaCosts() {
        return this.manaCosts;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public static class Serializer implements RecipeSerializer<DissolutionRecipe> {
        @Override
        public DissolutionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            SourceList manaCosts = JsonUtils.toSourceList(GsonHelper.getAsJsonObject(json, "mana", new JsonObject()));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            Ingredient ing = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            return new DissolutionRecipe(recipeId, group, ing, result, manaCosts);
        }

        @Override
        public DissolutionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            SourceList manaCosts = SourceList.EMPTY;
            for (int index = 0; index < Source.SORTED_SOURCES.size(); index++) {
                manaCosts.add(Source.SORTED_SOURCES.get(index), buffer.readVarInt());
            }
            Ingredient ing = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new DissolutionRecipe(recipeId, group, ing, result, manaCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DissolutionRecipe recipe) {
            buffer.writeUtf(recipe.group);
            for (Source source : Source.SORTED_SOURCES) {
                buffer.writeVarInt(recipe.manaCosts.getAmount(source));
            }
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
