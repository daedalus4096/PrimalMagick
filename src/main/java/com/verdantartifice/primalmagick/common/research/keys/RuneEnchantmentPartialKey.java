package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.runes.RuneType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentPartialKey extends AbstractResearchKey<RuneEnchantmentPartialKey> {
    public static final Codec<RuneEnchantmentPartialKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("enchantment").xmap(loc -> {
                return ForgeRegistries.ENCHANTMENTS.getValue(loc);
            }, ench -> {
                return ForgeRegistries.ENCHANTMENTS.getKey(ench);
            }).forGetter(key -> key.enchant),
            RuneType.CODEC.fieldOf("runeType").forGetter(key -> key.runeType)
        ).apply(instance, RuneEnchantmentPartialKey::new));
    
    private static final String PREFIX = "&";
    
    protected final Enchantment enchant;
    protected final RuneType runeType;
    
    public RuneEnchantmentPartialKey(Enchantment enchant, RuneType runeType) {
        this.enchant = Preconditions.checkNotNull(enchant);
        this.runeType = Preconditions.checkNotNull(runeType);
        if (this.runeType == RuneType.POWER) {
            throw new IllegalArgumentException("Rune type may not be a power rune");
        }
    }

    @Override
    public String toString() {
        return PREFIX + ForgeRegistries.ENCHANTMENTS.getKey(this.enchant).toString() + "." + this.runeType.getSerializedName();
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
    public int hashCode() {
        return Objects.hash(ForgeRegistries.ENCHANTMENTS.getKey(this.enchant), runeType);
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
        return Objects.equals(ForgeRegistries.ENCHANTMENTS.getKey(this.enchant), ForgeRegistries.ENCHANTMENTS.getKey(other.enchant)) && runeType == other.runeType;
    }

    @Nonnull
    static RuneEnchantmentPartialKey fromNetworkInner(FriendlyByteBuf buf) {
        ResourceLocation loc = buf.readResourceLocation();
        RuneType runeType = buf.readEnum(RuneType.class);
        return new RuneEnchantmentPartialKey(ForgeRegistries.ENCHANTMENTS.getValue(loc), runeType);
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(this.enchant));
        buf.writeEnum(this.runeType);
    }
}
