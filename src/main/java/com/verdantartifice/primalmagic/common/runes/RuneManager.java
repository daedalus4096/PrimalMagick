package com.verdantartifice.primalmagic.common.runes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;

/**
 * Primary access point for rune-related methods.  Also stores registered rune combinations in a
 * static registry.
 * 
 * @author Daedalus4096
 */
public class RuneManager {
    protected static final Map<Enchantment, List<Rune>> REGISTRY = new HashMap<>();
    
    public static void registerRuneEnchantment(@Nullable Enchantment enchantment, @Nullable VerbRune verb, @Nullable NounRune noun, @Nullable SourceRune source) {
        if (enchantment != null && verb != null && noun != null && source != null) {
            if (REGISTRY.containsKey(enchantment)) {
                throw new IllegalArgumentException("Rune enchantment already registered for " + enchantment.getRegistryName().toString());
            }
            REGISTRY.put(enchantment, Arrays.asList(verb, noun, source));
        }
    }
}
