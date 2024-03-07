package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.common.item.AmuletItem;
import com.github.teamfusion.greedandbleed.common.item.GBOnAStickWithHoglinItem;
import com.github.teamfusion.greedandbleed.common.item.HoglinArmorItem;
import com.github.teamfusion.greedandbleed.common.item.HoglinSaddleItem;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import com.github.teamfusion.greedandbleed.platform.common.MobRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.of(BuiltInRegistries.ITEM, GreedAndBleed.MOD_ID);

    public static final Supplier<Item> CRIMSON_FUNGUS_ON_A_STICK = create("crimson_fungus_on_a_stick", () -> new GBOnAStickWithHoglinItem(new Item.Properties().durability(220), 1));
    public static final Supplier<Item> GOLDEN_HOGLIN_ARMOR = create("golden_hoglin_armor", () -> new HoglinArmorItem(7, "gold", new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> NETHERITE_HOGLIN_ARMOR = create("netherite_hoglin_armor", () -> new HoglinArmorItem(11, "netherite", new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> HOGLIN_SADDLE = create("hoglin_saddle", () -> new HoglinSaddleItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> AMULET = create("amulet", () -> new AmuletItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> SKELETAL_PIGLIN_SPAWN_EGG = create("skeletal_piglin_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SKELETAL_PIGLIN, 12698049, 4802889, new Item.Properties()));
    public static final Supplier<Item> HOGLET_SPAWN_EGG = create("hoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.HOGLET, 10051392, 16380836, new Item.Properties()));
    public static final Supplier<Item> ZOGLET_SPAWN_EGG = create("zoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.ZOGLET, 13004373, 0xE6E6E6, new Item.Properties()));
    public static final Supplier<Item> SKELET_SPAWN_EGG = create("skoglet_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SKOGLET, 0xC1C1C1, 0x494949, new Item.Properties()));
    public static final Supplier<Item> SHAMAN_PIGLIN_SPAWN_EGG = create("shaman_piglin_spawn_egg", () -> MobRegistry.spawnEgg(EntityTypeRegistry.SHAMAN_PIGLIN, 0xF2BA86, 0xC1C1C1, new Item.Properties()));

    public static <T extends Item> Supplier<T> create(String key, Supplier<T> entry) {
        return ITEMS.create(key, entry);
    }
}