package com.github.teamfusion.greedandbleed.common.entity.brain;

import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class SwitchSimpleJob<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    public SwitchSimpleJob() {
        super(ImmutableMap.of(MemoryRegistry.WORK_TIME.get(), MemoryStatus.REGISTERED), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E mob) {
        boolean flag = mob.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get());
        return !flag && mob.getBrain().isActive(Activity.WORK) || flag && mob.getBrain().isActive(Activity.IDLE);
    }

    @Override
    protected void start(ServerLevel serverLevel, E livingEntity, long l) {
        super.start(serverLevel, livingEntity, l);
        livingEntity.getBrain().setActiveActivityIfPossible(Activity.WORK);
    }
}

