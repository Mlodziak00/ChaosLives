package net.lightglow.chaoslifes.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.lightglow.chaoslifes.components.ChaosLifesComponents;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ChaosSetLifesCommand {
	public static int execute(ServerCommandSource context, Collection<ServerPlayerEntity> targets, int amount) throws CommandSyntaxException {
		ServerPlayerEntity serverPlayer = targets.iterator().next();
		int k = amount;
		ChaosLifesComponents.PLAYER_LIFES.maybeGet(serverPlayer).ifPresent(lifesComponent -> {
			if (amount < lifesComponent.getMaxLifes()+1) {
				lifesComponent.setLives((byte) (k));
				serverPlayer.playSound(SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0F, 1.0F);
				context.sendFeedback(Text.translatable("commands.life.set.success", new Object[]{((ServerPlayerEntity)targets.iterator().next()).getDisplayName(), amount}), true);
			} else {
				serverPlayer.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0F, 0.5F);
				context.sendError(Text.translatable("commands.life.set.error", new Object[]{((ServerPlayerEntity)targets.iterator().next()).getDisplayName(), amount, lifesComponent.getMaxLifes()}));
			}
			serverPlayer.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, serverPlayer));

		});
		return 1;
		// /data get entity @s cardinal_components
	}
}
