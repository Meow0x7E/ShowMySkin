package com.pz.showMySkin.network;

import com.pz.showMySkin.ShowMySkin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = ShowMySkin.MODID)
public class NetworkHandler {
    @SubscribeEvent
    public static void registerNetworkHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        DirectionalPayloadHandler<ArmorRenderPayload> handler = new DirectionalPayloadHandler<>(
                (payload, context) ->{
                    if (context.flow().isClientbound()) {
                        context.channelHandlerContext().executor().execute(() -> {
                            ArmorSyncTracker.updatePlayerData(payload);
                        });
                    }
                },
                (payload, context) -> {
                    if (context.flow().isServerbound()) {
                        context.channelHandlerContext().executor().execute(()->{
                            context.player().getServer().getPlayerList().getPlayers().forEach(
                                    player->{
                                        if (!player.getUUID().equals(payload.playerUUID())){
                                            player.connection.send(payload.toVanillaClientbound());
                                        }
                                    }
                            );
                        });
                    }
                }
        );

        registrar.commonBidirectional(
                ArmorRenderPayload.TYPE,
                ArmorRenderPayload.CODEC,
                handler
        );
    }
}
