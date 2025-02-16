package com.pz.showMySkin.client.gui.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.function.BooleanSupplier;


public class BodyPartButton extends Button {
    private final String part;
    private final BooleanSupplier getter;
    private final BooleanConsumer setter;
    private static final ItemStack BARRIER = new ItemStack(Items.BARRIER);


    public BodyPartButton(int x, int y, int width, int height, String part,
                          BooleanSupplier getter, BooleanConsumer setter) {
        super(x, y, width, height, Component.empty(),
                button -> setter.accept(!getter.getAsBoolean()), DEFAULT_NARRATION);
        this.part = part;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean isVisible = getter.getAsBoolean();

        String text = part.substring(0,1).toUpperCase();
        int textX = this.getX() + (this.width -Minecraft.getInstance().font.width(text))/2;
        int textY = this.getY() + (this.height - Minecraft.getInstance().font.lineHeight) / 2;

        int textColor;
        if (isVisible) {
            textColor = this.isHovered() ? new Color(0x55FFAA).getRGB() : new Color(0xFF00FF00).getRGB(); // 可见状态：悬停时亮绿色，正常时绿色
        } else {
            textColor = this.isHovered() ? new Color(0xF55E4A).getRGB() : new Color(0xFFFF0000).getRGB(); // 隐藏状态：悬停时亮红色，正常时红色
        }

        guiGraphics.drawString(Minecraft.getInstance().font, text, textX, textY, textColor);

        // 如果部位被隐藏，渲染屏障方块图标
        if (!isVisible) {
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();

            float prevAlpha = RenderSystem.getShaderColor()[3];
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.7F);

            float scale = 0.8f;
            float xOffset = (this.width * (1 - scale)) / 2;
            float yOffset = (this.height * (1 - scale)) / 2;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + xOffset, this.getY() + yOffset, 100);
            guiGraphics.pose().scale(scale, scale, 1.0f);

            guiGraphics.renderItem(BARRIER, 0, 0);

            guiGraphics.pose().popPose();

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prevAlpha);
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
        }

        // 当鼠标悬停时显示提示文本
        if (this.isHovered()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font,
                    Component.translatable("show_my_skin.part." + part +
                            (isVisible ? ".visible" : ".hidden")),
                    mouseX, mouseY);
        }
    }
}