package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IRecipeService;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.function.Predicate;

public class RecipeServiceNeoforge implements IRecipeService {
    @Override
    public <T> int[] findMatches(List<T> inputs, List<? extends Predicate<T>> tests) {
        return RecipeMatcher.findMatches(inputs, tests);
    }
}
