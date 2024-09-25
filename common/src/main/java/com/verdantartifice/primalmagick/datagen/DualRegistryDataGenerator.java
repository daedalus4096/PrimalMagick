package com.verdantartifice.primalmagick.datagen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Generates datapack JSON for the Primal Magick mod as well as supplementary entries for vanilla Minecraft.
 * 
 * @author Daedalus4096
 */
public class DualRegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS, RuneEnchantmentDefinitions::bootstrap);
    
    // Use addProviders() instead
    private DualRegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", Constants.MOD_ID));
    }
    
    public static CompletableFuture<HolderLookup.Provider> addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        return generator.addProvider(isServer, new DualRegistryDataGenerator(output, provider)).getFullRegistries();
    }

    @Override
    public String getName() {
        return "Dual-use Datapack Registries";
    }
}
