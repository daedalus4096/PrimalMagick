package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Primary interface for a spell payload component.  Spell payloads define the primary effect of the
 * spell, such as damage, block breaking, or other effects.  Spell payloads usually have properties
 * which determine the extent of their effect.  They also determine the base cost of the spell
 * package they're in, as well as what sound effect plays when the spell is cast.
 * 
 * @author Daedalus4096
 */
public interface ISpellPayload extends INBTSerializable<CompoundTag> {
    /**
     * Execute this spell payload upon the designated target.
     * 
     * @param target a raytrace result containing the block/entity upon which this payload should be executed
     * @param burstPoint the initial impact location of the spell if it has a Burst mod, or null if it does not
     * @param spell the full spell package containing this payload
     * @param world the world in which the payload should be executed
     * @param caster the player that originally casted the spell
     * @param spellSource the wand or scroll containing the spell package
     */
    public void execute(@Nullable HitResult target, @Nullable Vec3 burstPoint, @Nonnull SpellPackage spell, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource);
    
    /**
     * Determine whether this payload has an effect that should be executed.  Should be true for all but
     * placeholder payloads.
     * 
     * @return true if this payload has an effect that should be executed, false otherwise
     */
    public boolean isActive();
    
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
    public int getBaseManaCost();
    
    /**
     * Play the sound event corresponding to this spell payload.
     * 
     * @param world the world in which the sound event should be played
     * @param origin the origin position of the sound to be played
     */
    public void playSounds(@Nonnull Level world, @Nonnull BlockPos origin);
    
    /**
     * Get a name-ordered list of properties used by this spell payload.
     * 
     * @return a name-ordered list of properties used by this spell payload
     */
    @Nonnull
    public List<SpellProperty> getProperties();
    
    /**
     * Get a specific property of the spell payload.
     * 
     * @param name the name of the property to retrieve
     * @return the named property, or null if no such property is attached to this spell payload
     */
    @Nullable
    public SpellProperty getProperty(String name);
    
    /**
     * Get the value of a specific property of the spell payload.
     * 
     * @param name the name of the property value to retrieve
     * @return the named property's value, or zero if no such property is attached to this spell payload
     */
    public int getPropertyValue(String name);
    
    /**
     * Get a display text component containing the human-friendly name of this spell payload type.
     * 
     * @return the spell payload type name
     */
    @Nonnull
    public Component getTypeName();
    
    /**
     * Get a display text component to show in the details tooltip of the spell.
     * 
     * @return the spell payload details
     */
    public default Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return this.getTypeName();
    }
    
    /**
     * Get a display text component containing the human-friendly text to be used to identify the
     * spell payload in the default of a spell package.
     * 
     * @return the spell payload's default name
     */
    @Nonnull
    public Component getDefaultNamePiece();
}
