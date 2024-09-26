package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

/**
 * Definition of a self-targetting spell vehicle.  No direction vectors or special targeting are
 * necessary because the spell never travels beyond the caster.
 * 
 * @author Daedalus4096
 */
public class SelfSpellVehicle extends AbstractSpellVehicle<SelfSpellVehicle> {
    public static final SelfSpellVehicle INSTANCE = new SelfSpellVehicle();
    
    public static final MapCodec<SelfSpellVehicle> CODEC = MapCodec.unit(SelfSpellVehicle.INSTANCE);
    public static final StreamCodec<ByteBuf, SelfSpellVehicle> STREAM_CODEC = StreamCodec.unit(SelfSpellVehicle.INSTANCE);
    
    public static final String TYPE = "self";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.BASIC_SORCERY));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    public static SelfSpellVehicle getInstance() {
        return INSTANCE;
    }

    @Override
    public SpellVehicleType<SelfSpellVehicle> getType() {
        return SpellVehiclesPM.SELF.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.of();
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (spell.payload() != null) {
            HitResult result = new EntityHitResult(caster, caster.getEyePosition(1.0F));
            
            // Determine how many times the caster should be affected by the spell payload
            MutableInt forkCount = new MutableInt(1);
            spell.getMod(SpellModsPM.FORK.get()).ifPresent(forkMod -> {
                forkCount.setValue(forkMod.getPropertyValue(SpellPropertiesPM.FORKS.get()));
            });
            
            // Execute the spell payload once for each fork
            for (int index = 0; index < forkCount.intValue(); index++) {
                SpellManager.executeSpellPayload(spell, result, world, caster, spellSource, true, null);
            }
        }
    }

    @Override
    public boolean isIndirect() {
        return false;
    }
}
