package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.runes.RuneType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentPartialKey extends RuneEnchantmentKey {
    public static final Codec<RuneEnchantmentPartialKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("enchantment").xmap(loc -> {
                return ForgeRegistries.ENCHANTMENTS.getValue(loc);
            }, ench -> {
                return ForgeRegistries.ENCHANTMENTS.getKey(ench);
            }).forGetter(key -> key.enchant),
            RuneType.CODEC.fieldOf("runeType").forGetter(key -> key.runeType)
        ).apply(instance, RuneEnchantmentPartialKey::new));
    
    protected final RuneType runeType;
    
    public RuneEnchantmentPartialKey(Enchantment enchant, RuneType runeType) {
        super(enchant);
        this.runeType = Preconditions.checkNotNull(runeType);
        if (this.runeType == RuneType.POWER) {
            throw new IllegalArgumentException("Rune type may not be a power rune");
        }
    }

    @Override
    public String toString() {
        return super.toString() + "." + this.runeType.getSerializedName();
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.RUNE_ENCHANTMENT_PARTIAL.get();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(runeType);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RuneEnchantmentPartialKey other = (RuneEnchantmentPartialKey) obj;
        return runeType == other.runeType;
    }
}
