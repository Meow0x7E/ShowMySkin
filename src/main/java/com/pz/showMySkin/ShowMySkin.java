package com.pz.showMySkin;

import com.mojang.logging.LogUtils;
import com.pz.showMySkin.event.SeverEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(ShowMySkin.MODID)
public class ShowMySkin {
    public static final String MODID = "show_my_skin";
    private static final Logger LOGGER = LogUtils.getLogger();


    public ShowMySkin(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NeoForge.EVENT_BUS.register(SeverEvent.class);
    }
}
