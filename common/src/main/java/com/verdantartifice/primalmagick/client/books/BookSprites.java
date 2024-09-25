package com.verdantartifice.primalmagick.client.books;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

public record BookSprites(ResourceLocation background, WidgetSprites forward, WidgetSprites backward) {
    public static final BookSprites VANILLA = new BookSprites(ResourceLocation.withDefaultNamespace("textures/gui/book.png"), 
            new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/page_forward"), ResourceLocation.withDefaultNamespace("widget/page_forward_highlighted")), 
            new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/page_backward"), ResourceLocation.withDefaultNamespace("widget/page_backward_highlighted")));
    public static final BookSprites TABLET = new BookSprites(ResourceUtils.loc("textures/gui/tablet.png"),
            new WidgetSprites(ResourceUtils.loc("books/tablet/page_forward"), ResourceUtils.loc("books/tablet/page_forward_highlighted")),
            new WidgetSprites(ResourceUtils.loc("books/tablet/page_backward"), ResourceUtils.loc("books/tablet/page_backward_highlighted")));
    
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
