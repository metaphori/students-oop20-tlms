package collisions;

import static com.almasb.fxgl.dsl.FXGL.runOnce;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

import components.ComponentUtils;
import javafx.util.Duration;
import model.Beretta92;
import model.MachineGun;
import model.MagmaGun;
import model.TLMSType;

public class FirearmCollisionFactoryImpl implements FirearmCollisionFactory {

	@Override
	public CollisionHandler createGunCollision(TLMSType firearmType, int delay) {
		return new CollisionHandler(TLMSType.PLAYER, firearmType) {

			@Override
			public void onCollision(Entity player, Entity firearmProp) {
				//if current gun isn't the default one, there's another timer running,
				//this way I have him know there has been a multiple gun change, so that its timer
				//has to be disabled.
				//this prevents timers from piling up
				if(!player.getComponent(ComponentUtils.FIREARM_COMPONENT).isDefault()) {
					player.getComponent(ComponentUtils.FIREARM_COMPONENT).setChanged(true);
				}
				//I set MachineGun to be the new gun
				player.getComponent(ComponentUtils.FIREARM_COMPONENT)
					.setCurrentFirearm(firearmType.equals(TLMSType.MAGMAGUN)? new MagmaGun() : new MachineGun());
				//I remove the prop
				firearmProp.removeFromWorld();
				//after a delay, set the gun back to default, but if there has been a multiple change, do nothing
				//but resetting the boolean
				runOnce(()->{
					if(player.getComponent(ComponentUtils.FIREARM_COMPONENT).hasChanged()) {
						player.getComponent(ComponentUtils.FIREARM_COMPONENT).setChanged(false);
					} else {
						player.getComponent(ComponentUtils.FIREARM_COMPONENT).setCurrentFirearm(new Beretta92());
					}
				}, Duration.seconds(delay));
			}
		};
	}

}