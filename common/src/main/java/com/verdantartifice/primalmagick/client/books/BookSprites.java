package com.verdantartifice.primalmagick.client.books;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.Identifier;

public record BookSprites(Identifier background, WidgetSprites forward, WidgetSprites backward) {
    public static final BookSprites VANILLA = new BookSprites(Identifier.withDefaultNamespace("textures/gui/book.png"),
            new WidgetSprites(Identifier.withDefaultNamespace("widget/page_forward"), Identifier.withDefaultNamespace("widget/page_forward_highlighted")),
            new WidgetSprites(Identifier.withDefaultNamespace("widget/page_backward"), Identifier.withDefaultNamespace("widget/page_backward_highlighted")));
    public static final BookSprites TABLET = new BookSprites(ResourceUtils.loc("textures/gui/tablet.png"),
            new WidgetSprites(ResourceUtils.loc("books/tablet/page_forward"), ResourceUtils.loc("books/tablet/page_forward_highlighted")),
            new WidgetSprites(ResourceUtils.loc("books/tablet/page_backward"), ResourceUtils.loc("books/tablet/page_backward_highlighted")));
    
    public Identifier getPageButton(boolean forward, boolean highlighted) {
        return forward ? this.getForward(highlighted) : this.getBackward(highlighted);
    }
    
    public Identifier getForward(boolean highlighted) {
        return this.forward.get(true, highlighted);
    }
    
    public Identifier getBackward(boolean highlighted) {
        return this.backward.get(true, highlighted);
    }
}
