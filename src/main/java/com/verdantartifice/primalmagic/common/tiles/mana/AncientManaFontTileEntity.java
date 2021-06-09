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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Definition of an ancient mana font tile entity.  Provides the recharge and wand interaction
 * functionality for the corresponding block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock}
 */
public class AncientManaFontTileEntity extends TilePM implements ITickableTileEntity, IInteractWithWand {
    protected static final int MANA_CAPACITY = 100;
    protected static final int RECHARGE_TICKS = 20;
    
    protected int ticksExisted = 0;
    protected int mana = 0;
    
    public AncientManaFontTileEntity() {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT.get());
    }
    
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.mana = compound.getShort("mana");
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putShort("mana", (short)this.mana);
        return super.write(compound);
    }
    
    public int getMana() {
        return this.mana;
    }
    
    public int getManaCapacity() {
        return MANA_CAPACITY;
    }

    @Override
    public void tick() {
        this.ticksExisted++;
        if (!this.world.isRemote && this.ticksExisted % 10 == 0) {
            // Have players in range discover this font's shrine
            SimpleResearchKey research = SimpleResearchKey.parse("m_found_shrine");
            List<PlayerEntity> players = EntityUtils.getEntitiesInRange(this.world, this.pos, null, PlayerEntity.class, 5.0D);
            for (PlayerEntity player : players) {
                if (!ResearchManager.isResearchComplete(player, research) && !ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS)) {
                    ResearchManager.completeResearch(player, research);
                    player.sendMessage(new TranslationTextComponent("event.primalmagic.found_shrine").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
                }
                if (this.getBlockState().getBlock() instanceof AncientManaFontBlock) {
                    StatsManager.discoverShrine(player, ((AncientManaFontBlock)this.getBlockState().getBlock()).getSource(), this.pos);
                }
            }
        }
        if (!this.world.isRemote && this.ticksExisted % RECHARGE_TICKS == 0) {
            // Recharge the font over time
            this.mana++;
            if (this.mana > MANA_CAPACITY) {
                this.mana = MANA_CAPACITY;
            } else {
                // Sync the tile if its mana total changed
                this.markDirty();
                this.syncTile(true);
            }
        }
    }

    @Override
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction) {
        if (wandStack.getItem() instanceof IWand) {
            // On initial interaction, save this tile into the wand's NBT for use during future ticks
            IWand wand = (IWand)wandStack.getItem();
            wand.setTileInUse(wandStack, this);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, PlayerEntity player, int count) {
        if (count % 5 == 0 && wandStack.getItem() instanceof IWand) {
            IWand wand = (IWand)wandStack.getItem();
            if (this.getBlockState().getBlock() instanceof AncientManaFontBlock) {
                Source source = ((AncientManaFontBlock)this.getBlockState().getBlock()).getSource();
                if (source != null) {
                    // Transfer mana from the font to the wand
                    int tap = 1;
                    int leftover = wand.addRealMana(wandStack, source, tap);
                    if (leftover < tap) {
                        this.mana -= (tap - leftover);
                        this.markDirty();
                        this.syncTile(true);
                        
                        // Show fancy sparkles
                        Vector3d playerPos = player.getPositionVec();
                        double targetY = playerPos.y + (player.getEyeHeight() / 2.0D);
                        PacketHandler.sendToAllAround(
                                new ManaSparklePacket(this.pos, playerPos.x, targetY, playerPos.z, 20, source.getColor()), 
                                this.world.getDimensionKey(), 
                                this.pos, 
                                32.0D);
                    }
                }
            }
        }
    }
}
