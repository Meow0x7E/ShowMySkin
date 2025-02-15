package com.pz.showMySkin.client.gui.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ArmorVisibilityButton extends Button {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ShowMySkin.MODID,"textures/gui/texture.png");
    private final EquipmentSlot slot;
    private final ItemStack armorItem;

    public ArmorVisibilityButton(int x, int y, int width, int height, EquipmentSlot slot, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.slot = slot;
        // 为每个槽位创建对应的下界合金盔甲物品
        this.armorItem = getNetheriteArmorItem(slot);
    }


    private ItemStack getNetheriteArmorItem(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> new ItemStack(Items.NETHERITE_HELMET);
            case CHEST -> new ItemStack(Items.NETHERITE_CHESTPLATE);
            case LEGS -> new ItemStack(Items.NETHERITE_LEGGINGS);
            case FEET -> new ItemStack(Items.NETHERITE_BOOTS);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        int textureY = this.isHovered() ? 86 : 66;
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, textureY, this.width, this.height);
        // 渲染物品图标
        guiGraphics.renderItem(this.armorItem, this.getX() + 2, this.getY() + 2);

        // 如果不可见，渲染一个半透明的红色遮罩
        if (!getVisibilityState()) {
            guiGraphics.fill(this.getX() + 2, this.getY() + 2,
                    this.getX() + this.width - 2, this.getY() + this.height - 2,
                    0x80FF0000); // 半透明红色
        }

        // 如果鼠标悬停，显示提示文本
        if (this.isHovered()) {
            guiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    Component.translatable("show_my_skin.tooltip." + getArmorTypeName(slot) +
                            (getVisibilityState() ? ".visible" : ".hidden")),
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

    private boolean getVisibilityState() {
        return switch (slot) {
            case HEAD -> Config.helmetVisible.get();
            case CHEST -> Config.chestplateVisible.get();
            case LEGS -> Config.leggingsVisible.get();
            case FEET -> Config.bootsVisible.get();
            default -> true;
        };
    }
}
