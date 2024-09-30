package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Global loot modifier that allows mobs in the conditioned tag to drop Blood-Scrawled Ravings when killed.
 * 
 * @author Daedalus4096
 */
public class BloodNotesModifier extends LootModifier {
    public static final MapCodec<BloodNotesModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).and(
            TagKey.codec(Registries.ENTITY_TYPE).fieldOf("targetTag").forGetter(m -> m.targetTag)
        ).apply(inst, BloodNotesModifier::new));
    
    protected final TagKey<EntityType<?>> targetTag;

    public BloodNotesModifier(LootItemCondition[] conditionsIn, TagKey<EntityType<?>> targetTag) {
        super(conditionsIn);
        this.targetTag = targetTag;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Random chance is controlled by the RandomChanceWithLooting condition in the modifier JSON
        Entity targetEntity = context.getParam(LootContextParams.THIS_ENTITY);
        if (targetEntity.getType().is(this.targetTag)) {
            generatedLoot.add(new ItemStack(ItemsPM.BLOOD_NOTES.get()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
