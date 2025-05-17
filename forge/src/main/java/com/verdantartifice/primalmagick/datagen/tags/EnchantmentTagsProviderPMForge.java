package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.EnchantmentTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProviderPMForge extends EnchantmentTagsProvider {
    public EnchantmentTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Enchantments.INCREASE_BLOCK_DROPS).add(EnchantmentsPM.TREASURE);
        this.tag(Tags.Enchantments.INCREASE_ENTITY_DROPS).add(EnchantmentsPM.TREASURE);
    }
}
