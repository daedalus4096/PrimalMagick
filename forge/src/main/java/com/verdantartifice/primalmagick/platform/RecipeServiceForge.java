package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IRecipeService;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.function.Predicate;

public class RecipeServiceForge implements IRecipeService {
    @Override
    public <T> int[] findMatches(List<T> inputs, List<? extends Predicate<T>> tests) {
        return RecipeMatcher.findMatches(inputs, tests);
    }
}
