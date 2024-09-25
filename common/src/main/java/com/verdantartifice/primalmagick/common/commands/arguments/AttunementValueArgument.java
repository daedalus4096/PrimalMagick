package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Debug command argument definition for an attunement value.
 * 
 * @author Daedalus4096
 */
public class AttunementValueArgument implements ArgumentType<Integer> {
    public static AttunementValueArgument value() {
        return new AttunementValueArgument();
    }
    
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }
}
