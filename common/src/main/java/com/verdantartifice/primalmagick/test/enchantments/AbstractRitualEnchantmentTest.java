package com.verdantartifice.primalmagick.test.enchantments;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.loot.LootModifiers;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractRitualEnchantmentTest extends AbstractBaseTest {
    public Collection<TestFunction> essence_thief(String templateName) {
        var testParams = IntStream.rangeClosed(1, 4).boxed().collect(Collectors.toMap(i -> "level" + i, i -> i));
        return TestUtils.createParameterizedTestFunctions("essence_thief", templateName, testParams, (helper, enchLevel) -> {
            // Create a test player with a weapon enchanted with the tested level of Essence Thief
            var player = this.makeMockServerPlayer(helper);
            var swordStack = new ItemStack(Items.DIAMOND_SWORD);
            var enchantment = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentsPM.ESSENCE_THIEF);
            swordStack.enchant(enchantment, enchLevel);
            player.setItemInHand(InteractionHand.MAIN_HAND, swordStack);

            // Create a test entity to get loot from; cows have only blood affinity, so there's no randomness in the
            // source of essence generated
            var target = helper.spawnWithNoFreeWill(EntityType.COW, BlockPos.ZERO);

            // Assemble a loot context
            var lootParams = new LootParams.Builder(helper.getLevel())
                    .withParameter(LootContextParams.THIS_ENTITY, target)
                    .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchLevel)
                    .withParameter(LootContextParams.ORIGIN, target.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, player.damageSources().playerAttack(player))
                    .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                    .create(LootContextParamSets.ENCHANTED_DAMAGE);
            var lootContext = new LootContext.Builder(lootParams).create(Optional.empty());

            // Run the loot modifier for an otherwise empty starting loot list
            var initialLoot = new ObjectArrayList<ItemStack>();
            var actualLoot = LootModifiers.essenceThief(initialLoot, lootContext);

            // Confirm that the modified loot contains only the expected grade of essence
            helper.assertTrue(actualLoot.size() == 1, "Modified loot does not contain exactly one item");
            var stack = actualLoot.getFirst();
            helper.assertTrue(stack.getCount() == 1, "Modified loot stack does not have a count of one");
            var essenceItem = assertInstanceOf(helper, stack.getItem(), EssenceItem.class, "Loot item is not essence");
            var expectedEssenceType = switch (enchLevel) {
                case 1 -> EssenceType.DUST;
                case 2 -> EssenceType.SHARD;
                case 3 -> EssenceType.CRYSTAL;
                case 4 -> EssenceType.CLUSTER;
                default -> throw new IllegalStateException("Unexpected value: " + enchLevel);
            };
            helper.assertTrue(essenceItem.getEssenceType().equals(expectedEssenceType), "Modified loot essence is not of expected grade");
            helper.assertTrue(essenceItem.getSource().equals(Sources.BLOOD), "Modified loot essence is not of expected source");

            helper.succeed();
        });
    }
}
