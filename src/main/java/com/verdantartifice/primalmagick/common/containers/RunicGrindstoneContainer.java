package com.verdantartifice.primalmagick.common.containers;

import java.util.List;
import java.util.Set;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneContainer extends GrindstoneMenu {
    protected static final List<RuneType> RUNE_TYPES = List.of(RuneType.VERB, RuneType.NOUN, RuneType.SOURCE);
    
    protected ContainerLevelAccess worldPosCallable;
    protected Player player;

    public RunicGrindstoneContainer(int windowId, Inventory playerInv) {
        this(windowId, playerInv, ContainerLevelAccess.NULL);
    }
    
    public RunicGrindstoneContainer(int windowId, Inventory playerInv, ContainerLevelAccess worldPosCallable) {
        super(windowId, playerInv, worldPosCallable);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInv.player;
    }
    
    @Override
    public ItemStack removeNonCurses(ItemStack stack, int damage, int count) {
        ItemStack retVal = super.removeNonCurses(stack, damage, count);
        RuneManager.clearRunes(retVal);
        return retVal;
    }
    
    @Override
    public void createResult() {
        ItemStack itemstack = this.repairSlots.getItem(0);
        ItemStack itemstack1 = this.repairSlots.getItem(1);
        boolean flag = !itemstack.isEmpty() || !itemstack1.isEmpty();
        boolean flag1 = !itemstack.isEmpty() && !itemstack1.isEmpty();
        
        if (!flag) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            // Process even non-enchanted items, as long as they have runes attached
            boolean flag2 = !itemstack.isEmpty() && !itemstack.is(Items.ENCHANTED_BOOK) && !itemstack.isEnchanted() && !RuneManager.hasRunes(itemstack) || 
                            !itemstack1.isEmpty() && !itemstack1.is(Items.ENCHANTED_BOOK) && !itemstack1.isEnchanted() && !RuneManager.hasRunes(itemstack1);
            if (itemstack.getCount() > 1 || itemstack1.getCount() > 1 || !flag1 && flag2) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            int j = 1;
            int i;
            ItemStack itemstack2;
            if (flag1) {
                if (!itemstack.is(itemstack1.getItem())) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }

                int k = itemstack.getMaxDamage() - itemstack.getDamageValue();
                int l = itemstack.getMaxDamage() - itemstack1.getDamageValue();
                int i1 = k + l + itemstack.getMaxDamage() * 5 / 100;
                i = Math.max(itemstack.getMaxDamage() - i1, 0);
                itemstack2 = this.mergeEnchants(itemstack, itemstack1);
                if (!itemstack2.isRepairable()) i = itemstack.getDamageValue();
                if (!itemstack2.isDamageableItem() || !itemstack2.isRepairable()) {
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }
                    j = 2;
                }
            } else {
                boolean flag3 = !itemstack.isEmpty();
                i = flag3 ? itemstack.getDamageValue() : itemstack1.getDamageValue();
                itemstack2 = flag3 ? itemstack : itemstack1;
            }
            
            // Grant the player partial rune research for each applicable enchantment on the item being grinded
            this.grantHints(itemstack2);

            this.resultSlots.setItem(0, this.removeNonCurses(itemstack2, i, j));
        }

        this.broadcastChanges();
        
        this.worldPosCallable.execute((world, pos) -> {
            if (!world.isClientSide && this.player instanceof ServerPlayer spe) {
                ItemStack stack = this.resultSlots.getItem(0);
                spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 2, stack));
            }
        });
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
    
    protected void grantHints(ItemStack stack) {
        Set<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet();
        int hintCount = 0;
        
        for (Enchantment enchant : enchants) {
            SimpleResearchKey fullResearch = SimpleResearchKey.parseRuneEnchantment(enchant);
            if (RuneManager.hasRuneDefinition(enchant) && !fullResearch.isKnownByStrict(this.player)) {
                List<SimpleResearchKey> candidates = RUNE_TYPES.stream().map(rt -> SimpleResearchKey.parsePartialRuneEnchantment(enchant, rt)).filter(key -> !key.isKnownByStrict(this.player)).toList();
                if (candidates.size() == 1) {
                    // If only one hint remains to be given, grant it and the full research as well
                    ResearchManager.completeResearch(this.player, candidates.get(0));
                    ResearchManager.completeResearch(this.player, fullResearch);
                    hintCount++;
                } else if (!candidates.isEmpty()) {
                    // If more than one hint remains to be given, grant one at random
                    WeightedRandomBag<SimpleResearchKey> candidateBag = new WeightedRandomBag<>();
                    for (SimpleResearchKey candidate : candidates) {
                        candidateBag.add(candidate, 1);
                    }
                    ResearchManager.completeResearch(this.player, candidateBag.getRandom(this.player.getRandom()));
                    hintCount++;
                }
            }
        }
        if (hintCount > 0) {
            // If at least one hint was granted to the player, notify them
            this.player.displayClientMessage(Component.translatable("event.primalmagick.runic_grindstone.hints_granted"), true);
        }
    }
}
