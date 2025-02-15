package com.pz.showMySkin.client.gui.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pz.showMySkin.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EnchantmentButton extends Button {

    private static final ResourceLocation WIDGETS_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/widgets.png");
    private final EquipmentSlot slot;
    private final ItemStack enchantedBook;


    public EnchantmentButton(int x, int y, int width, int height, EquipmentSlot slot, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.slot = slot;
        // 创建附魔书物品
        this.enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 渲染按钮背景
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int textureY = this.isHovered() ? 86 : 66;
        guiGraphics.blit(WIDGETS_TEXTURE, this.getX(), this.getY(), 0, textureY, this.width, this.height);

        // 渲染附魔书图标
        guiGraphics.renderItem(this.enchantedBook, this.getX() + 2, this.getY() + 2);

        // 如果禁用了附魔效果，渲染一个灰色遮罩
        if (!getEnchantmentState()) {
            guiGraphics.fill(this.getX() + 2, this.getY() + 2,
                    this.getX() + this.width - 2, this.getY() + this.height - 2,
                    0x80808080); // 半透明灰色
        }

        // 渲染鼠标悬停提示
        if (this.isHovered()) {
            guiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    Component.translatable("show_my_skin.tooltip." + getArmorTypeName(slot) +
                            (getEnchantmentState() ? ".enchant.enabled" : ".enchant.disabled")),
                    mouseX, mouseY
            );
        }
    }


    private String getArmorTypeName(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> "unknown";
        };
    }

    private boolean getEnchantmentState() {
        return switch (slot) {
            case HEAD -> Config.helmetEnchantGlow.get();
            case CHEST -> Config.chestplateEnchantGlow.get();
            case LEGS -> Config.leggingsEnchantGlow.get();
            case FEET -> Config.bootsEnchantGlow.get();
            default -> true;
        };
    }
}
