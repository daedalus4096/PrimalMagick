package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class ModularStaffManaTestNeoforge extends AbstractWandManaTest {
    @Override
    protected Item getTestWandItem() {
        return ItemsPM.MODULAR_STAFF.get();
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_get_and_add_mana() {
        return super.wand_can_get_and_add_mana(this.makeTestName("wand_can_get_and_add_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_add_too_much_mana() {
        return super.wand_cannot_add_too_much_mana(this.makeTestName("wand_cannot_add_too_much_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_get_all_mana() {
        return super.wand_can_get_all_mana(this.makeTestName("wand_can_get_all_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_consume_mana() {
        return super.wand_can_consume_mana(this.makeTestName("wand_can_consume_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_consume_more_mana_than_it_has() {
        return super.wand_cannot_consume_more_mana_than_it_has(this.makeTestName("wand_cannot_consume_more_mana_than_it_has"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_consume_multiple_types_of_mana() {
        return super.wand_can_consume_multiple_types_of_mana(this.makeTestName("wand_can_consume_multiple_types_of_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_consume_more_mana_than_it_has_with_multiple_types() {
        return super.wand_cannot_consume_more_mana_than_it_has_with_multiple_types(this.makeTestName("wand_cannot_consume_more_mana_than_it_has_with_multiple_types"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_consume_real_mana() {
        return super.wand_can_consume_real_mana(this.makeTestName("wand_can_consume_real_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_consume_more_real_mana_than_it_has() {
        return super.wand_cannot_consume_more_real_mana_than_it_has(this.makeTestName("wand_cannot_consume_more_real_mana_than_it_has"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_consume_multiple_types_of_real_mana() {
        return super.wand_can_consume_multiple_types_of_real_mana(this.makeTestName("wand_can_consume_multiple_types_of_real_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_consume_more_real_mana_than_it_has_with_multiple_types() {
        return super.wand_cannot_consume_more_real_mana_than_it_has_with_multiple_types(this.makeTestName("wand_cannot_consume_more_real_mana_than_it_has_with_multiple_types"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_can_remove_mana_raw() {
        return super.wand_can_remove_mana_raw(this.makeTestName("wand_can_remove_mana_raw"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_cannot_remove_more_raw_mana_than_it_has() {
        return super.wand_cannot_remove_more_raw_mana_than_it_has(this.makeTestName("wand_cannot_remove_more_raw_mana_than_it_has"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_contains_mana() {
        return super.wand_contains_mana(this.makeTestName("wand_contains_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_contains_mana_list() {
        return super.wand_contains_mana_list(this.makeTestName("wand_contains_mana_list"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_contains_real_mana() {
        return super.wand_contains_real_mana(this.makeTestName("wand_contains_real_mana"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_contains_real_mana_list() {
        return super.wand_contains_real_mana_list(this.makeTestName("wand_contains_real_mana_list"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }

    @GameTestGenerator
    public Collection<TestFunction> wand_contains_mana_raw() {
        return super.wand_contains_mana_raw(this.makeTestName("wand_contains_mana_raw"), TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
