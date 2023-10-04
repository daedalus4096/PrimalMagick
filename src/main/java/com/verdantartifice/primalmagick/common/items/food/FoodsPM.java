package com.verdantartifice.primalmagick.common.items.food;

import net.minecraft.world.food.FoodProperties;

/**
 * Static repository for item food property definitions.
 * 
 * @author Daedalus4096
 */
public class FoodsPM {
    public static final FoodProperties AMBROSIA = new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build();
    public static final FoodProperties BLOODY_FLESH = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().alwaysEat().build();
    public static final FoodProperties HYDROMELON_SLICE = new FoodProperties.Builder().nutrition(3).saturationMod(0.5F).build();
}
