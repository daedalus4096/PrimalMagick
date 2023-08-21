package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying debug command-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class CommandLanguageBuilder extends AbstractLanguageBuilder<String, CommandLanguageBuilder> {
    public CommandLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, () -> String.join(".", "commands", PrimalMagick.MODID, cmd.toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("command/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return PrimalMagick.resource(base.toLowerCase());
    }
    
    public SubcommandBuilder sub(String token) {
        return new SubcommandBuilder(token);
    }
    
    public class SubcommandBuilder {
        protected final List<String> subcommands = new ArrayList<>();
        
        protected SubcommandBuilder(String token) {
            this.subcommands.add(token);
        }
        
        protected SubcommandBuilder(List<String> stack, String token) {
            this.subcommands.addAll(stack);
            this.subcommands.add(token);
        }
        
        private String getKey() {
            return CommandLanguageBuilder.this.getKey(this.subcommands);
        }
        
        private String getKey(int index) {
            List<String> cmds = new ArrayList<>();
            cmds.addAll(this.subcommands);
            cmds.add(Integer.toString(index));
            return CommandLanguageBuilder.this.getKey(cmds);
        }
        
        public SubcommandBuilder sub(String token) {
            return new SubcommandBuilder(this.subcommands, token);
        }
        
        public SubcommandBuilder output(String value) {
            CommandLanguageBuilder.this.add(this.getKey(), value);
            return this;
        }
        
        public SubcommandBuilder output(String... values) {
            int index = 1;
            for (String value : values) {
                CommandLanguageBuilder.this.add(this.getKey(index++), value);
            }
            return this;
        }
        
        public CommandLanguageBuilder end() {
            return CommandLanguageBuilder.this;
        }
    }
}
