package com.pz.showMySkin.client.gui.parts;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import java.util.function.Consumer;

public class OpacitySlider extends AbstractSliderButton {
    private final String translationKey;
    private final Consumer<Double> onValueChanged;

    public OpacitySlider(int x, int y, int width, int height, double currentValue, String translationKey, Consumer<Double> onValueChanged) {
        super(x, y, width, height, Component.empty(), currentValue / 100.0); // 将0-100的值转换为0-1
        this.translationKey = translationKey;
        this.onValueChanged = onValueChanged;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        // 显示当前透明度值（0-100）
        setMessage(Component.translatable(translationKey).append(": " + (int)(value * 100)));
    }

    @Override
    protected void applyValue() {
        // 将0-1范围的值转换回0-100范围
        double scaledValue = value * 100.0;
        onValueChanged.accept(scaledValue);
    }
}
