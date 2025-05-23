package com.verdantartifice.primalmagick.common.spells.payloads;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.tags.SpellPropertyTagsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for a spell payload.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellPayload<T extends AbstractSpellPayload<T>> implements ISpellPayload {
    public static Codec<AbstractSpellPayload<?>> dispatchCodec() {
        return Services.SPELL_PAYLOAD_TYPES_REGISTRY.codec().dispatch("mod_type", AbstractSpellPayload::getType, SpellPayloadType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractSpellPayload<?>> dispatchStreamCodec() {
        return Services.SPELL_PAYLOAD_TYPES_REGISTRY.registryFriendlyStreamCodec().dispatch(AbstractSpellPayload::getType, SpellPayloadType::streamCodec);
    }
    
    protected static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#######.##");

    public abstract SpellPayloadType<T> getType();

    /**
     * Get the type name for this spell payload.
     * 
     * @return the type name for this spell payload
     */
    protected abstract String getPayloadType();
    
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
    
    public int getModdedPropertyValue(@Nonnull SpellProperty property, @Nonnull SpellPackage spell, @Nullable ItemStack spellSource, @Nullable LivingEntity caster, HolderLookup.Provider registries) {
        MutableInt retVal = new MutableInt(spell.payload().getPropertyValue(property));
        if (retVal.intValue() > 0 && !SpellPropertiesPM.AMPLIFY_POWER.get().equals(property) && property.is(SpellPropertyTagsPM.AMPLIFIABLE)) {
            // For power or duration properties greater than zero, increase the total result by
            // the power of any attached Amplify spell mod or Spell Power enchantments
            spell.getMod(SpellModsPM.AMPLIFY.get()).ifPresent(ampMod -> {
                retVal.add(ampMod.getPropertyValue(SpellPropertiesPM.AMPLIFY_POWER.get()));
            });
            if (caster != null) {
                int enchLevel =
                        EnchantmentHelperPM.getEnchantmentLevel(caster.getMainHandItem(), EnchantmentsPM.SPELL_POWER, registries) +
                        EnchantmentHelperPM.getEnchantmentLevel(caster.getOffhandItem(), EnchantmentsPM.SPELL_POWER, registries);
                if (enchLevel > 0) {
                    retVal.add(enchLevel);
                }
            }
        }
        return retVal.intValue();
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
