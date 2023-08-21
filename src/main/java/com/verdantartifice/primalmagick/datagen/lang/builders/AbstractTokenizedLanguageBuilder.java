package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying tokenized localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTokenizedLanguageBuilder extends AbstractLanguageBuilder<String, AbstractTokenizedLanguageBuilder> {
    private final String rootToken;
    
    public AbstractTokenizedLanguageBuilder(String cmd, String rootToken, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, () -> String.join(".", rootToken, PrimalMagick.MODID, cmd.toLowerCase()), untracker, adder);
        this.rootToken = rootToken;
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix(this.rootToken + "/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return PrimalMagick.resource(base.toLowerCase());
    }
    
    public SubtokenBuilder sub(String token) {
        return new SubtokenBuilder(token);
    }
    
    public class SubtokenBuilder {
        protected final List<String> tokens = new ArrayList<>();
        
        protected SubtokenBuilder(String token) {
            this.tokens.add(token);
        }
        
        protected SubtokenBuilder(List<String> stack, String token) {
            this.tokens.addAll(stack);
            this.tokens.add(token);
        }
        
        private String getKey() {
            return AbstractTokenizedLanguageBuilder.this.getKey(this.tokens);
        }
        
        private String getKey(int index) {
            List<String> cmds = new ArrayList<>();
            cmds.addAll(this.tokens);
            cmds.add(Integer.toString(index));
            return AbstractTokenizedLanguageBuilder.this.getKey(cmds);
        }
        
        public SubtokenBuilder sub(String token) {
            return new SubtokenBuilder(this.tokens, token);
        }
        
        public SubtokenBuilder output(String value) {
            AbstractTokenizedLanguageBuilder.this.add(this.getKey(), value);
            return this;
        }
        
        public SubtokenBuilder output(String... values) {
            return this.output(1, values);
        }
        
        public SubtokenBuilder output(int start, String... values) {
            int index = start;
            for (String value : values) {
                AbstractTokenizedLanguageBuilder.this.add(this.getKey(index++), value);
            }
            return this;
        }
        
        public AbstractTokenizedLanguageBuilder end() {
            return AbstractTokenizedLanguageBuilder.this;
        }
    }
}
