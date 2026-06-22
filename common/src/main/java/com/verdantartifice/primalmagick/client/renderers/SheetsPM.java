package com.verdantartifice.primalmagick.client.renderers;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public class SheetsPM {
    public static final Identifier SHIELD_SHEET = ResourceUtils.loc("textures/atlas/shield_patterns.png");

    public static final SpriteMapper SHIELD_MAPPER = new SpriteMapper(SHIELD_SHEET, "entity/shield");

    public static final SpriteId PRIMALITE_SHIELD_BASE = SHIELD_MAPPER.apply(ResourceUtils.loc("primalite_shield_base"));
    public static final SpriteId PRIMALITE_SHIELD_BASE_NO_PATTERN = SHIELD_MAPPER.apply(ResourceUtils.loc("primalite_shield_base_nopattern"));
    public static final SpriteId HEXIUM_SHIELD_BASE = SHIELD_MAPPER.apply(ResourceUtils.loc("hexium_shield_base"));
    public static final SpriteId HEXIUM_SHIELD_BASE_NO_PATTERN = SHIELD_MAPPER.apply(ResourceUtils.loc("hexium_shield_base_nopattern"));
    public static final SpriteId HALLOWSTEEL_SHIELD_BASE = SHIELD_MAPPER.apply(ResourceUtils.loc("hallowsteel_shield_base"));
    public static final SpriteId HALLOWSTEEL_SHIELD_BASE_NO_PATTERN = SHIELD_MAPPER.apply(ResourceUtils.loc("hallowsteel_shield_base_nopattern"));
}
