package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.runes.RuneType;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class RuneEnchantmentPartialKey extends AbstractResearchKey<RuneEnchantmentPartialKey> {
    public static final MapCodec<RuneEnchantmentPartialKey> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Enchantment.CODEC.fieldOf("enchant").forGetter(key -> key.enchant),
            RuneType.CODEC.fieldOf("runeType").forGetter(key -> key.runeType)
        ).apply(instance, RuneEnchantmentPartialKey::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneEnchantmentPartialKey> STREAM_CODEC = StreamCodec.composite(
            Enchantment.STREAM_CODEC,
            key -> key.enchant,
            RuneType.STREAM_CODEC,
            key -> key.runeType,
            RuneEnchantmentPartialKey::new);
    
    private static final String PREFIX = "&";
    private static final ResourceLocation ICON_TUBE = ResourceUtils.loc("textures/research/research_tube.png");

    protected final Holder<Enchantment> enchant;
    protected final RuneType runeType;
    
    public RuneEnchantmentPartialKey(Holder<Enchantment> enchant, RuneType runeType) {
        this.enchant = Preconditions.checkNotNull(enchant);
        this.runeType = Preconditions.checkNotNull(runeType);
        if (this.runeType == RuneType.POWER) {
            throw new IllegalArgumentException("Rune type may not be a power rune");
        }
    }

    @Override
    public String toString() {
        return PREFIX + this.enchant.unwrapKey().get().location().toString() + "." + this.runeType.getSerializedName();
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
        return Objects.hash(this.enchant, this.runeType);
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
        return this.enchant.equals(other.enchant) && this.runeType == other.runeType;
    }
}
