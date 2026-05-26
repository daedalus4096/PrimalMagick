package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IStaff;
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
 * Special definition for a wand glamour recipe.
 * 
 * @author Daedalus4096
 */
public class WandGlamourRecipe extends CustomRecipe {
    public static final MapCodec<WandGlamourRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("caster").forGetter(o -> o.caster),
            Ingredient.CODEC.fieldOf("core").forGetter(o -> o.core),
            Ingredient.CODEC.fieldOf("cap").forGetter(o -> o.cap),
            Ingredient.CODEC.fieldOf("gem").forGetter(o -> o.gem),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
    ).apply(instance, WandGlamourRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandGlamourRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.caster,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.core,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.cap,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.gem,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            WandGlamourRecipe::new);

    public static final RecipeSerializer<WandGlamourRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> WAND_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_glamour_wand"));
    public static final ResourceKey<Recipe<?>> STAFF_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_glamour_staff"));

    private final Ingredient caster;
    private final Ingredient core;
    private final Ingredient cap;
    private final Ingredient gem;
    private final ItemStackTemplate result;

    public WandGlamourRecipe(Ingredient caster, Ingredient core, Ingredient cap, Ingredient gem, ItemStackTemplate result) {
        this.caster = caster;
        this.core = core;
        this.cap = cap;
        this.gem = gem;
        this.result = result;
    }

    private static ItemStack getItem(CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        ItemStack casterStack = getItem(inv, 0);
        ItemStack coreStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        ItemStack gemStack = getItem(inv, 3);
        boolean isStaff = (casterStack.getItem() instanceof IStaff);
        
        // Make sure the crafting inventory has a modular wand/staff, as well as an optional core, cap, and/or gem
        return this.caster.test(casterStack) && (casterStack.getItem() instanceof ModularWandItem) &&
               (coreStack.isEmpty() || (this.core.test(coreStack) && ((isStaff && (coreStack.getItem() instanceof StaffCoreItem)) || (!isStaff && (coreStack.getItem() instanceof WandCoreItem))))) &&
               (capStack.isEmpty() || (this.cap.test(capStack) && capStack.getItem() instanceof WandCapItem)) &&
               (gemStack.isEmpty() || (this.gem.test(gemStack) && gemStack.getItem() instanceof WandGemItem));
    }
    
    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack coreStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        ItemStack gemStack = getItem(inv, 3);

        ItemStack retVal = TransmuteRecipe.createWithOriginalComponents(this.result, wandStack);
        if (retVal.getItem() instanceof IHasWandComponents wandItem) {
            if (this.core.test(coreStack)) {
                if (coreStack.getItem() instanceof WandCoreItem wandCoreItem) {
                    wandItem.setWandCoreAppearance(retVal, wandCoreItem.getWandCore());
                } else if (coreStack.getItem() instanceof StaffCoreItem staffCoreItem) {
                    wandItem.setWandCoreAppearance(retVal, staffCoreItem.getWandCore());
                } else {
                    wandItem.setWandCoreAppearance(retVal, null);
                }
            }

            if (this.cap.test(capStack) && capStack.getItem() instanceof WandCapItem capItem) {
                wandItem.setWandCapAppearance(retVal, capItem.getWandCap());
            } else {
                wandItem.setWandCapAppearance(retVal, null);
            }

            if (this.gem.test(gemStack) && gemStack.getItem() instanceof WandGemItem gemItem) {
                wandItem.setWandGemAppearance(retVal, gemItem.getWandGem());
            } else {
                wandItem.setWandGemAppearance(retVal, null);
            }

            return retVal;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
