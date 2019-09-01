package com.verdantartifice.primalmagic.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;

public class KnowledgeTypeParser {
    protected final StringReader reader;
    protected IPlayerKnowledge.KnowledgeType type;
    
    public KnowledgeTypeParser(StringReader reader) {
        this.reader = reader;
    }
    
    public IPlayerKnowledge.KnowledgeType getType() {
        return this.type;
    }
    
    public void readKey() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        while (this.reader.canRead() && this.isValidResearchCharacter(this.reader.peek())) {
            this.reader.skip();
        }
        String rawKey = this.reader.getString().substring(i, this.reader.getCursor());
        try {
            this.type = IPlayerKnowledge.KnowledgeType.valueOf(rawKey);
        } catch (Exception e) {}
    }
    
    protected boolean isValidResearchCharacter(char character) {
        return (character >= '0' && character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || character == '-';
    }
    
    public KnowledgeTypeParser parse() throws CommandSyntaxException {
        this.readKey();
        return this;
    }
}
