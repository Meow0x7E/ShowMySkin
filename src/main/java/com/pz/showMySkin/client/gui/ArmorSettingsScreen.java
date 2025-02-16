package com.pz.showMySkin.client.gui;

import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.client.gui.parts.ItemButton;
import com.pz.showMySkin.client.gui.parts.TextButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.breeze.Slide;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;

public class ArmorSettingsScreen extends Screen {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ShowMySkin.MODID, "textures/gui/settings.png");

    // 控制组件的尺寸常量
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 4;
    private static final int PART_BUTTON_SIZE = 16;
    private static final int PART_BUTTON_SPACING = 2;
    // GUI尺寸常量
    private static final int GUI_WIDTH = 236;
    private static final int GUI_HEIGHT = 175;
    private static final int BORDER_SIZE = 7;

    // 装备槽位的垂直间距
    private static final int SLOT_SPACING = 40;

    // 用于定位的变量
    private int guiLeft;
    private int guiTop;

    // 玩家模型区域常量
    private static final int PLAYER_VIEW_WIDTH = 110;
    private static final int PLAYER_VIEW_HEIGHT = 140;

    // 存储控件的Map
    private final Map<EquipmentSlot, ItemButton> visibilityButtons = new HashMap<>();
    private final Map<EquipmentSlot, ExtendedSlider> opacitySliders = new HashMap<>();
    private final Map<EquipmentSlot, ItemButton> enchantButtons = new HashMap<>();
    private final Map<String, TextButton> partButtons = new HashMap<>();

    Button saveButton;
    // 装备物品Map
    private final Map<EquipmentSlot, ItemStack> armorItems = new HashMap<>();
    public ArmorSettingsScreen(Component title) {
        super(title);

        // 初始化装备ItemStack
        armorItems.put(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        armorItems.put(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        armorItems.put(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        armorItems.put(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
    }


    @Override
    protected void init() {
        this.guiLeft = (this.width - GUI_WIDTH)/2;
        this.guiTop = (this.height - GUI_HEIGHT)/2;

        // 添加装备控制组
        int startY = guiTop + BORDER_SIZE + 10;

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET}){
            createSlotControls(slot, guiLeft + BORDER_SIZE + 5, startY);
            startY += SLOT_SPACING;

        }
        this.saveButton =Button.builder(Component.literal("save"), button -> onClose())
                .pos(guiLeft + GUI_WIDTH / 2 +48, guiTop + GUI_HEIGHT - 10)
                .size(80, 20)
                .build();
        // 添加保存按钮
        addRenderableWidget(saveButton);

        super.init();
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics,mouseX,mouseY,partialTick);

        guiGraphics.setColor(1,1,1,1);
        guiGraphics.blit(TEXTURE,guiLeft,guiTop,GUI_WIDTH+27,GUI_HEIGHT+20,1,1,236,176,256,256);
        // 渲染玩家模型
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                guiLeft +126 - PLAYER_VIEW_WIDTH +BORDER_SIZE,
                guiTop + BORDER_SIZE,
                PLAYER_VIEW_WIDTH +guiLeft +GUI_WIDTH ,
                PLAYER_VIEW_HEIGHT +guiTop,
                45,
                0.0F,
                mouseX,
                mouseY,
                this.minecraft.player
        );


        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (visibilityButtons.containsKey(slot)) {
                visibilityButtons.get(slot).render(guiGraphics, mouseX, mouseY, partialTick);
            }
            if (opacitySliders.containsKey(slot)) {
                opacitySliders.get(slot).render(guiGraphics, mouseX, mouseY, partialTick);
            }
            if (enchantButtons.containsKey(slot)) {
                enchantButtons.get(slot).render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        for (TextButton button : partButtons.values()) {
            button.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        saveButton.render(guiGraphics, mouseX, mouseY, partialTick);

    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        super.renderMenuBackground(guiGraphics, x, y, width, height);
    }

    private void createSlotControls(EquipmentSlot slot , int x, int y) {
        ItemButton visibilityBtn = new ItemButton(
                x,y,
                20,20,
                armorItems.get(slot),
                button -> toggleArmorVisibility(slot)
        );
        visibilityBtn.setActive(getArmorVisibility(slot));
        visibilityButtons.put(slot,visibilityBtn);
        addRenderableWidget(visibilityBtn);

        ExtendedSlider opacitySlider = new ExtendedSlider(
                x + 25,
                y,
                80,
                20,
                Component.translatable("gui.showmyskin.opacity"),
                Component.empty(),
                0,
                100,
                getArmorOpacity(slot),
                true
        ){
            @Override
            protected void updateMessage() {
                setMessage(Component.translatable("gui.showmyskin.opacity: " + (int)value));
            }

            @Override
            protected void applyValue() {
                setArmorOpacity(slot, (int)value);
            }
        };
        opacitySliders.put(slot, opacitySlider);
        addRenderableWidget(opacitySlider);

        // 创建附魔效果按钮
        ItemButton enchantBtn = new ItemButton(
                x + 110,
                y,
                20,
                20,
                new ItemStack(Items.ENCHANTED_BOOK),
                button -> toggleEnchantEffect(slot)
        );
        enchantBtn.setActive(getEnchantGlow(slot));
        enchantButtons.put(slot, enchantBtn);
        addRenderableWidget(enchantBtn);

        // 创建部件按钮
        int partY = y + 22;
        int partX = x;

        switch (slot) {
            case HEAD -> {
                addPartButton("H", partX, partY, "helmetHead", Config.helmetHeadVisible);
                addPartButton("HAT", partX + PART_BUTTON_SIZE + PART_BUTTON_SPACING, partY, "helmetHat", Config.helmetHatVisible);
            }
            case CHEST -> {
                addPartButton("B", partX, partY, "chestplateBody", Config.chestplateBodyVisible);
                addPartButton("RA", partX + PART_BUTTON_SIZE + PART_BUTTON_SPACING, partY, "chestplateRightArm", Config.chestplateRightArmVisible);
                addPartButton("LA", partX + (PART_BUTTON_SIZE + PART_BUTTON_SPACING) * 2, partY, "chestplateLeftArm", Config.chestplateLeftArmVisible);
            }
            case LEGS -> {
                addPartButton("B", partX, partY, "leggingsBody", Config.leggingsBodyVisible);
                addPartButton("RL", partX + PART_BUTTON_SIZE + PART_BUTTON_SPACING, partY, "leggingsRightLeg", Config.leggingsRightLegVisible);
                addPartButton("LL", partX + (PART_BUTTON_SIZE + PART_BUTTON_SPACING) * 2, partY, "leggingsLeftLeg", Config.leggingsLeftLegVisible);
            }
            case FEET -> {
                addPartButton("RL", partX, partY, "bootsRightLeg", Config.bootsRightLegVisible);
                addPartButton("LL", partX + PART_BUTTON_SIZE + PART_BUTTON_SPACING, partY, "bootsLeftLeg", Config.bootsLeftLegVisible);
            }
        }
    }

    private void toggleArmorVisibility(EquipmentSlot slot){
        boolean newState = !getArmorVisibility(slot);
        setArmorVisibility(slot, newState);
        visibilityButtons.get(slot).setActive(newState);
    }

    private void toggleEnchantEffect(EquipmentSlot slot) {
        boolean newState = !getEnchantGlow(slot);
        setEnchantGlow(slot, newState);
        enchantButtons.get(slot).setActive(newState);
    }


    private void addPartButton(String text, int x, int y, String id, ModConfigSpec.BooleanValue config) {
        TextButton button = new TextButton(
                x,
                y,
                PART_BUTTON_SIZE,
                PART_BUTTON_SIZE,
                text,
                btn -> togglePartVisibility(id, config)
        );
        button.setActive(config.get());
        partButtons.put(id, button);
        addRenderableWidget(button);
    }

    private void togglePartVisibility(String id, ModConfigSpec.BooleanValue config) {
        boolean newState = !config.get();
        config.set(newState);
        partButtons.get(id).setActive(newState);
    }


    private boolean getArmorVisibility(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> Config.helmetVisible.get();
            case CHEST -> Config.chestplateVisible.get();
            case LEGS -> Config.leggingsVisible.get();
            case MAINHAND, BODY, OFFHAND -> false;
            case FEET -> Config.bootsVisible.get();
        };
    }

    private void setArmorVisibility(EquipmentSlot slot, boolean visible) {
        switch (slot) {
            case HEAD -> Config.helmetVisible.set(visible);
            case CHEST -> Config.chestplateVisible.set(visible);
            case LEGS -> Config.leggingsVisible.set(visible);
            case FEET -> Config.bootsVisible.set(visible);
        }
    }

    private int getArmorOpacity(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> Config.helmetOpacity.get();
            case CHEST -> Config.chestplateOpacity.get();
            case LEGS -> Config.leggingsOpacity.get();
            case MAINHAND, OFFHAND, BODY -> 0;
            case FEET -> Config.bootsOpacity.get();
        };
    }

    private void setArmorOpacity(EquipmentSlot slot, int opacity) {
        switch (slot) {
            case HEAD -> Config.helmetOpacity.set(opacity);
            case CHEST -> Config.chestplateOpacity.set(opacity);
            case LEGS -> Config.leggingsOpacity.set(opacity);
            case FEET -> Config.bootsOpacity.set(opacity);
        }
    }

    private boolean getEnchantGlow(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> Config.helmetEnchantGlow.get();
            case CHEST -> Config.chestplateEnchantGlow.get();
            case LEGS -> Config.leggingsEnchantGlow.get();
            case MAINHAND, OFFHAND, BODY -> false;
            case FEET -> Config.bootsEnchantGlow.get();
        };
    }

    private void setEnchantGlow(EquipmentSlot slot, boolean glow) {
        switch (slot) {
            case HEAD -> Config.helmetEnchantGlow.set(glow);
            case CHEST -> Config.chestplateEnchantGlow.set(glow);
            case LEGS -> Config.leggingsEnchantGlow.set(glow);
            case FEET -> Config.bootsEnchantGlow.set(glow);
        }
    }
}
