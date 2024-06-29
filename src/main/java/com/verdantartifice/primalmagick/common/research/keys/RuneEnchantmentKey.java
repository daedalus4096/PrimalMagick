package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class RuneEnchantmentKey extends AbstractResearchKey<RuneEnchantmentKey> {
    public static final MapCodec<RuneEnchantmentKey> CODEC = ResourceKey.codec(Registries.ENCHANTMENT).fieldOf("enchantKey").xmap(RuneEnchantmentKey::new, key -> key.enchantKey);
    public static final StreamCodec<ByteBuf, RuneEnchantmentKey> STREAM_CODEC = ResourceKey.streamCodec(Registries.ENCHANTMENT).map(RuneEnchantmentKey::new, key -> key.enchantKey);
    
    private static final String PREFIX = "&";
    private static final ResourceLocation ICON_TUBE = PrimalMagick.resource("textures/research/research_tube.png");

    protected final ResourceKey<Enchantment> enchantKey;
    
    public RuneEnchantmentKey(ResourceKey<Enchantment> enchant) {
        this.enchantKey = Preconditions.checkNotNull(enchant);
    }

    @Override
    public String toString() {
        return PREFIX + this.enchantKey.location().toString();
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
        return Objects.hash(this.enchantKey);
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
        return this.enchantKey.equals(other.enchantKey);
    }
}
