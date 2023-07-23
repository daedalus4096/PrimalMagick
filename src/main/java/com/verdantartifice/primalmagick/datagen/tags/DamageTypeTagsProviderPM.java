package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's damage type tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class DamageTypeTagsProviderPM extends TagsProvider<DamageType> {
    public DamageTypeTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, PrimalMagick.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(DamageTypesPM.BLEEDING, DamageTypesPM.SORCERY_BLOOD);
        this.tag(DamageTypeTags.IS_FIRE).add(DamageTypesPM.HELLISH_CHAIN, DamageTypesPM.SORCERY_INFERNAL);
        this.tag(DamageTypeTagsPM.IS_MAGIC).add(DamageTypes.MAGIC).addTag(DamageTypeTagsPM.IS_SORCERY);
        this.tag(DamageTypeTagsPM.IS_SORCERY).add(DamageTypesPM.SORCERY_EARTH, DamageTypesPM.SORCERY_SEA, DamageTypesPM.SORCERY_SKY, DamageTypesPM.SORCERY_SUN, DamageTypesPM.SORCERY_MOON, DamageTypesPM.SORCERY_BLOOD, DamageTypesPM.SORCERY_INFERNAL, DamageTypesPM.SORCERY_VOID, DamageTypesPM.SORCERY_HALLOWED);
    }
}
