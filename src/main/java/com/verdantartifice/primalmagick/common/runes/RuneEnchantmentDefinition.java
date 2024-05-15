package com.verdantartifice.primalmagick.common.runes;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class encapsulating a data-defined definition for a rune enchantment.  These definitions specify the
 * runes used to imbue specific enchantments on items at a runescribing altar.
 * 
 * @author Daedalus4096
 */
public record RuneEnchantmentDefinition(Enchantment result, VerbRune verb, NounRune noun, SourceRune source, Optional<AbstractRequirement<?>> requirementOpt) {
    public static final Codec<RuneEnchantmentDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("result").xmap(loc -> ForgeRegistries.ENCHANTMENTS.getValue(loc), ench -> ForgeRegistries.ENCHANTMENTS.getKey(ench)).forGetter(RuneEnchantmentDefinition::result),
            VerbRune.CODEC.fieldOf("verb").forGetter(RuneEnchantmentDefinition::verb),
            NounRune.CODEC.fieldOf("noun").forGetter(RuneEnchantmentDefinition::noun),
            SourceRune.CODEC.fieldOf("source").forGetter(RuneEnchantmentDefinition::source),
            AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(RuneEnchantmentDefinition::requirementOpt)
        ).apply(instance, RuneEnchantmentDefinition::new));
    
    public ResourceLocation getId() {
        return ForgeRegistries.ENCHANTMENTS.getKey(this.result);
    }
    
    public List<Rune> getRunes() {
        return List.of(this.verb(), this.noun(), this.source());
    }
    
    public static class Serializer implements IRuneEnchantmentDefinitionSerializer {
        @Override
        public RuneEnchantmentDefinition fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation resultLoc = buf.readResourceLocation();
            if (!ForgeRegistries.ENCHANTMENTS.containsKey(resultLoc)) {
                throw new IllegalArgumentException("Unknown rune enchantment definition result " + resultLoc);
            }
            
            Optional<AbstractRequirement<?>> requirementOpt = buf.readOptional(AbstractRequirement::fromNetwork);
            
            ResourceLocation verbLoc = buf.readResourceLocation();
            if (!(Rune.getRune(verbLoc) instanceof VerbRune verb)) {
                throw new IllegalArgumentException("Unknown rune enchantment definition verb " + verbLoc);
            }
            
            ResourceLocation nounLoc = buf.readResourceLocation();
            if (!(Rune.getRune(nounLoc) instanceof NounRune noun)) {
                throw new IllegalArgumentException("Unknown rune enchantment definition noun " + nounLoc);
            }

            ResourceLocation sourceLoc = buf.readResourceLocation();
            if (!(Rune.getRune(sourceLoc) instanceof SourceRune source)) {
                throw new IllegalArgumentException("Unknown rune enchantment definition source " + sourceLoc);
            }
            
            return new RuneEnchantmentDefinition(ForgeRegistries.ENCHANTMENTS.getValue(resultLoc), verb, noun, source, requirementOpt);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RuneEnchantmentDefinition data) {
            buf.writeResourceLocation(data.getId());
            buf.writeOptional(data.requirementOpt, (b, r) -> r.toNetwork(b));
            buf.writeResourceLocation(data.verb().getId());
            buf.writeResourceLocation(data.noun().getId());
            buf.writeResourceLocation(data.source().getId());
        }
    }
}
