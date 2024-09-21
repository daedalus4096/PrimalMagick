package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Research topic that points to a rune enchantment entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class EnchantmentResearchTopic extends AbstractResearchTopic<EnchantmentResearchTopic> {
    public static final MapCodec<EnchantmentResearchTopic> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Enchantment.CODEC.fieldOf("enchantment").forGetter(EnchantmentResearchTopic::getEnchantment),
            Codec.INT.fieldOf("page").forGetter(EnchantmentResearchTopic::getPage)
        ).apply(instance, EnchantmentResearchTopic::new));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentResearchTopic> STREAM_CODEC = StreamCodec.composite(
            Enchantment.STREAM_CODEC, EnchantmentResearchTopic::getEnchantment,
            ByteBufCodecs.VAR_INT, EnchantmentResearchTopic::getPage,
            EnchantmentResearchTopic::new);
    
    protected final Holder<Enchantment> enchantment;
    
    public EnchantmentResearchTopic(Holder<Enchantment> enchantment, int page) {
        super(page);
        this.enchantment = enchantment;
    }
    
    public Holder<Enchantment> getEnchantment() {
        return this.enchantment;
    }

    @Override
    public ResearchTopicType<EnchantmentResearchTopic> getType() {
        return ResearchTopicTypesPM.ENCHANTMENT.get();
    }

    @Override
    public EnchantmentResearchTopic withPage(int newPage) {
        return new EnchantmentResearchTopic(this.enchantment, newPage);
    }
}
