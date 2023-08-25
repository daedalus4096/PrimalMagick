package com.verdantartifice.primalmagick.common.spells.payloads;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.AmplifySpellMod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for a spell payload.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellPayload implements ISpellPayload {
    protected static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#######.##");

    protected final Map<String, SpellProperty> properties;
    
    public AbstractSpellPayload() {
        this.properties = this.initProperties();
    }
    
    /**
     * Get the type name for this spell payload.
     * 
     * @return the type name for this spell payload
     */
    protected abstract String getPayloadType();
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("PayloadType", this.getPayloadType());
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            nbt.putInt(entry.getKey(), entry.getValue().getValue());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (Map.Entry<String, SpellProperty> entry : this.properties.entrySet()) {
            entry.getValue().setValue(nbt.getInt(entry.getKey()));
        }
    }
    
    /**
     * Initialize the property map for this spell payload.  Should create a maximum of two properties.
     * 
     * @return a map of property names to spell properties
     */
    @Nonnull
    protected Map<String, SpellProperty> initProperties() {
        return new HashMap<>();
    }
    
    @Override
    public List<SpellProperty> getProperties() {
        // Sort properties by their display names
        return this.properties.values().stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList());
    }
    
    @Override
    public SpellProperty getProperty(String name) {
        return this.properties.get(name);
    }
    
    @Override
    public int getPropertyValue(String name) {
        return this.properties.containsKey(name) ? this.properties.get(name).getValue() : 0;
    }
    
    public int getModdedPropertyValue(@Nonnull String name, @Nonnull SpellPackage spell, @Nullable ItemStack spellSource) {
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
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".type");
    }
    
    @Override
    public Component getDefaultNamePiece() {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".default_name");
    }
    
    @Override
    public boolean isActive() {
        return true;
    }
}
