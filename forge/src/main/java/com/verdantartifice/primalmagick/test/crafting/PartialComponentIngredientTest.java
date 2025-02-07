package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.crafting.ingredients.PartialComponentIngredient;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.partial_component_ingredient")
public class PartialComponentIngredientTest extends AbstractBaseTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    public void predicate_works(GameTestHelper helper) {
        var ingredient = PartialComponentIngredient.builder()
                .item(ItemsPM.CONCOCTION.get())
                .data(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                .data(DataComponentsPM.CONCOCTION_TYPE.get(), ConcoctionType.WATER)
                .build();
        
        // Confirm that a standard water flask matches
        var stack = ConcoctionUtils.newConcoction(Potions.WATER, ConcoctionType.WATER);
        helper.assertTrue(ingredient.test(stack), "Water flask not a match");
        
        // Confirm that the same water flask still matches even when it's low on doses
        ConcoctionUtils.setCurrentDoses(stack, 1);
        helper.assertTrue(ingredient.test(stack), "Drained water flask not a match");
        
        // Confirm that a concoction of a different type does not match
        helper.assertFalse(ingredient.test(ConcoctionUtils.newConcoction(Potions.FIRE_RESISTANCE, ConcoctionType.TINCTURE)), "Fire resistance tincture incorrectly matches");
        
        // Confirm that a standard water bomb does not match
        helper.assertFalse(ingredient.test(ConcoctionUtils.newBomb(Potions.WATER)), "Water bomb incorrectly matches");

        helper.succeed();
    }
}
