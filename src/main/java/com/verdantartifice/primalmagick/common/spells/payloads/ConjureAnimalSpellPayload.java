package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition for an animal conjuration spell.  Spawns a random passive animal at the target location.
 * Chooses from separate pools depending on whether the target location is in water or not.  Not
 * compatible with the Burst spell mod.
 * 
 * @author Daedalus4096
 */
public class ConjureAnimalSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "conjure_animal";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_CONJURE_ANIMAL.get().compoundKey();
    protected static final WeightedRandomBag<EntityType<?>> LAND_ANIMALS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(EntityType.BAT, 2);
        bag.add(EntityType.CAT, 5);
        bag.add(EntityType.CHICKEN, 10);
        bag.add(EntityType.COW, 10);
        bag.add(EntityType.DONKEY, 2);
        bag.add(EntityType.FOX, 5);
        bag.add(EntityType.GOAT, 5);
        bag.add(EntityType.HORSE, 2);
        bag.add(EntityType.MOOSHROOM, 1);
        bag.add(EntityType.OCELOT, 5);
        bag.add(EntityType.PARROT, 2);
        bag.add(EntityType.PIG, 10);
        bag.add(EntityType.RABBIT, 5);
        bag.add(EntityType.SHEEP, 10);
        bag.add(EntityType.TURTLE, 5);
    });
    protected static final WeightedRandomBag<EntityType<?>> WATER_ANIMALS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(EntityType.AXOLOTL, 2);
        bag.add(EntityType.COD, 10);
        bag.add(EntityType.GLOW_SQUID, 2);
        bag.add(EntityType.PUFFERFISH, 10);
        bag.add(EntityType.SALMON, 10);
        bag.add(EntityType.SQUID, 5);
        bag.add(EntityType.TROPICAL_FISH, 10);
        bag.add(EntityType.TURTLE, 5);
    });
    
    public ConjureAnimalSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (burstPoint != null) {
            // Do nothing if this is a burst spell
            return;
        } else if (target.getType() == HitResult.Type.BLOCK) {
            this.placeRandomAnimal(world, (BlockHitResult)target);
        } else if (target.getType() == HitResult.Type.ENTITY) {
            this.placeRandomAnimal(world, RayTraceUtils.getBlockResultFromEntityResult((EntityHitResult)target));
        }
    }
    
    protected void placeRandomAnimal(Level world, BlockHitResult blockTarget) {
        BlockPos pos = blockTarget.getBlockPos().relative(blockTarget.getDirection());
        FluidState state = world.getFluidState(pos);
        
        // Get a random entity type for either land or water, depending on the fluid state of the target location
        EntityType<?> entityType = (state.is(FluidTags.WATER) && state.isSource()) ? 
                WATER_ANIMALS.getRandom(world.random) : 
                LAND_ANIMALS.getRandom(world.random);
        if (entityType != null && world instanceof ServerLevel serverWorld) {
            entityType.spawn(serverWorld, (ItemStack)null, (Player)null, pos, MobSpawnType.MOB_SUMMONED, false, false);
        }
    }

    @Override
    public Source getSource() {
        return Source.BLOOD;
    }

    @Override
    public int getBaseManaCost() {
        return 100;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.EGG_CRACK.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
