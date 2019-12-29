package com.verdantartifice.primalmagic.common.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.slots.SpellScrollBlankSlot;
import com.verdantartifice.primalmagic.common.containers.slots.SpellcraftingResultSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagic.common.crafting.SpellcraftingRecipe;
import com.verdantartifice.primalmagic.common.crafting.WandInventory;
import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellComponent;
import com.verdantartifice.primalmagic.common.spells.SpellFactory;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    protected SpellPackage spellPackageCache = null;
    protected Map<SpellComponent, Map<String, Integer>> spellPropertyCache = new HashMap<>();

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.SPELLCRAFTING_ALTAR, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        for (SpellComponent comp : SpellComponent.values()) {
            this.spellPropertyCache.put(comp, new HashMap<>());
        }
        
        // Slot 0: Result
        this.addSlot(new SpellcraftingResultSlot(this.player, this.scrollInv, this.wandInv, this::getManaCosts, this.resultInv, 0, 206, 8));
        
        // Slot 1: Input wand
        this.wandSlot = this.addSlot(new WandSlot(this.wandInv, 0, 8, 8));

        // Slot 2: Blank scroll
        this.scrollSlot = this.addSlot(new SpellScrollBlankSlot(this.scrollInv, 0, 160, 8));
        
        // Slots 3-29: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 35 + (j * 18), 140 + (i * 18)));
            }
        }
        
        // Slots 30-38: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 35 + (i * 18), 198));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.SPELLCRAFTING_ALTAR);
    }

    public SourceList getManaCosts() {
        return this.getSpellPackage().getManaCost();
    }
    
    public PlayerEntity getPlayer() {
        return this.player;
    }
    
    public SpellPackage getSpellPackage() {
        if (this.spellPackageCache == null) {
            this.spellPackageCache = this.makeFinalSpellPackage();
        }
        return this.spellPackageCache;
    }
    
    protected SpellPackage makeFinalSpellPackage() {
        SpellPackage spell = new SpellPackage();
        spell.setName(this.getSpellName());
        spell.setVehicle(this.getSpellVehicleComponent());
        spell.setPayload(this.getSpellPayloadComponent());
        spell.setPrimaryMod(this.getSpellPrimaryModComponent());
        spell.setSecondaryMod(this.getSpellSecondaryModComponent());
        return spell;
    }
    
    public String getSpellName() {
        return (this.spellName == null || this.spellName.isEmpty()) ? this.getDefaultSpellName().getString() : this.spellName;
    }
    
    @Nonnull
    public ITextComponent getDefaultSpellName() {
        ITextComponent vehiclePiece = this.getSpellVehicleComponent().getDefaultNamePiece();
        ITextComponent payloadPiece = this.getSpellPayloadComponent().getDefaultNamePiece();
        ITextComponent primaryModPiece = this.getSpellPrimaryModComponent().getDefaultNamePiece();
        ITextComponent secondaryModPiece = this.getSpellSecondaryModComponent().getDefaultNamePiece();
        boolean primaryActive = this.getSpellPrimaryModComponent().isActive();
        boolean secondaryActive = this.getSpellSecondaryModComponent().isActive();
        if (vehiclePiece == null || payloadPiece == null || vehiclePiece.getString().isEmpty() || payloadPiece.getString().isEmpty()) {
            return new StringTextComponent("");
        } else if (!primaryActive && !secondaryActive) {
            return new TranslationTextComponent("primalmagic.spell.default_name_format.mods.0", vehiclePiece, payloadPiece);
        } else if (primaryActive && secondaryActive) {
            return new TranslationTextComponent("primalmagic.spell.default_name_format.mods.2", vehiclePiece, payloadPiece, primaryModPiece, secondaryModPiece);
        } else if (primaryActive) {
            return new TranslationTextComponent("primalmagic.spell.default_name_format.mods.1", vehiclePiece, payloadPiece, primaryModPiece);
        } else {
            return new TranslationTextComponent("primalmagic.spell.default_name_format.mods.1", vehiclePiece, payloadPiece, secondaryModPiece);
        }
    }
    
    public void setSpellName(String name) {
        this.spellName = name;
        this.spellPackageCache = null;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellVehicle getSpellVehicleComponent() {
        return SpellFactory.getVehicleFromType(SpellManager.getVehicleTypes(this.player).get(this.getSpellPackageTypeIndex()));
    }
    
    public int getSpellPackageTypeIndex() {
        return this.spellPackageTypeIndex;
    }
    
    public void setSpellPackageTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getVehicleTypes(this.player).size() - 1);
        this.spellPackageTypeIndex = index;
        this.spellPackageCache = null;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellPayload getSpellPayloadComponent() {
        ISpellPayload retVal = SpellFactory.getPayloadFromType(SpellManager.getPayloadTypes(this.player).get(this.getSpellPayloadTypeIndex()));
        if (retVal != null) {
            for (Map.Entry<String, Integer> entry : this.spellPropertyCache.get(SpellComponent.PAYLOAD).entrySet()) {
                if (retVal.getProperty(entry.getKey()) != null) {
                    retVal.getProperty(entry.getKey()).setValue(entry.getValue().intValue());
                }
            }
        }
        return retVal;
    }
    
    public int getSpellPayloadTypeIndex() {
        return this.spellPayloadTypeIndex;
    }
    
    public void setSpellPayloadTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getPayloadTypes(this.player).size() - 1);
        this.spellPayloadTypeIndex = index;
        this.spellPackageCache = null;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellMod getSpellPrimaryModComponent() {
        ISpellMod retVal = SpellFactory.getModFromType(SpellManager.getModTypes(this.player).get(this.getSpellPrimaryModTypeIndex()));
        if (retVal != null) {
            for (Map.Entry<String, Integer> entry : this.spellPropertyCache.get(SpellComponent.PRIMARY_MOD).entrySet()) {
                if (retVal.getProperty(entry.getKey()) != null) {
                    retVal.getProperty(entry.getKey()).setValue(entry.getValue().intValue());
                }
            }
        }
        return retVal;
    }
    
    public int getSpellPrimaryModTypeIndex() {
        return this.spellPrimaryModTypeIndex;
    }
    
    public void setSpellPrimaryModTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getModTypes(this.player).size() - 1);
        this.spellPrimaryModTypeIndex = index;
        this.spellPackageCache = null;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellMod getSpellSecondaryModComponent() {
        ISpellMod retVal = SpellFactory.getModFromType(SpellManager.getModTypes(this.player).get(this.getSpellSecondaryModTypeIndex()));
        if (retVal != null) {
            for (Map.Entry<String, Integer> entry : this.spellPropertyCache.get(SpellComponent.SECONDARY_MOD).entrySet()) {
                if (retVal.getProperty(entry.getKey()) != null) {
                    retVal.getProperty(entry.getKey()).setValue(entry.getValue().intValue());
                }
            }
        }
        return retVal;
    }
    
    public int getSpellSecondaryModTypeIndex() {
        return this.spellSecondaryModTypeIndex;
    }
    
    public void setSpellSecondaryModTypeIndex(int index) {
        index = MathHelper.clamp(index, 0, SpellManager.getModTypes(this.player).size() - 1);
        this.spellSecondaryModTypeIndex = index;
        this.spellPackageCache = null;
        this.worldPosCallable.consume((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public void setSpellPropertyValue(SpellComponent component, String name, int value) {
        SpellPackage spell = this.getSpellPackage();
        SpellProperty property = null;
        if (component == SpellComponent.PAYLOAD && spell.getPayload() != null) {
            property = spell.getPayload().getProperty(name);
        } else if (component == SpellComponent.PRIMARY_MOD && spell.getPrimaryMod() != null) {
            property = spell.getPrimaryMod().getProperty(name);
        } else if (component == SpellComponent.SECONDARY_MOD && spell.getSecondaryMod() != null) {
            property = spell.getSecondaryMod().getProperty(name);
        }
        if (property != null) {
            property.setValue(value);
            this.spellPropertyCache.get(component).put(name, value);
            this.worldPosCallable.consume((world, blockPos) -> {
                this.slotChangedCraftingGrid(world);
            });
        }
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
                if (recipe.matches(this.scrollInv, world) && this.wandContainsEnoughMana(spe) && this.getSpellPackage().isValid()) {
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
