package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.rituals.OfferingPedestalBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualAltarBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.SaltSide;
import com.verdantartifice.primalmagick.common.capabilities.ItemStackHandlerPM;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.FakeMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.OfferingChannelPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropTileEntity;
import com.verdantartifice.primalmagick.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.rituals.Mishap;
import com.verdantartifice.primalmagick.common.rituals.steps.AbstractRitualStep;
import com.verdantartifice.primalmagick.common.rituals.steps.OfferingRitualStep;
import com.verdantartifice.primalmagick.common.rituals.steps.PropRitualStep;
import com.verdantartifice.primalmagick.common.rituals.steps.UniversalRitualStep;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;
import com.verdantartifice.primalmagick.common.wands.IInteractWithWand;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Definition of a ritual altar tile entity.  Provides the core functionality for the corresponding
 * block.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.rituals.RitualAltarBlock}
 */
public class RitualAltarTileEntity extends AbstractTileSidedInventoryPM implements IInteractWithWand {
    protected static final int OUTPUT_INV_INDEX = 0;
    protected static final float MIN_STABILITY = -100.0F;
    protected static final float MAX_STABILITY = 25.0F;
    protected static final int RITUAL_SOUND_LENGTH = 58;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RNG = new Random();
    
    protected final WeightedRandomBag<Mishap> mishaps;
    
    protected int ticksExisted = 0;
    protected boolean active = false;
    protected boolean currentStepComplete = false;
    protected int activeCount = 0;
    protected int nextCheckCount = 0;
    protected float stability = 0.0F;
    protected UUID activePlayerId = null;
    protected ResourceLocation activeRecipeId = null;
    protected AbstractRitualStep<?> currentStep = null;
    protected LinkedList<AbstractRitualStep<?>> remainingSteps = new LinkedList<>();
    protected BlockPos awaitedPropPos = null;
    protected BlockPos channeledOfferingPos = null;
    
    protected boolean scanDirty = false;
    protected boolean skipWarningMessage = false;
    protected float symmetryDelta = 0.0F;
    protected List<BlockPos> saltPositions = new ArrayList<>();
    protected List<BlockPos> pedestalPositions = new ArrayList<>();
    protected List<BlockPos> propPositions = new ArrayList<>();
    protected Map<Block, Integer> blockCounts = new HashMap<>();
    
    public RitualAltarTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.RITUAL_ALTAR.get(), pos, state);
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
    public Player getActivePlayer() {
        if (this.activePlayerId != null && this.level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().getPlayerList().getPlayer(this.activePlayerId);
        } else {
            return null;
        }
    }
    
    public void setActivePlayer(@Nullable Player player) {
        this.activePlayerId = player == null ? null : player.getUUID();
    }
    
    @Nullable
    protected Optional<RecipeHolder<IRitualRecipe>> getActiveRecipe() {
        if (this.activeRecipeId != null) {
            Optional<RecipeHolder<?>> recipeOpt = this.level.getServer().getRecipeManager().byKey(this.activeRecipeId);
            if (recipeOpt.isPresent() && recipeOpt.get().value() instanceof IRitualRecipe ritualRecipe) {
                return Optional.of(new RecipeHolder<>(recipeOpt.get().id(), ritualRecipe));
            }
        }
        return Optional.empty();
    }
    
    @Override
    protected Set<Integer> getSyncedSlotIndices(int inventoryIndex) {
        // Sync the altar's stack for client rendering use
        return inventoryIndex == OUTPUT_INV_INDEX ? ImmutableSet.of(Integer.valueOf(0)) : ImmutableSet.of();
    }
    
    public Color getOrbColor() {
        float hue, saturation;
        if (stability >= 0.0F) {
            hue = 120.0F / 360.0F;  // Green
            saturation = Mth.clamp(this.stability / MAX_STABILITY, 0.0F, 1.0F);
        } else {
            hue = 0.0F / 360.0F;    // Red
            saturation = Mth.clamp(this.stability / MIN_STABILITY, 0.0F, 1.0F);
        }
        return Color.getHSBColor(hue, saturation, 1.0F);
    }
    
    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.active = compound.getBoolean("Active");
        this.currentStepComplete = compound.getBoolean("CurrentStepComplete");
        this.activeCount = compound.getInt("ActiveCount");
        this.nextCheckCount = compound.getInt("NextCheckCount");
        this.stability = Mth.clamp(compound.getFloat("Stability"), MIN_STABILITY, MAX_STABILITY);
        
        if (compound.contains("ActivePlayer", Tag.TAG_COMPOUND)) {
            this.activePlayerId = NbtUtils.loadUUID(compound.getCompound("ActivePlayer"));
        } else {
            this.activePlayerId = null;
        }
        
        this.activeRecipeId = compound.contains("ActiveRecipeId", Tag.TAG_STRING) ? 
                ResourceLocation.parse(compound.getString("ActiveRecipeId")) : 
                null;
        
        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);
        
        this.currentStep = null;
        if (compound.contains("CurrentStep")) {
            AbstractRitualStep.dispatchCodec().parse(registryOps, compound.get("CurrentStep"))
                .resultOrPartial(msg -> LOGGER.error("Failed to decode current ritual step: {}", msg))
                .ifPresent(decodedStep -> this.currentStep = decodedStep);
        }
                
        this.remainingSteps.clear();
        if (compound.contains("RemainingSteps")) {
            AbstractRitualStep.dispatchCodec().listOf().parse(registryOps, compound.get("RemainingSteps"))
                .resultOrPartial(msg -> LOGGER.error("Failed to decode remaining ritual steps: {}", msg))
                .ifPresent(decodedSteps -> this.remainingSteps.addAll(decodedSteps));
        }
        
        this.awaitedPropPos = compound.contains("AwaitedPropPos", Tag.TAG_LONG) ?
                BlockPos.of(compound.getLong("AwaitedPropPos")) :
                null;
                
        this.channeledOfferingPos = compound.contains("ChanneledOfferingPos", Tag.TAG_LONG) ?
                BlockPos.of(compound.getLong("ChanneledOfferingPos")) :
                null;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);

        RegistryOps<Tag> registryOps = registries.createSerializationContext(NbtOps.INSTANCE);

        compound.putBoolean("Active", this.active);
        compound.putBoolean("CurrentStepComplete", this.currentStepComplete);
        compound.putInt("ActiveCount", this.activeCount);
        compound.putInt("NextCheckCount", this.nextCheckCount);
        compound.putFloat("Stability", this.stability);
        if (this.activePlayerId != null) {
            compound.putUUID("ActivePlayer", this.activePlayerId);
        }
        if (this.activeRecipeId != null) {
            compound.putString("ActiveRecipeId", this.activeRecipeId.toString());
        }
        if (this.currentStep != null) {
            AbstractRitualStep.dispatchCodec().encodeStart(registryOps, this.currentStep)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode current ritual step: {}", msg))
                .ifPresent(encodedStep -> compound.put("CurrentStep", encodedStep));
        }
        if (this.remainingSteps != null && !this.remainingSteps.isEmpty()) {
            AbstractRitualStep.dispatchCodec().listOf().encodeStart(registryOps, this.remainingSteps)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode remaining ritual steps: {}", msg))
                .ifPresent(encodedSteps -> compound.put("RemainingSteps", encodedSteps));
        }
        if (this.awaitedPropPos != null) {
            compound.putLong("AwaitedPropPos", this.awaitedPropPos.asLong());
        }
        if (this.channeledOfferingPos != null) {
            compound.putLong("ChanneledOfferingPos", this.channeledOfferingPos.asLong());
        }
    }
    
    protected void reset() {
        // If there's a prop being waited on, close it out
        if (this.awaitedPropPos != null) {
            BlockState state = this.level.getBlockState(this.awaitedPropPos);
            Block block = state.getBlock();
            if (block instanceof IRitualPropBlock) {
                ((IRitualPropBlock)block).closeProp(state, this.level, this.awaitedPropPos);
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

        this.setChanged();
        this.syncTile(false);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RitualAltarTileEntity entity) {
        if (entity.active) {
            entity.doEffects();
        }
        if (entity.ticksExisted % (entity.active ? 10 : 20) == 0 && !level.isClientSide) {
            entity.scanDirty = true;
        }
        if (entity.scanDirty && !level.isClientSide) {
            entity.scanSurroundings();
            entity.scanDirty = false;
        }
        if (!level.isClientSide && entity.active) {
            if (entity.currentStep == null || entity.currentStepComplete) {
                if (entity.remainingSteps.isEmpty()) {
                    // If there are no steps remaining in the ritual, finish it up
                    if (entity.activeCount >= entity.nextCheckCount) {
                        entity.finishCraft();
                        entity.setChanged();
                        entity.syncTile(false);
                    }
                    return;
                } else {
                    // Pull the next step from the queue and start it
                    entity.currentStep = entity.remainingSteps.poll();
                    entity.currentStepComplete = false;
                    entity.skipWarningMessage = false;
                }
            }
            float delta = entity.calculateStabilityDelta();
            entity.addStability(delta);
            if (entity.currentStep != null) {
                if (!entity.doStep(entity.currentStep)) {
                    // Add extra instability if the ritual step was not productive (e.g. waiting for prop activation)
                    entity.addStability(Math.min(0.0F, delta));
                }
            }
            if (entity.activeCount % 10 == 0 && entity.stability < 0.0F && level.random.nextInt(1500) < Math.abs(entity.stability)) {
                entity.doMishap();
            }
            entity.setChanged();
            entity.syncTile(false);
        }
        
        entity.ticksExisted++;
        if (entity.active) {
            entity.activeCount++;
        }
    }

    @Override
    public InteractionResult onWandRightClick(ItemStack wandStack, Level level, Player player, BlockPos pos, Direction direction) {
        if (!this.level.isClientSide && wandStack.getItem() instanceof IWand) {
            if (this.active) {
                player.displayClientMessage(Component.translatable("ritual.primalmagick.info.canceled"), false);
                this.doMishap();    // Trigger an automatic mishap if canceling a ritual early
                this.reset();
            } else if (!level.getBlockState(pos.above()).isAir() || !level.getBlockState(pos.above(2)).isAir()) {
                player.displayClientMessage(Component.translatable("ritual.primalmagick.warning.obstructed"), false);
                this.reset();
            } else if (this.startCraft(wandStack, player)) {
                this.active = true;
                this.activeCount = 0;
                player.displayClientMessage(Component.translatable("ritual.primalmagick.info.started"), false);
                this.setActivePlayer(player);
                this.setChanged();
                this.syncTile(false);
            } else {
                this.reset();
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onWandUseTick(ItemStack wandStack, Level level, Player player, Vec3 targetPos, int count) {
        // Do nothing; ritual altars don't support wand channeling
    }
    
    protected void doEffects() {
        if (this.level instanceof ServerLevel serverLevel && this.activeCount % RITUAL_SOUND_LENGTH == 0) {
            PacketHandler.sendToAllAround(new PlayClientSoundPacket(SoundsPM.RITUAL.get(), 1.0F, 1.0F), serverLevel, this.getBlockPos(), 16.0D);
        }
    }
    
    protected boolean startCraft(ItemStack wandStack, Player player) {
        // Scan altar surroundings for pedestals and props immediately when starting a ritual
        this.scanSurroundings();
        
        // Determine offerings
        List<ItemStack> offerings = new ArrayList<>();
        for (BlockPos offeringPos : this.pedestalPositions) {
            BlockEntity tile = this.level.getBlockEntity(offeringPos);
            if (tile instanceof OfferingPedestalTileEntity pedestalTile) {
                ItemStack stack = pedestalTile.getItem();
                if (stack != null && !stack.isEmpty()) {
                    offerings.add(stack);
                }
            }
        }
        
        // Determine recipe that corresponds to offerings
        CraftingContainer inv = new TransientCraftingContainer(new FakeMenu(), offerings.size(), 1);
        int offeringIndex = 0;
        for (ItemStack offering : offerings) {
            inv.setItem(offeringIndex++, offering);
        }
        Optional<RecipeHolder<IRitualRecipe>> recipeOpt = this.level.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.RITUAL.get(), inv.asCraftInput(), this.level);
        if (recipeOpt.isPresent()) {
            // Determine if the player has the research and mana to start this recipe
            RecipeHolder<IRitualRecipe> recipe = recipeOpt.get();
            if (this.canUseRitualRecipe(wandStack, player, recipe) && this.generateRitualSteps(recipe)) {
                this.activeRecipeId = recipe.id();
                this.currentStep = null;
                this.currentStepComplete = false;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    protected boolean generateRitualSteps(@Nonnull RecipeHolder<IRitualRecipe> recipe) {
        LinkedList<AbstractRitualStep<?>> offeringSteps = new LinkedList<>();
        LinkedList<AbstractRitualStep<?>> propSteps = new LinkedList<>();
        LinkedList<AbstractRitualStep<?>> newSteps = new LinkedList<>();

        // Add steps for the recipe offerings and props
        for (int index = 0; index < recipe.value().getIngredients().size(); index++) {
            offeringSteps.add(new OfferingRitualStep(index));
        }
        for (int index = 0; index < recipe.value().getProps().size(); index++) {
            propSteps.add(new PropRitualStep(index));
        }
        
        // Add steps for any universal props that were detected when scanning surroundings
        for (BlockPos propPos : this.propPositions) {
            BlockState propState = this.level.getBlockState(propPos);
            Block block = propState.getBlock();
            if (block instanceof IRitualPropBlock) {
                IRitualPropBlock propBlock = (IRitualPropBlock)block;
                if (propBlock.isUniversal() && !propBlock.isPropActivated(propState, this.level, propPos)) {
                    propSteps.add(new UniversalRitualStep(propPos, Services.BLOCKS.getKey(block)));
                }
            }
        }
        
        // Randomize the generated steps, trying to space props evenly between batches of offerings
        Collections.shuffle(offeringSteps, RNG);
        Collections.shuffle(propSteps, RNG);
        int numOfferings = offeringSteps.size();
        int numProps = propSteps.size();
        int[] offeringBuckets = new int[numProps + 1];
        Arrays.fill(offeringBuckets, (numOfferings / (numProps + 1)));
        int leftoverOfferings = numOfferings % (numProps + 1);
        if (leftoverOfferings > 0) {
            List<Integer> leftoverBuckets = new ArrayList<>();
            for (int index = 0; index < numProps + 1; index++) {
                leftoverBuckets.add(index < leftoverOfferings ? 1 : 0);
            }
            Collections.shuffle(leftoverBuckets, RNG);
            for (int index = 0; index < offeringBuckets.length; index++) {
                offeringBuckets[index] += leftoverBuckets.get(index);
            }
        }
        for (int index = 0; index < offeringBuckets.length; index++) {
            if (index > 0) {
                newSteps.add(propSteps.poll());
            }
            for (int bucketIndex = 0; bucketIndex < offeringBuckets[index]; bucketIndex++) {
                newSteps.add(offeringSteps.poll());
            }
        }
        
        this.remainingSteps = newSteps;
        return true;
    }
    
    protected void finishCraft() {
        this.getActiveRecipe().ifPresent(recipeHolder -> {
            ItemStack outputStack = recipeHolder.value().getResultItem(this.getLevel().registryAccess()).copy();
            this.setItem(OUTPUT_INV_INDEX, 0, outputStack);
            Player activePlayer = this.getActivePlayer();
            if (activePlayer != null) {
                activePlayer.displayClientMessage(Component.translatable("ritual.primalmagick.info.complete"), false);
                StatsManager.incrementValue(activePlayer, StatsPM.RITUALS_COMPLETED);
                StatsManager.incrementValue(activePlayer, StatsPM.CRAFTED_RITUAL, outputStack.getCount());
                ExpertiseManager.awardExpertise(activePlayer, recipeHolder);
            }
        });
        
        this.spawnSuccessParticles();
        this.reset();
    }
    
    protected boolean canUseRitualRecipe(ItemStack wandStack, Player player, RecipeHolder<IRitualRecipe> recipeHolder) {
        // Players must know the correct research and the wand must have enough mana in order to use the recipe
        IRitualRecipe recipe = recipeHolder.value();
        return (recipe.getRequirement().isEmpty() || recipe.getRequirement().get().isMetBy(player)) &&
                (recipe.getManaCosts().isEmpty() || this.consumeMana(wandStack, player, recipe));
    }
    
    protected boolean consumeMana(ItemStack wandStack, Player player, IRitualRecipe recipe) {
        if (wandStack == null || wandStack.isEmpty() || !(wandStack.getItem() instanceof IWand)) {
            return false;
        }
        IWand wand = (IWand)wandStack.getItem();
        return wand.consumeRealMana(wandStack, player, recipe.getManaCosts(), player.registryAccess());
    }
    
    protected void scanSurroundings() {
        this.saltPositions.clear();
        this.pedestalPositions.clear();
        this.propPositions.clear();
        this.blockCounts.clear();
        
        Set<BlockPos> scanHistory = new HashSet<BlockPos>();
        scanHistory.add(this.worldPosition);
        
        Queue<BlockPos> toScan = new LinkedList<BlockPos>();
        toScan.offer(this.worldPosition.north());
        toScan.offer(this.worldPosition.east());
        toScan.offer(this.worldPosition.south());
        toScan.offer(this.worldPosition.west());
        
        while (!toScan.isEmpty()) {
            BlockPos pos = toScan.poll();
            this.scanPosition(pos, toScan, scanHistory);
        }
        this.symmetryDelta = this.calculateSymmetryDelta();
        
        Collections.shuffle(this.pedestalPositions, RNG);
        Collections.shuffle(this.propPositions, RNG);
    }
    
    protected void scanPosition(BlockPos pos, Queue<BlockPos> toScan, Set<BlockPos> history) {
        if (history.contains(pos)) {
            return;
        } else {
            history.add(pos);
        }
        
        BlockState state = this.level.getBlockState(pos);
        Block block = state.getBlock();
        
        // Determine if the scan position is within the theoretical range of this altar
        int dist = Math.abs(this.worldPosition.getX() - pos.getX()) + Math.abs(this.worldPosition.getZ() - pos.getZ());
        if (dist > ((RitualAltarBlock)this.getBlockState().getBlock()).getMaxSaltPower()) {
            return;
        }
        
        if (block == BlocksPM.SALT_TRAIL.get()) {
            // Keep scanning along the salt lines
            this.saltPositions.add(pos);
            for (Map.Entry<Direction, EnumProperty<SaltSide>> entry : SaltTrailBlock.FACING_PROPERTY_MAP.entrySet()) {
                BlockPos nextPos = pos.relative(entry.getKey());
                SaltSide saltSide = state.getValue(entry.getValue());
                if (saltSide == SaltSide.UP) {
                    toScan.add(nextPos.above());
                } else if (saltSide == SaltSide.SIDE) {
                    // The adjacent salt trail could be at the same height or one below, so check both
                    toScan.add(nextPos);
                    toScan.add(nextPos.below());
                }
            }
        } else if (block == BlocksPM.OFFERING_PEDESTAL.get()) {
            // Add this position to the offering pedestal collection
            OfferingPedestalBlock pedestalBlock = (OfferingPedestalBlock)block;
            if (pedestalBlock.isBlockSaltPowered(this.level, pos)) {
                this.pedestalPositions.add(pos);
                if (this.level.getBlockEntity(pos) instanceof OfferingPedestalTileEntity pedestalTile) {
                    pedestalTile.setAltarPos(this.getBlockPos());
                }
            }
        } else if (block instanceof IRitualPropBlock) {
            // Add this position to the prop collection
            IRitualPropBlock propBlock = (IRitualPropBlock)block;
            if (propBlock.isBlockSaltPowered(this.level, pos)) {
                this.propPositions.add(pos);
                if (this.level.getBlockEntity(pos) instanceof IRitualPropTileEntity propTile) {
                    propTile.setAltarPos(this.getBlockPos());
                }
            }
        }
    }
    
    @Nullable
    public static BlockPos getSymmetricPosition(@Nullable BlockPos altarPos, @Nullable BlockPos propPos) {
        if (altarPos == null || propPos == null) {
            return null;
        } else {
            int dx = altarPos.getX() - propPos.getX();
            int dz = altarPos.getZ() - propPos.getZ();
            return new BlockPos(altarPos.getX() + dx, propPos.getY(), altarPos.getZ() + dz);
        }
    }
    
    protected float calculateSymmetryDelta() {
        float delta = 0.0F;
        
        Set<BlockPos> toScan = new HashSet<>();
        toScan.addAll(this.pedestalPositions);
        toScan.addAll(this.propPositions);
        
        for (BlockPos scanPos : toScan) {
            BlockPos symPos = getSymmetricPosition(this.worldPosition, scanPos);
            Block block = this.level.getBlockState(scanPos).getBlock();
            
            if (block instanceof IRitualStabilizer) {
                IRitualStabilizer stabilizer = (IRitualStabilizer)block;
                if (stabilizer.hasSymmetryPenalty(this.level, scanPos, symPos)) {
                    delta -= stabilizer.getSymmetryPenalty(this.level, scanPos);
                } else {
                    delta += this.calculateDiminishingStabilityBonus(block, stabilizer.getStabilityBonus(this.level, scanPos));
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

    protected boolean doStep(@Nonnull AbstractRitualStep<?> step) {
        return step.execute(this);
    }
    
    public static boolean doOfferingStep(RitualAltarTileEntity altar, OfferingRitualStep step) {
        IRitualRecipe recipe = altar.getActiveRecipe().map(holder -> holder.value()).orElse(null);
        if (recipe == null) {
            LOGGER.warn("No recipe found when trying to do offering ritual step");
            return false;
        }
        
        int offeringIndex = step.getIndex();
        Ingredient requiredOffering = recipe.getIngredients().get(offeringIndex);
        if (altar.activeCount >= altar.nextCheckCount && altar.channeledOfferingPos == null) {
            // Search for a match for the required ingredient
            for (BlockPos pedestalPos : altar.pedestalPositions) {
                BlockEntity tile = altar.level.getBlockEntity(pedestalPos);
                Block block = altar.level.getBlockState(pedestalPos).getBlock();
                if (tile instanceof OfferingPedestalTileEntity pedestalTile && block instanceof ISaltPowered saltBlock) {
                    if (requiredOffering.test(pedestalTile.getItem()) && saltBlock.isBlockSaltPowered(altar.level, pedestalPos)) {
                        // Upon finding an ingredient match, start channeling it
                        altar.channeledOfferingPos = pedestalPos;
                        altar.nextCheckCount = altar.activeCount + 60;
                        return true;
                    }
                }
            }
            
            // If no match was found, warn the player the first time then check again in a second
            if (!altar.skipWarningMessage && altar.getActivePlayer() != null) {
                if (requiredOffering.isEmpty()) {
                    altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_offering.empty"), false);                    
                } else {
                    altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_offering", requiredOffering.getItems()[0].getHoverName()), false);
                }
                altar.skipWarningMessage = true;
            }
            altar.nextCheckCount = altar.activeCount + 20;
        }
        
        if (altar.channeledOfferingPos != null) {
            BlockEntity tile = altar.level.getBlockEntity(altar.channeledOfferingPos);
            Block block = altar.level.getBlockState(altar.channeledOfferingPos).getBlock();
            
            // Confirm that the channeled offering is still valid
            if ( tile instanceof OfferingPedestalTileEntity pedestalTile &&
                 block instanceof ISaltPowered saltBlock &&
                 requiredOffering.test(pedestalTile.getItem()) &&
                 saltBlock.isBlockSaltPowered(altar.level, altar.channeledOfferingPos) ) {
                if (altar.activeCount >= altar.nextCheckCount) {
                    // Once the channel is complete, consume it and mark the step as complete
                    pedestalTile.removeItem(1);
                    altar.currentStepComplete = true;
                    altar.channeledOfferingPos = null;
                } else {
                    // If the channel is still in progress, spawn particles
                    altar.spawnOfferingParticles(altar.channeledOfferingPos, pedestalTile.getItem());
                }
                return true;
            } else {
                // If the channel was interrupted, add an instability spike and start looking again
                altar.channeledOfferingPos = null;
                if (altar.getActivePlayer() != null) {
                    if (requiredOffering.isEmpty()) {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.channel_interrupt.empty"), false);
                    } else {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.channel_interrupt", requiredOffering.getItems()[0].getHoverName()), false);
                    }
                    altar.skipWarningMessage = true;
                }
                altar.addStability(Mth.clamp(50 * Math.min(0.0F, altar.calculateStabilityDelta()), -25.0F, -1.0F));
            }
        }
        return false;
    }
    
    public static boolean doPropStep(RitualAltarTileEntity altar, PropRitualStep step) {
        IRitualRecipe recipe = altar.getActiveRecipe().map(holder -> holder.value()).orElse(null);
        if (recipe == null) {
            LOGGER.warn("No recipe found when trying to do prop ritual step");
            return false;
        }
        
        int propIndex = step.getIndex();
        BlockIngredient requiredProp = recipe.getProps().get(propIndex);
        if (altar.activeCount >= altar.nextCheckCount) {
            if (altar.awaitedPropPos == null) {
                // Search for the required prop block
                for (BlockPos propPos : altar.propPositions) {
                    // Open the prop block if it's valid
                    BlockState propState = altar.level.getBlockState(propPos);
                    Block block = propState.getBlock();
                    if (requiredProp.test(block) && altar.openProp(propPos, (b) -> {
                        return !b.isPropActivated(propState, altar.level, propPos) && b.isBlockSaltPowered(altar.level, propPos);
                    })) {
                        return true;
                    }
                }
                
                // If no match was found, warn the player the first time
                if (!altar.skipWarningMessage && altar.getActivePlayer() != null) {
                    if (requiredProp.hasNoMatchingBlocks()) {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_prop.empty"), false);
                    } else {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_prop", requiredProp.getMatchingBlocks()[0].getName()), false);
                    }
                    altar.skipWarningMessage = true;
                }
            } else {
                BlockState propState = altar.level.getBlockState(altar.awaitedPropPos);
                Block block = propState.getBlock();
                if ( !(block instanceof IRitualPropBlock) || 
                     !requiredProp.test(block) ||
                     !((IRitualPropBlock)block).isBlockSaltPowered(altar.level, altar.awaitedPropPos) ) {
                    altar.onPropInterrupted(block, propState, Services.BLOCKS.getKey(requiredProp.getMatchingBlocks()[0]));
                }
            }
            altar.nextCheckCount = altar.activeCount + 20;
        }
        return false;
    }
    
    public static boolean doUniversalPropStep(RitualAltarTileEntity altar, UniversalRitualStep step) {
        IRitualRecipe recipe = altar.getActiveRecipe().map(holder -> holder.value()).orElse(null);
        if (recipe == null) {
            LOGGER.warn("No recipe found when trying to do universal ritual step");
            return false;
        }
        
        BlockPos propPos = step.getPos();
        ResourceLocation expectedId = step.getExpectedId();
        if (altar.activeCount >= altar.nextCheckCount) {
            if (altar.awaitedPropPos == null) {
                // Open the prop block if it's valid
                BlockState propState = altar.level.getBlockState(propPos);
                if (altar.openProp(propPos, (b) -> {
                    return b.isUniversal() && !b.isPropActivated(propState, altar.level, propPos) && b.isBlockSaltPowered(altar.level, propPos);
                })) {
                    return true;
                }
                
                // If no match was found, warn the player the first time
                if (!altar.skipWarningMessage && altar.getActivePlayer() != null) {
                    Block stepBlock = Services.BLOCKS.get(expectedId);
                    if (stepBlock == null) {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_prop.empty"), false);
                    } else {
                        altar.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.missing_prop", stepBlock.getName()), false);
                    }
                    altar.skipWarningMessage = true;
                }
            } else {
                BlockState propState = altar.level.getBlockState(altar.awaitedPropPos);
                Block block = propState.getBlock();
                if ( !(block instanceof IRitualPropBlock) ||
                     !((IRitualPropBlock)block).isUniversal() ||
                     !((IRitualPropBlock)block).isBlockSaltPowered(altar.level, altar.awaitedPropPos) ) {
                    altar.onPropInterrupted(block, propState, expectedId);
                }
            }
            altar.nextCheckCount = altar.activeCount + 20;
        }
        return false;
    }
    
    protected boolean openProp(BlockPos propPos, Predicate<IRitualPropBlock> isPropValid) {
        // Validate the prop block
        BlockState propState = this.level.getBlockState(propPos);
        Block block = propState.getBlock();
        if (block instanceof IRitualPropBlock) {
            IRitualPropBlock propBlock = (IRitualPropBlock)block;
            if (isPropValid.test(propBlock)) {
                // Upon confirmation, open the prop for activation
                propBlock.openProp(propState, this.level, propPos, this.getActivePlayer(), this.worldPosition);
                this.awaitedPropPos = propPos;
                this.nextCheckCount = this.activeCount + 20;
                return true;
            }
        }
        return false;
    }
    
    protected void onPropInterrupted(Block block, BlockState propState, ResourceLocation expectedId) {
        Block expectedProp = Services.BLOCKS.get(expectedId);
        
        // If contact with the prop was lost, add an instability spike and start looking again
        if (this.getActivePlayer() != null) {
            if (expectedProp == null) {
                this.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.prop_interrupt.empty"), false);
            } else {
                this.getActivePlayer().displayClientMessage(Component.translatable("ritual.primalmagick.warning.prop_interrupt", expectedProp.getName()), false);
            }
            this.skipWarningMessage = true;
        }
        if (block instanceof IRitualPropBlock) {
            // If the block still exists (i.e. salt was broken), then close it to activation
            ((IRitualPropBlock)block).closeProp(propState, this.level, this.awaitedPropPos);
        }
        this.awaitedPropPos = null;
        this.addStability(Mth.clamp(50 * Math.min(0.0F, this.calculateStabilityDelta()), -25.0F, -1.0F));
    }
    
    public void onPropActivation(BlockPos propPos, float stabilityBonus) {
        if (this.awaitedPropPos != null && this.awaitedPropPos.equals(propPos)) {
            // If the activated prop is the one we're waiting for, close it and mark the step as complete
            BlockState propState = this.level.getBlockState(propPos);
            Block block = propState.getBlock();
            if (block instanceof IRitualPropBlock) {
                IRitualPropBlock propBlock = (IRitualPropBlock)block;
                propBlock.closeProp(propState, this.level, propPos);
                this.addStability(stabilityBonus);
            }
            this.currentStepComplete = true;
            this.nextCheckCount = this.activeCount;
            this.awaitedPropPos = null;
            this.setChanged();
            this.syncTile(false);
        }
    }
    
    protected void addStability(float delta) {
        this.stability = Mth.clamp(this.stability + delta, MIN_STABILITY, MAX_STABILITY);
    }

    public float calculateStabilityDelta() {
        MutableFloat retVal = new MutableFloat(0F);
        
        // Deduct stability based on the active recipe
        this.getActiveRecipe().ifPresent(recipeHolder -> retVal.subtract(0.01F * recipeHolder.value().getInstability()));
        
        // Deduct stability for each salt trail in excess of the safe amount
        if (this.getBlockState().getBlock() instanceof RitualAltarBlock altarBlock) {
            int safeSaltCount = altarBlock.getMaxSafeSalt();
            if (this.saltPositions.size() > safeSaltCount) {
                retVal.subtract(0.001F * (this.saltPositions.size() - safeSaltCount));
            }
        }
        
        // Add or subtract stability based on pedestal/prop symmetry
        retVal.add(this.symmetryDelta);
        
        return retVal.floatValue();
    }
    
    protected void spawnOfferingParticles(BlockPos startPos, ItemStack stack) {
        if (this.level instanceof ServerLevel serverLevel) {
            double sx = startPos.getX() + 0.4D + (this.level.random.nextDouble() * 0.2D);
            double sy = startPos.getY() + 1.4D + (this.level.random.nextDouble() * 0.2D);
            double sz = startPos.getZ() + 0.4D + (this.level.random.nextDouble() * 0.2D);
            PacketHandler.sendToAllAround(new OfferingChannelPacket(sx, sy, sz, this.worldPosition.above(2), stack), serverLevel, startPos, 32.0D);
        }
    }
    
    protected void spawnSuccessParticles() {
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER, 
                    this.worldPosition.getX() + 0.5D, 
                    this.worldPosition.getY() + 1.2D, 
                    this.worldPosition.getZ() + 0.5D, 
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
            Mishap mishap = this.mishaps.getRandom(this.level.random);
            if (mishap != null && mishap.execute(this.stability)) {
                this.addStability(5.0F + (5.0F * this.level.random.nextFloat()));
                StatsManager.incrementValue(this.getActivePlayer(), StatsPM.RITUAL_MISHAPS);
                break;
            }
        }
    }
    
    protected void doMishapEffects(BlockPos target, boolean playSound) {
        if (this.level instanceof ServerLevel serverLevel) {
            BlockPos source = this.worldPosition.above(2);
            PacketHandler.sendToAllAround(new SpellBoltPacket(source, target, this.getOrbColor().getRGB()), serverLevel, source, 32.0D);
            if (playSound) {
                this.level.playSound(null, source, SoundsPM.ELECTRIC.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(level.random.nextGaussian() * 0.05D));
            }
        }
    }
    
    protected void mishapOffering(boolean destroy) {
        int attempts = 0;
        while (attempts++ < 25 && !this.pedestalPositions.isEmpty()) {
            // Search for a populated offering pedestal
            BlockPos pedestalPos = this.pedestalPositions.get(this.level.random.nextInt(this.pedestalPositions.size()));
            BlockEntity tile = this.level.getBlockEntity(pedestalPos);
            if (tile instanceof OfferingPedestalTileEntity pedestalTile) {
                if (!pedestalTile.getItem().isEmpty()) {
                    if (destroy) {
                        pedestalTile.setItem(ItemStack.EMPTY);
                    } else {
                        pedestalTile.dropContents(this.level, pedestalPos);
                    }
                    pedestalTile.setChanged();
                    pedestalTile.syncTile(false);
                    this.doMishapEffects(pedestalPos, true);
                    break;
                }
            }
        }
    }
    
    protected void mishapSalt(boolean multiple) {
        int breakCount = multiple ? 2 + this.level.random.nextInt(4) : 1;
        for (int breakIndex = 0; breakIndex < breakCount; breakIndex++) {
            int attempts = 0;
            while (attempts++ < 25 && !this.saltPositions.isEmpty()) {
                // Search for one or more salt trails in range
                BlockPos saltPos = this.saltPositions.get(this.level.random.nextInt(this.saltPositions.size()));
                Block block = this.level.getBlockState(saltPos).getBlock();
                if (block == BlocksPM.SALT_TRAIL.get()) {
                    Containers.dropItemStack(this.level, saltPos.getX() + 0.5D, saltPos.getY() + 0.5D, saltPos.getZ() + 0.5D, new ItemStack(ItemsPM.REFINED_SALT.get()));
                    this.level.removeBlock(saltPos, false);
                    this.doMishapEffects(saltPos, breakIndex == 0); // Only play sounds once
                    break;
                }
            }
        }
        this.scanDirty = true;
    }
    
    protected void mishapDamage(boolean allTargets) {
        // Damage one or all living entities in range and afflict them with mana impedance
        List<LivingEntity> targets = EntityUtils.getEntitiesInRange(this.level, this.worldPosition, null, LivingEntity.class, 10.0D);
        if (targets != null && !targets.isEmpty()) {
            for (int index = 0; index < targets.size(); index++) {
                LivingEntity target = targets.get(index);
                int damage = 5 + Mth.floor(Math.sqrt(Math.abs(Math.min(0.0F, this.stability))) / 2.0D);
                int amp = Math.max(0, damage - 6);
                target.hurt(target.damageSources().magic(), damage);
                target.addEffect(new MobEffectInstance(EffectsPM.MANA_IMPEDANCE.getHolder().get(), 12000, amp));
                this.doMishapEffects(target.blockPosition(), index == 0); // Only play sounds once
                if (!allTargets) {
                    break;
                }
            }
        }
    }
    
    protected void mishapDetonate(boolean central) {
        BlockPos target = null;
        if (central) {
            target = this.worldPosition;
        } else {
            // If not targeting the central altar, try to find a populated pedestal
            int attempts = 0;
            while (attempts++ < 25 && !this.pedestalPositions.isEmpty()) {
                BlockPos pedestalPos = this.pedestalPositions.get(this.level.random.nextInt(this.pedestalPositions.size()));
                BlockEntity tile = this.level.getBlockEntity(pedestalPos);
                if (tile instanceof OfferingPedestalTileEntity pedestalTile) {
                    if (!pedestalTile.getItem().isEmpty()) {
                        target = pedestalPos;
                        break;
                    }
                }
            }
            if (target == null && !this.pedestalPositions.isEmpty()) {
                // If we can't find a populated pedestal, just pick one at random
                target = this.pedestalPositions.get(this.level.random.nextInt(this.pedestalPositions.size()));
            }
        }
        if (target != null) {
            if (central && this.awaitedPropPos != null) {
                // If destroying the central altar, close out any waiting props
                BlockState state = this.level.getBlockState(this.awaitedPropPos);
                Block block = state.getBlock();
                if (block instanceof IRitualPropBlock) {
                    ((IRitualPropBlock)block).closeProp(state, this.level, this.awaitedPropPos);
                }
            }
            if (!central) {
                this.doMishapEffects(target, true);
                this.scanDirty = true;
            }
            float force = central ? 3.0F + this.level.random.nextFloat() : 2.0F;
            this.level.explode(null, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, force, Level.ExplosionInteraction.TNT);
        }
    }

    public ItemStack getItem() {
        return this.getItem(OUTPUT_INV_INDEX, 0);
    }
    
    public ItemStack getSyncedStack() {
        return this.syncedInventories.get(OUTPUT_INV_INDEX).get(0);
    }
    
    public void setItem(ItemStack stack) {
        this.setItem(OUTPUT_INV_INDEX, 0, stack);
    }
    
    @Override
    protected int getInventoryCount() {
        return 1;
    }

    @Override
    protected int getInventorySize(int inventoryIndex) {
        return inventoryIndex == OUTPUT_INV_INDEX ? 1 : 0;
    }

    @Override
    protected OptionalInt getInventoryIndexForFace(Direction face) {
        return OptionalInt.of(OUTPUT_INV_INDEX);
    }

    @Override
    protected NonNullList<ItemStackHandler> createHandlers() {
        NonNullList<ItemStackHandler> retVal = NonNullList.withSize(this.getInventoryCount(), new ItemStackHandlerPM(this));
        
        // Create output handler
        retVal.set(OUTPUT_INV_INDEX, new ItemStackHandlerPM(this.inventories.get(OUTPUT_INV_INDEX), this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });

        return retVal;
    }
}
