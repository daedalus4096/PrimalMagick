package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.tags.EnchantmentTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProviderPM extends EnchantmentTagsProvider {
    public EnchantmentTagsProviderPM(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider) {
        super(pOutput, pLookupProvider);
    }
    
    @Override
    protected void addTags(Provider pProvider) {
        // TODO Does enchantment tooltip order need to be registered here as well?
        
        // Add enchantments to vanilla tags
        this.tag(EnchantmentTags.ARMOR_EXCLUSIVE).add(EnchantmentsPM.AEGIS);
        this.tag(EnchantmentTags.DAMAGE_EXCLUSIVE).add(EnchantmentsPM.JUDGMENT);
        this.tag(EnchantmentTags.TREASURE).addTag(EnchantmentTagsPM.RUNE_EXCLUSIVE_ENCHANTMENTS).add(
            EnchantmentsPM.JUDGMENT,
            EnchantmentsPM.ENDERPORT,
            EnchantmentsPM.REGROWTH,
            EnchantmentsPM.AEGIS
        );
        this.tag(EnchantmentTags.NON_TREASURE).add(
            EnchantmentsPM.LIFESTEAL,
            EnchantmentsPM.ENDERLOCK,
            EnchantmentsPM.MANA_EFFICIENCY,
            EnchantmentsPM.SPELL_POWER,
            EnchantmentsPM.TREASURE,
            EnchantmentsPM.BLUDGEONING,
            EnchantmentsPM.MAGICK_PROTECTION,
            EnchantmentsPM.GUILLOTINE
        );
        
        // Define mod custom tags
        this.tag(EnchantmentTagsPM.DIGGING_AREA_EXCLUSIVE).add(EnchantmentsPM.REVERBERATION, EnchantmentsPM.DISINTEGRATION);
        this.tag(EnchantmentTagsPM.RUNE_EXCLUSIVE_ENCHANTMENTS).add(
            EnchantmentsPM.REVERBERATION,
            EnchantmentsPM.BOUNTY,
            EnchantmentsPM.DISINTEGRATION,
            EnchantmentsPM.VERDANT,
            EnchantmentsPM.LUCKY_STRIKE,
            EnchantmentsPM.RENDING,
            EnchantmentsPM.SOULPIERCING,
            EnchantmentsPM.ESSENCE_THIEF,
            EnchantmentsPM.BULWARK
        );
    }
}
