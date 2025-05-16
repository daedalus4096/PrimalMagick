package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProviderPMNeoforge extends EnchantmentTagsProvider {
    public EnchantmentTagsProviderPMNeoforge(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Enchantments.INCREASE_BLOCK_DROPS).add(EnchantmentsPM.TREASURE);
        this.tag(Tags.Enchantments.INCREASE_ENTITY_DROPS).add(EnchantmentsPM.TREASURE);
    }
}
