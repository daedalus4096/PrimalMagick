package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Definition for an animal conjuration spell.  Spawns a random passive animal at the target location.
 * Chooses from separate pools depending on whether the target location is in water or not.  Not
 * compatible with the Burst spell mod.
 * 
 * @author Daedalus4096
 */
public class ConjureAnimalSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "conjure_animal";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_CONJURE_ANIMAL"));
    protected static final WeightedRandomBag<EntityType<?>> LAND_ANIMALS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(EntityType.BAT, 2);
        bag.add(EntityType.CAT, 5);
        bag.add(EntityType.CHICKEN, 10);
        bag.add(EntityType.COW, 10);
        bag.add(EntityType.DONKEY, 2);
        bag.add(EntityType.FOX, 5);
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
        bag.add(EntityType.COD, 10);
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
    public void execute(RayTraceResult target, Vector3d burstPoint, SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (burstPoint != null) {
            // Do nothing if this is a burst spell
            return;
        } else if (target.getType() == RayTraceResult.Type.BLOCK) {
            this.placeRandomAnimal(world, (BlockRayTraceResult)target);
        } else if (target.getType() == RayTraceResult.Type.ENTITY) {
            this.placeRandomAnimal(world, RayTraceUtils.getBlockResultFromEntityResult((EntityRayTraceResult)target));
        }
    }
    
    protected void placeRandomAnimal(World world, BlockRayTraceResult blockTarget) {
        BlockPos pos = blockTarget.getPos().offset(blockTarget.getFace());
        FluidState state = world.getFluidState(pos);
        
        // Get a random entity type for either land or water, depending on the fluid state of the target location
        EntityType<?> entityType = (state.isTagged(FluidTags.WATER) && state.isSource()) ? 
                WATER_ANIMALS.getRandom(world.rand) : 
                LAND_ANIMALS.getRandom(world.rand);
        if (entityType != null && world instanceof ServerWorld) {
            entityType.spawn((ServerWorld)world, null, null, pos, SpawnReason.MOB_SUMMONED, false, false);
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
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.EGG_CRACK.get(), SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
