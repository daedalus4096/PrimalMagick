package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.runes.RuneType;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class RuneEnchantmentPartialKey extends AbstractResearchKey<RuneEnchantmentPartialKey> {
    public static final MapCodec<RuneEnchantmentPartialKey> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.ENCHANTMENT).fieldOf("enchantKey").forGetter(key -> key.enchantKey),
            RuneType.CODEC.fieldOf("runeType").forGetter(key -> key.runeType)
        ).apply(instance, RuneEnchantmentPartialKey::new));
    public static final StreamCodec<ByteBuf, RuneEnchantmentPartialKey> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.ENCHANTMENT),
            key -> key.enchantKey,
            RuneType.STREAM_CODEC,
            key -> key.runeType,
            RuneEnchantmentPartialKey::new);
    
    private static final String PREFIX = "&";
    private static final ResourceLocation ICON_TUBE = PrimalMagick.resource("textures/research/research_tube.png");

    protected final ResourceKey<Enchantment> enchantKey;
    protected final RuneType runeType;
    
    public RuneEnchantmentPartialKey(ResourceKey<Enchantment> enchant, RuneType runeType) {
        this.enchantKey = Preconditions.checkNotNull(enchant);
        this.runeType = Preconditions.checkNotNull(runeType);
        if (this.runeType == RuneType.POWER) {
            throw new IllegalArgumentException("Rune type may not be a power rune");
        }
    }

    @Override
    public String toString() {
        return PREFIX + this.enchantKey.location().toString() + "." + this.runeType.getSerializedName();
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<RuneEnchantmentPartialKey> getType() {
        return ResearchKeyTypesPM.RUNE_ENCHANTMENT_PARTIAL.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(ICON_TUBE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enchantKey, this.runeType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RuneEnchantmentPartialKey other = (RuneEnchantmentPartialKey) obj;
        return this.enchantKey.equals(other.enchantKey) && this.runeType == other.runeType;
    }
}
