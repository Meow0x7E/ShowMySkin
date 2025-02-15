package com.pz.showMySkin.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pz.showMySkin.Config;
import com.pz.showMySkin.CustomArmorRenderType;
import com.pz.showMySkin.uilt.ArmorRenderTracker;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Shadow
    protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_,
                                        Model p_289658_, int p_350798_, ResourceLocation p_324344_);



    @Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void onRenderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A p_model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ItemStack itemstack = livingEntity.getItemBySlot(slot);

        // 储存当前渲染的装备槽位
        ArmorRenderTracker.setCurrentSlot(slot);

        boolean isVisible = switch (slot) {
            case HEAD ->  Config.helmetVisible.get();
            case CHEST ->  Config.chestplateVisible.get();
            case LEGS ->  Config.leggingsVisible.get();
            case FEET ->  Config.bootsVisible.get();
            default -> true;
        };

        // 如果设置为不可见，取消渲染
        if (!isVisible) {
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
        // 检查该部位的附魔效果是否应该显示
        boolean showEnchantment = switch (slot) {
            case HEAD -> Config.helmetEnchantGlow.get();
            case CHEST -> Config.chestplateEnchantGlow.get();
            case LEGS -> Config.leggingsEnchantGlow.get();
            case FEET -> Config.bootsEnchantGlow.get();
            default -> true;
        };

        if (!showEnchantment) {
            // 如果不显示附魔效果，跳过renderGlint的调用
            ci.cancel();
        }
    }

    @Inject(method = "setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At("HEAD"),cancellable = true)
    protected void setPartVisibility(A model, EquipmentSlot slot, CallbackInfo ci) {
        model.setAllVisible(false);
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
        ci.cancel();
    }
}
