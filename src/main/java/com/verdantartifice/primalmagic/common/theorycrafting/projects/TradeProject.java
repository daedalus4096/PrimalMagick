package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class TradeProject extends AbstractProject {
    public static final String TYPE = "trade";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    
    static {
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "gems/emerald"), true), 10);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "gems/diamond"), true), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.COAL, true), 2);
        OPTIONS.add(new ItemProjectMaterial(Items.COMPASS, true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "ingots/gold"), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.PUMPKIN, true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "string"), true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "leather"), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.BOOK, true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "gems/quartz"), true), 1);
        OPTIONS.add(new ItemProjectMaterial(Items.WHITE_WOOL, true), 1);
        OPTIONS.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "ingots/iron"), true), 1);
    }

    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        return OPTIONS;
    }
}
