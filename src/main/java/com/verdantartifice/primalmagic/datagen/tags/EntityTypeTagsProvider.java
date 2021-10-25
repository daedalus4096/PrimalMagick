package com.verdantartifice.primalmagic.datagen.tags;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.tags.EntityTypeTagsPM;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's entity type tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class EntityTypeTagsProvider extends TagsProvider<EntityType<?>> {
    @SuppressWarnings("deprecation")
    public EntityTypeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Registry.ENTITY_TYPE, PrimalMagic.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Primal Magic Entity Type Tags";
    }

    @Override
    protected void addTags() {
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
        
        this.tag(EntityTypeTagsPM.FLYING_CREATURES).addTag(EntityTypeTagsPM.PIXIES).add(EntityType.BAT, EntityType.BEE, EntityType.BLAZE, EntityType.CHICKEN, EntityType.ENDER_DRAGON,
                EntityType.GHAST, EntityType.PARROT, EntityType.PHANTOM, EntityType.VEX, EntityType.WITHER);
        this.tag(EntityTypeTagsPM.GOLEMS).addTag(EntityTypeTagsPM.ENCHANTED_GOLEMS).add(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM);
        
        this.tag(EntityTypeTagsPM.DROPS_BLOODY_FLESH).add(EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VILLAGER, EntityType.VINDICATOR,
                EntityType.WANDERING_TRADER, EntityType.WITCH);
        this.tag(EntityTypeTagsPM.DROPS_BLOOD_NOTES_HIGH).add(EntityType.EVOKER);
        this.tag(EntityTypeTagsPM.DROPS_BLOOD_NOTES_LOW).add(EntityType.ILLUSIONER, EntityType.WITCH);
        this.tag(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_HIGH).add(EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.WITCH, EntityType.ELDER_GUARDIAN);
        this.tag(EntityTypeTagsPM.DROPS_RELIC_FRAGMENTS_LOW).add(EntityType.CREEPER, EntityType.DROWNED, EntityType.ENDERMAN, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK,
                EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.SKELETON, EntityType.STRAY, EntityType.VINDICATOR, EntityType.WITHER_SKELETON,
                EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityTypesPM.TREEFOLK.get());
    }

    @Override
    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/entity_types/" + id.getPath() + ".json");
    }
}
