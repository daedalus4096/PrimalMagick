package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special definition for a wand assembly recipe.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyRecipe extends CustomRecipe {
    public static final MapCodec<WandAssemblyRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("core").forGetter(o -> o.core),
            Ingredient.CODEC.fieldOf("cap").forGetter(o -> o.cap),
            Ingredient.CODEC.fieldOf("gem").forGetter(o -> o.gem),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, WandAssemblyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WandAssemblyRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.core,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.cap,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.gem,
            ItemStackTemplate.STREAM_CODEC, o -> o.result,
            WandAssemblyRecipe::new);

    public static final RecipeSerializer<WandAssemblyRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
    public static final ResourceKey<Recipe<?>> WAND_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_assembly_wand"));
    public static final ResourceKey<Recipe<?>> STAFF_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("caster_assembly_staff"));

    private final Ingredient core;
    private final Ingredient cap;
    private final Ingredient gem;
    private final ItemStackTemplate result;
    
    public WandAssemblyRecipe(Ingredient core, Ingredient cap, Ingredient gem, ItemStackTemplate result) {
        this.core = core;
        this.cap = cap;
        this.gem = gem;
        this.result = result;
    }

    private static ItemStack getItem(@NotNull CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        if (inv.ingredientCount() != 4) {
            return false;
        }

        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack1 = getItem(inv, 2);
        ItemStack capStack2 = getItem(inv, 3);
        
        // Make sure the crafting inventory has a core, a gem, and two identical caps
        return this.core.test(coreStack) && ((coreStack.getItem() instanceof WandCoreItem) || (coreStack.getItem() instanceof StaffCoreItem)) &&
               this.gem.test(gemStack) && (gemStack.getItem() instanceof WandGemItem) &&
               this.cap.test(capStack1) && (capStack1.getItem() instanceof WandCapItem) &&
               this.cap.test(capStack2) && ItemStack.isSameItem(capStack1, capStack2);
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack coreStack = getItem(inv, 0);
        ItemStack gemStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);

        ItemStack outputStack = this.result.create();
        if (!(outputStack.getItem() instanceof IHasWandComponents outputItem)) {
            return ItemStack.EMPTY;
        }

        // Set the components of the modular wand/staff
        if (this.core.test(coreStack) && this.gem.test(gemStack) && this.cap.test(capStack)) {
            if (coreStack.getItem() instanceof StaffCoreItem staffCoreItem) {
                outputItem.setWandCore(outputStack, staffCoreItem.getWandCore());
            } else if (coreStack.getItem() instanceof WandCoreItem wandCoreItem) {
                outputItem.setWandCore(outputStack, wandCoreItem.getWandCore());
            } else {
                return ItemStack.EMPTY;
            }

            if (capStack.getItem() instanceof WandCapItem capItem) {
                outputItem.setWandCap(outputStack, capItem.getWandCap());
            } else {
                return ItemStack.EMPTY;
            }

            if (gemStack.getItem() instanceof WandGemItem gemItem) {
                outputItem.setWandGem(outputStack, gemItem.getWandGem());
                outputStack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(gemItem.getWandGem().getCapacity()));
            }

            return outputStack;
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
