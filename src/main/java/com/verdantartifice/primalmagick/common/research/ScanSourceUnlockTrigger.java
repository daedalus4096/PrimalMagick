package com.verdantartifice.primalmagick.common.research;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ItemLike;

/**
 * Subtype of research scan trigger that grants the required research for a primal source.  In addition
 * to granting the research, it will also display a localized message in the player's chat window.
 * 
 * @author Daedalus4096
 */
public class ScanSourceUnlockTrigger extends ScanItemResearchTrigger {
    protected final Source source;
    
    public ScanSourceUnlockTrigger(ItemLike target, Source source) {
        super(target, source.getDiscoverKey(), false);
        this.source = source;
    }
    
    @Override
    public void onMatch(ServerPlayer player, Object obj) {
        super.onMatch(player, obj);
        player.displayClientMessage(Component.translatable("event.primalmagick.discover_source." + this.source.getTag()).withStyle(ChatFormatting.GREEN), false);
    }
}
