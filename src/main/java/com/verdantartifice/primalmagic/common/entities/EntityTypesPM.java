package com.verdantartifice.primalmagic.common.entities;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holder for mod entity types.  Actual values populated by Forge post-registration.
 * 
 * @author Daedalus4096
 */
@ObjectHolder(PrimalMagic.MODID)
public class EntityTypesPM {
    public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = null;
    public static final EntityType<SpellMineEntity> SPELL_MINE = null;
}
