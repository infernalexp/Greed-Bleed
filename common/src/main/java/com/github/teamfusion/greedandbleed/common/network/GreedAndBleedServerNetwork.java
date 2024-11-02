package com.github.teamfusion.greedandbleed.common.network;

import com.github.teamfusion.greedandbleed.common.block.blockentity.PygmyStationBlockEntity;
import com.github.teamfusion.greedandbleed.common.entity.piglin.pygmy.GBPygmy;
import com.github.teamfusion.greedandbleed.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.greedandbleed.common.registry.ItemRegistry;
import com.github.teamfusion.greedandbleed.common.registry.MemoryRegistry;
import com.github.teamfusion.greedandbleed.common.registry.PoiRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

public class GreedAndBleedServerNetwork implements GreedAndBleedNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SELECT_SYNC_PACKET, GreedAndBleedServerNetwork::onSelectSync);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, RECRUIT_PACKET, GreedAndBleedServerNetwork::onRecruit);
    }

    private static void onRecruit(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        Player player = packetContext.getPlayer();
        Level level = player.level();
        GBPygmy pygmy = level.getNearestEntity(
            GBPygmy.class,
            TargetingConditions.forNonCombat()
                .range(10F)
                    .ignoreLineOfSight()
                    .ignoreInvisibilityTesting(),
            player,
            player.blockPosition().getX(),
            player.blockPosition().getY(),
            player.blockPosition().getZ(),
            new AABB(player.blockPosition())
                .inflate(10F)
        );

        BlockPos origin = friendlyByteBuf.readBlockPos();
        if (pygmy != null && level instanceof ServerLevel server) {
            Brain<?> brain = pygmy.getBrain();
            BlockEntity blockEntity = server.getChunkAt(origin).getBlockEntity(origin, LevelChunk.EntityCreationType.IMMEDIATE);
            
            if (blockEntity instanceof PygmyStationBlockEntity station) {
                ItemStack stack = station.getItem(0);
                
                if (stack.getItem() == ItemRegistry.PIGLIN_BELT.get()) {
                    server.getPoiManager()
                        .take(
                                holder -> holder.is(PoiRegistry.PYGMY_STATION_KEY),
                            (holder, pos) -> pos.equals(origin), origin, 1
                        ).ifPresent(pos -> {
                            brain.setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(server.dimension(), origin));
                            
                            addWorkTime(pygmy, 24000 * stack.getCount());
                            stack.shrink(stack.getCount());
                            pygmy.playSound(SoundEvents.ITEM_PICKUP, 0.7F, 1.25F);
                            pygmy.swing(InteractionHand.MAIN_HAND);
                            DebugPackets.sendPoiTicketCountPacket(server, origin);
                        });
                }
            }
        }
    }

    private static void onSelectSync(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        int id = friendlyByteBuf.readInt();
        Player player = packetContext.getPlayer();
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;

        if (!pouch.isEmpty()) {
            SlingshotPouchItem.cycle(id, pouch);
        }
    }

    public static void addWorkTime(GBPygmy gbPygmy, int time) {
        if (gbPygmy.getBrain().hasMemoryValue(MemoryRegistry.WORK_TIME.get())) {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time + gbPygmy.getBrain().getMemory(MemoryRegistry.WORK_TIME.get()).get());
        } else {
            gbPygmy.getBrain().setMemory(MemoryRegistry.WORK_TIME.get(), time);
        }
        gbPygmy.getBrain().setActiveActivityIfPossible(Activity.WORK);
    }
}
