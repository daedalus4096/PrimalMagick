package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Definition of a self-targetting spell vehicle.  No direction vectors or special targeting are
 * necessary because the spell never travels beyond the caster.
 * 
 * @author Daedalus4096
 */
public class SelfSpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "self";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        if (spell.getPayload() != null) {
            ForkSpellMod forkMod = spell.getMod(ForkSpellMod.class, "forks");
            HitResult result = new EntityHitResult(caster, caster.getEyePosition(1.0F));
            
            // Determine how many times the caster should be affected by the spell payload
            int forks = (forkMod == null) ? 1 : forkMod.getPropertyValue("forks");
            for (int index = 0; index < forks; index++) {
                SpellManager.executeSpellPayload(spell, result, world, caster, spellSource, true);
            }
        }
    }
}
