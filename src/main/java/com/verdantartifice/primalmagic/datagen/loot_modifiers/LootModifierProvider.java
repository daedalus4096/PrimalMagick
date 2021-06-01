package com.verdantartifice.primalmagic.datagen.loot_modifiers;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.loot.modifiers.BloodyFleshModifier;
import com.verdantartifice.primalmagic.common.loot.modifiers.LootModifierSerializersPM;
import com.verdantartifice.primalmagic.common.tags.EntityTypeTagsPM;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootContext.EntityTarget;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

/**
 * Data provider for all of the mod's loot modifiers.
 * 
 * @author Daedalus4096
 */
public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, PrimalMagic.MODID);
    }

    @Override
    protected void start() {
        this.add("bloody_flesh", LootModifierSerializersPM.BLOODY_FLESH.get(), new BloodyFleshModifier(
                new ILootCondition[] {
                        EntityHasProperty.builder(EntityTarget.THIS, EntityPredicate.Builder.create().type(EntityTypeTagsPM.DROPS_BLOODY_FLESH)).build()
                }));
    }
}
