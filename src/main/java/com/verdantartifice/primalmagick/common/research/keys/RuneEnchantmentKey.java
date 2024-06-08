package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentKey extends AbstractResearchKey<RuneEnchantmentKey> {
    public static final Codec<RuneEnchantmentKey> CODEC = ResourceLocation.CODEC.fieldOf("enchantment").xmap(loc -> {
        return new RuneEnchantmentKey(ForgeRegistries.ENCHANTMENTS.getValue(loc));
    }, key -> {
        return ForgeRegistries.ENCHANTMENTS.getKey(key.enchant);
    }).codec();
    
    private static final String PREFIX = "&";
    private static final ResourceLocation ICON_TUBE = PrimalMagick.resource("textures/research/research_tube.png");

    protected final Enchantment enchant;
    
    public RuneEnchantmentKey(Enchantment enchant) {
        this.enchant = Preconditions.checkNotNull(enchant);
    }

    @Override
    public String toString() {
        return PREFIX + ForgeRegistries.ENCHANTMENTS.getKey(this.enchant).toString();
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
        return Objects.hash(ForgeRegistries.ENCHANTMENTS.getKey(this.enchant));
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
        return ForgeRegistries.ENCHANTMENTS.getKey(this.enchant).equals(ForgeRegistries.ENCHANTMENTS.getKey(other.enchant));
    }

    @Nonnull
    static RuneEnchantmentKey fromNetworkInner(FriendlyByteBuf buf) {
        ResourceLocation loc = buf.readResourceLocation();
        return new RuneEnchantmentKey(ForgeRegistries.ENCHANTMENTS.getValue(loc));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(this.enchant));
    }
}
