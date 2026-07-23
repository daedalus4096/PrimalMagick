package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.screens.GrimoireScreen;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Display widget for showing all possible item stacks for a given recipe slot display. Used on
 * recipe pages.
 *
 * @author Daedalus4096
 */
public class SlotDisplayWidget extends Button {
    protected final GrimoireScreen screen;
    protected final List<ItemStack> resolvedStacks;
    protected ItemStack lastStack = ItemStack.EMPTY;
    protected ItemStack currentStack = ItemStack.EMPTY;

    public SlotDisplayWidget(@Nullable SlotDisplay display, int x, int y, GrimoireScreen screen) {
        super(x, y, 16, 16, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
        this.screen = screen;
        Minecraft mc = Minecraft.getInstance();
        ContextMap contextMap = SlotDisplayContext.fromLevel(Objects.requireNonNull(mc.level));
        this.resolvedStacks = display == null ? List.of() : display.resolveForStacks(contextMap);
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphicsExtractor, int x, int y, float partialTick) {
        this.lastStack = this.currentStack;
        this.currentStack = this.getDisplayStack();
        if (!this.currentStack.isEmpty()) {
            GuiUtils.renderItemStack(guiGraphicsExtractor, this.currentStack, this.getX(), this.getY(), this.getMessage().getString(), false);

            // Update the widget's tooltip if necessary
            this.updateTooltip();
        }

        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
    }

    @Override
    protected boolean isValidClickButton(@NotNull MouseButtonInfo buttonInfo) {
        ItemStack displayStack = this.getDisplayStack();
        return super.isValidClickButton(buttonInfo) && !displayStack.isEmpty() && this.screen.isIndexKey(displayStack.getHoverName().getString());
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }

    protected void updateTooltip() {
        if (!ItemStack.isSameItemSameComponents(this.currentStack, this.lastStack)) {
            Minecraft mc = Minecraft.getInstance();
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.currentStack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player,
                    mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
        }
    }

    @NotNull
    protected ItemStack getDisplayStack() {
        if (this.resolvedStacks.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (this.resolvedStacks.size() == 1) {
            return this.resolvedStacks.getFirst();
        } else {
            int length = this.resolvedStacks.size();
            int index = ((int)(System.currentTimeMillis() / 1000L) % length);
            return this.resolvedStacks.get(index);
        }
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(@NotNull Button button) {
            if (button instanceof SlotDisplayWidget sdw) {
                // Set the new grimoire topic and open a new screen for it
                sdw.getScreen().gotoTopic(new OtherResearchTopic(sdw.getDisplayStack().getHoverName().getString(), 0));
            }
        }
    }
}
