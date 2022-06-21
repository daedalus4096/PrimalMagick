package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Research topic that points to a rune enchantment entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class EnchantmentResearchTopic extends AbstractResearchTopic {
    public EnchantmentResearchTopic(Enchantment enchantment, int page) {
        super(AbstractResearchTopic.Type.ENCHANTMENT, ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString(), page);
    }
    
    @Nullable
    public Enchantment getData() {
        ResourceLocation loc = ResourceLocation.tryParse(this.data);
        return ForgeRegistries.ENCHANTMENTS.getValue(loc);
    }
}
