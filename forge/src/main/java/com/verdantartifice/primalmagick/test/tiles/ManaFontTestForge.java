package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.mana_font")
public class ManaFontTestForge extends AbstractManaFontTest {
    @GameTestGenerator
    public Collection<TestFunction> font_siphoned_by_mundane_wand() {
        return super.font_siphoned_by_wand(TestUtilsForge.DEFAULT_TEMPLATE);
    }

//    @GameTestGenerator
//    public Collection<TestFunction> font_siphoned_by_modular_wand() {
//        return super.font_siphoned_by_wand(IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE),
//                TestUtilsForge.DEFAULT_TEMPLATE);
//    }
//
//    @GameTestGenerator
//    public Collection<TestFunction> font_siphoned_by_modular_staff() {
//        return super.font_siphoned_by_wand(IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE),
//                TestUtilsForge.DEFAULT_TEMPLATE);
//    }
}
