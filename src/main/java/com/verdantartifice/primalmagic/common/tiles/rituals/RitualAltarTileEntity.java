package com.verdantartifice.primalmagic.common.tiles.rituals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagic.common.containers.FakeContainer;
import com.verdantartifice.primalmagic.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagic.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagic.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.OfferingChannelPacket;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagic.common.rituals.IRitualProp;
import com.verdantartifice.primalmagic.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.rituals.Mishap;
import com.verdantartifice.primalmagic.common.rituals.RitualStep;
import com.verdantartifice.primalmagic.common.rituals.RitualStepType;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagic.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
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
    protected static final float MIN_STABILITY = -100.0F;
    protected static final float MAX_STABILITY = 25.0F;
    
    protected final WeightedRandomBag<Mishap> mishaps;
    
    protected boolean active = false;
    protected boolean currentStepComplete = false;
    protected int activeCount = 0;
    protected int nextCheckCount = 0;
    protected float stability = 0.0F;
    protected UUID activePlayerId = null;
    protected PlayerEntity activePlayerCache = null;
    protected ResourceLocation activeRecipeId = null;
    protected RitualStep currentStep = null;
    protected Queue<RitualStep> remainingSteps = new LinkedList<>();
    protected BlockPos awaitedPropPos = null;
    protected BlockPos channeledOfferingPos = null;
    
    protected boolean scanDirty = false;
    protected boolean skipWarningMessage = false;
    protected float symmetryDelta = 0.0F;
    protected List<BlockPos> saltPositions = new ArrayList<>();
    protected List<BlockPos> pedestalPositions = new ArrayList<>();
    protected List<BlockPos> propPositions = new ArrayList<>();
    protected Map<Block, Integer> blockCounts = new HashMap<>();
    
    public RitualAltarTileEntity() {
        super(TileEntityTypesPM.RITUAL_ALTAR.get(), 1);
        this.mishaps = Util.make(new WeightedRandomBag<>(), bag -> {
            bag.add(new Mishap(this::mishapOffering, false, 0.0F), 6.0D);
            bag.add(new Mishap(this::mishapSalt, false, 10.0F), 3.0D);
            bag.add(new Mishap(this::mishapDamage, false, 25.0F), 3.0D);
            bag.add(new Mishap(this::mishapSalt, true, 35.0F), 2.0D);
            bag.add(new Mishap(this::mishapDamage, true, 45.0F), 2.0D);
            bag.add(new Mishap(this::mishapOffering, true, 50.0F), 1.0D);
            bag.add(new Mishap(this::mishapDetonate, false, 75.0F), 2.0D);
            bag.add(new Mishap(this::mishapDetonate, true, 90.0F), 1.0D);
        });
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
    
    @Nullable
    protected IRitualRecipe getActiveRecipe() {
        if (this.activeRecipeId != null) {
            Optional<? extends IRecipe<?>> recipeOpt = this.world.getServer().getRecipeManager().getRecipe(this.activeRecipeId);
            if (recipeOpt.isPresent() && recipeOpt.get() instanceof IRitualRecipe) {
                return (IRitualRecipe)recipeOpt.get();
            }
        }
        return null;
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices() {
        // Sync the altar's stack for client rendering use
        return ImmutableSet.of(Integer.valueOf(0));
    }
    
    public Color getOrbColor() {
        float hue, saturation;
        if (stability >= 0.0F) {
            hue = 120.0F / 360.0F;  // Green
            saturation = MathHelper.clamp(this.stability / MAX_STABILITY, 0.0F, 1.0F);
        } else {
            hue = 0.0F / 360.0F;    // Red
            saturation = MathHelper.clamp(this.stability / MIN_STABILITY, 0.0F, 1.0F);
        }
        return Color.getHSBColor(hue, saturation, 1.0F);
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.active = compound.getBoolean("Active");
        this.currentStepComplete = compound.getBoolean("CurrentStepComplete");
        this.activeCount = compound.getInt("ActiveCount");
        this.nextCheckCount = compound.getInt("NextCheckCount");
        this.stability = MathHelper.clamp(compound.getFloat("Stability"), MIN_STABILITY, MAX_STABILITY);
        
        this.activePlayerCache = null;
        if (compound.contains("ActivePlayer", Constants.NBT.TAG_COMPOUND)) {
            this.activePlayerId = NBTUtil.readUniqueId(compound.getCompound("ActivePlayer"));
        } else {
            this.activePlayerId = null;
        }
        
        this.activeRecipeId = compound.contains("ActiveRecipeId", Constants.NBT.TAG_STRING) ? 
                new ResourceLocation(compound.getString("ActiveRecipeId")) : 
                null;
        
        this.currentStep = null;
        if (compound.contains("CurrentStep", Constants.NBT.TAG_COMPOUND)) {
            this.currentStep = new RitualStep();
            this.currentStep.deserializeNBT(compound.getCompound("CurrentStep"));
        }
                
        this.remainingSteps.clear();
        if (compound.contains("RemainingSteps", Constants.NBT.TAG_LIST)) {
            ListNBT stepList = compound.getList("RemainingSteps", Constants.NBT.TAG_COMPOUND);
            for (int index = 0; index < stepList.size(); index++) {
                RitualStep step = new RitualStep();
                step.deserializeNBT(stepList.getCompound(index));
                this.remainingSteps.offer(step);
            }
        }
        
        this.awaitedPropPos = compound.contains("AwaitedPropPos", Constants.NBT.TAG_LONG) ?
                BlockPos.fromLong(compound.getLong("AwaitedPropPos")) :
                null;
                
        this.channeledOfferingPos = compound.contains("ChanneledOfferingPos", Constants.NBT.TAG_LONG) ?
                BlockPos.fromLong(compound.getLong("ChanneledOfferingPos")) :
                null;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Active", this.active);
        compound.putBoolean("CurrentStepComplete", this.currentStepComplete);
        compound.putInt("ActiveCount", this.activeCount);
        compound.putInt("NextCheckCount", this.nextCheckCount);
        compound.putFloat("Stability", this.stability);
        if (this.activePlayerId != null) {
            compound.put("ActivePlayer", NBTUtil.writeUniqueId(this.activePlayerId));
        }
        if (this.activeRecipeId != null) {
            compound.putString("ActiveRecipeId", this.activeRecipeId.toString());
        }
        if (this.currentStep != null) {
            compound.put("CurrentStep", this.currentStep.serializeNBT());
        }
        if (this.remainingSteps != null && !this.remainingSteps.isEmpty()) {
            ListNBT stepList = new ListNBT();
            for (RitualStep step : this.remainingSteps) {
                stepList.add(step.serializeNBT());
            }
            compound.put("RemainingSteps", stepList);
        }
        if (this.awaitedPropPos != null) {
            compound.putLong("AwaitedPropPos", this.awaitedPropPos.toLong());
        }
        if (this.channeledOfferingPos != null) {
            compound.putLong("ChanneledOfferingPos", this.channeledOfferingPos.toLong());
        }
        return super.write(compound);
    }
    
    protected void reset() {
        // If there's a prop being waited on, close it out
        if (this.awaitedPropPos != null) {
            BlockState state = this.world.getBlockState(this.awaitedPropPos);
            Block block = state.getBlock();
            if (block instanceof IRitualProp) {
                ((IRitualProp)block).closeProp(state, this.world, this.awaitedPropPos);
            }
        }
        
        // Reset the altar's tile entity back to its default state
        this.active = false;
        this.currentStepComplete = false;
        this.activeCount = 0;
        this.nextCheckCount = 0;
        this.stability = 0.0F;
        this.setActivePlayer(null);
        this.activeRecipeId = null;
        this.currentStep = null;
        this.remainingSteps.clear();
        this.awaitedPropPos = null;
        this.channeledOfferingPos = null;

        this.scanDirty = false;
        this.skipWarningMessage = false;
        this.symmetryDelta = 0.0F;
        this.pedestalPositions.clear();
        this.propPositions.clear();
        this.saltPositions.clear();
        this.blockCounts.clear();

        this.markDirty();
        this.syncTile(false);
    }

    @Override
    public void tick() {
        if (this.active) {
            this.activeCount++;
        }
        if (this.active && this.activeCount % 10 == 0 && !this.world.isRemote) {
            this.scanDirty = true;
        }
        if (this.scanDirty && !this.world.isRemote) {
            this.scanSurroundings();
            this.scanDirty = false;
        }
        if (!this.world.isRemote && this.active) {
            if (this.currentStep == null || this.currentStepComplete) {
                if (this.remainingSteps.isEmpty()) {
                    // If there are no steps remaining in the ritual, finish it up
                    if (this.activeCount >= this.nextCheckCount) {
                        this.finishCraft();
                        this.markDirty();
                        this.syncTile(false);
                    }
                    return;
                } else {
                    // Pull the next step from the queue and start it
                    this.currentStep = this.remainingSteps.poll();
                    this.currentStepComplete = false;
                    this.skipWarningMessage = false;
                }
            }
            float delta = this.calculateStabilityDelta();
            this.addStability(delta);
            if (this.currentStep != null) {
                if (!this.doStep(this.currentStep)) {
                    // Add extra instability if the ritual step was not productive (e.g. waiting for prop activation)
                    this.addStability(Math.min(0.0F, delta));
                }
            }
            if (this.activeCount % 10 == 0 && this.stability < 0.0F && this.world.rand.nextInt(1500) < Math.abs(this.stability)) {
                this.doMishap();
            }
            if (this.activeCount % 10 == 0) {
                PrimalMagic.LOGGER.debug("Current stability: {}", this.stability);
            }
            this.markDirty();
            this.syncTile(false);
        }
    }

    @Override
    public ActionResultType onWandRightClick(ItemStack wandStack, World world, PlayerEntity player, BlockPos pos, Direction direction) {
        if (!this.world.isRemote && wandStack.getItem() instanceof IWand) {
            if (this.active) {
                player.sendStatusMessage(new StringTextComponent("Ritual canceled"), false);
                this.reset();
            } else if (this.startCraft(wandStack, player)) {
                this.active = true;
                this.activeCount = 0;
                player.sendStatusMessage(new StringTextComponent("Ritual started!"), false);
                this.setActivePlayer(player);
                this.markDirty();
                this.syncTile(false);
            } else {
                player.sendStatusMessage(new StringTextComponent("No valid ritual recipe found"), false);
                this.reset();
            }
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
            if (this.canUseRitualRecipe(wandStack, player, recipe) && this.generateRitualSteps(recipe)) {
                this.activeRecipeId = recipe.getId();
                this.currentStep = null;
                this.currentStepComplete = false;
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
    
    protected boolean generateRitualSteps(@Nonnull IRitualRecipe recipe) {
        // Add steps for the recipe offerings and props
        LinkedList<RitualStep> newSteps = new LinkedList<>();
        for (int index = 0; index < recipe.getIngredients().size(); index++) {
            newSteps.add(new RitualStep(RitualStepType.OFFERING, index));
        }
        for (int index = 0; index < recipe.getProps().size(); index++) {
            newSteps.add(new RitualStep(RitualStepType.PROP, index));
        }
        
        // Randomize and save the generated steps
        Collections.shuffle(newSteps, this.world.rand);
        this.remainingSteps = newSteps;
        
        return true;
    }
    
    protected void finishCraft() {
        IRitualRecipe recipe = this.getActiveRecipe();
        if (recipe != null) {
            this.setInventorySlotContents(0, recipe.getRecipeOutput().copy());
        }
        if (this.getActivePlayer() != null) {
            this.getActivePlayer().sendStatusMessage(new StringTextComponent("Ritual complete!"), false);
        }
        this.spawnSuccessParticles();
        this.reset();
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
        this.blockCounts.clear();
        
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
        this.symmetryDelta = this.calculateSymmetryDelta();
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
    
    protected float calculateSymmetryDelta() {
        float delta = 0.0F;
        
        Set<BlockPos> toScan = new HashSet<>();
        toScan.addAll(this.pedestalPositions);
        toScan.addAll(this.propPositions);
        
        for (BlockPos scanPos : toScan) {
            int dx = this.pos.getX() - scanPos.getX();
            int dz = this.pos.getZ() - scanPos.getZ();
            BlockPos symPos = new BlockPos(this.pos.getX() + dx, scanPos.getY(), this.pos.getZ() + dz);
            Block block = this.world.getBlockState(scanPos).getBlock();
            
            if (block instanceof IRitualStabilizer) {
                IRitualStabilizer stabilizer = (IRitualStabilizer)block;
                if (stabilizer.hasSymmetryPenalty(this.world, scanPos, symPos)) {
                    delta -= stabilizer.getSymmetryPenalty(this.world, scanPos);
                } else {
                    delta += this.calculateDiminishingStabilityBonus(block, stabilizer.getStabilityBonus(this.world, scanPos));
                }
            }
        }
        
        return delta;
    }
    
    protected float calculateDiminishingStabilityBonus(Block block, float baseValue) {
        // Calculate the block counts as we go, rather than doing it during scan, so that the first
        // instance of any blocks gets its full value and diminishes, rather than having every block
        // of that type have a fully diminished value.
        float retVal = baseValue;
        int count = this.blockCounts.getOrDefault(block, Integer.valueOf(0)).intValue();
        if (count > 0) {
            retVal = baseValue * (float)Math.pow(0.75D, count);
        }
        this.blockCounts.put(block, Integer.valueOf(count + 1));
        return retVal;
    }

    protected boolean doStep(@Nonnull RitualStep step) {
        IRitualRecipe recipe = this.getActiveRecipe();
        if (recipe == null) {
            PrimalMagic.LOGGER.debug("No recipe found when trying to do ritual step");
            return false;
        }
        
        if (step.getType() == RitualStepType.OFFERING) {
            return this.doOfferingStep(recipe, step.getIndex());
        } else if (step.getType() == RitualStepType.PROP) {
            return this.doPropStep(recipe, step.getIndex());
        } else {
            PrimalMagic.LOGGER.debug("Invalid ritual step type {}", step.getType());
            return false;
        }
    }
    
    protected boolean doOfferingStep(IRitualRecipe recipe, int offeringIndex) {
        Ingredient requiredOffering = recipe.getIngredients().get(offeringIndex);
        if (this.activeCount >= this.nextCheckCount && this.channeledOfferingPos == null) {
            for (BlockPos pedestalPos : this.pedestalPositions) {
                TileEntity tile = this.world.getTileEntity(pedestalPos);
                Block block = this.world.getBlockState(pedestalPos).getBlock();
                if (tile instanceof OfferingPedestalTileEntity && block instanceof ISaltPowered) {
                    OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                    ISaltPowered saltBlock = (ISaltPowered)block;
                    if (requiredOffering.test(pedestalTile.getStackInSlot(0)) && saltBlock.isBlockSaltPowered(this.world, pedestalPos)) {
                        this.channeledOfferingPos = pedestalPos;
                        ItemStack found = pedestalTile.getStackInSlot(0);
                        PrimalMagic.LOGGER.debug("Found match {} for ingredient {} at {}", found.getItem().getRegistryName().toString(), offeringIndex, pedestalPos.toString());
                        this.nextCheckCount = this.activeCount + 60;
                        return true;
                    }
                }
            }
            PrimalMagic.LOGGER.debug("No match found for current ingredient {}", offeringIndex);
            if (!this.skipWarningMessage && this.getActivePlayer() != null) {
                this.getActivePlayer().sendStatusMessage(new TranslationTextComponent("primalmagic.ritual.warning.missing_offering"), false);
                this.skipWarningMessage = true;
            }
            this.nextCheckCount = this.activeCount + 20;
        }
        if (this.channeledOfferingPos != null) {
            TileEntity tile = this.world.getTileEntity(this.channeledOfferingPos);
            Block block = this.world.getBlockState(this.channeledOfferingPos).getBlock();
            if ( tile instanceof OfferingPedestalTileEntity &&
                 block instanceof ISaltPowered &&
                 requiredOffering.test(((OfferingPedestalTileEntity)tile).getStackInSlot(0)) &&
                 ((ISaltPowered)block).isBlockSaltPowered(this.world, this.channeledOfferingPos) ) {
                OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                if (this.activeCount >= this.nextCheckCount) {
                    pedestalTile.removeStackFromSlot(0);
                    this.currentStepComplete = true;
                    this.channeledOfferingPos = null;
                } else {
                    this.spawnOfferingParticles(this.channeledOfferingPos, pedestalTile.getStackInSlot(0));
                }
                return true;
            } else {
                this.channeledOfferingPos = null;
                PrimalMagic.LOGGER.debug("Lost ingredient {} during channel!", offeringIndex);
                if (this.getActivePlayer() != null) {
                    this.getActivePlayer().sendStatusMessage(new TranslationTextComponent("primalmagic.ritual.warning.channel_interrupt"), false);
                    this.skipWarningMessage = true;
                }
                this.addStability(MathHelper.clamp(50 * Math.min(0.0F, this.calculateStabilityDelta()), -25.0F, -1.0F));
            }
        }
        return false;
    }
    
    protected boolean doPropStep(IRitualRecipe recipe, int propIndex) {
        BlockIngredient requiredProp = recipe.getProps().get(propIndex);
        if (this.activeCount >= this.nextCheckCount) {
            if (this.awaitedPropPos == null) {
                for (BlockPos propPos : this.propPositions) {
                    BlockState propState = this.world.getBlockState(propPos);
                    Block block = propState.getBlock();
                    if (block instanceof IRitualProp && requiredProp.test(block)) {
                        IRitualProp propBlock = (IRitualProp)block;
                        if (!propBlock.isPropActivated(propState, this.world, propPos) && propBlock.isBlockSaltPowered(this.world, propPos)) {
                            PrimalMagic.LOGGER.debug("Found match {} for prop {} at {}", block.getRegistryName().toString(), propIndex, propPos.toString());
                            propBlock.openProp(propState, this.world, propPos, this.getActivePlayer(), this.pos);
                            this.awaitedPropPos = propPos;
                            this.nextCheckCount = this.activeCount + 20;
                            return true;
                        }
                    }
                }
                PrimalMagic.LOGGER.debug("No match found for current prop {}", propIndex);
                if (!this.skipWarningMessage && this.getActivePlayer() != null) {
                    this.getActivePlayer().sendStatusMessage(new TranslationTextComponent("primalmagic.ritual.warning.missing_prop"), false);
                    this.skipWarningMessage = true;
                }
            } else {
                BlockState propState = this.world.getBlockState(this.awaitedPropPos);
                Block block = propState.getBlock();
                if ( block instanceof IRitualProp && 
                     requiredProp.test(block) &&
                     ((IRitualProp)block).isBlockSaltPowered(this.world, this.awaitedPropPos) ) {
                    PrimalMagic.LOGGER.debug("Awaiting prop activation");
                } else {
                    PrimalMagic.LOGGER.debug("Lost prop {} while awaiting activation!", propIndex);
                    if (this.getActivePlayer() != null) {
                        this.getActivePlayer().sendStatusMessage(new TranslationTextComponent("primalmagic.ritual.warning.prop_interrupt"), false);
                        this.skipWarningMessage = true;
                    }
                    if (block instanceof IRitualProp) {
                        ((IRitualProp)block).closeProp(propState, this.world, this.awaitedPropPos);
                    }
                    this.awaitedPropPos = null;
                    this.addStability(MathHelper.clamp(50 * Math.min(0.0F, this.calculateStabilityDelta()), -25.0F, -1.0F));
                }
            }
            this.nextCheckCount = this.activeCount + 20;
        }
        return false;
    }
    
    public void onPropActivation(BlockPos propPos) {
        if (this.awaitedPropPos != null && this.awaitedPropPos.equals(propPos)) {
            PrimalMagic.LOGGER.debug("Received awaited prop activation at {}", propPos);
            BlockState propState = this.world.getBlockState(propPos);
            Block block = propState.getBlock();
            if (block instanceof IRitualProp) {
                IRitualProp propBlock = (IRitualProp)block;
                propBlock.closeProp(propState, this.world, propPos);
                this.addStability(propBlock.getUsageStabilityBonus());
            }
            this.currentStepComplete = true;
            this.nextCheckCount = this.activeCount;
            this.awaitedPropPos = null;
            this.markDirty();
            this.syncTile(false);
        } else {
            PrimalMagic.LOGGER.debug("Received unexpected prop activation at {}", propPos);
        }
    }
    
    protected void addStability(float delta) {
        this.stability = MathHelper.clamp(this.stability + delta, MIN_STABILITY, MAX_STABILITY);
    }

    protected float calculateStabilityDelta() {
        float delta = 0.0F;
        
        // Deduct stability based on the active recipe
        IRitualRecipe recipe = this.getActiveRecipe();
        if (recipe != null) {
            delta -= (0.01F * recipe.getInstability());
        }
        
        // Deduct stability for each salt trail in excess of the safe amount
        Block block = this.getBlockState().getBlock();
        if (block instanceof RitualAltarBlock) {
            int safeSaltCount = ((RitualAltarBlock)block).getMaxSafeSalt();
            if (this.saltPositions.size() > safeSaltCount) {
                delta -= (0.001F * (this.saltPositions.size() - safeSaltCount));
            }
        }
        
        // Add or subtract stability based on pedestal/prop symmetry
        delta += this.symmetryDelta;
        
        return delta;
    }
    
    protected void spawnOfferingParticles(BlockPos startPos, ItemStack stack) {
        if (!this.world.isRemote) {
            double sx = startPos.getX() + 0.4D + (this.world.rand.nextDouble() * 0.2D);
            double sy = startPos.getY() + 1.4D + (this.world.rand.nextDouble() * 0.2D);
            double sz = startPos.getZ() + 0.4D + (this.world.rand.nextDouble() * 0.2D);
            PacketHandler.sendToAllAround(new OfferingChannelPacket(sx, sy, sz, this.pos.up(2), stack), this.world.dimension.getType(), startPos, 32.0D);
        }
    }
    
    protected void spawnSuccessParticles() {
        if (this.world instanceof ServerWorld) {
            ((ServerWorld)this.world).spawnParticle(
                    ParticleTypes.HAPPY_VILLAGER, 
                    this.pos.getX() + 0.5D, 
                    this.pos.getY() + 1.2D, 
                    this.pos.getZ() + 0.5D, 
                    15, 
                    0, 
                    0, 
                    0, 
                    0.1D);
        }
    }
    
    protected void doMishap() {
        int attempts = 0;
        while (attempts++ < 25) {
            Mishap mishap = this.mishaps.getRandom(this.world.rand);
            if (mishap != null && mishap.execute(this.stability)) {
                this.addStability(5.0F + (5.0F * this.world.rand.nextFloat()));
                break;
            }
        }
    }
    
    protected void doMishapEffects(BlockPos target, boolean playSound) {
        if (!this.world.isRemote) {
            BlockPos source = this.pos.up(2);
            PacketHandler.sendToAllAround(new SpellBoltPacket(source, target, this.getOrbColor().getRGB()), this.world.dimension.getType(), source, 32.0D);
            if (playSound) {
                this.world.playSound(null, source, SoundsPM.ELECTRIC.get(), SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
            }
        }
    }
    
    protected void mishapOffering(boolean destroy) {
        int attempts = 0;
        while (attempts++ < 25 && !this.pedestalPositions.isEmpty()) {
            BlockPos pedestalPos = this.pedestalPositions.get(this.world.rand.nextInt(this.pedestalPositions.size()));
            TileEntity tile = this.world.getTileEntity(pedestalPos);
            if (tile instanceof OfferingPedestalTileEntity) {
                OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                if (!pedestalTile.getStackInSlot(0).isEmpty()) {
                    if (destroy) {
                        PrimalMagic.LOGGER.debug("Destroying offering at {}", pedestalPos);
                        pedestalTile.setInventorySlotContents(0, ItemStack.EMPTY);
                    } else {
                        PrimalMagic.LOGGER.debug("Dislodging offering at {}", pedestalPos);
                        InventoryHelper.dropInventoryItems(this.world, pedestalPos, pedestalTile);
                    }
                    pedestalTile.markDirty();
                    pedestalTile.syncTile(false);
                    this.doMishapEffects(pedestalPos, true);
                    break;
                }
            }
        }
    }
    
    protected void mishapSalt(boolean multiple) {
        int breakCount = multiple ? 2 + this.world.rand.nextInt(4) : 1;
        for (int breakIndex = 0; breakIndex < breakCount; breakIndex++) {
            int attempts = 0;
            while (attempts++ < 25 && !this.saltPositions.isEmpty()) {
                BlockPos saltPos = this.saltPositions.get(this.world.rand.nextInt(this.saltPositions.size()));
                Block block = this.world.getBlockState(saltPos).getBlock();
                if (block == BlocksPM.SALT_TRAIL.get()) {
                    PrimalMagic.LOGGER.debug("Breaking salt trail at {}", saltPos);
                    InventoryHelper.spawnItemStack(this.world, saltPos.getX() + 0.5D, saltPos.getY() + 0.5D, saltPos.getZ() + 0.5D, new ItemStack(ItemsPM.REFINED_SALT.get()));
                    this.world.removeBlock(saltPos, false);
                    this.doMishapEffects(saltPos, breakIndex == 0); // Only play sounds once
                    break;
                }
            }
        }
        this.scanDirty = true;
    }
    
    protected void mishapDamage(boolean allTargets) {
        List<LivingEntity> targets = EntityUtils.getEntitiesInRange(this.world, this.pos, null, LivingEntity.class, 10.0D);
        if (targets != null && !targets.isEmpty()) {
            for (int index = 0; index < targets.size(); index++) {
                LivingEntity target = targets.get(index);
                PrimalMagic.LOGGER.debug("Damaging target {}", target.getDisplayName().getString());
                int damage = 5 + MathHelper.floor(Math.sqrt(Math.abs(Math.min(0.0F, this.stability))) / 2.0D);
                target.attackEntityFrom(DamageSource.MAGIC, damage);
                this.doMishapEffects(target.getPosition(), index == 0); // Only play sounds once
                if (!allTargets) {
                    break;
                }
            }
        }
    }
    
    protected void mishapDetonate(boolean central) {
        BlockPos target = null;
        if (central) {
            target = this.pos;
        } else {
            int attempts = 0;
            while (attempts++ < 25 && !this.pedestalPositions.isEmpty()) {
                BlockPos pedestalPos = this.pedestalPositions.get(this.world.rand.nextInt(this.pedestalPositions.size()));
                TileEntity tile = this.world.getTileEntity(pedestalPos);
                if (tile instanceof OfferingPedestalTileEntity) {
                    OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                    if (!pedestalTile.getStackInSlot(0).isEmpty()) {
                        target = pedestalPos;
                        break;
                    }
                }
            }
            if (target == null && !this.pedestalPositions.isEmpty()) {
                // If we can't find a populated pedestal, just pick one at random
                target = this.pedestalPositions.get(this.world.rand.nextInt(this.pedestalPositions.size()));
            }
        }
        if (target != null) {
            PrimalMagic.LOGGER.debug("Detonating {} at {}", central ? "altar" : "pedestal", target);
            if (central && this.awaitedPropPos != null) {
                // If destroying the central altar, close out any waiting props
                BlockState state = this.world.getBlockState(this.awaitedPropPos);
                Block block = state.getBlock();
                if (block instanceof IRitualProp) {
                    ((IRitualProp)block).closeProp(state, this.world, this.awaitedPropPos);
                }
            }
            if (!central) {
                this.doMishapEffects(target, true);
                this.scanDirty = true;
            }
            float force = central ? 2.0F + this.world.rand.nextFloat() : 1.0F;
            this.world.createExplosion(null, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, force, Explosion.Mode.BREAK);
        }
    }
}
