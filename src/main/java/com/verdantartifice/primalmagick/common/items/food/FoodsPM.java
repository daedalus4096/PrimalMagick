package com.verdantartifice.primalmagick.common.items.food;

import net.minecraft.world.food.FoodProperties;

/**
 * Static repository for item food property definitions.
 * 
 * @author Daedalus4096
 */
public class FoodsPM {
    public static final FoodProperties AMBROSIA = new FoodProperties.Builder().nutrition(4).saturationModifier(1.2F).alwaysEdible().build();
    public static final FoodProperties BLOODY_FLESH = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).alwaysEdible().build();
    public static final FoodProperties HYDROMELON_SLICE = new FoodProperties.Builder().nutrition(3).saturationModifier(0.5F).build();
}
