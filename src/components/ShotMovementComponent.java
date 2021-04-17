package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.image.Image;
import javafx.util.Duration;

public class ShotMovementComponent extends Component{
	
	private final static double SHOTSPEED = 500;
	
	private final static int NTEXTURES = 3;

	private PhysicsComponent physics;

	private AnimatedTexture texture;

	private AnimationChannel animFire;
	
	private double direction;

	public ShotMovementComponent(double direction, Image shotImage) {
		this.direction = direction;
		animFire = new AnimationChannel(shotImage, 3, (int) (shotImage.getWidth()/NTEXTURES)
				, (int) shotImage.getHeight(), Duration.seconds(0.80), 0, NTEXTURES - 1);
		texture = new AnimatedTexture(animFire);
		texture.loop();
	}

	@Override
	public void onAdded() {
		//get the entity to which the component connected, attaching the texture to it
		getEntity().getViewComponent().addChild(texture);
		//reduce bullet size, so to match player's one
		getEntity().setScaleUniform(0.2);
		//set image direction, taken from player
		getEntity().setScaleX(getEntity().getScaleX()*direction);
	}

	@Override
	public void onUpdate(double tpf) {
		if (physics.isMovingX()) {
			if (texture.getAnimationChannel() != animFire) {
				texture.loopAnimationChannel(animFire);
			}
		}
		//set movement direction, equals to player sign
		this.physics.setVelocityX(SHOTSPEED*direction);
	}

}