package com.github.teamfusion.greedandbleed.common.block.blockentity;

import com.github.teamfusion.greedandbleed.common.inventory.PygmyArmorStandMenu;
import com.github.teamfusion.greedandbleed.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PygmyArmorStandBlockEntity extends RandomizableContainerBlockEntity implements MenuProvider {
    public static final int CONTAINER_SIZE = 4;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public PygmyArmorStandBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.PYGMY_ARMOR_STAND.get(), blockPos, blockState);
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.greedandbleed.pygmy_armor_stand");
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new PygmyArmorStandMenu(i, inventory, this);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }

    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }

    }

    @Override
    public int getContainerSize() {
        return 4;
    }
}
