package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.ISpellContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special definition for a wand inscription recipe.
 * 
 * @author Daedalus4096
 */
public class WandInscriptionRecipe extends CustomRecipe {
    public static final MapCodec<WandInscriptionRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("caster").forGetter(o -> o.caster),
            Ingredient.CODEC.fieldOf("scroll").forGetter(o -> o.scroll),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, WandInscriptionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandInscriptionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.caster,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.scroll,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            WandInscriptionRecipe::new);

    public static final RecipeSerializer<WandInscriptionRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> WAND_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_inscription_wand"));
    public static final ResourceKey<Recipe<?>> STAFF_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_inscription_staff"));

    private final Ingredient caster;
    private final Ingredient scroll;
    private final ItemStackTemplate result;

    public WandInscriptionRecipe(Ingredient caster, Ingredient scroll, ItemStackTemplate result) {
        this.caster = caster;
        this.scroll = scroll;
        this.result = result;
    }

    private static ItemStack getItem(@NotNull CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        if (inv.ingredientCount() != 2) {
            return false;
        }

        ItemStack wandStack = getItem(inv, 0);
        ItemStack scrollStack = getItem(inv, 1);
        
        // Make sure a spell container is present
        if (this.caster.test(wandStack) && wandStack.getItem() instanceof ISpellContainer spellContainer) {
            if (this.scroll.test(scrollStack) && scrollStack.getItem() instanceof SpellScrollItem scrollItem) {
                // If a filled spell scroll is also present, check that the scroll's spell will fit into the wand
                return spellContainer.canAddSpell(wandStack, scrollItem.getSpell(scrollStack));
            } else {
                // If no item is present in the scroll slot, clear the wand; if it's something other than a filled spell scroll, don't allow combination
                return scrollStack.isEmpty();
            }
        } else {
            return false;
        }
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack scrollStack = getItem(inv, 1);
        
        if (this.caster.test(wandStack) && wandStack.getItem() instanceof ISpellContainer spellContainer) {
            if (this.scroll.test(scrollStack) && scrollStack.getItem() instanceof SpellScrollItem scrollItem) {
                // If a filled spell scroll is also present, create a copy of the given wand and add the scroll's spell to it
                ItemStack retVal = TransmuteRecipe.createWithOriginalComponents(this.result, wandStack);
                if (spellContainer.addSpell(retVal, scrollItem.getSpell(scrollStack)) && spellContainer.setActiveSpellIndex(retVal, spellContainer.getSpellCount(retVal) - 1)) {
                    return retVal;
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (scrollStack.isEmpty()) {
                // If no item is present in the scroll slot, clear the wand of spells
                ItemStack retVal = TransmuteRecipe.createWithOriginalComponents(this.result, wandStack);
                spellContainer.clearSpells(retVal);
                return retVal;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
