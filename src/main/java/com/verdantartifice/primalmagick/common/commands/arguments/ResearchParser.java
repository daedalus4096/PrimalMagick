package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

/**
 * Debug command argument parser for a research entry.  Reads the argument from the command line.
 * 
 * @author Daedalus4096
 */
public class ResearchParser {
    protected final StringReader reader;
    protected SimpleResearchKey key;
    
    public ResearchParser(StringReader reader) {
        this.reader = reader;
    }
    
    public SimpleResearchKey getKey() {
        return this.key;
    }
    
    public void readKey() throws CommandSyntaxException {
        // Read all valid characters from the command input string
        int i = this.reader.getCursor();
        while (this.reader.canRead() && this.isValidResearchCharacter(this.reader.peek())) {
            this.reader.skip();
        }
        String rawKey = this.reader.getString().substring(i, this.reader.getCursor());
        this.key = SimpleResearchKey.parse(rawKey);
    }
    
    protected boolean isValidResearchCharacter(char character) {
        return (character >= '0' && character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || character == '.' || character == '-';
    }
    
    public ResearchParser parse() throws CommandSyntaxException {
        this.readKey();
        return this;
    }
}
