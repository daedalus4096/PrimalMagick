package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.tags.SpellPropertyTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SpellPropertyTagsProviderPM extends IntrinsicHolderTagsProvider<SpellProperty> {
    public SpellPropertyTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.SPELL_PROPERTIES, future, p -> SpellPropertiesPM.PROPERTIES.get().getResourceKey(p).orElseThrow(), Constants.MOD_ID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magick Spell Property Tags";
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(SpellPropertyTagsPM.AMPLIFIABLE).add(SpellPropertiesPM.POWER.get(), SpellPropertiesPM.DURATION.get(), SpellPropertiesPM.BURST_POWER.get(), SpellPropertiesPM.NON_ZERO_DURATION.get());
    }
}
