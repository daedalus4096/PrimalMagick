package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableObject;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a projectile spell vehicle.  Projectiles are long range vehicles that have a travel
 * time and are affected by gravity.  They're like throwing a magick snowball.
 * 
 * @author Daedalus4096
 */
public class ProjectileSpellVehicle extends AbstractSpellVehicle<ProjectileSpellVehicle> {
    public static final ProjectileSpellVehicle INSTANCE = new ProjectileSpellVehicle();
    
    public static final MapCodec<ProjectileSpellVehicle> CODEC = MapCodec.unit(ProjectileSpellVehicle.INSTANCE);
    public static final StreamCodec<ByteBuf, ProjectileSpellVehicle> STREAM_CODEC = StreamCodec.unit(ProjectileSpellVehicle.INSTANCE);
    
    public static final String TYPE = "projectile";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_VEHICLE_PROJECTILE));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    public SpellVehicleType<ProjectileSpellVehicle> getType() {
        return SpellVehiclesPM.PROJECTILE.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (spell.payload() != null) {
            Vec3 baseLookVector = caster.getViewVector(1.0F);
            MutableObject<List<Vec3>> lookVectors = new MutableObject<>();
            spell.getMod(SpellModsPM.FORK.get()).ifPresentOrElse(forkMod -> {
                // If a Fork mod is in the spell package, calculate a direction vector for each fork, based on the caster's line of sight
                lookVectors.setValue(forkMod.getComponent().getDirectionUnitVectors(baseLookVector, world.random, spell, spellSource));
            }, () -> {
                // If no Fork mod is in the spell package, use the caster's line of sight for the direction vector
                lookVectors.setValue(Arrays.asList(baseLookVector.normalize()));
            });
            
            for (Vec3 lookVector : lookVectors.getValue()) {
                // Instantiate the projectile entity and launch it into the world
                SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, spell, spellSource);
                projectile.shoot(lookVector.x, lookVector.y, lookVector.z, 1.5F, 0.0F);
                world.addFreshEntity(projectile);
            }
        }
    }
    
    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        return 0;
    }

    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        return 2;
    }

    @Override
    public boolean isIndirect() {
        return true;
    }
}
