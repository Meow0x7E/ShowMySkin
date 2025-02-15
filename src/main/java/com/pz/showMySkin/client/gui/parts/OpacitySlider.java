package com.pz.showMySkin.client.gui.parts;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class OpacitySlider extends AbstractSliderButton {
    private final String translationKey;
    private final Consumer<Double> onValueChange;

    public OpacitySlider(int x, int y, int width, int height, double value, String translationKey, Consumer<Double> onValueChange) {
        super(x, y, width, height, Component.empty(), value / 100.0);
        this.translationKey = translationKey;
        this.onValueChange = onValueChange;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.translatable(this.translationKey)
                .append(": " + (int) (this.value * 100) + "%"));
    }

    @Override
    protected void applyValue() {
        this.onValueChange.accept(this.value * 100);
    }
}
