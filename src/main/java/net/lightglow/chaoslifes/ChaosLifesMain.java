package net.lightglow.chaoslifes;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lightglow.chaoslifes.commands.ChaosAddLifesCommand;
import net.lightglow.chaoslifes.commands.ChaosCheckLifesCommand;
import net.lightglow.chaoslifes.commands.ChaosRemoveLifesCommand;
import net.lightglow.chaoslifes.commands.ChaosSetLifesCommand;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

public class ChaosLifesMain implements ModInitializer {
	public static String MOD_ID = "chaoslifes";
	public static Identifier id(String string){
		return new Identifier(MOD_ID, string);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(CommandManager.literal("life")
				.then(CommandManager.literal("check").executes(ChaosCheckLifesCommand::execute))
				.then(CommandManager.literal("add").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
						.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes((context ->
								ChaosAddLifesCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))
						)))))
				.then(CommandManager.literal("remove").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
						.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes((context ->
								ChaosRemoveLifesCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))
						)))))
				.then(CommandManager.literal("set").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
				.then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes((context ->
						ChaosSetLifesCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))
				)))))));
	}
}
