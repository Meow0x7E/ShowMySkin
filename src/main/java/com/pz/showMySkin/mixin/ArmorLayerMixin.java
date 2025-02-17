package com.pz.showMySkin.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pz.showMySkin.Config;
import com.pz.showMySkin.client.renderer.CustomArmorRenderType;
import com.pz.showMySkin.network.ArmorRenderPayload;
import com.pz.showMySkin.network.ArmorSyncTracker;
import com.pz.showMySkin.uilt.ArmorRenderTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Shadow
    protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_,
                                        Model p_289658_, int p_350798_, ResourceLocation p_324344_);

    @Unique
    private boolean showMySkin$isArmorVisible(EquipmentSlot slot, LivingEntity entity) {
        if (entity == Minecraft.getInstance().player) {
            // 本地玩家使用本地配置
            return switch (slot) {
                case HEAD -> Config.helmetVisible.get();
                case CHEST -> Config.chestplateVisible.get();
                case LEGS -> Config.leggingsVisible.get();
                case FEET -> Config.bootsVisible.get();
                default -> true;
            };
        }

        // 其他玩家使用同步数据
        ArmorRenderPayload data = ArmorSyncTracker.getPlayerData(entity.getUUID());
        if (data == null) return true;

        int index = switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };

        return index >= 0 ? data.armorVisibility()[index] : true;
    }

    @Unique
    private boolean showMySkin$hasEnchantGlow(EquipmentSlot slot, LivingEntity entity) {
        if (entity == Minecraft.getInstance().player) {
            // 本地玩家使用本地配置
            return switch (slot) {
                case HEAD -> Config.helmetEnchantGlow.get();
                case CHEST -> Config.chestplateEnchantGlow.get();
                case LEGS -> Config.leggingsEnchantGlow.get();
                case FEET -> Config.bootsEnchantGlow.get();
                default -> true;
            };
        }

        // 其他玩家使用同步数据
        ArmorRenderPayload data = ArmorSyncTracker.getPlayerData(entity.getUUID());
        if (data == null) return true;

        int index = switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };

        return index >= 0 ? data.enchantGlow()[index] : true;
    }

    @Unique
    private float showMySkin$getArmorOpacity(EquipmentSlot slot, LivingEntity entity) {
        if (entity == Minecraft.getInstance().player) {
            // 本地玩家使用本地配置
            return switch (slot) {
                case HEAD -> Config.helmetOpacity.get() / 100.0f;
                case CHEST -> Config.chestplateOpacity.get() / 100.0f;
                case LEGS -> Config.leggingsOpacity.get() / 100.0f;
                case FEET -> Config.bootsOpacity.get() / 100.0f;
                default -> 1.0f;
            };
        }

        // 其他玩家使用同步数据
        ArmorRenderPayload data = ArmorSyncTracker.getPlayerData(entity.getUUID());
        if (data == null) return 1.0f;

        int index = switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };

        return index >= 0 ? data.armorOpacity()[index] / 100.0f : 1.0f;
    }



    @Unique
    private boolean showMySkin$isLocalPlayer(LivingEntity entity) {
        if (entity == null || Minecraft.getInstance().player == null) {
            return false;
        }
        // 使用UUID比较来确保完全匹配
        return entity.getUUID().equals(Minecraft.getInstance().player.getUUID());
    }



    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void onRenderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource,
                                    T livingEntity, EquipmentSlot slot, int packedLight,
                                    A p_model, float limbSwing, float limbSwingAmount,
                                    float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ItemStack itemstack = livingEntity.getItemBySlot(slot);


            // 储存当前渲染的装备槽位和实体
            ArmorRenderTracker.setCurrentSlot(slot);
            ArmorRenderTracker.setCurrentEntity(livingEntity);


        if (!showMySkin$isArmorVisible(slot, livingEntity) ||
                showMySkin$getArmorOpacity(slot, livingEntity) <= 0.0f) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z"
            ),
            cancellable = true
    )
    private void onCheckFoil(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A p_model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!showMySkin$hasEnchantGlow(slot, livingEntity)) {
            ci.cancel();
        }

    }

    @Inject(method = "setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At("HEAD"),cancellable = true)
    protected void setPartVisibility(A model, EquipmentSlot slot, CallbackInfo ci) {
        LivingEntity entity = ArmorRenderTracker.getCurrentEntity();
        if (entity == null ) {
            return;
        }
        model.setAllVisible(false);
        // 如果是本地玩家，使用本地配置
        if (entity == Minecraft.getInstance().player) {
            switch (slot) {
                case HEAD:
                    model.head.visible = Config.helmetHeadVisible.get();
                    model.hat.visible = Config.helmetHatVisible.get();
                    break;
                case CHEST:
                    model.body.visible = Config.chestplateBodyVisible.get();
                    model.rightArm.visible = Config.chestplateRightArmVisible.get();
                    model.leftArm.visible = Config.chestplateLeftArmVisible.get();
                    break;
                case LEGS:
                    model.body.visible = Config.leggingsBodyVisible.get();
                    model.rightLeg.visible = Config.leggingsRightLegVisible.get();
                    model.leftLeg.visible = Config.leggingsLeftLegVisible.get();
                    break;
                case FEET:
                    model.rightLeg.visible = Config.bootsRightLegVisible.get();
                    model.leftLeg.visible = Config.bootsLeftLegVisible.get();
                    break;
            }
        } else {
            // 如果是其他玩家，从网络同步数据中获取
            ArmorRenderPayload data = ArmorSyncTracker.getPlayerData(entity.getUUID());
            if (data != null) {
                boolean[] parts = data.armorPartVisibility();
                switch (slot) {
                    case HEAD:
                        model.head.visible = parts[0];
                        model.hat.visible = parts[1];
                        break;
                    case CHEST:
                        model.body.visible = parts[2];
                        model.rightArm.visible = parts[3];
                        model.leftArm.visible = parts[4];
                        break;
                    case LEGS:
                        model.body.visible = parts[5];
                        model.rightLeg.visible = parts[6];
                        model.leftLeg.visible = parts[7];
                        break;
                    case FEET:
                        model.rightLeg.visible = parts[8];
                        model.leftLeg.visible = parts[9];
                        break;
                }
            } else {
                // 如果没有同步数据，全部显示
                model.setAllVisible(true);
            }
        }
        ci.cancel();
    }

    // 拦截原始的renderModel方法，使用我们的自定义渲染类型
    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onRenderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, int p_350798_, ResourceLocation p_324344_, CallbackInfo ci) {
        EquipmentSlot currentSlot = ArmorRenderTracker.getCurrentSlot();
        LivingEntity entity = ArmorRenderTracker.getCurrentEntity();

        if (currentSlot == null || entity == null) {
            return;
        }
        float opacity = showMySkin$getArmorOpacity(currentSlot,entity);
        if (opacity <= 0.0f) {
            ci.cancel();
            return;
        }

        float r = (float)(p_350798_ >> 16 & 255) / 255.0F;
        float g = (float)(p_350798_ >> 8 & 255) / 255.0F;
        float b = (float)(p_350798_ & 255) / 255.0F;

        int color = new Color(r, g, b, opacity).getRGB();

        // 使用自定义的RenderType来支持透明度
        RenderType renderType = CustomArmorRenderType.armorTranslucent(p_324344_);
        VertexConsumer vertexConsumer = p_289689_.getBuffer(renderType);

        p_289658_.renderToBuffer(p_289664_, vertexConsumer, p_289681_, OverlayTexture.NO_OVERLAY, color);
        ci.cancel();
    }

    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At("TAIL"))
    private void afterCompleteRenderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A p_model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        // 在整个渲染流程结束后清理
        ArmorRenderTracker.clear();
    }

}
