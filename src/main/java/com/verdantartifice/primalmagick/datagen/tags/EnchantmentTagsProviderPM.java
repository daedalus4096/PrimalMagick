package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;

public class EnchantmentTagsProviderPM extends EnchantmentTagsProvider {
    public EnchantmentTagsProviderPM(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider) {
        super(pOutput, pLookupProvider);
    }
    
    @Override
    protected void addTags(Provider pProvider) {
        // TODO Does enchantment tooltip order need to be registered here as well?
        this.tag(EnchantmentTags.DAMAGE_EXCLUSIVE).add(EnchantmentsPM.JUDGMENT);
    }
}
