package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            ForkSpellMod forkMod = spell.getMod(ForkSpellMod.class, "forks");
            RayTraceResult result = new EntityRayTraceResult(caster, caster.getEyePosition(1.0F));
            int forks = (forkMod == null) ? 1 : forkMod.getPropertyValue("forks");
            for (int index = 0; index < forks; index++) {
                SpellManager.executeSpellPayload(spell, result, world, caster, true);
            }
        }
    }
}
