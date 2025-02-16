package com.pz.showMySkin.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.client.gui.parts.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;
import java.awt.*;


public class ArmorSettingsScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ShowMySkin.MODID, "textures/gui/settings.png");
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Screen lastScreen;
    private static final int BUTTON_SIZE = 20;
    private static final int PART_BUTTON_SIZE = 20;
    private static final int SLIDER_WIDTH = 100;
    private static final int ROW_HEIGHT = 25;
    private static final int SPACING = 5;
    private static final int EXPAND_SIZE = 20;

    private static final int PLAYER_RENDER_SIZE = 120;
    private float modelRotation = 0f;
    private static final float ROTATION_SPEED = 1f;



    private final boolean[] expandedStates = new boolean[4]; // HEAD, CHEST, LEGS, FEET



    public ArmorSettingsScreen(Screen lastScreen) {
        super(Component.translatable("show_my_skin.settings.title"));
        this.lastScreen = lastScreen;

    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        // 设置渲染状态
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 计算纹理绘制位置，使其居中显示
        int x = (this.width - TEXTURE_WIDTH) / 2;
        int y = (this.height - TEXTURE_HEIGHT) / 2;

        // 渲染自定义背景纹理
        guiGraphics.blit(TEXTURE, x, y, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // 恢复渲染状态
        RenderSystem.disableBlend();
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();

        // 计算左侧控制区域的起始位置
        int leftPanelWidth = BUTTON_SIZE + SLIDER_WIDTH + BUTTON_SIZE + EXPAND_SIZE + SPACING * 3;
        int leftX = this.width / 2 - PLAYER_RENDER_SIZE;
        int startY = this.height / 2 - 80;

        // 添加每个盔甲部件的控制行
        addArmorControlRow(EquipmentSlot.HEAD, leftX, startY, 0);
        addArmorControlRow(EquipmentSlot.CHEST, leftX, startY + ROW_HEIGHT * 2, 1);
        addArmorControlRow(EquipmentSlot.LEGS, leftX, startY + ROW_HEIGHT * 4, 2);
        addArmorControlRow(EquipmentSlot.FEET, leftX, startY + ROW_HEIGHT * 6, 3);

        // 添加完成按钮
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.done"),
                        button -> {
                            Config.SPEC.save();
                            this.minecraft.setScreen(this.lastScreen);
                        })
                .pos(leftX + leftPanelWidth / 2 - 50, startY + ROW_HEIGHT * 8)
                .size(100, 20)
                .build());

    }

    private void addArmorControlRow(EquipmentSlot slot, int x, int y, int index) {
        // 主控制行
        // 可见性按钮（带盔甲图标）
        addRenderableWidget(new ArmorVisibilityButton(
                x, y, BUTTON_SIZE, BUTTON_SIZE,
                slot,
                button -> toggleArmorVisibility(slot)
        ));

        // 透明度滑块
        addRenderableWidget(new OpacitySlider(
                x + BUTTON_SIZE + SPACING,
                y,
                SLIDER_WIDTH,
                BUTTON_SIZE,
                getOpacityValue(slot),
                getTranslationKey(slot),
                value -> setOpacityValue(slot, value)
        ));

        // 附魔效果按钮
        addRenderableWidget(new EnchantmentButton(
                x + BUTTON_SIZE + SLIDER_WIDTH + SPACING * 2,
                y,
                BUTTON_SIZE,
                BUTTON_SIZE,
                slot,
                button -> toggleEnchantmentGlow(slot)
        ));

        // 展开/折叠按钮
        addRenderableWidget(new ExpandButton(
                x + BUTTON_SIZE + SLIDER_WIDTH + BUTTON_SIZE + SPACING * 3,
                y,
                EXPAND_SIZE,
                BUTTON_SIZE,
                () -> expandedStates[index],
                expanded ->{
                    expandedStates[index] = expanded;
                    this.init(this.minecraft, this.width, this.height);
                }
        ));

        // 如果处于展开状态，添加部位控制按钮
        if (expandedStates[index]) {
            addPartButtons(slot, x + SPACING, y + BUTTON_SIZE + SPACING);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        // 更新模型旋转角度
        modelRotation += ROTATION_SPEED;

        // 渲染标题
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.title,
                this.width / 2, 20, new Color(0xFFFFFF).getRGB());

        // 渲染分隔线
        int centerX = this.width / 2;
        guiGraphics.fill(centerX - 1, 40, centerX + 1, this.height - 20, new Color(0x80FFFFFF).getRGB());


        int centerY = this.height / 2 - 30;
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                centerX,              // x1 (左边界)
                centerY - 60,        // y1 (上边界)
                centerX + PLAYER_RENDER_SIZE,  // x2 (右边界)
                centerY + 60,        // y2 (下边界)
                45,                  // scale (缩放)
                0.0F,               // yOffset (Y轴偏移)
                mouseX,             // mouseX (鼠标X坐标)
                mouseY,             // mouseY (鼠标Y坐标)
                this.minecraft.player // entity (要渲染的实体)
        );

    }

    private void addPartButtons(EquipmentSlot slot, int x, int y) {
        switch (slot) {
            case HEAD -> {
                addPartButton("head", x, y, Config.helmetHeadVisible);
                addPartButton("hat", x + PART_BUTTON_SIZE + SPACING, y, Config.helmetHatVisible);
            }
            case CHEST -> {
                addPartButton("body", x, y, Config.chestplateBodyVisible);
                addPartButton("rightArm", x + PART_BUTTON_SIZE + SPACING, y, Config.chestplateRightArmVisible);
                addPartButton("leftArm", x + (PART_BUTTON_SIZE + SPACING) * 2, y, Config.chestplateLeftArmVisible);
            }
            case LEGS -> {
                addPartButton("body", x, y, Config.leggingsBodyVisible);
                addPartButton("rightLeg", x + PART_BUTTON_SIZE + SPACING, y, Config.leggingsRightLegVisible);
                addPartButton("leftLeg", x + (PART_BUTTON_SIZE + SPACING) * 2, y, Config.leggingsLeftLegVisible);
            }
            case FEET -> {
                addPartButton("rightLeg", x, y, Config.bootsRightLegVisible);
                addPartButton("leftLeg", x + PART_BUTTON_SIZE + SPACING, y, Config.bootsLeftLegVisible);
            }
        }
    }

    private void addPartButton(String part, int x, int y, ModConfigSpec.BooleanValue config) {
        addRenderableWidget(new BodyPartButton(
                x, y, PART_BUTTON_SIZE, PART_BUTTON_SIZE,
                part,
                config::get,
                config::set
        ));
    }

    private String getTranslationKey(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> "show_my_skin.settings.helmet_opacity";
            case CHEST -> "show_my_skin.settings.chestplate_opacity";
            case LEGS -> "show_my_skin.settings.leggings_opacity";
            case MAINHAND, OFFHAND, BODY -> null;
            case FEET -> "show_my_skin.settings.boots_opacity";
        };
    }

    private double getOpacityValue(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> Config.helmetOpacity.get();
            case CHEST -> Config.chestplateOpacity.get();
            case LEGS -> Config.leggingsOpacity.get();
            case FEET -> Config.bootsOpacity.get();
            default -> throw new IllegalStateException("Unexpected value: " + slot);
        };
    }

    private void setOpacityValue(EquipmentSlot slot, double value) {
        switch (slot) {
            case HEAD -> Config.helmetOpacity.set((int) value);
            case CHEST -> Config.chestplateOpacity.set((int) value);
            case LEGS -> Config.leggingsOpacity.set((int) value);
            case FEET -> Config.bootsOpacity.set((int) value);
        }
    }

    private void toggleArmorVisibility(EquipmentSlot slot) {
        switch (slot) {
            case HEAD -> Config.helmetVisible.set(!Config.helmetVisible.get());
            case CHEST -> Config.chestplateVisible.set(!Config.chestplateVisible.get());
            case LEGS -> Config.leggingsVisible.set(!Config.leggingsVisible.get());
            case FEET -> Config.bootsVisible.set(!Config.bootsVisible.get());
        }
    }

    private void toggleEnchantmentGlow(EquipmentSlot slot) {
        switch (slot) {
            case HEAD -> Config.helmetEnchantGlow.set(!Config.helmetEnchantGlow.get());
            case CHEST -> Config.chestplateEnchantGlow.set(!Config.chestplateEnchantGlow.get());
            case LEGS -> Config.leggingsEnchantGlow.set(!Config.leggingsEnchantGlow.get());
            case FEET -> Config.bootsEnchantGlow.set(!Config.bootsEnchantGlow.get());
        }
    }
}
