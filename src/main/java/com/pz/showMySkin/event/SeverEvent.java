package com.pz.showMySkin.event;

import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.network.ArmorRenderPayload;
import com.pz.showMySkin.network.ArmorSyncTracker;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ShowMySkin.MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.DEDICATED_SERVER)
public class SeverEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() == Minecraft.getInstance().player) {
            ArmorRenderPayload payload = new ArmorRenderPayload(
                    event.getEntity().getUUID(),
                    new boolean[]{
                            Config.helmetVisible.get(),
                            Config.chestplateVisible.get(),
                            Config.leggingsVisible.get(),
                            Config.bootsVisible.get()
                    },
                    new int[]{
                            Config.helmetOpacity.get(),
                            Config.chestplateOpacity.get(),
                            Config.leggingsOpacity.get(),
                            Config.bootsOpacity.get()
                    },
                    new boolean[]{
                            Config.helmetEnchantGlow.get(),
                            Config.chestplateEnchantGlow.get(),
                            Config.leggingsEnchantGlow.get(),
                            Config.bootsEnchantGlow.get()
                    },
                    new boolean[]{
                            Config.helmetHeadVisible.get(),
                            Config.helmetHatVisible.get(),
                            Config.chestplateBodyVisible.get(),
                            Config.chestplateRightArmVisible.get(),
                            Config.chestplateLeftArmVisible.get(),
                            Config.leggingsBodyVisible.get(),
                            Config.leggingsRightLegVisible.get(),
                            Config.leggingsLeftLegVisible.get(),
                            Config.bootsRightLegVisible.get(),
                            Config.bootsLeftLegVisible.get()
                    }
            );
            Minecraft.getInstance().getConnection().send(payload.toVanillaServerbound());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ArmorSyncTracker.clearPlayerData(event.getEntity().getUUID());
    }

}
