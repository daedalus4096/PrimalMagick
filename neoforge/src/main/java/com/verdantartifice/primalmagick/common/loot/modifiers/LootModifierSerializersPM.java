package com.verdantartifice.primalmagick.common.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Deferred registry for the mod's global loot modifier serializers.
 * 
 * @author Daedalus4096
 */
public class LootModifierSerializersPM {
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MOD_ID);
    
    public static void init() {
        SERIALIZERS.register(PrimalMagick.getEventBus());
    }
    
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BloodyFleshModifier>> BLOODY_FLESH = SERIALIZERS.register("bloody_flesh", () -> BloodyFleshModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BonusNuggetModifier>> BONUS_NUGGET = SERIALIZERS.register("bonus_nugget", () -> BonusNuggetModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BountyFarmingModifier>> BOUNTY_FARMING = SERIALIZERS.register("bounty_farming", () -> BountyFarmingModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BountyFishingModifier>> BOUNTY_FISHING = SERIALIZERS.register("bounty_fishing", () -> BountyFishingModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BloodNotesModifier>> BLOOD_NOTES = SERIALIZERS.register("blood_notes", () -> BloodNotesModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RelicFragmentsModifier>> RELIC_FRAGMENTS = SERIALIZERS.register("relic_fragments", () -> RelicFragmentsModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FourLeafCloverModifier>> FOUR_LEAF_CLOVER = SERIALIZERS.register("four_leaf_clover", () -> FourLeafCloverModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemModifier>> ADD_ITEM = SERIALIZERS.register("add_item", () -> AddItemModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<EssenceThiefModifier>> ESSENCE_THIEF = SERIALIZERS.register("essence_thief", () -> EssenceThiefModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceItemModifier>> REPLACE_ITEM = SERIALIZERS.register("replace_item", () -> ReplaceItemModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GuillotineModifier>> GUILLOTINE = SERIALIZERS.register("guillotine", () -> GuillotineModifier.CODEC);
}
