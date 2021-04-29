package com.verdantartifice.primalmagic.common.entities;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicBloodPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicHallowedPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicInfernalPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicSunPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicVoidPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandBloodPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandHallowedPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandInfernalPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandSunPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.GrandVoidPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticBloodPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticEarthPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticHallowedPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticInfernalPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticMoonPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSeaPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSkyPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticSunPixieEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.MajesticVoidPixieEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod entity types.
 * 
 * @author Daedalus4096
 */
public class EntityTypesPM {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, PrimalMagic.MODID);
    
    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = ENTITY_TYPES.register("spell_projectile", () -> EntityType.Builder.<SpellProjectileEntity>create(SpellProjectileEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .setCustomClientFactory((spawnEntity, world) -> new SpellProjectileEntity(EntityTypesPM.SPELL_PROJECTILE.get(), world))
            .build(PrimalMagic.MODID + ":spell_projectile"));
    public static final RegistryObject<EntityType<SpellMineEntity>> SPELL_MINE = ENTITY_TYPES.register("spell_mine", () -> EntityType.Builder.<SpellMineEntity>create(SpellMineEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .setCustomClientFactory((spawnEntity, world) -> new SpellMineEntity(EntityTypesPM.SPELL_MINE.get(), world))
            .build(PrimalMagic.MODID + ":spell_mine"));
    public static final RegistryObject<EntityType<PrimaliteGolemEntity>> PRIMALITE_GOLEM = ENTITY_TYPES.register("primalite_golem", () -> EntityType.Builder.<PrimaliteGolemEntity>create(PrimaliteGolemEntity::new, EntityClassification.MISC)
            .size(1.4F, 2.7F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new PrimaliteGolemEntity(EntityTypesPM.PRIMALITE_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":primalite_golem"));
    public static final RegistryObject<EntityType<HexiumGolemEntity>> HEXIUM_GOLEM = ENTITY_TYPES.register("hexium_golem", () -> EntityType.Builder.<HexiumGolemEntity>create(HexiumGolemEntity::new, EntityClassification.MISC)
            .size(1.4F, 2.7F)
            .immuneToFire()
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new HexiumGolemEntity(EntityTypesPM.HEXIUM_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":hexium_golem"));
    public static final RegistryObject<EntityType<HallowsteelGolemEntity>> HALLOWSTEEL_GOLEM = ENTITY_TYPES.register("hallowsteel_golem", () -> EntityType.Builder.<HallowsteelGolemEntity>create(HallowsteelGolemEntity::new, EntityClassification.MISC)
            .size(1.4F, 2.7F)
            .immuneToFire()
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new HallowsteelGolemEntity(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":hallowsteel_golem"));
    public static final RegistryObject<EntityType<BasicEarthPixieEntity>> BASIC_EARTH_PIXIE = ENTITY_TYPES.register("pixie_basic_earth", () -> EntityType.Builder.<BasicEarthPixieEntity>create(BasicEarthPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicEarthPixieEntity(EntityTypesPM.BASIC_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_earth"));
    public static final RegistryObject<EntityType<GrandEarthPixieEntity>> GRAND_EARTH_PIXIE = ENTITY_TYPES.register("pixie_grand_earth", () -> EntityType.Builder.<GrandEarthPixieEntity>create(GrandEarthPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandEarthPixieEntity(EntityTypesPM.GRAND_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_earth"));
    public static final RegistryObject<EntityType<MajesticEarthPixieEntity>> MAJESTIC_EARTH_PIXIE = ENTITY_TYPES.register("pixie_majestic_earth", () -> EntityType.Builder.<MajesticEarthPixieEntity>create(MajesticEarthPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticEarthPixieEntity(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_earth"));
    public static final RegistryObject<EntityType<BasicSeaPixieEntity>> BASIC_SEA_PIXIE = ENTITY_TYPES.register("pixie_basic_sea", () -> EntityType.Builder.<BasicSeaPixieEntity>create(BasicSeaPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSeaPixieEntity(EntityTypesPM.BASIC_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sea"));
    public static final RegistryObject<EntityType<GrandSeaPixieEntity>> GRAND_SEA_PIXIE = ENTITY_TYPES.register("pixie_grand_sea", () -> EntityType.Builder.<GrandSeaPixieEntity>create(GrandSeaPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSeaPixieEntity(EntityTypesPM.GRAND_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sea"));
    public static final RegistryObject<EntityType<MajesticSeaPixieEntity>> MAJESTIC_SEA_PIXIE = ENTITY_TYPES.register("pixie_majestic_sea", () -> EntityType.Builder.<MajesticSeaPixieEntity>create(MajesticSeaPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSeaPixieEntity(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sea"));
    public static final RegistryObject<EntityType<BasicSkyPixieEntity>> BASIC_SKY_PIXIE = ENTITY_TYPES.register("pixie_basic_sky", () -> EntityType.Builder.<BasicSkyPixieEntity>create(BasicSkyPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSkyPixieEntity(EntityTypesPM.BASIC_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sky"));
    public static final RegistryObject<EntityType<GrandSkyPixieEntity>> GRAND_SKY_PIXIE = ENTITY_TYPES.register("pixie_grand_sky", () -> EntityType.Builder.<GrandSkyPixieEntity>create(GrandSkyPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSkyPixieEntity(EntityTypesPM.GRAND_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sky"));
    public static final RegistryObject<EntityType<MajesticSkyPixieEntity>> MAJESTIC_SKY_PIXIE = ENTITY_TYPES.register("pixie_majestic_sky", () -> EntityType.Builder.<MajesticSkyPixieEntity>create(MajesticSkyPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSkyPixieEntity(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sky"));
    public static final RegistryObject<EntityType<BasicSunPixieEntity>> BASIC_SUN_PIXIE = ENTITY_TYPES.register("pixie_basic_sun", () -> EntityType.Builder.<BasicSunPixieEntity>create(BasicSunPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSunPixieEntity(EntityTypesPM.BASIC_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sun"));
    public static final RegistryObject<EntityType<GrandSunPixieEntity>> GRAND_SUN_PIXIE = ENTITY_TYPES.register("pixie_grand_sun", () -> EntityType.Builder.<GrandSunPixieEntity>create(GrandSunPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSunPixieEntity(EntityTypesPM.GRAND_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sun"));
    public static final RegistryObject<EntityType<MajesticSunPixieEntity>> MAJESTIC_SUN_PIXIE = ENTITY_TYPES.register("pixie_majestic_sun", () -> EntityType.Builder.<MajesticSunPixieEntity>create(MajesticSunPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSunPixieEntity(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sun"));
    public static final RegistryObject<EntityType<BasicMoonPixieEntity>> BASIC_MOON_PIXIE = ENTITY_TYPES.register("pixie_basic_moon", () -> EntityType.Builder.<BasicMoonPixieEntity>create(BasicMoonPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicMoonPixieEntity(EntityTypesPM.BASIC_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_moon"));
    public static final RegistryObject<EntityType<GrandMoonPixieEntity>> GRAND_MOON_PIXIE = ENTITY_TYPES.register("pixie_grand_moon", () -> EntityType.Builder.<GrandMoonPixieEntity>create(GrandMoonPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandMoonPixieEntity(EntityTypesPM.GRAND_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_moon"));
    public static final RegistryObject<EntityType<MajesticMoonPixieEntity>> MAJESTIC_MOON_PIXIE = ENTITY_TYPES.register("pixie_majestic_moon", () -> EntityType.Builder.<MajesticMoonPixieEntity>create(MajesticMoonPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticMoonPixieEntity(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_moon"));
    public static final RegistryObject<EntityType<BasicBloodPixieEntity>> BASIC_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_basic_blood", () -> EntityType.Builder.<BasicBloodPixieEntity>create(BasicBloodPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicBloodPixieEntity(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_blood"));
    public static final RegistryObject<EntityType<GrandBloodPixieEntity>> GRAND_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_grand_blood", () -> EntityType.Builder.<GrandBloodPixieEntity>create(GrandBloodPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandBloodPixieEntity(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_blood"));
    public static final RegistryObject<EntityType<MajesticBloodPixieEntity>> MAJESTIC_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_majestic_blood", () -> EntityType.Builder.<MajesticBloodPixieEntity>create(MajesticBloodPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticBloodPixieEntity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_blood"));
    public static final RegistryObject<EntityType<BasicInfernalPixieEntity>> BASIC_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_basic_infernal", () -> EntityType.Builder.<BasicInfernalPixieEntity>create(BasicInfernalPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicInfernalPixieEntity(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_infernal"));
    public static final RegistryObject<EntityType<GrandInfernalPixieEntity>> GRAND_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_grand_infernal", () -> EntityType.Builder.<GrandInfernalPixieEntity>create(GrandInfernalPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandInfernalPixieEntity(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_infernal"));
    public static final RegistryObject<EntityType<MajesticInfernalPixieEntity>> MAJESTIC_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_majestic_infernal", () -> EntityType.Builder.<MajesticInfernalPixieEntity>create(MajesticInfernalPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticInfernalPixieEntity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_blood"));
    public static final RegistryObject<EntityType<BasicVoidPixieEntity>> BASIC_VOID_PIXIE = ENTITY_TYPES.register("pixie_basic_void", () -> EntityType.Builder.<BasicVoidPixieEntity>create(BasicVoidPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicVoidPixieEntity(EntityTypesPM.BASIC_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_void"));
    public static final RegistryObject<EntityType<GrandVoidPixieEntity>> GRAND_VOID_PIXIE = ENTITY_TYPES.register("pixie_grand_void", () -> EntityType.Builder.<GrandVoidPixieEntity>create(GrandVoidPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandVoidPixieEntity(EntityTypesPM.GRAND_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_void"));
    public static final RegistryObject<EntityType<MajesticVoidPixieEntity>> MAJESTIC_VOID_PIXIE = ENTITY_TYPES.register("pixie_majestic_void", () -> EntityType.Builder.<MajesticVoidPixieEntity>create(MajesticVoidPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticVoidPixieEntity(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_void"));
    public static final RegistryObject<EntityType<BasicHallowedPixieEntity>> BASIC_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_basic_hallowed", () -> EntityType.Builder.<BasicHallowedPixieEntity>create(BasicHallowedPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicHallowedPixieEntity(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_hallowed"));
    public static final RegistryObject<EntityType<GrandHallowedPixieEntity>> GRAND_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_grand_hallowed", () -> EntityType.Builder.<GrandHallowedPixieEntity>create(GrandHallowedPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandHallowedPixieEntity(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_hallowed"));
    public static final RegistryObject<EntityType<MajesticHallowedPixieEntity>> MAJESTIC_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_majestic_hallowed", () -> EntityType.Builder.<MajesticHallowedPixieEntity>create(MajesticHallowedPixieEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.5F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticHallowedPixieEntity(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_hallowed"));
}
