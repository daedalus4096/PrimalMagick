package com.verdantartifice.primalmagick.common.entities;

import com.verdantartifice.primalmagick.common.entities.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.entities.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.entities.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.entities.misc.SinCrystalEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicBloodPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicEarthPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicHallowedPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicInfernalPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicMoonPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicSeaPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicSkyPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicSunPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.BasicVoidPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandBloodPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandEarthPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandHallowedPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandInfernalPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandMoonPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandSeaPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandSkyPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandSunPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.GrandVoidPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticBloodPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticEarthPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticHallowedPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticInfernalPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticMoonPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticSeaPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticSkyPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticSunPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.MajesticVoidPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.BasicGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.GrandGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.entities.pixies.guardians.MajesticGuardianPixieEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.AlchemicalBombEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.AppleEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.ForbiddenTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HallowsteelTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.HexiumTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.IgnyxEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.PrimaliteTridentEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.SinCrashEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Deferred registry for mod entity types.
 * 
 * @author Daedalus4096
 */
public class EntityTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.ENTITY_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<EntityType<?>, EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = register("spell_projectile", EntityType.Builder.<SpellProjectileEntity>of(SpellProjectileEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F));
    public static final IRegistryItem<EntityType<?>, EntityType<SpellMineEntity>> SPELL_MINE = register("spell_mine", EntityType.Builder.<SpellMineEntity>of(SpellMineEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F));
    public static final IRegistryItem<EntityType<?>, EntityType<SinCrashEntity>> SIN_CRASH = register("sin_crash", EntityType.Builder.<SinCrashEntity>of(SinCrashEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final IRegistryItem<EntityType<?>, EntityType<SinCrystalEntity>> SIN_CRYSTAL = register("sin_crystal", EntityType.Builder.<SinCrystalEntity>of(SinCrystalEntity::new, MobCategory.MISC)
            .sized(2.0F, 2.0F)
            .clientTrackingRange(16)
            .updateInterval(Integer.MAX_VALUE));
    public static final IRegistryItem<EntityType<?>, EntityType<FlyingCarpetEntity>> FLYING_CARPET = register("flying_carpet", EntityType.Builder.<FlyingCarpetEntity>of(FlyingCarpetEntity::new, MobCategory.MISC)
            .sized(1.0F, 0.0625F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AppleEntity>> APPLE = register("apple", EntityType.Builder.<AppleEntity>of(AppleEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final IRegistryItem<EntityType<?>, EntityType<IgnyxEntity>> IGNYX = register("ignyx", EntityType.Builder.<IgnyxEntity>of(IgnyxEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AlchemicalBombEntity>> ALCHEMICAL_BOMB = register("alchemical_bomb", EntityType.Builder.<AlchemicalBombEntity>of(AlchemicalBombEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));
    public static final IRegistryItem<EntityType<?>, EntityType<ManaArrowEntity>> MANA_ARROW = register("mana_arrow", EntityType.Builder.<ManaArrowEntity>of(ManaArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractTridentEntity>> PRIMALITE_TRIDENT = register("primalite_trident", EntityType.Builder.<AbstractTridentEntity>of(PrimaliteTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractTridentEntity>> HEXIUM_TRIDENT = register("hexium_trident", EntityType.Builder.<AbstractTridentEntity>of(HexiumTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractTridentEntity>> HALLOWSTEEL_TRIDENT = register("hallowsteel_trident", EntityType.Builder.<AbstractTridentEntity>of(HallowsteelTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractTridentEntity>> FORBIDDEN_TRIDENT = register("forbidden_trident", EntityType.Builder.<AbstractTridentEntity>of(ForbiddenTridentEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20));
    public static final IRegistryItem<EntityType<?>, EntityType<TreefolkEntity>> TREEFOLK = register("treefolk", EntityType.Builder.of(TreefolkEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8));
    public static final IRegistryItem<EntityType<?>, EntityType<InnerDemonEntity>> INNER_DEMON = register("inner_demon", EntityType.Builder.of(InnerDemonEntity::new, MobCategory.MONSTER)
            .sized(1.2F, 3.9F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<FriendlyWitchEntity>> FRIENDLY_WITCH = register("friendly_witch", EntityType.Builder.of(FriendlyWitchEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8));
    public static final IRegistryItem<EntityType<?>, EntityType<PrimaliteGolemEntity>> PRIMALITE_GOLEM = register("primalite_golem", EntityType.Builder.of(PrimaliteGolemEntity::new, MobCategory.CREATURE)
            .sized(1.4F, 2.7F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<HexiumGolemEntity>> HEXIUM_GOLEM = register("hexium_golem", EntityType.Builder.of(HexiumGolemEntity::new, MobCategory.CREATURE)
            .sized(1.4F, 2.7F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<HallowsteelGolemEntity>> HALLOWSTEEL_GOLEM = register("hallowsteel_golem", EntityType.Builder.of(HallowsteelGolemEntity::new, MobCategory.CREATURE)
            .sized(1.4F, 2.7F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_EARTH_PIXIE = register("pixie_basic_earth", EntityType.Builder.<AbstractPixieEntity>of(BasicEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_EARTH_PIXIE = register("pixie_grand_earth", EntityType.Builder.<AbstractPixieEntity>of(GrandEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_EARTH_PIXIE = register("pixie_majestic_earth", EntityType.Builder.<AbstractPixieEntity>of(MajesticEarthPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_SEA_PIXIE = register("pixie_basic_sea", EntityType.Builder.<AbstractPixieEntity>of(BasicSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_SEA_PIXIE = register("pixie_grand_sea", EntityType.Builder.<AbstractPixieEntity>of(GrandSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_SEA_PIXIE = register("pixie_majestic_sea", EntityType.Builder.<AbstractPixieEntity>of(MajesticSeaPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_SKY_PIXIE = register("pixie_basic_sky", EntityType.Builder.<AbstractPixieEntity>of(BasicSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_SKY_PIXIE = register("pixie_grand_sky", EntityType.Builder.<AbstractPixieEntity>of(GrandSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_SKY_PIXIE = register("pixie_majestic_sky", EntityType.Builder.<AbstractPixieEntity>of(MajesticSkyPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_SUN_PIXIE = register("pixie_basic_sun", EntityType.Builder.<AbstractPixieEntity>of(BasicSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_SUN_PIXIE = register("pixie_grand_sun", EntityType.Builder.<AbstractPixieEntity>of(GrandSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_SUN_PIXIE = register("pixie_majestic_sun", EntityType.Builder.<AbstractPixieEntity>of(MajesticSunPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_MOON_PIXIE = register("pixie_basic_moon", EntityType.Builder.<AbstractPixieEntity>of(BasicMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_MOON_PIXIE = register("pixie_grand_moon", EntityType.Builder.<AbstractPixieEntity>of(GrandMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_MOON_PIXIE = register("pixie_majestic_moon", EntityType.Builder.<AbstractPixieEntity>of(MajesticMoonPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_BLOOD_PIXIE = register("pixie_basic_blood", EntityType.Builder.<AbstractPixieEntity>of(BasicBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_BLOOD_PIXIE = register("pixie_grand_blood", EntityType.Builder.<AbstractPixieEntity>of(GrandBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_BLOOD_PIXIE = register("pixie_majestic_blood", EntityType.Builder.<AbstractPixieEntity>of(MajesticBloodPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_INFERNAL_PIXIE = register("pixie_basic_infernal", EntityType.Builder.<AbstractPixieEntity>of(BasicInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_INFERNAL_PIXIE = register("pixie_grand_infernal", EntityType.Builder.<AbstractPixieEntity>of(GrandInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_INFERNAL_PIXIE = register("pixie_majestic_infernal", EntityType.Builder.<AbstractPixieEntity>of(MajesticInfernalPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .fireImmune()
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_VOID_PIXIE = register("pixie_basic_void", EntityType.Builder.<AbstractPixieEntity>of(BasicVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_VOID_PIXIE = register("pixie_grand_void", EntityType.Builder.<AbstractPixieEntity>of(GrandVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_VOID_PIXIE = register("pixie_majestic_void", EntityType.Builder.<AbstractPixieEntity>of(MajesticVoidPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> BASIC_HALLOWED_PIXIE = register("pixie_basic_hallowed", EntityType.Builder.<AbstractPixieEntity>of(BasicHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> GRAND_HALLOWED_PIXIE = register("pixie_grand_hallowed", EntityType.Builder.<AbstractPixieEntity>of(GrandHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<AbstractPixieEntity>> MAJESTIC_HALLOWED_PIXIE = register("pixie_majestic_hallowed", EntityType.Builder.<AbstractPixieEntity>of(MajesticHallowedPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<PixieHouseEntity>> PIXIE_HOUSE = register("pixie_house", EntityType.Builder.of(PixieHouseEntity::new, MobCategory.MISC)
            .sized(0.875F, 2.0F)
            .eyeHeight(1.7775F)
            .clientTrackingRange(10));
    public static final IRegistryItem<EntityType<?>, EntityType<BasicGuardianPixieEntity>> BASIC_GUARDIAN_PIXIE = register("guardian_pixie_basic", EntityType.Builder.of(BasicGuardianPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .noSummon());
    public static final IRegistryItem<EntityType<?>, EntityType<GrandGuardianPixieEntity>> GRAND_GUARDIAN_PIXIE = register("guardian_pixie_grand", EntityType.Builder.of(GrandGuardianPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .noSummon());
    public static final IRegistryItem<EntityType<?>, EntityType<MajesticGuardianPixieEntity>> MAJESTIC_GUARDIAN_PIXIE = register("guardian_pixie_majestic", EntityType.Builder.of(MajesticGuardianPixieEntity::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .noSummon());

    private static <T extends Entity> IRegistryItem<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, ResourceUtils.loc(name));
        return Services.ENTITY_TYPES_REGISTRY.register(name, () -> builder.build(key));
    }
}
