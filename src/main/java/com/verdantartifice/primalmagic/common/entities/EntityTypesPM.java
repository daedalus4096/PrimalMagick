package com.verdantartifice.primalmagic.common.entities;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;
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
            .size(0.5F, 0.9F)
            .trackingRange(10)
            .setCustomClientFactory((spawnEntity, world) -> new BasicEarthPixieEntity(EntityTypesPM.BASIC_EARTH_PIXIE.get(), world))
            .build(PrimalMagic.MODID + ":pixie_basic_earth"));
}
