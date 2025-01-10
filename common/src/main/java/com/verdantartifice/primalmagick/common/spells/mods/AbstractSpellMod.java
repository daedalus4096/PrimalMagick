package com.verdantartifice.primalmagick.common.spells.mods;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.tags.SpellPropertyTagsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for a spell mod.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellMod<T extends AbstractSpellMod<T>> implements ISpellMod {
    public static Codec<AbstractSpellMod<?>> dispatchCodec() {
        return Services.SPELL_MOD_TYPES_REGISTRY.codec().dispatch("mod_type", AbstractSpellMod::getType, SpellModType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractSpellMod<?>> dispatchStreamCodec() {
        return Services.SPELL_MOD_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractSpellMod::getType, SpellModType::streamCodec);
    }
    
    public abstract SpellModType<T> getType();
    
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

    public int getModdedPropertyValue(SpellProperty property, SpellPackage spell, @Nullable ItemStack spellSource, HolderLookup.Provider registries) {
        MutableInt retVal = new MutableInt(spell.getMod(this.getType()).orElseThrow().getPropertyValue(property));
        if (retVal.intValue() > 0 && !SpellPropertiesPM.AMPLIFY_POWER.get().equals(property) && SpellPropertiesPM.PROPERTIES.get().tags().getTag(SpellPropertyTagsPM.AMPLIFIABLE).contains(property)) {
            // For power or duration properties greater than zero, increase the total result by
            // the power of any attached Amplify spell mod or Spell Power enchantment
            spell.getMod(SpellModsPM.AMPLIFY.get()).ifPresent(ampMod -> {
                retVal.add(ampMod.getPropertyValue(SpellPropertiesPM.AMPLIFY_POWER.get()));
            });
            if (spellSource != null) {
                int enchLevel = EnchantmentHelperPM.getEnchantmentLevel(spellSource, EnchantmentsPM.SPELL_POWER, registries);
                if (enchLevel > 0) {
                    retVal.add(enchLevel);
                }
            }
        }
        return retVal.intValue();
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
