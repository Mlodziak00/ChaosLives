package net.lightglow.chaoslifes.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.internal.base.ComponentRegistrationInitializer;
import net.lightglow.chaoslifes.ChaosLifesMain;

public class ChaosLifesComponents implements EntityComponentInitializer {
	public static final ComponentKey<LifesComponent> PLAYER_LIFES = ComponentRegistry.getOrCreate(ChaosLifesMain.id("playerlifes"), LifesComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(PLAYER_LIFES, LifesComponent::new, RespawnCopyStrategy.CHARACTER);
	}
}
