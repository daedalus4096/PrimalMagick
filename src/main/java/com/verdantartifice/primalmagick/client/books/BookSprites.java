package com.verdantartifice.primalmagick.client.books;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

public record BookSprites(ResourceLocation background, WidgetSprites forward, WidgetSprites backward) {
    public static final BookSprites VANILLA = new BookSprites(ResourceLocation.withDefaultNamespace("textures/gui/book.png"), 
            new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/page_forward"), ResourceLocation.withDefaultNamespace("widget/page_forward_highlighted")), 
            new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/page_backward"), ResourceLocation.withDefaultNamespace("widget/page_backward_highlighted")));
    public static final BookSprites TABLET = new BookSprites(PrimalMagick.resource("textures/gui/tablet.png"), 
            new WidgetSprites(PrimalMagick.resource("books/tablet/page_forward"), PrimalMagick.resource("books/tablet/page_forward_highlighted")), 
            new WidgetSprites(PrimalMagick.resource("books/tablet/page_backward"), PrimalMagick.resource("books/tablet/page_backward_highlighted")));
    
    public ResourceLocation getPageButton(boolean forward, boolean highlighted) {
        return forward ? this.getForward(highlighted) : this.getBackward(highlighted);
    }
    
    public ResourceLocation getForward(boolean highlighted) {
        return this.forward.get(true, highlighted);
    }
    
    public ResourceLocation getBackward(boolean highlighted) {
        return this.backward.get(true, highlighted);
    }
}
