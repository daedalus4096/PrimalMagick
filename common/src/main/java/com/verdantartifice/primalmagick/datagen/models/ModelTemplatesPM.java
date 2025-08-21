package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

public class ModelTemplatesPM {
    public static final ModelTemplate MANA_ORB = createItem("template_mana_orb", TextureSlot.PARTICLE);
    public static final ModelTemplate SPELLTOME = createItem("template_spelltome", TextureSlot.PARTICLE);

    public static ModelTemplate createBlock(String name, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("block/")), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createItem(String name, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("item/")), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createBlock(String name, String suffix, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("block/")), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createItem(String name, String suffix, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("item/")), Optional.empty(), requiredSlots);
    }
}
