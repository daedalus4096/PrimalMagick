package com.verdantartifice.primalmagick.common.spells.payloads;

import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import net.minecraft.core.BlockPos;
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
public class TeleportSpellPayload extends AbstractSpellPayload {
    public static final String TYPE = "teleport";
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_PAYLOAD_TELEPORT.get().compoundKey();

    public TeleportSpellPayload() {
        super();
    }
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
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
        return Source.VOID;
    }

    @Override
    public int getBaseManaCost() {
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
