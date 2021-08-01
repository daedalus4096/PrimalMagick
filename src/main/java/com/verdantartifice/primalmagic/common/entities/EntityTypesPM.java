package com.verdantartifice.primalmagic.common.entities;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;
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
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagic.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagic.common.entities.misc.SinCrystalEntity;
import com.verdantartifice.primalmagic.common.entities.misc.TreefolkEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.AlchemicalBombEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.AppleEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.ForbiddenTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.HallowsteelTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.HexiumTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.PrimaliteTridentEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SinCrashEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
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
    
    public static final RegistryObject<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = ENTITY_TYPES.register("spell_projectile", () -> EntityType.Builder.<SpellProjectileEntity>of(SpellProjectileEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .setCustomClientFactory((spawnEntity, world) -> new SpellProjectileEntity(EntityTypesPM.SPELL_PROJECTILE.get(), world))
            .build(PrimalMagic.MODID + ":spell_projectile"));
    public static final RegistryObject<EntityType<SpellMineEntity>> SPELL_MINE = ENTITY_TYPES.register("spell_mine", () -> EntityType.Builder.<SpellMineEntity>of(SpellMineEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .setCustomClientFactory((spawnEntity, world) -> new SpellMineEntity(EntityTypesPM.SPELL_MINE.get(), world))
            .build(PrimalMagic.MODID + ":spell_mine"));
    public static final RegistryObject<EntityType<SinCrashEntity>> SIN_CRASH = ENTITY_TYPES.register("sin_crash", () -> EntityType.Builder.<SinCrashEntity>of(SinCrashEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .setCustomClientFactory((spawnEntity, world) -> new SinCrashEntity(EntityTypesPM.SIN_CRASH.get(), world))
            .build(PrimalMagic.MODID + ":sin_crash"));
    public static final RegistryObject<EntityType<SinCrystalEntity>> SIN_CRYSTAL = ENTITY_TYPES.register("sin_crystal", () -> EntityType.Builder.<SinCrystalEntity>of(SinCrystalEntity::new, MobCategory.MISC)
            .sized(2.0F, 2.0F)
            .clientTrackingRange(16)
            .updateInterval(Integer.MAX_VALUE)
            .setCustomClientFactory((spawnEntity, world) -> new SinCrystalEntity(EntityTypesPM.SIN_CRYSTAL.get(), world))
            .build(PrimalMagic.MODID + ":sin_crystal"));
    public static final RegistryObject<EntityType<FlyingCarpetEntity>> FLYING_CARPET = ENTITY_TYPES.register("flying_carpet", () -> EntityType.Builder.<FlyingCarpetEntity>of(FlyingCarpetEntity::new, MobCategory.MISC)
            .sized(1.0F, 0.0625F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new FlyingCarpetEntity(EntityTypesPM.FLYING_CARPET.get(), world))
            .build(PrimalMagic.MODID + ":flying_carpet"));
    public static final RegistryObject<EntityType<AppleEntity>> APPLE = ENTITY_TYPES.register("apple", () -> EntityType.Builder.<AppleEntity>of(AppleEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .setCustomClientFactory((spawnEntity, world) -> new AppleEntity(EntityTypesPM.APPLE.get(), world))
            .build(PrimalMagic.MODID + ":apple"));
    public static final RegistryObject<EntityType<AlchemicalBombEntity>> ALCHEMICAL_BOMB = ENTITY_TYPES.register("alchemical_bomb", () -> EntityType.Builder.<AlchemicalBombEntity>of(AlchemicalBombEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .setCustomClientFactory((spawnEntity, world) -> new AlchemicalBombEntity(EntityTypesPM.ALCHEMICAL_BOMB.get(), world))
            .build(PrimalMagic.MODID + ":alchemical_bomb"));
    public static final RegistryObject<EntityType<AbstractTridentEntity>> PRIMALITE_TRIDENT = ENTITY_TYPES.register("primalite_trident", () -> EntityType.Builder.<AbstractTridentEntity>of(PrimaliteTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory((spawnEntity, world) -> new PrimaliteTridentEntity(EntityTypesPM.PRIMALITE_TRIDENT.get(), world))
            .build(PrimalMagic.MODID + ":primalite_trident"));
    public static final RegistryObject<EntityType<AbstractTridentEntity>> HEXIUM_TRIDENT = ENTITY_TYPES.register("hexium_trident", () -> EntityType.Builder.<AbstractTridentEntity>of(HexiumTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory((spawnEntity, world) -> new HexiumTridentEntity(EntityTypesPM.HEXIUM_TRIDENT.get(), world))
            .build(PrimalMagic.MODID + ":hexium_trident"));
    public static final RegistryObject<EntityType<AbstractTridentEntity>> HALLOWSTEEL_TRIDENT = ENTITY_TYPES.register("hallowsteel_trident", () -> EntityType.Builder.<AbstractTridentEntity>of(HallowsteelTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory((spawnEntity, world) -> new HallowsteelTridentEntity(EntityTypesPM.HALLOWSTEEL_TRIDENT.get(), world))
            .build(PrimalMagic.MODID + ":hallowsteel_trident"));
    public static final RegistryObject<EntityType<AbstractTridentEntity>> FORBIDDEN_TRIDENT = ENTITY_TYPES.register("forbidden_trident", () -> EntityType.Builder.<AbstractTridentEntity>of(ForbiddenTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory((spawnEntity, world) -> new ForbiddenTridentEntity(EntityTypesPM.FORBIDDEN_TRIDENT.get(), world))
            .build(PrimalMagic.MODID + ":forbidden_trident"));
    public static final RegistryObject<EntityType<TreefolkEntity>> TREEFOLK = ENTITY_TYPES.register("treefolk", () -> EntityType.Builder.<TreefolkEntity>of(TreefolkEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .setCustomClientFactory((spawnEntity, world) -> new TreefolkEntity(EntityTypesPM.TREEFOLK.get(), world))
            .build(PrimalMagic.MODID + ":treefolk"));
    public static final RegistryObject<EntityType<InnerDemonEntity>> INNER_DEMON = ENTITY_TYPES.register("inner_demon", () -> EntityType.Builder.<InnerDemonEntity>of(InnerDemonEntity::new, MobCategory.MONSTER)
            .sized(1.2F, 3.9F)
            .fireImmune()
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new InnerDemonEntity(EntityTypesPM.INNER_DEMON.get(), world))
            .build(PrimalMagic.MODID + ":inner_demon"));
    public static final RegistryObject<EntityType<PrimaliteGolemEntity>> PRIMALITE_GOLEM = ENTITY_TYPES.register("primalite_golem", () -> EntityType.Builder.<PrimaliteGolemEntity>of(PrimaliteGolemEntity::new, MobCategory.MISC)
            .sized(1.4F, 2.7F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new PrimaliteGolemEntity(EntityTypesPM.PRIMALITE_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":primalite_golem"));
    public static final RegistryObject<EntityType<HexiumGolemEntity>> HEXIUM_GOLEM = ENTITY_TYPES.register("hexium_golem", () -> EntityType.Builder.<HexiumGolemEntity>of(HexiumGolemEntity::new, MobCategory.MISC)
            .sized(1.4F, 2.7F)
            .fireImmune()
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new HexiumGolemEntity(EntityTypesPM.HEXIUM_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":hexium_golem"));
    public static final RegistryObject<EntityType<HallowsteelGolemEntity>> HALLOWSTEEL_GOLEM = ENTITY_TYPES.register("hallowsteel_golem", () -> EntityType.Builder.<HallowsteelGolemEntity>of(HallowsteelGolemEntity::new, MobCategory.MISC)
            .sized(1.4F, 2.7F)
            .fireImmune()
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new HallowsteelGolemEntity(EntityTypesPM.HALLOWSTEEL_GOLEM.get(), world))
            .build(PrimalMagic.MODID + ":hallowsteel_golem"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_EARTH_PIXIE = ENTITY_TYPES.register("pixie_basic_earth", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicEarthPixieEntity(EntityTypesPM.BASIC_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_earth"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_EARTH_PIXIE = ENTITY_TYPES.register("pixie_grand_earth", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandEarthPixieEntity(EntityTypesPM.GRAND_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_earth"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_EARTH_PIXIE = ENTITY_TYPES.register("pixie_majestic_earth", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticEarthPixieEntity(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_earth"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_SEA_PIXIE = ENTITY_TYPES.register("pixie_basic_sea", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSeaPixieEntity(EntityTypesPM.BASIC_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sea"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_SEA_PIXIE = ENTITY_TYPES.register("pixie_grand_sea", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSeaPixieEntity(EntityTypesPM.GRAND_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sea"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_SEA_PIXIE = ENTITY_TYPES.register("pixie_majestic_sea", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSeaPixieEntity(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sea"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_SKY_PIXIE = ENTITY_TYPES.register("pixie_basic_sky", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSkyPixieEntity(EntityTypesPM.BASIC_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sky"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_SKY_PIXIE = ENTITY_TYPES.register("pixie_grand_sky", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSkyPixieEntity(EntityTypesPM.GRAND_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sky"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_SKY_PIXIE = ENTITY_TYPES.register("pixie_majestic_sky", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSkyPixieEntity(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sky"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_SUN_PIXIE = ENTITY_TYPES.register("pixie_basic_sun", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicSunPixieEntity(EntityTypesPM.BASIC_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_sun"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_SUN_PIXIE = ENTITY_TYPES.register("pixie_grand_sun", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandSunPixieEntity(EntityTypesPM.GRAND_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_sun"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_SUN_PIXIE = ENTITY_TYPES.register("pixie_majestic_sun", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticSunPixieEntity(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_sun"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_MOON_PIXIE = ENTITY_TYPES.register("pixie_basic_moon", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicMoonPixieEntity(EntityTypesPM.BASIC_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_moon"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_MOON_PIXIE = ENTITY_TYPES.register("pixie_grand_moon", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandMoonPixieEntity(EntityTypesPM.GRAND_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_moon"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_MOON_PIXIE = ENTITY_TYPES.register("pixie_majestic_moon", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticMoonPixieEntity(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_moon"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_basic_blood", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicBloodPixieEntity(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_blood"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_grand_blood", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandBloodPixieEntity(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_blood"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_BLOOD_PIXIE = ENTITY_TYPES.register("pixie_majestic_blood", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticBloodPixieEntity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_blood"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_basic_infernal", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicInfernalPixieEntity(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_infernal"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_grand_infernal", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandInfernalPixieEntity(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_infernal"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_INFERNAL_PIXIE = ENTITY_TYPES.register("pixie_majestic_infernal", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticInfernalPixieEntity(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_blood"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_VOID_PIXIE = ENTITY_TYPES.register("pixie_basic_void", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicVoidPixieEntity(EntityTypesPM.BASIC_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_void"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_VOID_PIXIE = ENTITY_TYPES.register("pixie_grand_void", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandVoidPixieEntity(EntityTypesPM.GRAND_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_void"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_VOID_PIXIE = ENTITY_TYPES.register("pixie_majestic_void", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticVoidPixieEntity(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_void"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> BASIC_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_basic_hallowed", () -> EntityType.Builder.<AbstractPixieEntity>of(BasicHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicHallowedPixieEntity(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_hallowed"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> GRAND_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_grand_hallowed", () -> EntityType.Builder.<AbstractPixieEntity>of(GrandHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new GrandHallowedPixieEntity(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_grand_hallowed"));
    public static final RegistryObject<EntityType<AbstractPixieEntity>> MAJESTIC_HALLOWED_PIXIE = ENTITY_TYPES.register("pixie_majestic_hallowed", () -> EntityType.Builder.<AbstractPixieEntity>of(MajesticHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new MajesticHallowedPixieEntity(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_majestic_hallowed"));
}
