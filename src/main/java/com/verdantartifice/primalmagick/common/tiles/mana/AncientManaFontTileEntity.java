package com.verdantartifice.primalmagick.common.tiles.mana;

import java.util.List;

import com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an ancient mana font tile entity.  The focal point for discovering ancient
 * shrines.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock}
 */
public class AncientManaFontTileEntity extends AbstractManaFontTileEntity {
    public AncientManaFontTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), pos, state);
    }
    
    @Override
    protected int getInitialMana() {
        return this.getManaCapacity();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AncientManaFontTileEntity entity) {
        entity.ticksExisted++;
        if (!level.isClientSide && entity.ticksExisted % 10 == 0) {
            // Have players in range discover this font's shrine
            SimpleResearchKey shrineResearch = ResearchNames.INTERNAL_FOUND_SHRINE.get().simpleKey();
            SimpleResearchKey siphonResearch = ResearchNames.INTERNAL_SIPHON_PROMPT.get().simpleKey();
            List<Player> players = EntityUtils.getEntitiesInRange(level, pos, null, Player.class, 5.0D);
            for (Player player : players) {
                if (!ResearchManager.isResearchComplete(player, shrineResearch) && !ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS)) {
                    ResearchManager.completeResearch(player, shrineResearch);
                    player.sendSystemMessage(Component.translatable("event.primalmagick.found_shrine").withStyle(ChatFormatting.GREEN));
                }
                if (!ResearchManager.isResearchComplete(player, siphonResearch) && InventoryUtils.isPlayerCarrying(player, new ItemStack(ItemsPM.MUNDANE_WAND.get()))) {
                    ResearchManager.completeResearch(player, siphonResearch);
                    player.sendSystemMessage(Component.translatable("event.primalmagick.siphon_prompt").withStyle(ChatFormatting.GREEN));
                }
                if (state.getBlock() instanceof AncientManaFontBlock fontBlock) {
                    StatsManager.discoverShrine(player, fontBlock.getSource(), pos);
                }
            }
        }
        if (!level.isClientSide && entity.ticksExisted % RECHARGE_TICKS == 0) {
            entity.doRecharge();
        }
    }
}
