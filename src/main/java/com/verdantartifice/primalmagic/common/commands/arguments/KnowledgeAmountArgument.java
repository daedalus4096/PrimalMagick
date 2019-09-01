package com.verdantartifice.primalmagic.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class KnowledgeAmountArgument implements ArgumentType<Integer> {
    public static KnowledgeAmountArgument amount() {
        return new KnowledgeAmountArgument();
    }
    
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return reader.readInt();
    }
}
