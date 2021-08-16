package com.verdantartifice.primalmagic.common.tiles.mana;

import java.util.List;

import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an ancient mana font tile entity.  Provides the recharge and wand interaction
 * functionality for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock}
 */
public class AncientManaFontTileEntity extends TilePM implements IInteractWithWand {
    protected static final int MANA_CAPACITY = 100;
    protected static final int RECHARGE_TICKS = 20;
    
    protected int ticksExisted = 0;
    protected int mana = 0;
    
    public AncientManaFontTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), pos, state);
    }
    
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.mana = compound.getShort("mana");
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putShort("mana", (short)this.mana);
        return super.save(compound);
    }
    
    public int getMana() {
        return this.mana;
    }
    
    public int getManaCapacity() {
        return MANA_CAPACITY;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AncientManaFontTileEntity entity) {
        entity.ticksExisted++;
        if (!level.isClientSide && entity.ticksExisted % 10 == 0) {
            // Have players in range discover this font's shrine
            SimpleResearchKey research = SimpleResearchKey.parse("m_found_shrine");
            List<? extends Player> players = EntityUtils.getEntitiesInRange(level, pos, null, Player.class, 5.0D);
            for (Player player : players) {
                if (!ResearchManager.isResearchComplete(player, research) && !ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS)) {
                    ResearchManager.completeResearch(player, research);
                    player.sendMessage(new TranslatableComponent("event.primalmagic.found_shrine").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
                }
                if (state.getBlock() instanceof AncientManaFontBlock) {
                    StatsManager.discoverShrine(player, ((AncientManaFontBlock)state.getBlock()).getSource(), pos);
                }
            }
        }
        if (!level.isClientSide && entity.ticksExisted % RECHARGE_TICKS == 0) {
            // Recharge the font over time
            entity.mana++;
            if (entity.mana > MANA_CAPACITY) {
                entity.mana = MANA_CAPACITY;
            } else {
                // Sync the tile if its mana total changed
                entity.setChanged();
                entity.syncTile(true);
            }
        }
    }

    @Override
    public InteractionResult onWandRightClick(ItemStack wandStack, Level world, Player player, BlockPos pos, Direction direction) {
        if (wandStack.getItem() instanceof IWand) {
            // On initial interaction, save this tile into the wand's NBT for use during future ticks
            IWand wand = (IWand)wandStack.getItem();
            wand.setTileInUse(wandStack, this);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, Player player, int count) {
        if (count % 5 == 0 && wandStack.getItem() instanceof IWand) {
            IWand wand = (IWand)wandStack.getItem();
            if (this.getBlockState().getBlock() instanceof AncientManaFontBlock) {
                Source source = ((AncientManaFontBlock)this.getBlockState().getBlock()).getSource();
                if (source != null) {
                    // Transfer mana from the font to the wand
                    int tap = Math.min(this.mana, wand.getSiphonAmount(wandStack));
                    int leftover = wand.addRealMana(wandStack, source, tap);
                    if (leftover < tap) {
                        this.mana -= (tap - leftover);
                        this.setChanged();
                        this.syncTile(true);
                        
                        // Show fancy sparkles
                        Vec3 playerPos = player.position();
                        double targetY = playerPos.y + (player.getEyeHeight() / 2.0D);
                        PacketHandler.sendToAllAround(
                                new ManaSparklePacket(this.worldPosition, playerPos.x, targetY, playerPos.z, 20, source.getColor()), 
                                this.level.dimension(), 
                                this.worldPosition, 
                                32.0D);
                    }
                }
            }
        }
    }
}
