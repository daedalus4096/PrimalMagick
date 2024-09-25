package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class RuneEnchantmentKey extends AbstractResearchKey<RuneEnchantmentKey> {
    public static final MapCodec<RuneEnchantmentKey> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Enchantment.CODEC.fieldOf("enchant").forGetter(k -> k.enchant)
        ).apply(instance, RuneEnchantmentKey::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RuneEnchantmentKey> STREAM_CODEC = StreamCodec.composite(Enchantment.STREAM_CODEC, k -> k.enchant, RuneEnchantmentKey::new);
    
    private static final String PREFIX = "&";
    private static final ResourceLocation ICON_TUBE = ResourceUtils.loc("textures/research/research_tube.png");

    protected final Holder<Enchantment> enchant;
    
    public RuneEnchantmentKey(Holder<Enchantment> enchant) {
        this.enchant = Preconditions.checkNotNull(enchant);
    }

    @Override
    public String toString() {
        return PREFIX + this.enchant.unwrapKey().get().location().toString();
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<RuneEnchantmentKey> getType() {
        return ResearchKeyTypesPM.RUNE_ENCHANTMENT.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(ICON_TUBE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enchant);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RuneEnchantmentKey other = (RuneEnchantmentKey) obj;
        return this.enchant.equals(other.enchant);
    }
}
