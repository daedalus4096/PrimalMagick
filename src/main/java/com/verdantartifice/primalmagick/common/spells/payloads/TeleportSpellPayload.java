package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a teleport spell.  Teleports the caster to the target location.  Works similarly to
 * throwing ender pearls.  Not compatible with Burst mods.
 * 
 * @author Daedalus4096
 */
public class TeleportSpellPayload extends AbstractSpellPayload<TeleportSpellPayload> {
    public static final TeleportSpellPayload INSTANCE = new TeleportSpellPayload();
    
    public static final MapCodec<TeleportSpellPayload> CODEC = MapCodec.unit(TeleportSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, TeleportSpellPayload> STREAM_CODEC = StreamCodec.unit(TeleportSpellPayload.INSTANCE);
    
    public static final String TYPE = "teleport";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_TELEPORT));

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    public SpellPayloadType<TeleportSpellPayload> getType() {
        return SpellPayloadsPM.TELEPORT.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (burstPoint != null) {
            // Do nothing if this was from a burst spell
            return;
        }
        if (target.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)target).getEntity();
            if (entity.equals(caster)) {
                // Do nothing if the caster targeted themselves
                return;
            }
        }
        EntityUtils.teleportEntity(caster, world, target.getLocation());
    }

    @Override
    public Source getSource() {
        return Sources.VOID;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return 10;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
}
