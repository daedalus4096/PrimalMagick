package com.verdantartifice.primalmagic.common.research;

import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ScanSourceUnlockTrigger extends ScanResearchTrigger {
    protected final Source source;
    
    public ScanSourceUnlockTrigger(IItemProvider target, Source source) {
        super(target, source.getDiscoverKey());
        this.source = source;
    }
    
    @Override
    public void onMatch(ServerPlayerEntity player, IItemProvider itemProvider) {
        super.onMatch(player, itemProvider);
        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source." + this.source.getTag()).applyTextStyle(TextFormatting.GREEN), false);
    }
}
