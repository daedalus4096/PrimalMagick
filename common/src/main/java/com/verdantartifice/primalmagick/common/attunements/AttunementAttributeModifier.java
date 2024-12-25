package com.verdantartifice.primalmagick.common.attunements;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Definition of an attunement-linked attribute modifier.  Used to modify entity attributes when
 * crossing certain attunement thresholds.
 * 
 * @author Daedalus4096
 */
public class AttunementAttributeModifier {
    protected final Source source;
    protected final AttunementThreshold threshold;
    protected final Holder<Attribute> attribute;
    protected final AttributeModifier modifier;
    
    public AttunementAttributeModifier(@Nonnull Source source, AttunementThreshold threshold, @Nonnull Holder<Attribute> attribute, @Nonnull ResourceLocation id, double modValue, @Nonnull AttributeModifier.Operation modOperation) {
        this.source = source;
        this.threshold = threshold;
        this.attribute = attribute;
        this.modifier = new AttributeModifier(id, modValue, modOperation);
    }
    
    @Nonnull
    public Source getSource() {
        return this.source;
    }
    
    public AttunementThreshold getThreshold() {
        return this.threshold;
    }
    
    @Nonnull
    public Holder<Attribute> getAttribute() {
        return this.attribute;
    }
    
    @Nonnull
    public AttributeModifier getModifier() {
        return this.modifier;
    }
    
    public void applyToEntity(@Nullable LivingEntity entity) {
        if (entity != null && !entity.level().isClientSide) {
            AttributeInstance instance = entity.getAttribute(this.getAttribute());
            if (instance != null) {
                instance.removeModifier(this.getModifier());
                instance.addPermanentModifier(this.getModifier());
            }
        }
    }
    
    public void removeFromEntity(@Nullable LivingEntity entity) {
        if (entity != null && !entity.level().isClientSide) {
            AttributeInstance instance = entity.getAttribute(this.getAttribute());
            if (instance != null) {
                instance.removeModifier(this.getModifier());
            }
        }
    }
}
