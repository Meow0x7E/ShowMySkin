package com.pz.showMySkin.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用于追踪和存储来自网络的玩家盔甲渲染设置
 */
public class ArmorSyncTracker {
    private static final Map<UUID, ArmorRenderPayload> PLAYER_DATA = new HashMap<>();

    /**
     * 更新玩家的盔甲渲染数据
     *
     * @param data 包含玩家盔甲渲染设置的数据包
     */
    public static void updatePlayerData(ArmorRenderPayload data) {
        PLAYER_DATA.put(data.playerUUID(), data);
    }

    /**
     * 获取指定玩家的盔甲渲染数据
     *
     * @param playerUUID 玩家的UUID
     * @return 玩家的盔甲渲染设置，如果不存在则返回null
     */
    public static ArmorRenderPayload getPlayerData(UUID playerUUID) {
        return PLAYER_DATA.get(playerUUID);
    }

    /**
     * 移除指定玩家的盔甲渲染数据
     *
     * @param playerUUID 要移除数据的玩家UUID
     */
    public static void clearPlayerData(UUID playerUUID) {
        PLAYER_DATA.remove(playerUUID);
    }

    /**
     * 清理所有玩家的数据
     */
    public static void clearAllData() {
        PLAYER_DATA.clear();
    }
}