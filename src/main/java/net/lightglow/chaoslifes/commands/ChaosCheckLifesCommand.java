package net.lightglow.chaoslifes.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.lightglow.chaoslifes.components.ChaosLifesComponents;
import net.lightglow.chaoslifes.components.LifesComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.Iterator;
import java.util.List;

public class ChaosCheckLifesCommand {
	public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		ChaosLifesComponents.PLAYER_LIFES.maybeGet(serverPlayer).ifPresent(lifesComponent -> {
			byte lifes = lifesComponent.getLifes();
			final Text text1 = Text.literal("You have %s lifes".formatted(lifes));
			if (lifes >= 1){
				serverPlayer.playSound(SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0F, 1.0F);
				context.getSource().sendFeedback(text1, false);
			}else {
				serverPlayer.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0F, 0.5F);
				context.getSource().sendFeedback(Text.literal("You are dead."), false);
			}

			serverPlayer.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, serverPlayer));

		});
		return 1;
	}
}
