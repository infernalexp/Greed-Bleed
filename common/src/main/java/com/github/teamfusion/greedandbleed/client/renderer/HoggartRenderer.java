package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.models.HoggartModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoggart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HoggartRenderer<T extends Hoggart> extends MobRenderer<T, HoggartModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoggart"), "main");

    public HoggartRenderer(EntityRendererProvider.Context context) {
        super(context, new HoggartModel<>(context.bakeLayer(MAIN)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/piglin/hoggart/crimson_hoggart.png");
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}