package com.verdantartifice.primalmagick.common.spells.payloads;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.ISpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Primary interface for a spell payload component.  Spell payloads define the primary effect of the
 * spell, such as damage, block breaking, or other effects.  Spell payloads usually have properties
 * which determine the extent of their effect.  They also determine the base cost of the spell
 * package they're in, as well as what sound effect plays when the spell is cast.
 * 
 * @author Daedalus4096
 */
public interface ISpellPayload extends ISpellComponent {
    /**
     * Execute this spell payload upon the designated target.
     * 
     * @param target a raytrace result containing the block/entity upon which this payload should be executed
     * @param burstPoint the initial impact location of the spell if it has a Burst mod, or null if it does not
     * @param spell the full spell package containing this payload
     * @param world the world in which the payload should be executed
     * @param caster the player that originally casted the spell
     * @param spellSource the wand or scroll containing the spell package
     * @param projectileEntity the entity carrying this spell payload, if it's a projectile
     */
    public void execute(@Nullable HitResult target, @Nullable Vec3 burstPoint, @Nonnull SpellPackage spell, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource, @Nullable Entity projectileEntity);
    
    /**
     * Get the primal source of the payload.  Determines the type of mana that must be spent to cast it.
     * 
     * @return the primal source of the payload
     */
    @Nonnull
    public Source getSource();
    
    /**
     * Get the base mana cost of a spell containing this payload.
     * 
     * @return the base mana cost of the spell
     */
    @Nonnull
    public int getBaseManaCost(SpellPropertyConfiguration properties);
    
    /**
     * Play the sound event corresponding to this spell payload.
     * 
     * @param world the world in which the sound event should be played
     * @param origin the origin position of the sound to be played
     */
    public void playSounds(@Nonnull Level world, @Nonnull BlockPos origin);
}
