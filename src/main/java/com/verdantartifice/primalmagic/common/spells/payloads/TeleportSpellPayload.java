package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.TeleportArrivalPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Definition of a teleport spell.  Teleports the caster to the target location.  Works similarly to
 * throwing ender pearls.  Not compatible with Burst mods.
 * 
 * @author Daedalus4096
 */
public class TeleportSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "teleport";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_PAYLOAD_TELEPORT"));

    public TeleportSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    public void execute(RayTraceResult target, Vec3d burstPoint, SpellPackage spell, World world, PlayerEntity caster) {
        if (burstPoint != null) {
            // Do nothing if this was from a burst spell
            return;
        }
        if (target.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)target).getEntity();
            if (entity.equals(caster)) {
                // Do nothing if the caster targeted themselves
                return;
            }
        }
        
        // Show a teleport particle effect at the destination
        Vec3d hitVec = target.getHitVec();
        PacketHandler.sendToAllAround(new TeleportArrivalPacket(hitVec.x, hitVec.y, hitVec.z), world.dimension.getType(), new BlockPos(hitVec), 64.0D);
        
        if (!world.isRemote && caster instanceof ServerPlayerEntity) {
            ServerPlayerEntity spe = (ServerPlayerEntity)caster;
            if (spe.connection.getNetworkManager().isChannelOpen() && spe.world == world && !spe.isSleeping()) {
                if (caster.isPassenger()) {
                    caster.stopRiding();
                }
                
                // Do the teleportation
                caster.setPositionAndUpdate(hitVec.x, hitVec.y, hitVec.z);
                caster.fallDistance = 0.0F;
            }
        }
    }

    @Override
    public Source getSource() {
        return Source.VOID;
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public void playSounds(World world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F + (float)(world.rand.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
