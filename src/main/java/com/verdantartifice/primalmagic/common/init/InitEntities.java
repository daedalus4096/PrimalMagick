package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Point of registration for mod entity types.
 * 
 * @author Daedalus4096
 */
public class InitEntities {
    public static void initEntityTypes(IForgeRegistry<EntityType<?>> registry) {
        registry.register(EntityType.Builder.<SpellProjectileEntity>create(SpellProjectileEntity::new, EntityClassification.MISC)
                .size(0.25F, 0.25F)
                .setCustomClientFactory((spawnEntity, world) -> new SpellProjectileEntity(EntityTypesPM.SPELL_PROJECTILE, world))
                .build(PrimalMagic.MODID + ":spell_projectile")
                .setRegistryName(PrimalMagic.MODID, "spell_projectile"));
        
        registry.register(EntityType.Builder.<SpellMineEntity>create(SpellMineEntity::new, EntityClassification.MISC)
                .size(0.25F, 0.25F)
                .setCustomClientFactory((spawnEntity, world) -> new SpellMineEntity(EntityTypesPM.SPELL_MINE, world))
                .build(PrimalMagic.MODID + ":spell_mine")
                .setRegistryName(PrimalMagic.MODID, "spell_mine"));
    }
}
