package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

import java.util.Optional;

/**
 * Server data container for the study vocabulary mode of the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyMenu extends AbstractScribeTableMenu {
    public static final ResourceLocation BOOK_SLOT_TEXTURE = ResourceUtils.loc("item/empty_book_slot");
    protected static final Component ANCIENT_BOOK_TOOLTIP = Component.translatable("tooltip.primalmagick.scribe_table.slot.ancient_book");
    protected static final int[] COSTS_PER_SLOT = new int[] { 25, 100, 300 };

    public final int[] costs = new int[3];
    public final int[] minLevels = new int[] { 10, 20, 30 };
    private final DataSlot nameSeed = DataSlot.standalone();
    private final DataSlot languageClue = DataSlot.standalone();
    private final DataSlot vocabularyCount = DataSlot.standalone();

    protected Slot studySlot;
    
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_STUDY_VOCABULARY.get(), windowId, inv, pos, entity);
        this.addDataSlot(DataSlot.shared(this.costs, 0));
        this.addDataSlot(DataSlot.shared(this.costs, 1));
        this.addDataSlot(DataSlot.shared(this.costs, 2));
        this.addDataSlot(DataSlot.shared(this.minLevels, 0));
        this.addDataSlot(DataSlot.shared(this.minLevels, 1));
        this.addDataSlot(DataSlot.shared(this.minLevels, 2));
        this.addDataSlot(this.nameSeed).set(this.player.getEnchantmentSeed());
        this.addDataSlot(this.languageClue);
        this.addDataSlot(this.vocabularyCount);
        this.refreshBookData();
    }
    
    @Override
    protected void createModeSlots() {
        // Slot 0: Original book
        this.studySlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(Direction.UP), 0, 15, 75,
                new FilteredSlotProperties().filter(this::isAncientBookStack).background(BOOK_SLOT_TEXTURE).tooltip(ANCIENT_BOOK_TOOLTIP)));
    }
    
    @Override
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(0, 56);
    }

    @Override
    public void itemsChanged(IItemHandlerPM pContainer) {
        super.itemsChanged(pContainer);
        this.refreshBookData();
    }
    
    protected void refreshBookData() {
        ItemStack bookStack = this.studySlot.getItem();
        if (bookStack.is(ItemTagsPM.STATIC_BOOKS)) {
            this.getContainerLevelAccess().execute((level, blockPos) -> {
                Optional<Holder<BookLanguage>> langHolderOpt = StaticBookItem.getBookLanguage(bookStack);
                langHolderOpt.ifPresentOrElse(lang -> {
                    int studyCount = StaticBookItem.getBookDefinition(bookStack).map(h -> LinguisticsManager.getTimesStudied(this.player, h, lang)).orElse(0);
                    for (int index = 0; index < 3; index++) {
                        // Set the cost of each slot, including the cost of any previous unstudied slots.  Studied slots are given a cost
                        // of -1 as a marker.  In isolation, each slot's cost is equal to its index plus one (i.e. 1, 2, and 3 respectively).
                        // Thus, the final costs in the case where none have been studied would be 1, 3, and 6 respectively.  If, rather, the
                        // first slot had been studied, the costs would instead be -1, 2, and 5 respectively.
                        this.costs[index] = (index >= studyCount) ? COSTS_PER_SLOT[index] + (index > 0 ? Math.max(this.costs[index - 1], 0) : 0) : -1;
                    }
                    this.languageClue.set(lang.getRegisteredName().hashCode());
                    this.vocabularyCount.set(LinguisticsManager.getVocabulary(this.player, lang));
                }, () -> {
                    this.setDefaultBookData();
                });
            });
        } else {
            this.setDefaultBookData();
        }
    }
    
    protected void setDefaultBookData() {
        for (int index = 0; index < 3; index++) {
            this.costs[index] = 0;
        }
        this.languageClue.set(BookLanguagesPM.DEFAULT.location().toString().hashCode());
        this.vocabularyCount.set(0);
    }
    
    public boolean checkStudyClick(Player player, int slotId) {
        // Check if the given player is allowed to study vocabulary in the given slot
        if (slotId >= 0 && slotId < this.costs.length) {
            ItemStack bookStack = this.studySlot.getItem();
            return (this.costs[slotId] > 0 &&
                    !bookStack.isEmpty() &&
                    ((player.experienceLevel >= this.minLevels[slotId] && player.totalExperience >= this.costs[slotId]) || player.getAbilities().instabuild));
        } else {
            LOGGER.error("{} pressed invalid study vocabulary slot index {}", player.getName().getString(), slotId);
            return false;
        }
    }
    
    public void doStudyClick(Player player, int slotId) {
        if (this.checkStudyClick(player, slotId)) {
            // Perform vocabulary study for the given slot
            this.getContainerLevelAccess().execute((level, blockPos) -> {
                ItemStack bookStack = this.studySlot.getItem();
                Holder<BookDefinition> bookDef = StaticBookItem.getBookDefinition(bookStack).orElse(null);
                Holder<BookLanguage> bookLanguage = StaticBookItem.getBookLanguage(bookStack).orElse(null);
                
                int studyDelta = 0;
                for (int costIndex = 0; costIndex <= slotId && costIndex < this.costs.length; costIndex++) {
                    if (this.costs[costIndex] > 0) {
                        studyDelta++;
                    }
                }

                if (studyDelta > 0) {
                    // Deduct the experience cost for the study and update the player's enchantment seed
                    player.giveExperiencePoints(-this.costs[slotId]);
                    player.onEnchantmentPerformed(ItemStack.EMPTY, 0);
                    
                    // Grant the player increased vocabulary for the book's language
                    LinguisticsManager.incrementVocabulary(player, bookLanguage, studyDelta);
                    
                    // Mark the book as having been studied
                    LinguisticsManager.incrementTimesStudied(player, bookDef, bookLanguage, studyDelta);
                    
                    // Award statistics for study
                    StatsManager.incrementValue(player, StatsPM.VOCABULARY_STUDIED, studyDelta);
                    
                    this.nameSeed.set(player.getEnchantmentSeed());
                    level.playSound(null, blockPos, SoundsPM.WRITING.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                    this.refreshBookData();
                }
            });
        }
    }
    
    public int getNameSeed() {
        return this.nameSeed.get();
    }
    
    public Holder.Reference<BookLanguage> getBookLanguage() {
        int hashCode = this.languageClue.get();
        return this.level.registryAccess().registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).holders().filter(h -> h.key().location().toString().hashCode() == hashCode).findFirst()
                .orElse(BookLanguagesPM.getLanguageOrThrow(BookLanguagesPM.DEFAULT, this.level.registryAccess()));
    }

    public int getVocabularyCount() {
        return this.vocabularyCount.get();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (pIndex == 0) {
                // If transferring the study item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (pIndex >= 1 && pIndex < 28) {
                // If transferring from the backpack, move static books to the appropriate slot, and everything else to the hotbar
                if (this.studySlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (pIndex >= 28 && pIndex < 37) {
                // If transferring from the hotbar, move static books to the appropriate slot, and everything else to the backpack
                if (this.studySlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 1, 37, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(pPlayer, slotStack);
        }
        return stack;
    }
}
