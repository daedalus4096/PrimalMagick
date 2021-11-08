package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Debug command argument parser for a source.  Reads the argument from the command line.
 * 
 * @author Daedalus4096
 */
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
        // Read all valid characters from the command input string
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
