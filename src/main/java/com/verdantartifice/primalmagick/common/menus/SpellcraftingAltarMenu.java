package com.verdantartifice.primalmagick.common.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.SpellcraftingRecipe;
import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.SpellcraftingResultSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellFactory;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

/**
 * Server data container for the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarMenu extends AbstractTileMenu<SpellcraftingAltarTileEntity> {
    protected static final ResourceLocation RECIPE_LOC = PrimalMagick.resource("spellcrafting");

    protected final CraftingContainer scrollInv = new TransientCraftingContainer(this, 1, 1);
    protected final WandInventory wandInv = new WandInventory(this);
    protected final ResultContainer resultInv = new ResultContainer();
    protected final Player player;
    protected final Slot wandSlot;
    protected final Slot scrollSlot;
    
    protected String spellName = "";
    protected int spellVehicleTypeIndex = 0;
    protected int spellPayloadTypeIndex = 0;
    protected int spellPrimaryModTypeIndex = 0;
    protected int spellSecondaryModTypeIndex = 0;
    protected SpellPackage spellPackageCache = null;
    protected Map<SpellComponent, Map<String, Integer>> spellPropertyCache = new HashMap<>();

    public SpellcraftingAltarMenu(int windowId, Inventory inv, BlockPos tilePos) {
        this(windowId, inv, tilePos, null);
    }

    public SpellcraftingAltarMenu(int windowId, Inventory inv, BlockPos tilePos, SpellcraftingAltarTileEntity altar) {
        super(MenuTypesPM.SPELLCRAFTING_ALTAR.get(), windowId, SpellcraftingAltarTileEntity.class, inv.player.level(), tilePos, altar);
        this.player = inv.player;
        for (SpellComponent comp : SpellComponent.values()) {
            this.spellPropertyCache.put(comp, new HashMap<>());
        }
        
        // Slot 0: Result
        this.addSlot(new SpellcraftingResultSlot(this.player, this.scrollInv, this.wandInv, this::getManaCosts, this.resultInv, 0, 206, 8));
        
        // Slot 1: Input wand
        this.wandSlot = this.addSlot(new WandSlot(InventoryUtils.wrapInventory(this.wandInv, null), 0, 8, 8, false));

        // Slot 2: Blank scroll
        this.scrollSlot = this.addSlot(new FilteredSlot(InventoryUtils.wrapInventory(this.scrollInv, null), 0, 160, 8, new FilteredSlot.Properties().item(ItemsPM.SPELL_SCROLL_BLANK.get())));
        
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
    public boolean stillValid(Player playerIn) {
        return stillValid(this.containerLevelAccess, playerIn, BlocksPM.SPELLCRAFTING_ALTAR.get());
    }

    public SourceList getManaCosts() {
        return this.getSpellPackage().getManaCost();
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public SpellPackage getSpellPackage() {
        if (this.spellPackageCache == null) {
            this.spellPackageCache = this.makeFinalSpellPackage();
        }
        return this.spellPackageCache;
    }
    
    protected SpellPackage makeFinalSpellPackage() {
        // Assemble the final spell package from the input types and properties
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
    public Component getDefaultSpellName() {
        // Don't use getSpellPackage here, or it will cause infinite recursion
        Component vehiclePiece = this.getSpellVehicleComponent().getDefaultNamePiece();
        Component payloadPiece = this.getSpellPayloadComponent().getDefaultNamePiece();
        Component primaryModPiece = this.getSpellPrimaryModComponent().getDefaultNamePiece();
        Component secondaryModPiece = this.getSpellSecondaryModComponent().getDefaultNamePiece();
        boolean primaryActive = this.getSpellPrimaryModComponent().isActive();
        boolean secondaryActive = this.getSpellSecondaryModComponent().isActive();
        if (vehiclePiece == null || payloadPiece == null || vehiclePiece.getString().isEmpty() || payloadPiece.getString().isEmpty()) {
            // If the constructed spell is invalid, don't show a default name
            return Component.literal("");
        } else if (!primaryActive && !secondaryActive) {
            // No mods selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.0", vehiclePiece, payloadPiece);
        } else if (primaryActive && secondaryActive) {
            // Two mods selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.2", vehiclePiece, payloadPiece, primaryModPiece, secondaryModPiece);
        } else if (primaryActive) {
            // Only a primary mod selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.1", vehiclePiece, payloadPiece, primaryModPiece);
        } else {
            // Only a secondary mod selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.1", vehiclePiece, payloadPiece, secondaryModPiece);
        }
    }
    
    public void setSpellName(String name) {
        // Clear the spell package cache and trigger a regeneration of the output item on change
        this.spellName = name;
        this.spellPackageCache = null;
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellVehicle getSpellVehicleComponent() {
        // Construct a new spell vehicle from the saved type index and populate it with any cached properties
        ISpellVehicle retVal = SpellFactory.getVehicleFromType(SpellManager.getVehicleTypes(this.player).get(this.getSpellVehicleTypeIndex()));
        if (retVal != null) {
            for (Map.Entry<String, Integer> entry : this.spellPropertyCache.get(SpellComponent.VEHICLE).entrySet()) {
                if (retVal.getProperty(entry.getKey()) != null) {
                    retVal.getProperty(entry.getKey()).setValue(entry.getValue().intValue());
                }
            }
        }
        return retVal;
    }
    
    public int getSpellVehicleTypeIndex() {
        return this.spellVehicleTypeIndex;
    }
    
    public void setSpellVehicleTypeIndex(int index) {
        // Clear the spell package cache and trigger a regeneration of the output item on change
        index = Mth.clamp(index, 0, SpellManager.getVehicleTypes(this.player).size() - 1);
        this.spellVehicleTypeIndex = index;
        this.spellPackageCache = null;
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellPayload getSpellPayloadComponent() {
        // Construct a new spell payload from the saved type index and populate it with any cached properties
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
        // Clear the spell package cache and trigger a regeneration of the output item on change
        index = Mth.clamp(index, 0, SpellManager.getPayloadTypes(this.player).size() - 1);
        this.spellPayloadTypeIndex = index;
        this.spellPackageCache = null;
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellMod getSpellPrimaryModComponent() {
        // Construct a new spell mod from the saved type index and populate it with any cached properties
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
        // Clear the spell package cache and trigger a regeneration of the output item on change
        index = Mth.clamp(index, 0, SpellManager.getModTypes(this.player).size() - 1);
        this.spellPrimaryModTypeIndex = index;
        this.spellPackageCache = null;
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    protected ISpellMod getSpellSecondaryModComponent() {
        // Construct a new spell mod from the saved type index and populate it with any cached properties
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
        // Clear the spell package cache and trigger a regeneration of the output item on change
        index = Mth.clamp(index, 0, SpellManager.getModTypes(this.player).size() - 1);
        this.spellSecondaryModTypeIndex = index;
        this.spellPackageCache = null;
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }
    
    public void setSpellPropertyValue(SpellComponent component, String name, int value) {
        SpellPackage spell = this.getSpellPackage();
        SpellProperty property = null;
        
        // Determine which property is to be changed
        if (component == SpellComponent.VEHICLE && spell.getVehicle() != null) {
            property = spell.getVehicle().getProperty(name);
        } else if (component == SpellComponent.PAYLOAD && spell.getPayload() != null) {
            property = spell.getPayload().getProperty(name);
        } else if (component == SpellComponent.PRIMARY_MOD && spell.getPrimaryMod() != null) {
            property = spell.getPrimaryMod().getProperty(name);
        } else if (component == SpellComponent.SECONDARY_MOD && spell.getSecondaryMod() != null) {
            property = spell.getSecondaryMod().getProperty(name);
        }
        
        // Set and cache the changed value, then trigger a regeneration of the output item
        if (property != null) {
            property.setValue(value);
            this.spellPropertyCache.get(component).put(name, value);
            this.containerLevelAccess.execute((world, blockPos) -> {
                this.slotChangedCraftingGrid(world);
            });
        }
    }
    
    @Override
    public void removed(Player playerIn) {
        // Return input scroll and wand to the player's inventory when the GUI is closed
        super.removed(playerIn);
        this.clearContainer(playerIn, this.wandInv);
        this.clearContainer(playerIn, this.scrollInv);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 3 && index < 30) {
                // If transferring from the backpack, move wands or blank scrolls to the appropriate slot, and anything else to the hotbar
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.scrollSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the hotbar, move wands or blank scrolls to the appropriate slot, and anything else to the backpack
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.scrollSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
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
            
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.resultInv && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public void slotsChanged(Container inventoryIn) {
        super.slotsChanged(inventoryIn);
        this.containerLevelAccess.execute((world, blockPos) -> {
            this.slotChangedCraftingGrid(world);
        });
    }

    protected void slotChangedCraftingGrid(Level world) {
        if (!world.isClientSide && this.player instanceof ServerPlayer) {
            ServerPlayer spe = (ServerPlayer)this.player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<RecipeHolder<?>> opt = world.getServer().getRecipeManager().byKey(RECIPE_LOC);
            if (opt.isPresent() && opt.get().value() instanceof SpellcraftingRecipe recipe) {
                // If the ingredients are present, enough mana is had, and the spell is valid, show the filled scroll in the output
                if (recipe.matches(this.scrollInv, world) && this.wandContainsEnoughMana(spe) && this.getSpellPackage().isValid()) {
                    stack = recipe.assemble(this.scrollInv, world.registryAccess());
                    if (stack != null && stack.getItem() instanceof SpellScrollItem) {
                        ((SpellScrollItem)stack.getItem()).setSpell(stack, this.getSpellPackage());
                    }
                }
            }

            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setItem(0, stack);
            spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }
    }
    
    protected boolean wandContainsEnoughMana(Player player) {
        ItemStack stack = this.wandInv.getItem(0);
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof IWand)) {
            return false;
        }
        IWand wand = (IWand)stack.getItem();
        return wand.containsRealMana(stack, player, this.getManaCosts());
    }
    
    public ItemStack getWand() {
        return this.wandInv.getItem(0);
    }
}
