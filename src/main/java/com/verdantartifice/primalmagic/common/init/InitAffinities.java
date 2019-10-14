package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class InitAffinities {
    public static void initAffinities() {
        initItemAffinities();
        // TODO init entity affinities
    }
    
    protected static void initItemAffinities() {
        AffinityManager.registerAffinities(new ResourceLocation("coals"), new SourceList().add(Source.EARTH, 5).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("fishes"), new SourceList().add(Source.SEA, 5).add(Source.BLOOD, 5));
        AffinityManager.registerAffinities(new ResourceLocation("leaves"), new SourceList().add(Source.EARTH, 5).add(Source.SKY, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("logs"), new SourceList().add(Source.EARTH, 10).add(Source.SUN, 10));
        AffinityManager.registerAffinities(new ResourceLocation("planks"), new SourceList().add(Source.EARTH, 2).add(Source.SUN, 2));
        AffinityManager.registerAffinities(new ResourceLocation("sand"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("saplings"), new SourceList().add(Source.EARTH, 10).add(Source.SUN, 10));
        AffinityManager.registerAffinities(new ResourceLocation("small_flowers"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("wool"), new SourceList().add(Source.BLOOD, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "cobblestone"), new SourceList().add(Source.EARTH, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "eggs"), new SourceList().add(Source.BLOOD, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "end_stones"), new SourceList().add(Source.EARTH, 5).add(Source.VOID, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ender_pearls"), new SourceList().add(Source.VOID, 20));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "feathers"), new SourceList().add(Source.SKY, 10).add(Source.BLOOD, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gravel"), new SourceList().add(Source.EARTH, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gunpowder"), new SourceList().add(Source.EARTH, 5).add(Source.INFERNAL, 15));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "heads"), new SourceList().add(Source.BLOOD, 20));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "leather"), new SourceList().add(Source.BLOOD, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "mushrooms"), new SourceList().add(Source.EARTH, 5).add(Source.MOON, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "nether_stars"), new SourceList().add(Source.EARTH, 10).add(Source.SEA, 10).add(Source.SKY, 10).add(Source.SUN, 10).add(Source.MOON, 10).add(Source.BLOOD, 10).add(Source.INFERNAL, 10).add(Source.VOID, 10).add(Source.HALLOWED, 10));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "netherack"), new SourceList().add(Source.EARTH, 5).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "obsidian"), new SourceList().add(Source.EARTH, 5).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "sandstone"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "slimeballs"), new SourceList().add(Source.SEA, 5).add(Source.BLOOD, 2));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "stone"), new SourceList().add(Source.EARTH, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "string"), new SourceList().add(Source.SKY, 5).add(Source.BLOOD, 2));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "crops/beetroot"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "crops/carrot"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "crops/nether_wart"), new SourceList().add(Source.EARTH, 5).add(Source.MOON, 5).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "crops/potato"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "crops/wheat"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "dusts/glowstone"), new SourceList().add(Source.SUN, 10).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "dusts/prismarine"), new SourceList().add(Source.EARTH, 2).add(Source.SEA, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "dusts/redstone"), new SourceList().add(Source.EARTH, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gems/diamond"), new SourceList().add(Source.EARTH, 20));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gems/emerald"), new SourceList().add(Source.EARTH, 20));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gems/lapis"), new SourceList().add(Source.EARTH, 10));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gems/prismarine"), new SourceList().add(Source.EARTH, 5).add(Source.SEA, 10));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "gems/quartz"), new SourceList().add(Source.EARTH, 10).add(Source.INFERNAL, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ingots/brick"), new SourceList().add(Source.EARTH, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ingots/gold"), new SourceList().add(Source.EARTH, 10));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ingots/iron"), new SourceList().add(Source.EARTH, 10));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ingots/nether_brick"), new SourceList().add(Source.EARTH, 5).add(Source.INFERNAL, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/coal"), new SourceList().add(Source.EARTH, 10).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/diamond"), new SourceList().add(Source.EARTH, 25));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/emerald"), new SourceList().add(Source.EARTH, 25));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/gold"), new SourceList().add(Source.EARTH, 15));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/iron"), new SourceList().add(Source.EARTH, 15));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/lapis"), new SourceList().add(Source.EARTH, 15));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/quartz"), new SourceList().add(Source.EARTH, 15).add(Source.INFERNAL, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "ores/redstone"), new SourceList().add(Source.EARTH, 10));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "rods/blaze"), new SourceList().add(Source.INFERNAL, 20));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "sand/colorless"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "sand/red"), new SourceList().add(Source.EARTH, 5).add(Source.SUN, 5));
        
        AffinityManager.registerAffinities(new ResourceLocation("forge", "seeds/beetroot"), new SourceList().add(Source.EARTH, 2).add(Source.SUN, 2));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "seeds/melon"), new SourceList().add(Source.EARTH, 2).add(Source.SUN, 2));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "seeds/pumpkin"), new SourceList().add(Source.EARTH, 2).add(Source.SUN, 2));
        AffinityManager.registerAffinities(new ResourceLocation("forge", "seeds/wheat"), new SourceList().add(Source.EARTH, 2).add(Source.SUN, 2));
        
        AffinityManager.registerAffinities(new ItemStack(Blocks.MYCELIUM), new SourceList().add(Source.EARTH, 5).add(Source.MOON, 10));
        
        AffinityManager.registerAffinities(new ItemStack(Items.BONE), new SourceList().add(Source.MOON, 5).add(Source.BLOOD, 10));
        
        AffinityManager.registerAffinities(new ItemStack(BlocksPM.MARBLE_RAW), new SourceList().add(Source.EARTH, 5));
    }
}
