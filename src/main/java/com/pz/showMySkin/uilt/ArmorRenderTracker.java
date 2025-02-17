package com.pz.showMySkin.uilt;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * 用于追踪当前正在渲染的盔甲部位和实体
 */
public class ArmorRenderTracker {
    // 使用ThreadLocal确保线程安全
    private static final ThreadLocal<EquipmentSlot> currentSlot = new ThreadLocal<>();
    private static final ThreadLocal<LivingEntity> currentEntity = new ThreadLocal<>();

    /**
     * 设置当前正在渲染的盔甲部位
     *
     * @param slot 装备槽位
     */
    public static void setCurrentSlot(EquipmentSlot slot) {
        currentSlot.set(slot);
    }

    /**
     * 获取当前正在渲染的盔甲部位
     *
     * @return 当前的装备槽位，如果未设置则返回null
     */
    public static EquipmentSlot getCurrentSlot() {
        return currentSlot.get();
    }

    /**
     * 设置当前正在渲染的实体
     *
     * @param entity 正在渲染的实体
     */
    public static void setCurrentEntity(LivingEntity entity) {
        currentEntity.set(entity);
    }

    /**
     * 获取当前正在渲染的实体
     *
     * @return 当前的实体，如果未设置则返回null
     */
    public static LivingEntity getCurrentEntity() {
        return currentEntity.get();
    }

    /**
     * 清理当前槽位和实体信息，防止内存泄漏
     */
    public static void clear() {
        currentSlot.remove();
        currentEntity.remove();
    }
}