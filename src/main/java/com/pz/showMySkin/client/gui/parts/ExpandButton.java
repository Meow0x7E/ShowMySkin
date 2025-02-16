package com.pz.showMySkin.client.gui.parts;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;


import java.util.function.BooleanSupplier;

public class ExpandButton extends Button {

    private final BooleanSupplier isExpanded;
    private final BooleanConsumer setExpanded;

    public ExpandButton(int x, int y, int width, int height,
                        BooleanSupplier isExpanded, BooleanConsumer setExpanded) {
        super(x, y, width, height, Component.empty(),
                button -> setExpanded.accept(!isExpanded.getAsBoolean()),DEFAULT_NARRATION);
        this.isExpanded = isExpanded;
        this.setExpanded = setExpanded;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean expanded = isExpanded.getAsBoolean();

        // 渲染展开/折叠图标
        String text = expanded ? "▼" : "▶";
        int textWidth = Minecraft.getInstance().font.width(text);
        int textX = this.getX() + (this.width - textWidth) / 2;
        int textY = this.getY() + (this.height - Minecraft.getInstance().font.lineHeight) / 2;

        int textColor = this.isHovered() ? 0xFFFFFF55 : 0xFFFFFFFF;
        guiGraphics.drawString(Minecraft.getInstance().font, text, textX, textY, textColor);

        if (this.isHovered()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font,
                    Component.translatable("show_my_skin.expand." +
                            (expanded ? "collapse" : "expand")),
                    mouseX, mouseY);
        }
    }
}
