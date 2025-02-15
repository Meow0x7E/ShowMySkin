package com.pz.showMySkin.client.gui;

import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.client.gui.parts.ArmorVisibilityButton;
import com.pz.showMySkin.client.gui.parts.EnchantmentButton;
import com.pz.showMySkin.client.gui.parts.OpacitySlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorSettingsScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ShowMySkin.MODID, "textures/gui/settings.png");
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    private final Screen lastScreen;
    private static final int BUTTON_SIZE = 20;
    private static final int SLIDER_WIDTH = 100;
    private static final int ROW_HEIGHT = 25;
    private static final int SPACING = 5;

    public ArmorSettingsScreen(Screen lastScreen) {
        super(Component.translatable("show_my_skin.settings.title"));
        this.lastScreen = lastScreen;
    }


    protected ArmorSettingsScreen(Component title, Screen lastScreen) {
        super(title);
        this.lastScreen = lastScreen;
    }


    @Override
    protected void init() {
        super.init();
        int leftX = this.width / 2 - (BUTTON_SIZE + SLIDER_WIDTH + BUTTON_SIZE + SPACING * 2) / 2;
        int startY = this.height / 4;

        // 添加每个盔甲部件的控制行
        addArmorControls(EquipmentSlot.HEAD, leftX, startY);
        addArmorControls(EquipmentSlot.CHEST, leftX, startY + ROW_HEIGHT);
        addArmorControls(EquipmentSlot.LEGS, leftX, startY + ROW_HEIGHT * 2);
        addArmorControls(EquipmentSlot.FEET, leftX, startY + ROW_HEIGHT * 3);

        // 添加完成按钮
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.done"),
                        button -> this.minecraft.setScreen(this.lastScreen))
                .pos(this.width / 2 - 50, startY + ROW_HEIGHT * 4 + 10)
                .size(100, 20)
                .build());
    }

    private void addArmorControls(EquipmentSlot slot, int x, int y) {
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

        // 附魔效果开关
        addRenderableWidget(new EnchantmentButton(
                x + BUTTON_SIZE + SLIDER_WIDTH + SPACING * 2,
                y,
                BUTTON_SIZE,
                BUTTON_SIZE,
                slot,
                button -> toggleEnchantmentGlow(slot)
        ));
    }


    // 辅助方法
    private String getTranslationKey(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> "show_my_skin.settings.helmet_opacity";
            case CHEST -> "show_my_skin.settings.chestplate_opacity";
            case LEGS -> "show_my_skin.settings.leggings_opacity";
            case MAINHAND -> null;
            case OFFHAND -> null;
            case FEET -> "show_my_skin.settings.boots_opacity";
            case BODY -> null;
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
