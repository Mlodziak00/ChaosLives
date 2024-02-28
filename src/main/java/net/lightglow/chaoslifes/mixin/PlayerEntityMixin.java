package net.lightglow.chaoslifes.mixin;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.impl.screenhandler.client.ClientNetworking;
import net.lightglow.chaoslifes.components.ChaosLifesComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerEntityMixin {
	@Shadow
	public abstract boolean changeGameMode(GameMode gameMode);

	@Shadow
	public ServerPlayNetworkHandler networkHandler;

	@Shadow
	public abstract ServerWorld getWorld();

	@Shadow
	@Final
	public MinecraftServer server;

	@Shadow
	public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

	@Inject(method = "onDeath", at = @At("TAIL"))
	public void chaosLives$onDeath(CallbackInfo ci) {
		ChaosLifesComponents.PLAYER_LIFES.maybeGet(this).ifPresent(lifesComponent -> {
			if (lifesComponent.getLifes() > 0){
				lifesComponent.setLives((byte) (lifesComponent.getLifes() - 1));
				this.playSound(SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
			}
			if (lifesComponent.getLifes() <= 0){
				changeGameMode(GameMode.SPECTATOR);
				this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0f, 0.75f);
			}
			this.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, this.networkHandler.player));
		});

	}
}
