package com.verdantartifice.primalmagic.common.tiles.mana;

import java.util.List;

import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.util.InventoryUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an ancient mana font tile entity.  The focal point for discovering ancient
 * shrines.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock}
 */
public class AncientManaFontTileEntity extends AbstractManaFontTileEntity {
    public AncientManaFontTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), pos, state);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, AncientManaFontTileEntity entity) {
        entity.ticksExisted++;
        if (!level.isClientSide && entity.ticksExisted % 10 == 0) {
            // Have players in range discover this font's shrine
            SimpleResearchKey shrineResearch = SimpleResearchKey.parse("m_found_shrine");
            SimpleResearchKey siphonResearch = SimpleResearchKey.parse("m_siphon_prompt");
            List<Player> players = EntityUtils.getEntitiesInRange(level, pos, null, Player.class, 5.0D);
            for (Player player : players) {
                if (!ResearchManager.isResearchComplete(player, shrineResearch) && !ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS)) {
                    ResearchManager.completeResearch(player, shrineResearch);
                    player.sendMessage(new TranslatableComponent("event.primalmagic.found_shrine").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                }
                if (!ResearchManager.isResearchComplete(player, siphonResearch) && InventoryUtils.isPlayerCarrying(player, new ItemStack(ItemsPM.MUNDANE_WAND.get()))) {
                    ResearchManager.completeResearch(player, siphonResearch);
                    player.sendMessage(new TranslatableComponent("event.primalmagic.siphon_prompt").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                }
                if (state.getBlock() instanceof AncientManaFontBlock) {
                    StatsManager.discoverShrine(player, ((AncientManaFontBlock)state.getBlock()).getSource(), pos);
                }
            }
        }
        if (!level.isClientSide && entity.ticksExisted % RECHARGE_TICKS == 0) {
            entity.doRecharge();
        }
    }
}
