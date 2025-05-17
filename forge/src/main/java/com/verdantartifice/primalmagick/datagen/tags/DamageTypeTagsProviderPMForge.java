package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
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

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all of the mod's damage type tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class DamageTypeTagsProviderPMForge extends TagsProvider<DamageType> {
    public DamageTypeTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(DamageTypesPM.BLEEDING, DamageTypesPM.SORCERY_BLOOD);
        this.tag(DamageTypeTags.IS_FIRE).add(DamageTypesPM.HELLISH_CHAIN, DamageTypesPM.SORCERY_INFERNAL);

        this.tag(DamageTypeTagsPM.IS_MAGIC).add(DamageTypes.MAGIC).addTag(DamageTypeTagsPM.IS_SORCERY);
        this.tag(DamageTypeTagsPM.IS_SORCERY).addTag(DamageTypeTagsPM.IS_SORCERY_EARTH).addTag(DamageTypeTagsPM.IS_SORCERY_SEA).addTag(DamageTypeTagsPM.IS_SORCERY_SKY).addTag(DamageTypeTagsPM.IS_SORCERY_SUN).addTag(DamageTypeTagsPM.IS_SORCERY_MOON).addTag(DamageTypeTagsPM.IS_SORCERY_BLOOD).addTag(DamageTypeTagsPM.IS_SORCERY_INFERNAL).addTag(DamageTypeTagsPM.IS_SORCERY_VOID).addTag(DamageTypeTagsPM.IS_SORCERY_HALLOWED);
        this.tag(DamageTypeTagsPM.IS_SORCERY_EARTH).add(DamageTypesPM.SORCERY_EARTH);
        this.tag(DamageTypeTagsPM.IS_SORCERY_SEA).add(DamageTypesPM.SORCERY_SEA);
        this.tag(DamageTypeTagsPM.IS_SORCERY_SKY).add(DamageTypesPM.SORCERY_SKY);
        this.tag(DamageTypeTagsPM.IS_SORCERY_SUN).add(DamageTypesPM.SORCERY_SUN);
        this.tag(DamageTypeTagsPM.IS_SORCERY_MOON).add(DamageTypesPM.SORCERY_MOON);
        this.tag(DamageTypeTagsPM.IS_SORCERY_BLOOD).add(DamageTypesPM.SORCERY_BLOOD);
        this.tag(DamageTypeTagsPM.IS_SORCERY_INFERNAL).add(DamageTypesPM.SORCERY_INFERNAL);
        this.tag(DamageTypeTagsPM.IS_SORCERY_VOID).add(DamageTypesPM.SORCERY_VOID);
        this.tag(DamageTypeTagsPM.IS_SORCERY_HALLOWED).add(DamageTypesPM.SORCERY_HALLOWED);

        this.tag(DamageTypeTagsPM.IGNITES_PIXIE_HOUSES).add(DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE);
        this.tag(DamageTypeTagsPM.BURNS_PIXIE_HOUSES).add(DamageTypes.ON_FIRE);
        this.tag(DamageTypeTagsPM.ALWAYS_KILLS_PIXIE_HOUSES).add(DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE);
        this.tag(DamageTypeTagsPM.CAN_BREAK_PIXIE_HOUSES).add(DamageTypes.PLAYER_ATTACK, DamageTypes.PLAYER_EXPLOSION);
    }
}
