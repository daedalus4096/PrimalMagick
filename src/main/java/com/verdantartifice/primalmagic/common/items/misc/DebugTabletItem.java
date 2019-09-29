package com.verdantartifice.primalmagic.common.items.misc;

import java.util.Set;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.items.base.ItemPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DebugTabletItem extends ItemPM {
    public DebugTabletItem() {
        super("debug_tablet", new Item.Properties().group(PrimalMagic.ITEM_GROUP));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(playerIn);
        if (knowledge != null) {
            Set<SimpleResearchKey> researchSet = knowledge.getResearchSet();
            String[] researchList = researchSet.stream()
                                        .map(k -> k.getRootKey())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[researchSet.size()]);
            String output = String.join(", ", researchList);
            PrimalMagic.LOGGER.debug("Checking {}-side knowledge for {}: {}", (worldIn.isRemote ? "client" : "server"), playerIn.getName().getString(), output);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
