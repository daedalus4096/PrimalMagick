package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's entity type tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class EntityTypeTagsProviderPM extends EntityTypeTagsProvider {
    public EntityTypeTagsProviderPM(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, PrimalMagick.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Primal Magick Entity Type Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Add entries to vanilla tags
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(EntityTypesPM.MANA_ARROW.get(), EntityTypesPM.PRIMALITE_TRIDENT.get(), EntityTypesPM.HEXIUM_TRIDENT.get(),
                EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), EntityTypesPM.FORBIDDEN_TRIDENT.get(), EntityTypesPM.APPLE.get());
        
        // Add entries to Forge tags
        this.tag(Tags.EntityTypes.BOSSES).add(EntityTypesPM.INNER_DEMON.get());
        
        // Create custom tags
        this.tag(EntityTypeTagsPM.ENCHANTED_GOLEMS).add(EntityTypesPM.PRIMALITE_GOLEM.get(), EntityTypesPM.HEXIUM_GOLEM.get(), EntityTypesPM.HALLOWSTEEL_GOLEM.get());
        this.tag(EntityTypeTagsPM.PIXIES).add(EntityTypesPM.BASIC_EARTH_PIXIE.get(), EntityTypesPM.GRAND_EARTH_PIXIE.get(), EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(),
                EntityTypesPM.BASIC_SEA_PIXIE.get(), EntityTypesPM.GRAND_SEA_PIXIE.get(), EntityTypesPM.MAJESTIC_SEA_PIXIE.get(),
                EntityTypesPM.BASIC_SKY_PIXIE.get(), EntityTypesPM.GRAND_SKY_PIXIE.get(), EntityTypesPM.MAJESTIC_SKY_PIXIE.get(),
                EntityTypesPM.BASIC_SUN_PIXIE.get(), EntityTypesPM.GRAND_SUN_PIXIE.get(), EntityTypesPM.MAJESTIC_SUN_PIXIE.get(),
                EntityTypesPM.BASIC_MOON_PIXIE.get(), EntityTypesPM.GRAND_MOON_PIXIE.get(), EntityTypesPM.MAJESTIC_MOON_PIXIE.get(),
                EntityTypesPM.BASIC_BLOOD_PIXIE.get(), EntityTypesPM.GRAND_BLOOD_PIXIE.get(), EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(),
                EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(),
                EntityTypesPM.BASIC_VOID_PIXIE.get(), EntityTypesPM.GRAND_VOID_PIXIE.get(), EntityTypesPM.MAJESTIC_VOID_PIXIE.get(),
                EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get());
        
        this.tag(EntityTypeTagsPM.BLOOD_ROSE_IMMUNE).add(EntityType.BEE);
        this.tag(EntityTypeTagsPM.FLYING_CREATURES).addTag(EntityTypeTagsPM.PIXIES).add(EntityType.ALLAY, EntityType.BAT, EntityType.BEE, EntityType.BLAZE, EntityType.CHICKEN, EntityType.ENDER_DRAGON,
                EntityType.GHAST, EntityType.PARROT, EntityType.PHANTOM, EntityType.VEX, EntityType.WITHER);
        this.tag(EntityTypeTagsPM.GOLEMS).addTag(EntityTypeTagsPM.ENCHANTED_GOLEMS).add(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM);
        
        this.tag(EntityTypeTagsPM.POLYMORPH_ALLOW).add(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER).addTag(EntityTypeTagsPM.ENCHANTED_GOLEMS);
        this.tag(EntityTypeTagsPM.POLYMORPH_BAN).addTag(Tags.EntityTypes.BOSSES).add(EntityType.WARDEN, EntityType.WOLF);
        
        this.tag(EntityTypeTagsPM.DROPS_BLOODY_FLESH).add(EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VILLAGER, EntityType.VINDICATOR,
                EntityType.WANDERING_TRADER, EntityType.WITCH);
        this.tag(EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH).add(EntityType.EVOKER);
        this.tag(EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW).add(EntityType.ILLUSIONER, EntityType.WITCH);
        this.tag(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH).add(EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.WITCH, EntityType.ELDER_GUARDIAN);
        this.tag(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW).add(EntityType.CREEPER, EntityType.DROWNED, EntityType.ENDERMAN, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK,
                EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.SKELETON, EntityType.STRAY, EntityType.VINDICATOR, EntityType.WITHER_SKELETON,
                EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityTypesPM.TREEFOLK.get());
        
        this.tag(EntityTypeTagsPM.GUILLOTINE_ZOMBIE_HEAD).add(EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);
        this.tag(EntityTypeTagsPM.GUILLOTINE_SKELETON_SKULL).add(EntityType.SKELETON, EntityType.STRAY);
        this.tag(EntityTypeTagsPM.GUILLOTINE_CREEPER_HEAD).add(EntityType.CREEPER);
        this.tag(EntityTypeTagsPM.GUILLOTINE_DRAGON_HEAD).add(EntityType.ENDER_DRAGON);
        this.tag(EntityTypeTagsPM.GUILLOTINE_WITHER_SKELETON_SKULL).add(EntityType.WITHER_SKELETON);
        this.tag(EntityTypeTagsPM.GUILLOTINE_PIGLIN_HEAD).add(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE);
        this.tag(EntityTypeTagsPM.GUILLOTINE_PLAYER_HEAD).add(EntityType.PLAYER);
    }
}
