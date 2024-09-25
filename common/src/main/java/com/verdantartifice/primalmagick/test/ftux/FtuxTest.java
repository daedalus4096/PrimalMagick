package com.verdantartifice.primalmagick.test.ftux;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.crafting.SimpleCraftingContainer;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;

@GameTestHolder(Constants.MOD_ID + ".ftux")
public class FtuxTest {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @GameTestGenerator
    public static Collection<TestFunction> font_discovery_tests() {
        Map<String, Block> testParams = ImmutableMap.<String, Block>builder()
                .put("earth", BlocksPM.ANCIENT_FONT_EARTH.get())
                .put("sea", BlocksPM.ANCIENT_FONT_SEA.get())
                .put("sky", BlocksPM.ANCIENT_FONT_SKY.get())
                .put("sun", BlocksPM.ANCIENT_FONT_SUN.get())
                .put("moon", BlocksPM.ANCIENT_FONT_MOON.get())
                .build();
        return TestUtils.createParameterizedTestFunctions("font_discovery_tests", testParams, (helper, block) -> {
            // Create a player in the level and confirm that they start out not having found a shrine
            @SuppressWarnings("removal")
            ServerPlayer player = helper.makeMockServerPlayerInLevel();
            player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(BlockPos.ZERO)));
            helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research already present on new player");

            // Place an ancient font near the player
            BlockPos fontPos = new BlockPos(1, 1, 1);
            helper.setBlock(fontPos, block);
            helper.assertBlockState(fontPos, state -> {
                return state.is(block);
            }, () -> "Test font not found!");
            
            // Succeed once the player has been marked as having found a shrine
            helper.succeedWhen(() -> {
                helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE), "Found Shrine research not complete");
                helper.getLevel().getServer().getPlayerList().remove(player);
            });
        });
    }
    
    @GameTest(template = "primalmagick:test/floor5x5x5", timeoutTicks = 150)
    public static void sleeping_after_shrine_grants_dream(GameTestHelper helper) {
        // Create a player who's found a shrine and is primed for the dream, but hasn't had it yet
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.assertTrue(ResearchManager.completeResearch(player, ResearchEntries.FOUND_SHRINE), "Failed to grant prerequisite research");
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.GOT_DREAM), "Created player already marked as having had the dream");
        helper.assertTrue(player.getInventory().isEmpty(), "Fresh player inventory is not empty");
        
        // Place a bed and sleep in it
        BlockPos bedPos = new BlockPos(2, 2, 2);
        TestUtils.placeBed(helper, bedPos);
        helper.setNight();
        player.startSleepInBed(helper.absolutePos(bedPos));
        player.stopSleeping();
        
        // Succeed once the player has been marked as having had the dream and received the dream journal
        helper.succeedWhen(() -> {
            helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.GOT_DREAM), "Got Dream research not complete");
            NonNullList<ItemStack> stacks = InventoryUtils.find(player, ItemTagsPM.STATIC_BOOKS);
            helper.assertTrue(stacks.size() == 1, "No potential Dream Journals found");
            helper.assertTrue(stacks.stream().anyMatch(stack -> {
                return StaticBookItem.getBookId(stack).filter(id -> BooksPM.DREAM_JOURNAL.equals(id)).isPresent() &&
                        StaticBookItem.getBookLanguageId(stack).filter(id -> BookLanguagesPM.DEFAULT.equals(id)).isPresent() &&
                        StaticBookItem.getAuthor(stack).equals(player.getName());
            }), "Dream Journal components are not a match to expected");
        });
    }
    
    @GameTestGenerator
    public static Collection<TestFunction> mundane_wand_crafting_tests() {
        Map<String, Item> testParams = ImmutableMap.<String, Item>builder()
                .put("earth", ItemsPM.ESSENCE_DUST_EARTH.get())
                .put("sea", ItemsPM.ESSENCE_DUST_SEA.get())
                .put("sky", ItemsPM.ESSENCE_DUST_SKY.get())
                .put("sun", ItemsPM.ESSENCE_DUST_SUN.get())
                .put("moon", ItemsPM.ESSENCE_DUST_MOON.get())
                .build();
        return TestUtils.createParameterizedTestFunctions("mundane_wand_crafting_tests", testParams, (helper, dust) -> {
            var container = SimpleCraftingContainer.builder()
                    .pattern("SD ")
                    .define('S', Items.STICK)
                    .define('D', dust)
                    .build();
            var recipe = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, helper.getLevel());
            helper.assertTrue(recipe.isPresent(), "Recipe not found when expected");
            helper.assertTrue(recipe.get().value().getResultItem(helper.getLevel().registryAccess()).is(ItemsPM.MUNDANE_WAND.get()), "Recipe result does not match expectations");
            helper.succeed();
        });
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void transform_abort_gives_hint(GameTestHelper helper) {
        // Create a player who has gotten the dream
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.assertTrue(ResearchManager.completeResearch(player, ResearchEntries.GOT_DREAM), "Failed to grant prerequisite research");
        helper.assertFalse(ResearchManager.isResearchComplete(player, ResearchEntries.WAND_TRANSFORM_HINT), "Newly created player already has sought research");
        
        // Put a mundane wand in that player's main hand
        ItemStack wandStack = new ItemStack(ItemsPM.MUNDANE_WAND.get());
        Item wandItem = wandStack.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, wandStack);
        
        // Place a crafting table
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, Blocks.BOOKSHELF);
        helper.assertBlockPresent(Blocks.BOOKSHELF, pos);
        helper.assertItemEntityNotPresent(ItemsPM.GRIMOIRE.get(), pos, 1D);
        
        // Start transforming the table
        BlockPos posAbs = helper.absolutePos(pos);
        BlockHitResult blockHitResult = new BlockHitResult(Vec3.atCenterOf(posAbs), Direction.UP, posAbs, false);
        UseOnContext useContext = new UseOnContext(player, InteractionHand.MAIN_HAND, blockHitResult);
        helper.assertTrue(wandItem.onItemUseFirst(wandStack, useContext).equals(InteractionResult.SUCCESS), "Failed to start using wand on block");
        
        // Immediately stop transforming and check for hint flag research
        int remainingTicks = wandItem.getUseDuration(wandStack, player);
        wandItem.releaseUsing(wandStack, helper.getLevel(), player, remainingTicks);
        helper.assertTrue(ResearchManager.isResearchComplete(player, ResearchEntries.WAND_TRANSFORM_HINT), "Sought research not found");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void transform_grimoire(GameTestHelper helper) {
        // Create a player who has gotten the dream
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.assertTrue(ResearchManager.completeResearch(player, ResearchEntries.GOT_DREAM), "Failed to grant prerequisite research");
        
        // Put a mundane wand in that player's main hand
        ItemStack wandStack = new ItemStack(ItemsPM.MUNDANE_WAND.get());
        Item wandItem = wandStack.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, wandStack);
        
        // Place a crafting table
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, Blocks.BOOKSHELF);
        helper.assertBlockPresent(Blocks.BOOKSHELF, pos);
        helper.assertItemEntityNotPresent(ItemsPM.GRIMOIRE.get(), pos, 1D);
        
        // Start transforming the table
        BlockPos posAbs = helper.absolutePos(pos);
        BlockHitResult blockHitResult = new BlockHitResult(Vec3.atCenterOf(posAbs), Direction.UP, posAbs, false);
        UseOnContext useContext = new UseOnContext(player, InteractionHand.MAIN_HAND, blockHitResult);
        helper.assertTrue(wandItem.onItemUseFirst(wandStack, useContext).equals(InteractionResult.SUCCESS), "Failed to start using wand on block");
        
        // Continue channeling the wand until the transformation succeeds or the test times out and fails
        MutableInt remainingTicks = new MutableInt(wandItem.getUseDuration(wandStack, player));
        helper.onEachTick(() -> {
            wandItem.onUseTick(helper.getLevel(), player, wandStack, remainingTicks.decrementAndGet());
        });
        helper.succeedWhen(() -> {
            helper.assertBlockNotPresent(Blocks.BOOKSHELF, pos);
            helper.assertItemEntityPresent(ItemsPM.GRIMOIRE.get(), pos, 1D);
        });
    }
}
