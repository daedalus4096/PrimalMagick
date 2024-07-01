package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementType;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for a spell mod.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellMod<T extends AbstractSpellMod<T>> implements ISpellMod {
    public static Codec<AbstractSpellMod<?>> dispatchCodec() {
        return RegistryCodecs.codec(SpellModsPM.TYPES).dispatch("mod_type", AbstractSpellMod::getType, SpellModType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractSpellMod<?>> dispatchStreamCodec() {
        return RegistryCodecs.streamCodec(SpellModsPM.TYPES).dispatch(AbstractSpellMod::getType, SpellModType::streamCodec);
    }
    
    protected abstract SpellModType<T> getType();
    
    /**
     * Get the type name for this spell mod.
     * 
     * @return the type name for this spell mod
     */
    protected abstract String getModType();
    
    @Override
    public boolean isActive() {
        return true;
    }
    
    @Override
    public List<SpellProperty> getProperties() {
        // Sort properties by their display names
        return this.getPropertiesInner().stream().sorted(Comparator.comparing(SpellProperty::id)).collect(Collectors.toList());
    }
    
    protected abstract List<SpellProperty> getPropertiesInner();

    @Override
    public SpellProperty getProperty(ResourceLocation id) {
        return this.getPropertiesInner().stream().filter(prop -> prop.id().equals(id)).findFirst().orElse(null);
    }

    public int getModdedPropertyValue(String name, SpellPackage spell, @Nullable ItemStack spellSource) {
        int retVal = this.getPropertyValue(name);
        if (retVal > 0 && ("power".equals(name) || "duration".equals(name))) {
            // For power or duration properties greater than zero, increase the total result by
            // the power of any attached Amplify spell mod or Spell Power enchantment
            AmplifySpellMod ampMod = spell.getMod(AmplifySpellMod.class, "power");
            if (ampMod != null) {
                retVal += ampMod.getPropertyValue("power");
            }
            if (spellSource != null) {
                int enchLevel = spellSource.getEnchantmentLevel(EnchantmentsPM.SPELL_POWER.get());
                if (enchLevel > 0) {
                    retVal += enchLevel;
                }
            }
        }
        return retVal;
    }
    
    @Override
    public Component getTypeName() {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".type");
    }
    
    @Override
    public Component getDefaultNamePiece() {
        return Component.translatable("spells.primalmagick.mod." + this.getModType() + ".default_name");
    }
}
