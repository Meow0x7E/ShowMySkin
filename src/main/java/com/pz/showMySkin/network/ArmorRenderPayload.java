package com.pz.showMySkin.network;

import com.pz.showMySkin.ShowMySkin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ArmorRenderPayload(
        UUID playerUUID,
        boolean[] armorVisibility,
        int[] armorOpacity,
        boolean[] enchantGlow,
        boolean[] armorPartVisibility // 所有装备部件的可见性
) implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<ArmorRenderPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ShowMySkin.MODID, "armor_render"));


    public static final StreamCodec<FriendlyByteBuf,ArmorRenderPayload> CODEC =
            CustomPacketPayload.codec(ArmorRenderPayload::write, ArmorRenderPayload::new);

    // 从buffer读取数据的构造函数
    public ArmorRenderPayload(FriendlyByteBuf buf) {
        this(
                buf.readUUID(),
                readBooleanArray(buf, 4),
                readIntArray(buf, 4),
                readBooleanArray(buf, 4),
                readBooleanArray(buf, 10)
        );
    }

    // 写入数据到buffer的方法
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        writeBooleanArray(buf, armorVisibility);
        writeIntArray(buf, armorOpacity);
        writeBooleanArray(buf, enchantGlow);
        writeBooleanArray(buf, armorPartVisibility);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static boolean[] readBooleanArray(FriendlyByteBuf buf, int length) {
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = buf.readBoolean();
        }
        return array;
    }

    private static int[] readIntArray(FriendlyByteBuf buf, int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = buf.readInt();
        }
        return array;
    }

    private static void writeBooleanArray(FriendlyByteBuf buf, boolean[] array) {
        for (boolean b : array) {
            buf.writeBoolean(b);
        }
    }

    private static void writeIntArray(FriendlyByteBuf buf, int[] array) {
        for (int i : array) {
            buf.writeInt(i);
        }
    }
}
