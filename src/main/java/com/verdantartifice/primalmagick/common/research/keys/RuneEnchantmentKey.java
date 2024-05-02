package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentKey extends AbstractResearchKey {
    public static final Codec<RuneEnchantmentKey> CODEC = ResourceLocation.CODEC.fieldOf("enchantment").xmap(loc -> {
        return new RuneEnchantmentKey(ForgeRegistries.ENCHANTMENTS.getValue(loc));
    }, key -> {
        return ForgeRegistries.ENCHANTMENTS.getKey(key.enchant);
    }).codec();
    
    private static final String PREFIX = "&";
    
    protected final Enchantment enchant;
    
    public RuneEnchantmentKey(Enchantment enchant) {
        this.enchant = Preconditions.checkNotNull(enchant);
    }

    @Override
    public String toString() {
        return PREFIX + ForgeRegistries.ENCHANTMENTS.getKey(this.enchant).toString();
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.RUNE_ENCHANTMENT.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(enchant);
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

}
