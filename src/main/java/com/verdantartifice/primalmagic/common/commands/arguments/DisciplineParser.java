package com.verdantartifice.primalmagic.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Debug command argument parser for a research discipline.  Reads the argument from the command line.
 * 
 * @author Daedalus4096
 */
public class DisciplineParser {
    protected final StringReader reader;
    protected String disciplineKey;
    
    public DisciplineParser(StringReader reader) {
        this.reader = reader;
    }
    
    public String getDisciplineKey() {
        return this.disciplineKey;
    }
    
    public void readDiscipline() throws CommandSyntaxException {
        // Read all valid characters from the command input string
        int i = this.reader.getCursor();
        while (this.reader.canRead() && this.isValidCharacter(this.reader.peek())) {
            this.reader.skip();
        }
        String rawKey = this.reader.getString().substring(i, this.reader.getCursor());
        this.disciplineKey = rawKey;
    }
    
    protected boolean isValidCharacter(char character) {
        return (character >= '0' && character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_' || character == '.' || character == '-';
    }
    
    public DisciplineParser parse() throws CommandSyntaxException {
        this.readDiscipline();
        return this;
    }
}
