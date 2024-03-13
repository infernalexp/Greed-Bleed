package com.github.teamfusion.greedandbleed.client.renderer;

import com.github.teamfusion.greedandbleed.GreedAndBleed;
import com.github.teamfusion.greedandbleed.client.layers.HogletItemLayer;
import com.github.teamfusion.greedandbleed.client.models.HogletModel;
import com.github.teamfusion.greedandbleed.common.entity.piglin.Hoglet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HogletRenderer<T extends Hoglet> extends MobRenderer<T, HogletModel<T>> {
    public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglet"), "main");
    public static final ModelLayerLocation MONSTER = new ModelLayerLocation(new ResourceLocation(GreedAndBleed.MOD_ID, "hoglet_monster"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglet/hoglet.png");
    public static final ResourceLocation TEXTURE_TAMED = new ResourceLocation(GreedAndBleed.MOD_ID, "textures/entity/hoglet/hoglettamed.png");

    public HogletRenderer(EntityRendererProvider.Context context) {
        super(context, new HogletModel<>(context.bakeLayer(MAIN)), 0.35F);
        this.addLayer(new HogletItemLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.isTame() ? TEXTURE_TAMED : TEXTURE;
    }

    @Override
    protected boolean isShaking(T livingEntity) {
        return super.isShaking(livingEntity) || livingEntity.isConverting();
    }
}