package com.verdantartifice.primalmagic.common.containers;

import java.util.Optional;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.SpellScrollBlankSlot;
import com.verdantartifice.primalmagic.common.containers.slots.SpellcraftingResultSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagic.common.crafting.SpellcraftingRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandInventory;
import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellFactory;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SpellcraftingAltarContainer extends Container {
    protected static final ResourceLocation RECIPE_LOC = new ResourceLocation(PrimalMagic.MODID, "spellcrafting");

    protected final CraftingInventory scrollInv = new CraftingInventory(this, 1, 1);
    protected final WandInventory wandInv = new WandInventory(this);
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;
    protected final Slot wandSlot;
    protected final Slot scrollSlot;
    
    protected String spellName = "";
    protected int spellPackageTypeIndex = 0;
    protected int spellPayloadTypeIndex = 0;
    protected int spellPrimaryModTypeIndex = 0;
    protected int spellSecondaryModTypeIndex = 0;

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.SPELLCRAFTING_ALTAR, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Result
        this.addSlot(new SpellcraftingResultSlot(this.player, this.scrollInv, this.wandInv, this::getManaCosts, this.resultInv, 0, 188, 8));
        
        // Slot 1: Input wand
        this.wandSlot = this.addSlot(new WandSlot(this.wandInv, 0, 8, 8));

        // Slot 2: Blank scroll
        this.scrollSlot = this.addSlot(new SpellScrollBlankSlot(this.scrollInv, 0, 142, 8));
        
        // Slots 3-29: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 26 + (j * 18), 140 + (i * 18)));
            }
        }
        
        // Slots 30-38: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 26 + (i * 18), 198));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.SPELLCRAFTING_ALTAR);
    }

    public SourceList getManaCosts() {
        return this.getSpellPackage().getManaCost();
    }
    
    protected ISpellPackage getSpellPackage() {
        ISpellPackage spell = SpellFactory.getPackageFromType(SpellManager.getPackageTypes().get(this.getSpellPackageTypeIndex()));
        if (spell != null) {
            spell.setName(this.getSpellName());
            spell.setPayload(SpellFactory.getPayloadFromType(SpellManager.getPayloadTypes().get(this.getSpellPayloadTypeIndex())));
            // TODO set payload properties
            spell.setPrimaryMod(SpellFactory.getModFromType(SpellManager.getModTypes().get(this.getSpellPrimaryModTypeIndex())));
            // TODO set primary mod properties
            spell.setSecondaryMod(SpellFactory.getModFromType(SpellManager.getModTypes().get(this.getSpellSecondaryModTypeIndex())));
            // TODO set secondary mod properties
        }
        return spell;
    }
    
    public String getSpellName() {
        return (this.spellName == null || this.spellName.isEmpty()) ? this.getDefaultSpellName() : this.spellName;
    }
    
    protected String getDefaultSpellName() {
        // TODO Determine default spell name based on spell package
        return "Crafted Spell";
    }
    
    public void setSpellName(String name) {
        this.spellName = name;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public int getSpellPackageTypeIndex() {
        return this.spellPackageTypeIndex;
    }
    
    public void setSpellPackageTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getPackageTypes().size() - 1);
        PrimalMagic.LOGGER.debug("Setting crafted spell package type index to {}", index);
        this.spellPackageTypeIndex = index;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public int getSpellPayloadTypeIndex() {
        return this.spellPayloadTypeIndex;
    }
    
    public void setSpellPayloadTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getPayloadTypes().size() - 1);
        PrimalMagic.LOGGER.debug("Setting crafted spell payload type index to {}", index);
        this.spellPayloadTypeIndex = index;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public int getSpellPrimaryModTypeIndex() {
        return this.spellPrimaryModTypeIndex;
    }
    
    public void setSpellPrimaryModTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getModTypes().size() - 1);
        PrimalMagic.LOGGER.debug("Setting crafted spell mod 1 type index to {}", index);
        this.spellPrimaryModTypeIndex = index;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public int getSpellSecondaryModTypeIndex() {
        return this.spellSecondaryModTypeIndex;
    }
    
    public void setSpellSecondaryModTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getModTypes().size() - 1);
        PrimalMagic.LOGGER.debug("Setting crafted spell mod 2 type index to {}", index);
        this.spellSecondaryModTypeIndex = index;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.clearContainer(playerIn, world, this.wandInv);
            this.clearContainer(playerIn, world, this.scrollInv);
        });
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index == 0) {
                this.worldPosCallable.consume((world, blockPos) -> {
                    slotStack.getItem().onCreated(slotStack, world, playerIn);
                });
                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stack);
            } else if (index >= 3 && index < 30) {
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.scrollSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 30 && index < 39) {
                if (this.wandSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.scrollSlot.isItemValid(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(slotStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            ItemStack taken = slot.onTake(playerIn, slotStack);
            if (index == 0) {
                playerIn.dropItem(taken, false);
            }
        }
        return stack;
    }
    
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.resultInv && super.canMergeSlot(stack, slotIn);
    }
    
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }

    protected void slotChangedCraftingGrid(World world) {
        if (!world.isRemote && this.player instanceof ServerPlayerEntity) {
            ServerPlayerEntity spe = (ServerPlayerEntity)this.player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<? extends IRecipe<?>> opt = world.getServer().getRecipeManager().getRecipe(RECIPE_LOC);
            if (opt.isPresent() && opt.get() instanceof SpellcraftingRecipe) {
                SpellcraftingRecipe recipe = (SpellcraftingRecipe)opt.get();
                if (recipe.matches(this.scrollInv, world) && this.wandContainsEnoughMana(spe)) {
                    stack = recipe.getCraftingResult(this.scrollInv);
                    if (stack != null && stack.getItem() instanceof SpellScrollItem) {
                        ((SpellScrollItem)stack.getItem()).setSpell(stack, this.getSpellPackage());
                    }
                }
            }
            this.resultInv.setInventorySlotContents(0, stack);
            spe.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, stack));
        }
    }
    
    protected boolean wandContainsEnoughMana(PlayerEntity player) {
        ItemStack stack = this.wandInv.getStackInSlot(0);
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof IWand)) {
            return false;
        }
        IWand wand = (IWand)stack.getItem();
        return wand.containsMana(stack, player, this.getManaCosts());
    }
}
