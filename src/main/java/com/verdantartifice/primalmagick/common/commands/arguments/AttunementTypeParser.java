package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;

/**
 * Debug command argument parser for an attunement type.  Reads the argument from the command line.
 * 
 * @author Daedalus4096
 */
public class AttunementTypeParser {
    protected final StringReader reader;
    protected AttunementType type;
    
    public AttunementTypeParser(StringReader reader) {
        this.reader = reader;
    }
    
    public AttunementType getType() {
        return this.type;
    }
    
    public void readKey() throws CommandSyntaxException {
        // Read all valid characters from the command input string
        int i = this.reader.getCursor();
        while (this.reader.canRead() && this.isValidResearchCharacter(this.reader.peek())) {
            this.reader.skip();
        }
        String rawKey = this.reader.getString().substring(i, this.reader.getCursor());
        try {
            this.type = AttunementType.valueOf(rawKey);
        } catch (Exception e) {}
    }
    
    protected boolean isValidResearchCharacter(char character) {
        return (character >= '0' && character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || character == '-';
    }
    
    public AttunementTypeParser parse() throws CommandSyntaxException {
        this.readKey();
        return this;
    }
}
