package com.verdantartifice.primalmagick.common.runes;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

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
public class RuneEnchantmentDefinition {
    protected Enchantment result;
    protected VerbRune verb;
    protected NounRune noun;
    protected SourceRune source;
    protected CompoundResearchKey requiredResearch;
    
    protected RuneEnchantmentDefinition(@Nonnull Enchantment result, @Nonnull VerbRune verb, @Nonnull NounRune noun, @Nonnull SourceRune source, @Nullable CompoundResearchKey research) {
        this.result = result;
        this.verb = verb;
        this.noun = noun;
        this.source = source;
        this.requiredResearch = research;
    }
    
    public Enchantment getResult() {
        return this.result;
    }
    
    public ResourceLocation getId() {
        return ForgeRegistries.ENCHANTMENTS.getKey(this.result);
    }
    
    public VerbRune getVerb() {
        return this.verb;
    }
    
    public NounRune getNoun() {
        return this.noun;
    }
    
    public SourceRune getSource() {
        return this.source;
    }
    
    public List<Rune> getRunes() {
        return List.of(this.getVerb(), this.getNoun(), this.getSource());
    }
    
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
    
    public static class Serializer implements IRuneEnchantmentDefinitionSerializer {
        @Override
        public RuneEnchantmentDefinition read(ResourceLocation id, JsonObject json) {
            ResourceLocation resultLoc = ResourceLocation.tryParse(json.getAsJsonPrimitive("result").getAsString());
            if (!ForgeRegistries.ENCHANTMENTS.containsKey(resultLoc)) {
                throw new JsonSyntaxException("Invalid result in rune enchantment definition for " + id.toString());
            }
            
            CompoundResearchKey requiredResearch = null;
            if (json.has("required_research")) {
                requiredResearch = CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString());
            }
            
            ResourceLocation verbLoc = ResourceLocation.tryParse(json.getAsJsonPrimitive("verb").getAsString());
            if (!(Rune.getRune(verbLoc) instanceof VerbRune verb)) {
                throw new JsonSyntaxException("Invalid verb in rune enchantment definition for " + id.toString());
            }
            
            ResourceLocation nounLoc = ResourceLocation.tryParse(json.getAsJsonPrimitive("noun").getAsString());
            if (!(Rune.getRune(nounLoc) instanceof NounRune noun)) {
                throw new JsonSyntaxException("Invalid noun in rune enchantment definition for " + id.toString());
            }
            
            ResourceLocation sourceLoc = ResourceLocation.tryParse(json.getAsJsonPrimitive("source").getAsString());
            if (!(Rune.getRune(sourceLoc) instanceof SourceRune source)) {
                throw new JsonSyntaxException("Invalid source in rune enchantment definition for " + id.toString());
            }
            
            return new RuneEnchantmentDefinition(ForgeRegistries.ENCHANTMENTS.getValue(resultLoc), verb, noun, source, requiredResearch);
        }

        @Override
        public RuneEnchantmentDefinition fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation resultLoc = buf.readResourceLocation();
            if (!ForgeRegistries.ENCHANTMENTS.containsKey(resultLoc)) {
                throw new IllegalArgumentException("Unknown rune enchantment definition result " + resultLoc);
            }
            
            CompoundResearchKey requiredResearch = buf.readBoolean() ? CompoundResearchKey.parse(buf.readUtf()) : null;
            
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
            
            return new RuneEnchantmentDefinition(ForgeRegistries.ENCHANTMENTS.getValue(resultLoc), verb, noun, source, requiredResearch);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RuneEnchantmentDefinition data) {
            buf.writeResourceLocation(data.getId());
            if (data.getRequiredResearch() == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeUtf(data.getRequiredResearch().toString());
            }
            buf.writeResourceLocation(data.getVerb().getId());
            buf.writeResourceLocation(data.getNoun().getId());
            buf.writeResourceLocation(data.getSource().getId());
        }
    }
}
