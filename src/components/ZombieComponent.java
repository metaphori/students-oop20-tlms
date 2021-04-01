package components;



import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import models.Zombie;

public class ZombieComponent extends Component {

	private PhysicsComponent physics;

	private AnimatedTexture texture;

	private AnimationChannel animIdle, animWalk;
	
	private Zombie zombie;

	private double levelTime = 0.0;
	private int seconds = 0;
	private int nextDirection = 0;
	Random rnd = new Random();

	public ZombieComponent(Zombie zombie) {
		this.zombie = zombie;
		animIdle = new AnimationChannel(this.zombie.getIdleTexture(), 4, 85, 120, Duration.seconds(0.66), 0, 3);
		animWalk = new AnimationChannel(this.zombie.getMovementTexture(), 6, 70, 120, Duration.seconds(0.66), 0, 3);
		texture = new AnimatedTexture(animIdle);
		texture.loop();
	}
	
	

	@Override
	public void onAdded() {
		entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
		entity.getViewComponent().addChild(texture);
		/*
		 * physics.onGroundProperty().addListener((obs, old, isOnGround) -> { if
		 * (isOnGround) { //play("land.wav"); jumps = 2; //numero massimo di salti } });
		 */
	}

	@Override
	public void onUpdate(double tpf) {
		if (physics.isMovingX()) {
			if (texture.getAnimationChannel() != animWalk) {
				texture.loopAnimationChannel(animWalk);
			}
		} else {
			if (texture.getAnimationChannel() != animIdle) {
				texture.loopAnimationChannel(animIdle);
			}
		}
		

	}


}