package com.pz.showMySkin.client.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TextButton extends Button {
    private final String text;
    private boolean isActive;

    public TextButton(int x, int y, int width, int height, String text, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.text = text;
        this.isActive = true;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        // 居中渲染文本
        int textX = getX() + (width - Minecraft.getInstance().font.width(text)) / 2;
        int textY = getY() + (height - 8) / 2;

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                text,
                textX,
                textY,
                isActive ? new Color(0xFFFFFF).getRGB() : new Color(0x7F7F7F).getRGB()
        );
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
    public boolean isActive() {
        return isActive;
    }
}