package com.github.teamfusion.greedandbleed.common.registry;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.platform.CoreRegistry;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class PoiRegistry {
    public static final CoreRegistry<PoiType> POI_TYPES = CoreRegistry.of(BuiltInRegistries.POINT_OF_INTEREST_TYPE, GreedAndBleed.MOD_ID);

    public static final ResourceKey<PoiType> PYGMY_STATION_KEY = register("pygmy_station");
    public static final Supplier<PoiType> PYGMY_STATION = POI_TYPES.create("pygmy_station",
        () -> new PoiType(
            ImmutableSet.<BlockState>builder().addAll(BlockRegistry.PYGMY_STATION.get().getStateDefinition().getPossibleStates()).build(),
                1,
                1
        )
    );

    private static ResourceKey<PoiType> register(String key) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(GreedAndBleed.MOD_ID, key));
    }
}