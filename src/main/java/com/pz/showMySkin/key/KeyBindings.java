package com.pz.showMySkin.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final KeyMapping OPEN_SETTINGS = new KeyMapping(
            "key.show_my_skin.open_settings",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,  // 默认使用K键
            "key.categories.show_my_skin"
    );
}
