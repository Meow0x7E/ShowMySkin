package com.pz.showMySkin.client.gui.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemButton extends Button {


    private final ItemStack displayItem;
    private final ItemStack disabledOverlay;
    private boolean isActive;

    public ItemButton(int x, int y, int width, int height, ItemStack displayItem, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.displayItem = displayItem;
        this.disabledOverlay = new ItemStack(Items.BARRIER);
        this.isActive = true;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        // 居中渲染物品
        int itemX = getX() + (width - 16) / 2;
        int itemY = getY() + (height - 16) / 2;

        // 渲染主要物品
        guiGraphics.renderItem(displayItem, itemX, itemY);

        // 如果未激活，渲染屏障物品
        if (!isActive) {
            // 保存当前渲染状态
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // 设置半透明的颜色
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.75F);

            // 渲染屏障物品
            guiGraphics.renderItem(disabledOverlay, itemX, itemY);

            // 恢复渲染状态
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }
}
