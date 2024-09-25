package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec2;

public class DisplayInfoBuilder {
    protected final String id;
    protected ItemStack iconStack = ItemStack.EMPTY;
    protected Optional<ResourceLocation> background = Optional.empty();
    protected AdvancementType type = AdvancementType.TASK;
    protected boolean showToast = true;
    protected boolean announceChat = true;
    protected boolean hidden = false;
    protected Optional<Vec2> location = Optional.empty();
    
    protected DisplayInfoBuilder(String id) {
        this.id = id;
    }
    
    public static DisplayInfoBuilder id(String id) {
        return new DisplayInfoBuilder(id);
    }
    
    public DisplayInfoBuilder icon(ItemStack iconStack) {
        this.iconStack = iconStack.copyWithCount(1);
        return this;
    }
    
    public DisplayInfoBuilder icon(ItemLike iconItem) {
        return this.icon(new ItemStack(iconItem.asItem()));
    }
    
    public DisplayInfoBuilder background(ResourceLocation background) {
        this.background = Optional.ofNullable(background);
        return this;
    }
    
    public DisplayInfoBuilder type(AdvancementType type) {
        this.type = type;
        return this;
    }
    
    public DisplayInfoBuilder noToast() {
        this.showToast = false;
        return this;
    }
    
    public DisplayInfoBuilder noChat() {
        this.announceChat = false;
        return this;
    }
    
    public DisplayInfoBuilder hidden() {
        this.hidden = true;
        return this;
    }
    
    public DisplayInfoBuilder location(float x, float y) {
        this.location = Optional.of(new Vec2(x, y));
        return this;
    }
    
    private void validate() {
        if (this.iconStack == null || this.iconStack.isEmpty()) {
            throw new IllegalStateException("No icon specified for advancement display info");
        }
    }
    
    protected Component makeTitle(String id) {
        return Component.translatable(String.join(".", "advancements", Constants.MOD_ID, id, "title"));
    }
    
    protected Component makeDescription(String id) {
        return Component.translatable(String.join(".", "advancements", Constants.MOD_ID, id, "description"));
    }
    
    public DisplayInfo build() {
        this.validate();
        DisplayInfo retVal = new DisplayInfo(this.iconStack, makeTitle(this.id), makeDescription(this.id), this.background, this.type, this.showToast, this.announceChat, this.hidden);
        this.location.ifPresent(loc -> retVal.setLocation(loc.x, loc.y));
        return retVal;
    }
}
