package com.verdantartifice.primalmagick.platform.services;

import java.util.List;
import java.util.function.Predicate;

public interface IRecipeService {
    <T> int[] findMatches(List<T> inputs, List<? extends Predicate<T>> tests);
}
