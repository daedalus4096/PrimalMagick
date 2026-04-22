package com.verdantartifice.primalmagick.common.books;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

/**
 * Definition of a book type.  Determines the background texture used when rendering the book GUI.
 * 
 * @author Daedalus4096
 */
public enum BookType implements StringRepresentable {
    BOOK("book", Identifier.withDefaultNamespace("textures/gui/book.png")),
    TABLET("tablet", ResourceUtils.loc("textures/gui/tablet.png"));
    
    private final String tag;
    private final Identifier bgTexture;
    
    private BookType(String tag, Identifier bgTexture) {
        this.tag = tag;
        this.bgTexture = bgTexture;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public Identifier getBackgroundTexture() {
        return this.bgTexture;
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }
}
