package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Consumer;

public class DatapackEvents {
    public static void onDatapackSync(Consumer<RecipeType<?>> registrar) {
        // Request that all relevant recipe types be sent to the player for client-side access
        registrar.accept(RecipeType.CRAFTING);
        registrar.accept(RecipeType.STONECUTTING);
        registrar.accept(RecipeType.SMELTING);
        registrar.accept(RecipeType.SMOKING);
        registrar.accept(RecipeType.BLASTING);
        registrar.accept(RecipeType.CAMPFIRE_COOKING);
        registrar.accept(RecipeType.SMITHING);
        registrar.accept(RecipeTypesPM.ARCANE_CRAFTING.get());
        registrar.accept(RecipeTypesPM.RITUAL.get());
        registrar.accept(RecipeTypesPM.RUNECARVING.get());
        registrar.accept(RecipeTypesPM.CONCOCTING.get());
        registrar.accept(RecipeTypesPM.DISSOLUTION.get());
    }
}
