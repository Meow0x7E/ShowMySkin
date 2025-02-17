package com.pz.showMySkin.event;

import com.pz.showMySkin.Config;
import com.pz.showMySkin.ShowMySkin;
import com.pz.showMySkin.network.ArmorRenderPayload;
import com.pz.showMySkin.network.ArmorSyncTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ShowMySkin.MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.DEDICATED_SERVER)
public class SeverEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer joiningPlayer) {
            ArmorRenderPayload newPlayerPayload = new ArmorRenderPayload(
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
            joiningPlayer.getServer().getPlayerList().getPlayers().forEach(otherPlayer -> {
                if (!otherPlayer.getUUID().equals(joiningPlayer.getUUID())) {
                    otherPlayer.connection.send(newPlayerPayload.toVanillaClientbound());
                }
            });

            joiningPlayer.getServer().getPlayerList().getPlayers().forEach(existingPlayer -> {
                if (!existingPlayer.getUUID().equals(joiningPlayer.getUUID())) {
                    // 获取已在线玩家的盔甲设置
                    ArmorRenderPayload existingPlayerPayload = ArmorSyncTracker.getPlayerData(existingPlayer.getUUID());
                    if (existingPlayerPayload != null) {
                        joiningPlayer.connection.send(existingPlayerPayload.toVanillaClientbound());
                    }
                }
            });
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ArmorSyncTracker.clearPlayerData(event.getEntity().getUUID());
    }

}
