package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ObservationProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class SpellworkProject extends AbstractProject {
    public static final String TYPE = "spellwork";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("BASIC_SORCERY");
    
    static {
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.SPELLCRAFTING_ALTAR.get(), false), 2);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.WAND_INSCRIPTION_TABLE.get(), false), 2);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.WAND_CHARGER.get(), false), 2);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.MUNDANE_WAND.get(), false), 1);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.SPELL_SCROLL_BLANK.get(), true), 5);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_EARTH.get(), true), 1);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SEA.get(), true), 1);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SKY.get(), true), 1);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_SUN.get(), true), 1);
        OPTIONS.add(new ItemProjectMaterial(ItemsPM.ESSENCE_DUST_MOON.get(), true), 1);
        OPTIONS.add(new ObservationProjectMaterial(), 5);
    }
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        return OPTIONS;
    }
    
    @Override
    public SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }
}
