package net.lightglow.chaoslifes.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class LifesComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity obj;
	public byte currentLives = 3;
	private byte maxLives = 6;


	public LifesComponent(PlayerEntity obj){
		this.obj = obj;
	}
	@Override
	public void readFromNbt(NbtCompound tag) {
		setLives(tag.getByte("CurrentLifes"));
		setMaxLives(tag.getByte("MaxLifes"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putByte("CurrentLifes", getLifes());
		tag.putByte("MaxLifes", getMaxLifes());
	}

	public void setLives(byte lifes){
		this.currentLives = lifes;
		ChaosLifesComponents.PLAYER_LIFES.sync(obj);
	}
	public byte getLifes(){
		return currentLives;
	}

	public void setMaxLives(byte maxLives){
		this.maxLives = maxLives;
		ChaosLifesComponents.PLAYER_LIFES.sync(obj);
	}
	public byte getMaxLifes(){
		return maxLives;
	}


	@Override
	public void serverTick() {
		if (getLifes()>getMaxLifes()){
			setLives(getMaxLifes());
		}
	}
}
