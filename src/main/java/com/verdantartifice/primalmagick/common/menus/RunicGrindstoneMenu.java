package com.verdantartifice.primalmagick.common.menus;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Server data container for the runic grindstone GUI.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneMenu extends AbstractContainerMenu {
    protected static final List<RuneType> RUNE_TYPES = List.of(RuneType.VERB, RuneType.NOUN, RuneType.SOURCE);
    protected static final Supplier<SimpleResearchKey> UNLOCK_INDEX_RESEARCH = ResearchNames.simpleKey(ResearchNames.UNLOCK_RUNE_ENCHANTMENTS);
    
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
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    if (!entry.getKey().isCurse()) {
                        total += entry.getKey().getMinCost(entry.getValue());
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
        return new FilteredSlot(InventoryUtils.wrapInventory(pContainer, null), pSlot, pX, pY,
                new FilteredSlot.Properties().filter(stack -> stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted() || stack.canGrindstoneRepair() || RuneManager.hasRunes(stack)));
    }
    
    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        if (pContainer == this.repairSlots) {
            this.createResult();
        }
    }

    public ItemStack removeNonCurses(ItemStack stack, int damage, int count) {
        ItemStack retVal = stack.copyWithCount(count);
        retVal.removeTagKey("Enchantments");
        retVal.removeTagKey("StoredEnchantments");
        if (damage > 0) {
            retVal.setDamageValue(damage);
        } else {
            retVal.removeTagKey("Damage");
        }

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((entry) -> {
            return entry.getKey().isCurse();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        EnchantmentHelper.setEnchantments(map, retVal);
        retVal.setRepairCost(0);
        if (retVal.is(Items.ENCHANTED_BOOK) && map.size() == 0) {
            retVal = new ItemStack(Items.BOOK);
            if (stack.hasCustomHoverName()) {
                retVal.setHoverName(stack.getHoverName());
            }
        }

        for (int i = 0; i < map.size(); ++i) {
            retVal.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(retVal.getBaseRepairCost()));
        }

        // Remove any runes applied to the item(s)
        RuneManager.clearRunes(retVal);
        
        return retVal;
    }
    
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
            
            if (j > itemstack2.getMaxStackSize()) {
                // Skip the repair if the result would give an item stack with a count not normally obtainable
                this.resultSlots.setItem(0, ItemStack.EMPTY);
            } else {
                this.resultSlots.setItem(0, this.removeNonCurses(itemstack2, i, j));
            }
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
        Set<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet();
        int hintCount = 0;
        
        for (Enchantment enchant : enchants) {
            SimpleResearchKey fullResearch = SimpleResearchKey.parseRuneEnchantment(enchant);
            if (RuneManager.hasRuneDefinition(enchant) && !fullResearch.isKnownByStrict(this.player)) {
                RuneEnchantmentDefinition definition = RuneManager.getRuneDefinition(enchant);
                CompoundResearchKey requirements = definition.getRequiredResearch();
                if (requirements == null || requirements.isKnownByStrict(this.player)) {
                    List<SimpleResearchKey> candidates = definition.getRunes().stream().filter(rune -> rune.getDiscoveryKey().isKnownByStrict(this.player))
                            .map(rune -> SimpleResearchKey.parsePartialRuneEnchantment(enchant, rune.getType())).filter(key -> !key.isKnownByStrict(this.player)).toList();
                    if (!candidates.isEmpty()) {
                        // If at least one hint is available, grant one at random
                        WeightedRandomBag<SimpleResearchKey> candidateBag = new WeightedRandomBag<>();
                        for (SimpleResearchKey candidate : candidates) {
                            candidateBag.add(candidate, 1);
                        }
                        ResearchManager.completeResearch(this.player, UNLOCK_INDEX_RESEARCH.get());
                        ResearchManager.completeResearch(this.player, candidateBag.getRandom(this.player.getRandom()));
                        hintCount++;
                        
                        // If, after granting the hint, the player knows all three pieces, then grant them the full research
                        if (definition.getRunes().stream().map(rune -> SimpleResearchKey.parsePartialRuneEnchantment(enchant, rune.getType())).allMatch(key -> key.isKnownByStrict(this.player))) {
                            ResearchManager.completeResearch(this.player, fullResearch);
                        }
                    }
                }
            }
        }
        if (hintCount > 0) {
            // If at least one hint was granted to the player, notify them
            this.player.displayClientMessage(Component.translatable("event.primalmagick.runic_grindstone.hints_granted").withStyle(ChatFormatting.GREEN), false);
        }
    }
    
    public ItemStack mergeEnchants(ItemStack pCopyTo, ItemStack pCopyFrom) {
        ItemStack itemstack = pCopyTo.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(pCopyFrom);
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (!enchantment.isCurse() || EnchantmentHelper.getTagEnchantmentLevel(enchantment, itemstack) == 0) {
                itemstack.enchant(enchantment, entry.getValue());
            }
        }
        return itemstack;
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
