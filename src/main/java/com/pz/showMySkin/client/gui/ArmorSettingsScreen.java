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
    // GUI实际区域尺寸（从1,1开始，所以实际占用238x177）
    private static final int GUI_WIDTH = 236;
    private static final int GUI_HEIGHT = 175;

    // 边框宽度
    private static final int BORDER_WIDTH = 7;

    // 玩家模型渲染区域尺寸
    private static final int PLAYER_RENDER_WIDTH = 110;
    private static final int PLAYER_RENDER_HEIGHT = 140;

    // 控件相关常量
    private static final int BUTTON_SIZE = 20;
    private static final int PART_BUTTON_SIZE = 20;
    private static final int SLIDER_WIDTH = 100;
    private static final int ROW_HEIGHT = 25;
    private static final int SPACING = 5;

    private final Screen lastScreen;
    private final boolean[] expandedStates = new boolean[4]; // HEAD, CHEST, LEGS, FEET
    private float modelRotation = 0f;
    private static final float ROTATION_SPEED = 1f;


    public ArmorSettingsScreen(Screen lastScreen) {
        super(Component.translatable("show_my_skin.settings.title"));
        this.lastScreen = lastScreen;

    }

    @Override
    protected void init() {
        clearWidgets();
        // 计算GUI的起始位置，使其居中
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        // 计算控件区域（左侧区域）
        int controlsWidth = GUI_WIDTH - PLAYER_RENDER_WIDTH - BORDER_WIDTH * 2;
        int controlsLeft = guiLeft + BORDER_WIDTH;
        int controlsTop = guiTop + BORDER_WIDTH;

        // 计算可用空间和行间距
        int availableHeight = GUI_HEIGHT - BORDER_WIDTH * 2;
        int totalRows = 4; // 装备槽位数量
        int rowSpacing = Math.max(SPACING, (availableHeight - (totalRows * ROW_HEIGHT)) / (totalRows + 1));

        // 添加装备控制行
        int currentY = controlsTop + rowSpacing;
        this.addArmorControlRow(EquipmentSlot.HEAD, controlsLeft, currentY, controlsWidth, 0);
        this.addArmorControlRow(EquipmentSlot.CHEST, controlsLeft, currentY + (ROW_HEIGHT + rowSpacing), controlsWidth, 1);
        this.addArmorControlRow(EquipmentSlot.LEGS, controlsLeft, currentY + (ROW_HEIGHT + rowSpacing) * 2, controlsWidth, 2);
        this.addArmorControlRow(EquipmentSlot.FEET, controlsLeft, currentY + (ROW_HEIGHT + rowSpacing) * 3, controlsWidth, 3);

        // 添加完成按钮
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.done"),
                        button -> {
                            try {
                                Config.SPEC.save();
                                LOGGER.info("Successfully saved ShowMySkin config");
                            } catch (Exception e) {
                                LOGGER.error("Failed to save ShowMySkin config: ", e);
                            }
                            this.minecraft.setScreen(this.lastScreen);
                        })
                .pos(guiLeft + (GUI_WIDTH - 100) / 2, guiTop + GUI_HEIGHT - BORDER_WIDTH - 25)
                .size(100, 20)
                .build());

        super.init();

    }
    private void addArmorControlRow(EquipmentSlot slot, int x, int y, int width, int index) {
        int minSpacing = 2;

        // 计算控件位置
        int availableWidth = width - BUTTON_SIZE * 2 - SLIDER_WIDTH - minSpacing * 3;
        int buttonX = x;
        int sliderX = buttonX + BUTTON_SIZE + minSpacing;
        int enchantX = sliderX + SLIDER_WIDTH + minSpacing;
        int expandX = enchantX + BUTTON_SIZE + minSpacing;

        // 添加主要控件
        addRenderableWidget(new ArmorVisibilityButton(
                buttonX, y, BUTTON_SIZE, BUTTON_SIZE,
                slot,
                button -> toggleArmorVisibility(slot)
        ));

        addRenderableWidget(new OpacitySlider(
                sliderX, y, SLIDER_WIDTH, BUTTON_SIZE,
                getOpacityValue(slot),
                getTranslationKey(slot),
                value -> setOpacityValue(slot, value)
        ));

        addRenderableWidget(new EnchantmentButton(
                enchantX, y, BUTTON_SIZE, BUTTON_SIZE,
                slot,
                button -> toggleEnchantmentGlow(slot)
        ));

        addRenderableWidget(new ExpandButton(
                expandX, y, BUTTON_SIZE, BUTTON_SIZE,
                () -> expandedStates[index],
                expanded -> {
                    expandedStates[index] = expanded;
                    init();
                }
        ));

        // 如果展开状态，添加部件按钮
        if (expandedStates[index]) {
            int partY = y + BUTTON_SIZE + minSpacing;
            addPartButtons(slot, x, partY);
        }
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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // 计算GUI位置
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;

        // 渲染GUI背景
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        guiGraphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        RenderSystem.disableBlend();
        // 渲染标题
        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                guiLeft + GUI_WIDTH / 2,
                guiTop + 16,
                0xFFFFFF
        );

        // 计算玩家模型渲染区域
        int playerLeft = guiLeft + GUI_WIDTH - PLAYER_RENDER_WIDTH;
        int playerTop = guiTop + BORDER_WIDTH;

        // 渲染玩家模型
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                playerLeft,
                playerTop,
                playerLeft + PLAYER_RENDER_WIDTH,
                playerTop + PLAYER_RENDER_HEIGHT,
                45,
                0.0F,
                mouseX,
                mouseY,
                this.minecraft.player
        );
        // 更新模型旋转
        modelRotation += ROTATION_SPEED;
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
            case FEET -> "show_my_skin.settings.boots_opacity";
            default -> null;
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
