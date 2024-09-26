package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
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
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.AbstractSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ConfiguredSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.payloads.AbstractSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ConfiguredSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.EmptySpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.vehicles.AbstractSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.ConfiguredSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.EmptySpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Server data container for the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarMenu extends AbstractTileMenu<SpellcraftingAltarTileEntity> {
    protected static final ResourceLocation RECIPE_LOC = ResourceUtils.loc("spellcrafting");
    protected static final Component SCROLL_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.spellcrafting_altar.slot.scroll");

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
    protected Map<SpellComponent, Map<SpellProperty, Integer>> spellPropertyCache = new HashMap<>();

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
        this.scrollSlot = this.addSlot(new FilteredSlot(InventoryUtils.wrapInventory(this.scrollInv, null), 0, 160, 8, 
                new FilteredSlot.Properties().item(ItemsPM.SPELL_SCROLL_BLANK.get()).tooltip(SCROLL_SLOT_TOOLTIP)));
        
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
        return stillValid(this.containerLevelAccess, playerIn, BlockRegistration.SPELLCRAFTING_ALTAR.get());
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
        return new SpellPackage(this.getSpellName(), this.getSpellVehicleComponent(), this.getSpellPayloadComponent(), this.getSpellPrimaryModComponent(), this.getSpellSecondaryModComponent());
    }
    
    public String getSpellName() {
        return (this.spellName == null || this.spellName.isEmpty()) ? this.getDefaultSpellName().getString() : this.spellName;
    }
    
    @Nonnull
    public Component getDefaultSpellName() {
        // Don't use getSpellPackage here, or it will cause infinite recursion
        Component vehiclePiece = this.getSpellVehicleComponent().getComponent().getDefaultNamePiece();
        Component payloadPiece = this.getSpellPayloadComponent().getComponent().getDefaultNamePiece();
        Optional<Component> primaryModPiece = this.getSpellPrimaryModComponent().filter(mod -> mod.getComponent().isActive()).map(mod -> mod.getComponent().getDefaultNamePiece());
        Optional<Component> secondaryModPiece = this.getSpellSecondaryModComponent().filter(mod -> mod.getComponent().isActive()).map(mod -> mod.getComponent().getDefaultNamePiece());
        boolean primaryActive = primaryModPiece.isPresent();
        boolean secondaryActive = secondaryModPiece.isPresent();
        if (vehiclePiece == null || payloadPiece == null || vehiclePiece.getString().isEmpty() || payloadPiece.getString().isEmpty()) {
            // If the constructed spell is invalid, don't show a default name
            return Component.literal("");
        } else if (!primaryActive && !secondaryActive) {
            // No mods selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.0", vehiclePiece, payloadPiece);
        } else if (primaryActive && secondaryActive) {
            // Two mods selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.2", vehiclePiece, payloadPiece, primaryModPiece.get(), secondaryModPiece.get());
        } else if (primaryActive) {
            // Only a primary mod selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.1", vehiclePiece, payloadPiece, primaryModPiece.get());
        } else {
            // Only a secondary mod selected
            return Component.translatable("spells.primalmagick.default_name_format.mods.1", vehiclePiece, payloadPiece, secondaryModPiece.get());
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
    
    @Nonnull
    protected ConfiguredSpellVehicle<?> getSpellVehicleComponent() {
        // Construct a new spell vehicle from the saved type index and populate it with any cached properties
        int index = this.getSpellVehicleTypeIndex();
        List<SpellVehicleType<?>> types = SpellManager.getVehicleTypes(this.player);
        if (index < 0 || index >= types.size()) {
            return new ConfiguredSpellVehicle<>(EmptySpellVehicle.INSTANCE);
        }
        AbstractSpellVehicle<?> baseVehicle = types.get(index).instanceSupplier().get();
        Map<SpellProperty, Integer> properties = new HashMap<>();
        this.spellPropertyCache.get(SpellComponent.VEHICLE).forEach((prop, val) -> {
            if (baseVehicle.getProperties().contains(prop)) {
                properties.put(prop, val);
            }
        });
        return new ConfiguredSpellVehicle<>(baseVehicle, properties);
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
    
    protected ConfiguredSpellPayload<?> getSpellPayloadComponent() {
        // Construct a new spell payload from the saved type index and populate it with any cached properties
        int index = this.getSpellPayloadTypeIndex();
        List<SpellPayloadType<?>> types = SpellManager.getPayloadTypes(this.player);
        if (index < 0 || index >= types.size()) {
            return new ConfiguredSpellPayload<>(EmptySpellPayload.INSTANCE);
        }
        AbstractSpellPayload<?> basePayload = types.get(index).instanceSupplier().get();
        Map<SpellProperty, Integer> properties = new HashMap<>();
        this.spellPropertyCache.get(SpellComponent.PAYLOAD).forEach((prop, val) -> {
            if (basePayload.getProperties().contains(prop)) {
                properties.put(prop, val);
            }
        });
        return new ConfiguredSpellPayload<>(basePayload, properties);
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
    
    private Optional<ConfiguredSpellMod<?>> getSpellModComponentInner(SpellComponent componentType, int index) {
        // Construct a new spell mod from the saved type index and populate it with any cached properties
        List<SpellModType<?>> types = SpellManager.getModTypes(this.player);
        if (index < 0 || index >= types.size()) {
            return Optional.empty();
        }
        AbstractSpellMod<?> baseMod = types.get(index).instanceSupplier().get();
        Map<SpellProperty, Integer> properties = new HashMap<>();
        this.spellPropertyCache.get(componentType).forEach((prop, val) -> {
            if (baseMod.getProperties().contains(prop)) {
                properties.put(prop, val);
            }
        });
        return Optional.of(new ConfiguredSpellMod<>(baseMod, properties));
    }
    
    protected Optional<ConfiguredSpellMod<?>> getSpellPrimaryModComponent() {
        return this.getSpellModComponentInner(SpellComponent.PRIMARY_MOD, this.getSpellPrimaryModTypeIndex());
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
    
    protected Optional<ConfiguredSpellMod<?>> getSpellSecondaryModComponent() {
        return this.getSpellModComponentInner(SpellComponent.SECONDARY_MOD, this.getSpellSecondaryModTypeIndex());
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
    
    public void setSpellPropertyValue(SpellComponent component, SpellProperty property, int value) {
        // Set and cache the changed value, then trigger a regeneration of the output item
        if (component != null && property != null) {
            this.spellPropertyCache.get(component).put(property, value);
            this.spellPackageCache = null;
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
                if (recipe.matches(this.scrollInv.asCraftInput(), world) && this.wandContainsEnoughMana(spe) && this.getSpellPackage().isValid()) {
                    stack = recipe.assemble(this.scrollInv.asCraftInput(), world.registryAccess());
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
        return wand.containsRealMana(stack, player, this.getManaCosts(), player.registryAccess());
    }
    
    public ItemStack getWand() {
        return this.wandInv.getItem(0);
    }
}
