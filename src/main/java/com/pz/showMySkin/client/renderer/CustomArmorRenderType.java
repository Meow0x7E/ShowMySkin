package com.pz.showMySkin.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CustomArmorRenderType extends RenderType {

    public static final Function<ResourceLocation, RenderType> ARMOR_TRANSLUCENT = Util.memoize(
            location -> createArmorTranslucent("armor_translucent", location)
    );

    private static RenderType createArmorTranslucent(String name, ResourceLocation id) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(id, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(true);

        return create(name, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    }

    public static RenderType armorTranslucent(ResourceLocation location) {
        return ARMOR_TRANSLUCENT.apply(location);
    }

    private CustomArmorRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }
}