package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

public class ModelTemplatesPM {
    public static final ModelTemplate MANA_ORB = createItem("template_mana_orb", TextureSlot.PARTICLE);
    public static final ModelTemplate SPELLTOME = createItem("template_spelltome", TextureSlot.PARTICLE);

    public static final ModelTemplate EMPTY = createBlock("empty");
    public static final ModelTemplate MANA_FONT = createBlock("mana_font", TextureSlotsPM.BASE);
    public static final ModelTemplate PILLAR = createBlock("pillar", TextureSlotsPM.INNER, TextureSlot.SIDE);
    public static final ModelTemplate PILLAR_BOTTOM = createBlock("pillar_bottom", TextureSlot.BOTTOM, TextureSlotsPM.INNER, TextureSlot.SIDE);
    public static final ModelTemplate PILLAR_TOP = createBlock("pillar_top", TextureSlotsPM.INNER, TextureSlot.SIDE, TextureSlot.TOP);
    public static final ModelTemplate RUNESCRIBING_ALTAR = createBlock("runescribing_altar", TextureSlotsPM.ALTAR_BOTTOM, TextureSlotsPM.ALTAR_SIDE);

    public static final ModelTemplate SKYGLASS_PANE_NOSIDE = createBlock("skyglass_pane_noside_base", TextureSlot.PANE);
    public static final ModelTemplate SKYGLASS_PANE_NOSIDE_ALT = createBlock("skyglass_pane_noside_alt_base", TextureSlot.PANE);
    public static final ModelTemplate SKYGLASS_PANE_POST = createBlock("skyglass_pane_post_base", TextureSlot.EDGE);
    public static final ModelTemplate SKYGLASS_PANE_SIDE = createBlock("skyglass_pane_side_base", TextureSlot.PANE, TextureSlot.EDGE);
    public static final ModelTemplate SKYGLASS_PANE_SIDE_ALT = createBlock("skyglass_pane_side_alt_base", TextureSlot.PANE, TextureSlot.EDGE);

    public static ModelTemplate createBlock(String name, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("block/")), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createItem(String name, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(ResourceUtils.loc(name).withPrefix("item/")), Optional.empty(), requiredSlots);
    }
}
