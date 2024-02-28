package net.lightglow.chaoslifes.mixin;

import net.lightglow.chaoslifes.components.ChaosLifesComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends Entity {

	public ServerPlayerMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getPlayerListName", at = @At("RETURN"), cancellable = true)
	private void replacePlayerName(CallbackInfoReturnable<Text> cir){
		Formatting blue = Formatting.AQUA;
		Formatting green = Formatting.GREEN;
		Formatting yellow = Formatting.YELLOW;
		Formatting red = Formatting.RED;
		Formatting gray = Formatting.GRAY;
		ChaosLifesComponents.PLAYER_LIFES.maybeGet(this).ifPresent(lifesComponent -> {
			Text listEntires = Text.translatable("gui.playernames.withlifes", new Object[]{lifesComponent.getLifes(), this.getDisplayName()});
			if (lifesComponent.getLifes() == 0){
				cir.setReturnValue(listEntires.copy().formatted(gray));
			} else if (lifesComponent.getLifes() == 1){
				cir.setReturnValue(listEntires.copy().formatted(red));
			} else if (lifesComponent.getLifes() == 2){
				cir.setReturnValue(listEntires.copy().formatted(yellow));
			} else if (lifesComponent.getLifes() == 3){
				cir.setReturnValue(listEntires.copy().formatted(green));
			} else {
				cir.setReturnValue(listEntires.copy().formatted(blue));
			}
		});
	}
}
