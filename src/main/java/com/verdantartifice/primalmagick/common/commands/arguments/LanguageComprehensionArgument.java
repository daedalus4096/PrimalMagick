package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Debug command argument definition for a linguistics comprehension value.
 * 
 * @author Daedalus4096
 */
public class LanguageComprehensionArgument implements ArgumentType<Integer> {
    public static LanguageComprehensionArgument value() {
        return new LanguageComprehensionArgument();
    }
    
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }
}
