package com.pz.showMySkin.event.clinet;

import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.client.gui.ArmorSettingsScreen;
import com.pz.showMySkin.key.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;


@EventBusSubscriber(modid = ShowMySkin.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.OPEN_SETTINGS.consumeClick()){
            Minecraft.getInstance().setScreen(
                    new ArmorSettingsScreen(Component.translatable("gui.showmyskin.settings")));
        }
    }
}
