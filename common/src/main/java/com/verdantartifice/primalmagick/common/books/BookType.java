package com.verdantartifice.primalmagick.common.books;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

/**
 * Definition of a book type.  Determines the background texture used when rendering the book GUI.
 * 
 * @author Daedalus4096
 */
public enum BookType implements StringRepresentable {
    BOOK("book", ResourceLocation.withDefaultNamespace("textures/gui/book.png")),
    TABLET("tablet", ResourceUtils.loc("textures/gui/tablet.png"));
    
    private final String tag;
    private final ResourceLocation bgTexture;
    
    private BookType(String tag, ResourceLocation bgTexture) {
        this.tag = tag;
        this.bgTexture = bgTexture;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public ResourceLocation getBackgroundTexture() {
        return this.bgTexture;
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }
}
