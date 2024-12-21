package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentPartialKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneMenu extends AbstractContainerMenu {
    protected static final List<RuneType> RUNE_TYPES = List.of(RuneType.VERB, RuneType.NOUN, RuneType.SOURCE);
    
    public final Container resultSlots = new ResultContainer();
    public final Container repairSlots = new SimpleContainer(2) {
       /**
        * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
        * it hasn't changed and skip it.
        */
       public void setChanged() {
          super.setChanged();
          RunicGrindstoneMenu.this.slotsChanged(this);
       }
    };
    protected final ContainerLevelAccess worldPosCallable;
    protected Player player;
    private int xp = -1;

    public RunicGrindstoneMenu(int windowId, Inventory playerInv) {
        this(windowId, playerInv, ContainerLevelAccess.NULL);
    }
    
    public RunicGrindstoneMenu(int windowId, Inventory playerInv, ContainerLevelAccess worldPosCallable) {
        super(MenuTypesPM.RUNIC_GRINDSTONE.get(), windowId);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInv.player;

        // Add grindstone slots
        this.addSlot(makeInputSlot(this.repairSlots, 0, 49, 19));
        this.addSlot(makeInputSlot(this.repairSlots, 1, 49, 40));
        this.addSlot(new Slot(this.resultSlots, 2, 129, 34) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
            
            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                worldPosCallable.execute((level, pos) -> {
                    if (level instanceof ServerLevel serverLevel) {
                        ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), this.getExperienceAmount(level));
                        
                        // Grant runic knowledge hints to the player based on the enchantments on the pre-wipe item
                        ItemStack preWipeStack = RunicGrindstoneMenu.this.mergeEnchants(
                                RunicGrindstoneMenu.this.repairSlots.getItem(0), 
                                RunicGrindstoneMenu.this.repairSlots.getItem(1));
                        RunicGrindstoneMenu.this.grantHints(preWipeStack);
                    }
                    level.levelEvent(1042, pos, 0);
                });
                RunicGrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
                RunicGrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
            }

            private int getExperienceAmount(Level level) {
                if (xp > -1) return xp;
                int l = 0;
                l += this.getExperienceFromItem(RunicGrindstoneMenu.this.repairSlots.getItem(0));
                l += this.getExperienceFromItem(RunicGrindstoneMenu.this.repairSlots.getItem(1));
                if (l > 0) {
                    int i1 = (int)Math.ceil((double)l / 2.0D);
                    return i1 + level.random.nextInt(i1);
                } else {
                    return 0;
                }
            }
            
            private int getExperienceFromItem(ItemStack stack) {
                int total = 0;
                for (var entry : EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet()) {
                    Holder<Enchantment> enchHolder = entry.getKey();
                    int val = entry.getIntValue();
                    if (!enchHolder.is(EnchantmentTags.CURSE)) {
                        total += enchHolder.value().getMinCost(val);
                    }
                }
                return total;
            }
        });
        
        // Add inventory slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
             this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
         }
    }
    
    protected static Slot makeInputSlot(Container pContainer, int pSlot, int pX, int pY) {
        return Services.MENU.makeFilteredSlot(Services.ITEM_HANDLERS.wrap(pContainer, null), pSlot, pX, pY,
                new FilteredSlotProperties().filter(stack -> stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted() || stack.canGrindstoneRepair() || RuneManager.hasRunes(stack)));
    }
    
    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        if (pContainer == this.repairSlots) {
            this.createResult();
        }
    }

    public ItemStack removeNonCurses(ItemStack pItem) {
        ItemEnchantments enchantments = EnchantmentHelper.updateEnchantments(pItem, updater -> updater.removeIf(enchHolder -> !enchHolder.is(EnchantmentTags.CURSE)));
        if (pItem.is(Items.ENCHANTED_BOOK) && enchantments.isEmpty()) {
            pItem = pItem.transmuteCopy(Items.BOOK);
        }

        int cost = 0;
        for (int index = 0; index < enchantments.size(); index++) {
            cost = AnvilMenu.calculateIncreasedRepairCost(cost);
        }

        pItem.set(DataComponents.REPAIR_COST, cost);

        // Remove any runes applied to the item(s)
        RuneManager.clearRunes(pItem);
        
        return pItem;
    }
    
    public void createResult() {
        this.resultSlots.setItem(0, this.computeResult(this.repairSlots.getItem(0), this.repairSlots.getItem(1)));

        this.broadcastChanges();
        
        this.worldPosCallable.execute((world, pos) -> {
            if (!world.isClientSide && this.player instanceof ServerPlayer spe) {
                ItemStack stack = this.resultSlots.getItem(0);
                spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 2, stack));
            }
        });
    }
    
    private ItemStack computeResult(ItemStack pInputItem, ItemStack pAdditionalItem) {
        var event = net.minecraftforge.event.ForgeEventFactory.onGrindstoneChange(pInputItem, pAdditionalItem, this.resultSlots, -1);
        if (event.isCanceled()) {
            this.xp = -1;
            return ItemStack.EMPTY;
        } else if (!event.getOutput().isEmpty()) {
            this.xp = event.getXp();
            return event.getOutput();
        } else {
            this.xp = Integer.MIN_VALUE;
        }

        boolean flag = !pInputItem.isEmpty() || !pAdditionalItem.isEmpty();
        if (!flag) {
            return ItemStack.EMPTY;
        } else if (pInputItem.getCount() <= 1 && pAdditionalItem.getCount() <= 1) {
            boolean flag1 = !pInputItem.isEmpty() && !pAdditionalItem.isEmpty();
            if (!flag1) {
                ItemStack itemstack = !pInputItem.isEmpty() ? pInputItem : pAdditionalItem;
                return !EnchantmentHelper.hasAnyEnchantments(itemstack) ? ItemStack.EMPTY : this.removeNonCurses(itemstack.copy());
            } else {
                return this.mergeItems(pInputItem, pAdditionalItem);
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    private ItemStack mergeItems(ItemStack pInputItem, ItemStack pAdditionalItem) {
        if (!pInputItem.is(pAdditionalItem.getItem())) {
            return ItemStack.EMPTY;
        } else {
            int i = Math.max(pInputItem.getMaxDamage(), pAdditionalItem.getMaxDamage());
            int j = pInputItem.getMaxDamage() - pInputItem.getDamageValue();
            int k = pAdditionalItem.getMaxDamage() - pAdditionalItem.getDamageValue();
            int l = j + k + i * 5 / 100;
            int i1 = 1;
            if (!pInputItem.isDamageableItem()) {
                if (pInputItem.getMaxStackSize() < 2 || !ItemStack.matches(pInputItem, pAdditionalItem)) {
                    return ItemStack.EMPTY;
                }

                i1 = 2;
            }

            ItemStack itemstack = pInputItem.copyWithCount(i1);
            if (itemstack.isDamageableItem()) {
                itemstack.set(DataComponents.MAX_DAMAGE, i);
                itemstack.setDamageValue(Math.max(i - l, 0));
            }

            this.mergeEnchants(itemstack, pAdditionalItem);
            return this.removeNonCurses(itemstack);
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.worldPosCallable.execute((level, pos) -> {
            this.clearContainer(pPlayer, this.repairSlots);
        });
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNIC_GRINDSTONE.get());
    }
    
    protected void grantHints(ItemStack stack) {
        Set<Holder<Enchantment>> enchants = stack.getEnchantments().keySet();
        
        this.worldPosCallable.execute((level, pos) -> {
            MutableInt hintCount = new MutableInt(0);
            for (Holder<Enchantment> enchant : enchants) {
                RuneManager.getRuneDefinition(level.registryAccess(), enchant).ifPresent(definition -> {
                    RuneEnchantmentKey fullResearch = new RuneEnchantmentKey(enchant);
                    if (!fullResearch.isKnownBy(this.player)) {
                        Optional<AbstractRequirement<?>> requirementOpt = definition.requirementOpt();
                        if (requirementOpt.isEmpty() || requirementOpt.get().isMetBy(this.player)) {
                            List<RuneEnchantmentPartialKey> candidates = definition.getRunes().stream().filter(rune -> rune.getRequirement().isMetBy(this.player))
                                    .map(rune -> new RuneEnchantmentPartialKey(enchant, rune.getType())).filter(key -> !key.isKnownBy(this.player)).toList();
                            if (!candidates.isEmpty()) {
                                // If at least one hint is available, grant one at random
                                WeightedRandomBag<RuneEnchantmentPartialKey> candidateBag = new WeightedRandomBag<>();
                                for (RuneEnchantmentPartialKey candidate : candidates) {
                                    candidateBag.add(candidate, 1);
                                }
                                ResearchManager.completeResearch(this.player, ResearchEntries.UNLOCK_RUNE_ENCHANTMENTS);
                                ResearchManager.completeResearch(this.player, candidateBag.getRandom(this.player.getRandom()));
                                hintCount.increment();
                                
                                // If, after granting the hint, the player knows all three pieces, then grant them the full research
                                if (definition.getRunes().stream().map(rune -> new RuneEnchantmentPartialKey(enchant, rune.getType())).allMatch(key -> key.isKnownBy(this.player))) {
                                    ResearchManager.completeResearch(this.player, fullResearch);
                                }
                            }
                        }
                    }
                });
            }
            if (hintCount.intValue() > 0) {
                // If at least one hint was granted to the player, notify them
                this.player.displayClientMessage(Component.translatable("event.primalmagick.runic_grindstone.hints_granted").withStyle(ChatFormatting.GREEN), false);
            }
        });
    }
    
    public ItemStack mergeEnchants(ItemStack pCopyTo, ItemStack pCopyFrom) {
        ItemStack retVal = pCopyTo.copy();
        EnchantmentHelper.updateEnchantments(pCopyTo, updater -> {
            ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(pCopyFrom);
            for (Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                Holder<Enchantment> holder = entry.getKey();
                if (!holder.is(EnchantmentTags.CURSE) || updater.getLevel(holder) == 0) {
                    updater.upgrade(holder, entry.getIntValue());
                }
            }
        });
        return retVal;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.repairSlots.getItem(0);
            ItemStack itemstack3 = this.repairSlots.getItem(1);
            if (pIndex == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex != 0 && pIndex != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (pIndex >= 3 && pIndex < 30) {
                        if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (pIndex >= 30 && pIndex < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }
}
