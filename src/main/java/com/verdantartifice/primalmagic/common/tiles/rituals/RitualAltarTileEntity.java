package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagic.common.containers.FakeContainer;
import com.verdantartifice.primalmagic.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.rituals.IRitualProp;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a ritual altar tile entity.  Provides the core functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock}
 */
public class RitualAltarTileEntity extends TileInventoryPM implements ITickableTileEntity, IInteractWithWand {
    protected boolean active = false;
    protected int activeCount = 0;
    protected UUID activePlayerId = null;
    protected PlayerEntity activePlayerCache = null;
    protected ResourceLocation activeRecipeId = null;
    
    protected boolean scanDirty = false;
    protected Set<BlockPos> saltPositions = new HashSet<>();
    protected List<BlockPos> pedestalPositions = new ArrayList<>();
    protected List<BlockPos> propPositions = new ArrayList<>();
    
    public RitualAltarTileEntity() {
        super(TileEntityTypesPM.RITUAL_ALTAR.get(), 1);
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public int getActiveCount() {
        return this.activeCount;
    }
    
    @Nullable
    public PlayerEntity getActivePlayer() {
        if (this.activePlayerCache == null && this.activePlayerId != null && this.world instanceof ServerWorld) {
            // If the active player cache is empty, find the entity matching the caster's unique ID
            ServerPlayerEntity player = ((ServerWorld)this.world).getServer().getPlayerList().getPlayerByUUID(this.activePlayerId);
            if (player != null) {
                this.activePlayerCache = player;
            } else {
                this.activePlayerId = null;
            }
        }
        return this.activePlayerCache;
    }
    
    public void setActivePlayer(@Nullable PlayerEntity player) {
        if (player == null) {
            this.activePlayerCache = null;
            this.activePlayerId = null;
        } else {
            this.activePlayerCache = player;
            this.activePlayerId = player.getUniqueID();
        }
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the altar's stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.active = compound.getBoolean("Active");
        this.activeCount = compound.getInt("ActiveCount");
        
        this.activePlayerCache = null;
        if (compound.contains("ActivePlayer", Constants.NBT.TAG_COMPOUND)) {
            this.activePlayerId = NBTUtil.readUniqueId(compound.getCompound("ActivePlayer"));
        } else {
            this.activePlayerId = null;
        }
        
        this.activeRecipeId = compound.contains("ActiveRecipeId", Constants.NBT.TAG_STRING) ? 
                new ResourceLocation(compound.getString("ActiveRecipeId")) : 
                null;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Active", this.active);
        compound.putInt("ActiveCount", this.activeCount);
        if (this.activePlayerId != null) {
            compound.put("ActivePlayer", NBTUtil.writeUniqueId(this.activePlayerId));
        }
        if (this.activeRecipeId != null) {
            compound.putString("ActiveRecipeId", this.activeRecipeId.toString());
        }
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (this.active) {
            this.activeCount++;
        }
        if (this.scanDirty && !this.world.isRemote) {
            this.scanSurroundings();
            this.scanDirty = false;
        }
        if (!this.world.isRemote && this.active) {
            if (this.activeCount >= 100) {
                if (this.activeRecipeId != null) {
                    Optional<? extends IRecipe<?>> recipeOpt = this.world.getServer().getRecipeManager().getRecipe(this.activeRecipeId);
                    if (recipeOpt.isPresent()) {
                        this.setInventorySlotContents(0, recipeOpt.get().getRecipeOutput().copy());
                    }
                    this.activeRecipeId = null;
                }
                if (this.getActivePlayer() != null) {
                    this.getActivePlayer().sendStatusMessage(new StringTextComponent("Ritual complete!"), false);
                }
                this.active = false;
                this.activeCount = 0;
                this.setActivePlayer(null);
                this.markDirty();
                this.syncTile(false);
            }
        }
    }

    @Override
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction) {
        if (!this.world.isRemote && wandStack.getItem() instanceof IWand) {
            if (this.active) {
                this.active = false;
                this.activeCount = 0;
                player.sendStatusMessage(new StringTextComponent("Ritual canceled"), false);
                this.setActivePlayer(null);
                this.activeRecipeId = null;
            } else if (this.startCraft(wandStack, player)) {
                this.active = true;
                this.activeCount = 0;
                player.sendStatusMessage(new StringTextComponent("Ritual started!"), false);
                this.setActivePlayer(player);
            } else {
                player.sendStatusMessage(new StringTextComponent("No valid ritual recipe found"), false);
            }
            this.markDirty();
            this.syncTile(false);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, PlayerEntity player, int count) {
        // Do nothing; ritual altars don't support wand channeling
    }
    
    protected boolean startCraft(ItemStack wandStack, PlayerEntity player) {
        // Scan altar surroundings for pedestals and props immediately when starting a ritual
        this.scanSurroundings();
        
        // Determine offerings
        List<ItemStack> offerings = new ArrayList<>();
        for (BlockPos offeringPos : this.pedestalPositions) {
            TileEntity tile = this.world.getTileEntity(offeringPos);
            if (tile instanceof OfferingPedestalTileEntity) {
                OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                ItemStack stack = pedestalTile.getStackInSlot(0);
                if (stack != null && !stack.isEmpty()) {
                    offerings.add(stack);
                }
            }
        }
        
        // Determine recipe that corresponds to offerings
        CraftingInventory inv = new CraftingInventory(new FakeContainer(), offerings.size(), 1);
        int offeringIndex = 0;
        for (ItemStack offering : offerings) {
            PrimalMagic.LOGGER.debug("Found ritual offering: {}", offering.getItem().getRegistryName().toString());
            inv.setInventorySlotContents(offeringIndex++, offering);
        }
        Optional<IRitualRecipe> recipeOpt = this.world.getServer().getRecipeManager().getRecipe(RecipeTypesPM.RITUAL, inv, this.world);
        if (recipeOpt.isPresent()) {
            // Determine if the player has the research and mana to start this recipe
            IRitualRecipe recipe = recipeOpt.get();
            if (this.canUseRitualRecipe(wandStack, player, recipe)) {
                this.activeRecipeId = recipe.getId();
                PrimalMagic.LOGGER.debug("Found recipe {}", this.activeRecipeId.toString());
                return true;
            } else {
                PrimalMagic.LOGGER.debug("Cannot use found recipe {}", recipe.getId().toString());
                return false;
            }
        } else {
            PrimalMagic.LOGGER.debug("No matching ritual recipe found");
            return false;
        }
    }
    
    protected boolean canUseRitualRecipe(ItemStack wandStack, PlayerEntity player, IRitualRecipe recipe) {
        // Players must know the correct research and the wand must have enough mana in order to use the recipe
        return (recipe.getRequiredResearch() == null || recipe.getRequiredResearch().isKnownByStrict(player)) &&
                (recipe.getManaCosts().isEmpty() || this.consumeMana(wandStack, player, recipe));
    }
    
    protected boolean consumeMana(ItemStack wandStack, PlayerEntity player, IRitualRecipe recipe) {
        if (wandStack == null || wandStack.isEmpty() || !(wandStack.getItem() instanceof IWand)) {
            return false;
        }
        IWand wand = (IWand)wandStack.getItem();
        return wand.consumeRealMana(wandStack, player, recipe.getManaCosts());
    }
    
    protected void scanSurroundings() {
        this.saltPositions.clear();
        this.pedestalPositions.clear();
        this.propPositions.clear();
        
        Set<BlockPos> scanHistory = new HashSet<BlockPos>();
        scanHistory.add(this.pos);
        
        Queue<BlockPos> toScan = new LinkedList<BlockPos>();
        toScan.offer(this.pos.north());
        toScan.offer(this.pos.east());
        toScan.offer(this.pos.south());
        toScan.offer(this.pos.west());
        
        while (!toScan.isEmpty()) {
            BlockPos pos = toScan.poll();
            this.scanPosition(pos, toScan, scanHistory);
        }
    }
    
    protected void scanPosition(BlockPos pos, Queue<BlockPos> toScan, Set<BlockPos> history) {
        if (history.contains(pos)) {
            return;
        } else {
            history.add(pos);
        }
        
        BlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();
        
        // Determine if the scan position is within the theoretical range of this altar
        int dist = Math.abs(this.pos.getX() - pos.getX()) + Math.abs(this.pos.getZ() - pos.getZ());
        if (dist > ((RitualAltarBlock)this.getBlockState().getBlock()).getMaxSaltPower()) {
            return;
        }
        
        if (block == BlocksPM.SALT_TRAIL.get()) {
            // Keep scanning along the salt lines
            this.saltPositions.add(pos);
            for (Map.Entry<Direction, EnumProperty<SaltSide>> entry : SaltTrailBlock.FACING_PROPERTY_MAP.entrySet()) {
                BlockPos nextPos = pos.offset(entry.getKey());
                SaltSide saltSide = state.get(entry.getValue());
                if (saltSide == SaltSide.UP) {
                    toScan.add(nextPos.up());
                } else if (saltSide == SaltSide.SIDE) {
                    // The adjacent salt trail could be at the same height or one below, so check both
                    toScan.add(nextPos);
                    toScan.add(nextPos.down());
                }
            }
        } else if (block == BlocksPM.OFFERING_PEDESTAL.get()) {
            // Add this position to the offering pedestal collection
            OfferingPedestalBlock pedestalBlock = (OfferingPedestalBlock)block;
            if (pedestalBlock.isBlockSaltPowered(this.world, pos)) {
                this.pedestalPositions.add(pos);
            }
        } else if (block instanceof IRitualProp) {
            // Add this position to the prop collection
            IRitualProp propBlock = (IRitualProp)block;
            if (propBlock.isBlockSaltPowered(this.world, pos)) {
                this.propPositions.add(pos);
            }
        }
    }
}
