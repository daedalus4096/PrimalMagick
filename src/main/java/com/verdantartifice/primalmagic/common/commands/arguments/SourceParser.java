package com.verdantartifice.primalmagic.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class SourceParser {
    protected final StringReader reader;
    protected String sourceTag;
    
    public SourceParser(StringReader reader) {
        this.reader = reader;
    }
    
    public String getSourceTag() {
        return this.sourceTag;
    }
    
    public void readSource() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        while (this.reader.canRead() && this.isValidCharacter(this.reader.peek())) {
            this.reader.skip();
        }
        String rawTag = this.reader.getString().substring(i, this.reader.getCursor());
        this.sourceTag = rawTag;
    }
    
    protected boolean isValidCharacter(char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || character == '.' || character == '-';
    }
    
    public SourceParser parse() throws CommandSyntaxException {
        this.readSource();
        return this;
    }
}
