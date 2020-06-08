package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.runes.Rune;
import com.verdantartifice.primalmagic.common.runes.RuneManager;

import net.minecraft.enchantment.Enchantments;

/**
 * Point of registration for mod rune enchantment combinations.
 * 
 * @author Daedalus4096
 */
public class InitRunes {
    public static void initRuneEnchantments() {
        RuneManager.registerRuneEnchantment(Enchantments.PROTECTION, Rune.PROTECT, Rune.SELF, Rune.EARTH);
    }
}
