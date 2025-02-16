package com.pz.showMySkin;

import net.neoforged.neoforge.common.ModConfigSpec;

import javax.xml.stream.events.Comment;

public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    /**
     * 盔甲是否渲染
     */
    public static final ModConfigSpec.BooleanValue helmetVisible ;
    public static final ModConfigSpec.BooleanValue chestplateVisible;
    public static final ModConfigSpec.BooleanValue leggingsVisible;
    public static final ModConfigSpec.BooleanValue bootsVisible;

    /**
     * 盔甲透明度
     */
    public static final ModConfigSpec.IntValue helmetOpacity;
    public static final ModConfigSpec.IntValue chestplateOpacity;
    public static final ModConfigSpec.IntValue leggingsOpacity;
    public static final ModConfigSpec.IntValue bootsOpacity;

    /**
     * 盔甲附魔是否渲染
     */
    public static final ModConfigSpec.BooleanValue helmetEnchantGlow;
    public static final ModConfigSpec.BooleanValue chestplateEnchantGlow;
    public static final ModConfigSpec.BooleanValue leggingsEnchantGlow;
    public static final ModConfigSpec.BooleanValue bootsEnchantGlow;


    // 头盔部位配置
    public static ModConfigSpec.BooleanValue helmetHeadVisible;
    public static ModConfigSpec.BooleanValue helmetHatVisible;

    // 胸甲部位配置
    public static ModConfigSpec.BooleanValue chestplateBodyVisible;
    public static ModConfigSpec.BooleanValue chestplateRightArmVisible;
    public static ModConfigSpec.BooleanValue chestplateLeftArmVisible;

    // 护腿部位配置
    public static ModConfigSpec.BooleanValue leggingsBodyVisible;
    public static ModConfigSpec.BooleanValue leggingsRightLegVisible;
    public static ModConfigSpec.BooleanValue leggingsLeftLegVisible;

    // 靴子部位配置
    public static ModConfigSpec.BooleanValue bootsRightLegVisible;
    public static ModConfigSpec.BooleanValue bootsLeftLegVisible;

    static {
        BUILDER.push("Armor Settings");
        // 可见性设置
        helmetVisible = BUILDER.comment("Show helmet?").define("helmetVisible",true);
        chestplateVisible = BUILDER.comment("Show chestplate?").define("chestplateVisible",true);
        leggingsVisible = BUILDER.comment("Show leggings?").define("leggingsVisible",true);
        bootsVisible = BUILDER.comment("Show boots?").define("bootsVisible",true);
        BUILDER.pop();

        // 透明度设置
        BUILDER.push("Transparency");
        helmetOpacity = BUILDER.comment("Helmet Opacity (0-100)").defineInRange("helmetOpacity", 100, 0, 100);
        chestplateOpacity = BUILDER.comment("Chestplate Opacity (0-100)").defineInRange("chestplateOpacity", 100, 0, 100);
        leggingsOpacity = BUILDER.comment("Leggings Opacity (0-100)").defineInRange("leggingsOpacity", 100, 0, 100);
        bootsOpacity = BUILDER.comment("Boots Opacity (0-100)").defineInRange("bootsOpacity", 100, 0, 100);
        BUILDER.pop();

        // 附魔光效设置
        BUILDER.push("Enchantment Glow");
        helmetEnchantGlow = BUILDER.comment("Show helmet enchantment?").define("helmetEnchantGlow", true);
        chestplateEnchantGlow = BUILDER.comment("Show chestplate enchantment?").define("chestplateEnchantGlow", true);
        leggingsEnchantGlow = BUILDER.comment("Show leggings enchantment?").define("leggingsEnchantGlow", true);
        bootsEnchantGlow = BUILDER.comment("Show boots enchantment?").define("bootsEnchantGlow", true);
        BUILDER.pop();

        // 盔甲模型部位可见性设置
        BUILDER.push("Armor Model Parts Visibility");

        // 头盔部位设置
        BUILDER.push("Helmet Parts");
        helmetHeadVisible = BUILDER
                .comment("Whether the helmet head part is visible")
                .define("helmetHeadVisible", true);
        helmetHatVisible = BUILDER
                .comment("Whether the helmet hat part is visible")
                .define("helmetHatVisible", true);
        BUILDER.pop();

        // 胸甲部位设置
        BUILDER.push("Chestplate Parts");
        chestplateBodyVisible = BUILDER
                .comment("Whether the chestplate body part is visible")
                .define("chestplateBodyVisible", true);
        chestplateRightArmVisible = BUILDER
                .comment("Whether the chestplate right arm is visible")
                .define("chestplateRightArmVisible", true);
        chestplateLeftArmVisible = BUILDER
                .comment("Whether the chestplate left arm is visible")
                .define("chestplateLeftArmVisible", true);
        BUILDER.pop();

        // 护腿部位设置
        BUILDER.push("Leggings Parts");
        leggingsBodyVisible = BUILDER
                .comment("Whether the leggings body part is visible")
                .define("leggingsBodyVisible", true);
        leggingsRightLegVisible = BUILDER
                .comment("Whether the leggings right leg is visible")
                .define("leggingsRightLegVisible", true);
        leggingsLeftLegVisible = BUILDER
                .comment("Whether the leggings left leg is visible")
                .define("leggingsLeftLegVisible", true);
        BUILDER.pop();

        // 靴子部位设置
        BUILDER.push("Boots Parts");
        bootsRightLegVisible = BUILDER
                .comment("Whether the boots right leg is visible")
                .define("bootsRightLegVisible", true);
        bootsLeftLegVisible = BUILDER
                .comment("Whether the boots left leg is visible")
                .define("bootsLeftLegVisible", true);
        BUILDER.pop();

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

}
