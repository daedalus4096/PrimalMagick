package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Debug command argument definition for a statistic value.
 * 
 * @author Daedalus4096
 */
public class StatValueArgument implements ArgumentType<Integer> {
    public static StatValueArgument value() {
        return new StatValueArgument();
    }
    
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }
}
