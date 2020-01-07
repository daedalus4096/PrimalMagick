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
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ConjureAnimalSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "conjure_animal";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_CONJURE_ANIMAL"));
    protected static final WeightedRandomBag<EntityType<?>> LAND_ANIMALS = new WeightedRandomBag<>();
    protected static final WeightedRandomBag<EntityType<?>> WATER_ANIMALS = new WeightedRandomBag<>();
    
    static {
        LAND_ANIMALS.add(EntityType.BAT, 2);
        LAND_ANIMALS.add(EntityType.CAT, 5);
        LAND_ANIMALS.add(EntityType.CHICKEN, 10);
        LAND_ANIMALS.add(EntityType.COW, 10);
        LAND_ANIMALS.add(EntityType.DONKEY, 2);
        LAND_ANIMALS.add(EntityType.FOX, 5);
        LAND_ANIMALS.add(EntityType.HORSE, 2);
        LAND_ANIMALS.add(EntityType.MOOSHROOM, 1);
        LAND_ANIMALS.add(EntityType.OCELOT, 5);
        LAND_ANIMALS.add(EntityType.PARROT, 2);
        LAND_ANIMALS.add(EntityType.PIG, 10);
        LAND_ANIMALS.add(EntityType.RABBIT, 5);
        LAND_ANIMALS.add(EntityType.SHEEP, 10);
        LAND_ANIMALS.add(EntityType.TURTLE, 5);
        
        WATER_ANIMALS.add(EntityType.COD, 10);
        WATER_ANIMALS.add(EntityType.PUFFERFISH, 10);
        WATER_ANIMALS.add(EntityType.SALMON, 10);
        WATER_ANIMALS.add(EntityType.SQUID, 5);
        WATER_ANIMALS.add(EntityType.TROPICAL_FISH, 10);
        WATER_ANIMALS.add(EntityType.TURTLE, 5);
    }

    public ConjureAnimalSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster) {
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
        IFluidState state = world.getFluidState(pos);
        EntityType<?> entityType = (state.isTagged(FluidTags.WATER) && state.isSource()) ? WATER_ANIMALS.getRandom() : LAND_ANIMALS.getRandom();
        if (entityType != null) {
            entityType.spawn(world, null, null, pos, SpawnReason.MOB_SUMMONED, false, false);
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
        world.playSound(null, origin, SoundsPM.EGG_CRACK, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
