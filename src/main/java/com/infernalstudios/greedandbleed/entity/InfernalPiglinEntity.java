package com.infernalstudios.greedandbleed.entity;

import com.infernalstudios.greedandbleed.api.IHasTaskManager;
import com.infernalstudios.greedandbleed.api.ITaskManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class InfernalPiglinEntity extends AbstractPiglinEntity implements IHasTaskManager {
    protected static final DataParameter<Boolean> DATA_BABY_ID = EntityDataManager.defineId(InfernalPiglinEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(InfernalPiglinEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> DATA_IS_DANCING = EntityDataManager.defineId(InfernalPiglinEntity.class, DataSerializers.BOOLEAN);
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
    public static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", (double)0.2F, AttributeModifier.Operation.MULTIPLY_BASE);

    protected boolean cannotHunt = false;
    protected ITaskManager<?> taskManager;

    public InfernalPiglinEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected int getExperienceReward(PlayerEntity playerEntity) {
        return this.xpReward;
    }

    @Override
    protected boolean canHunt() {
        return !this.cannotHunt;
    }


    protected void setCannotHunt(boolean cannotHunt) {
        this.cannotHunt = cannotHunt;
    }


    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.isPersistenceRequired();
    }


    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compoundNBT) {
        // extra spawn stuff goes here

        this.taskManager.initMemories();
        this.populateDefaultEquipmentSlots(difficultyInstance);
        this.populateDefaultEquipmentEnchantments(difficultyInstance);
        return this.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, entityData, compoundNBT);
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        boolean isHurt = super.hurt(damageSource, amount);
        if (this.level.isClientSide) {
            return false;
        } else {
            if (isHurt && damageSource.getEntity() instanceof LivingEntity) {
                this.taskManager.wasHurtBy((LivingEntity)damageSource.getEntity());
            }

            return isHurt;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level.isClientSide ?
                null : this.taskManager.getSoundForCurrentActivity().orElse((SoundEvent)null);
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getEntityData().set(DATA_BABY_ID, baby);
        if (!this.level.isClientSide) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (baby) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean isDancing) {
        this.entityData.set(DATA_IS_DANCING, isDancing);
    }

    protected boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean chargingCrossbow) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, chargingCrossbow);
    }

    // HAS TASK MASTER

    @Override
    public ITaskManager<?> getTaskManager() {
        return this.taskManager;
    }

    @Override
    public void playSound(SoundEvent soundEvent) {
        this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
    }
}
