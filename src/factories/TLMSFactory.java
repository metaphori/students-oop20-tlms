package factories;

import static com.almasb.fxgl.dsl.FXGL.*;

import static model.TLMSType.*;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import components.*;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import model.*;
import components.PlayerComponent;
import components.FirePowerComponent;
import components.DamagingComponent;
import components.RandomMovementComponent;
import components.TextureComponent;
import components.ZombieTextureComponent;
import model.TLMSType;
import model.Player;
import model.PlayerImpl;
import model.PlayerTexture;

/**
 * 
 * @version 3.1
 * This factory creates various types of entities that can be spawned with the "spawn ()" method
 */

public class TLMSFactory implements EntityFactory{
	
	private Entity player;
	
	public void setPlayer(Entity player) {
		this.player = player;
	}

	/**
	 * 
	 * @param data, data to customize the creation of the zombie
	 * @return
	 */
	@Spawns("stupidZombie")
    public Entity newStupidZombie(SpawnData data) {
		
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        ZombieTextureDecorator zombie = data.get("zombie");

        return entityBuilder(data)
                .type(ZOMBIE)
                .bbox(new HitBox(new Point2D(35,28), BoundingShape.box(65, 125)))
                .with(physics)
                .with(new DamagingComponent(zombie.getDamage()))
                .with(new HealthIntComponent(zombie.getLife()))
                .with(new CollidableComponent(true))
                .with(new RandomMovementComponent(physics, zombie.getSpeed()))
                .with(new ZombieTextureComponent(zombie.getTexture().getTextureMap()))
                .build();
    }
	
	@Spawns("followingZombie")
    public Entity newFollowingZombie(SpawnData data) {
		
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        ZombieTextureDecorator zombie = data.get("zombie");

        return entityBuilder(data)
                .type(ZOMBIE)
                .bbox(new HitBox(new Point2D(35,28), BoundingShape.box(65, 125)))
                .with(physics)
                .with(new DamagingComponent(zombie.getDamage()))
                .with(new HealthIntComponent(zombie.getLife()))
                .with(new CollidableComponent(true))
                .with(new FollowPlayerComponent(this.player, physics, zombie.getSpeed()))
                .with(new ZombieTextureComponent(zombie.getTexture().getTextureMap()))
                .build();
    }
	
	@Spawns("player")
    public Entity newPlayer(SpawnData data) {
		Player playerAbility = new PlayerImpl();
		PlayerTexture pt = new PlayerTexture();
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        return entityBuilder(data)
                .type(TLMSType.PLAYER)
                .bbox(new HitBox(new Point2D(15,7), BoundingShape.box(15, 30))) //x collisioni e x piattaforme. Immagini sono incollate sulle hitbox //busto
                //point2D ti dice il punto di inizio in alto a sx, bounding shape ti da la forma del tuo player
                //x,y
                .with(physics)
                .with(new GunComponent(new TexturedGunFactoryImpl().createBeretta92()))
                .with(new CollidableComponent(true)) //può essere colpito e può atterrare su piattaforme
                .with(new HealthIntComponent(playerAbility.getHealt())) //gli da i punti vita
                .with(new PlayerComponent()) 
                .with(new TextureComponent(pt.getTextureBlue().getTextureMap()))
                .build();  //builda tutto quello che ho scritto
    }

	@Spawns("shot")
    public Entity newShot(SpawnData data) {
		final TexturedGun currentGun = player.getComponent(ComponentUtils.GUN_COMPONENT).getCurrentGun();
	 	final double direction = Math.signum(this.player.getScaleX());
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        
        return entityBuilder(data)
                .type(SHOT)
                .bbox(new HitBox(new Point2D(50,100), BoundingShape.box(130, 130)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new ShotMovementComponent(physics, direction, currentGun.getShotspeed(), 
                		new Image(currentGun.getTextureMap().get(TLMSType.SHOT))))
                .with(new DamagingComponent(currentGun.getShotDamage()))
                .build();
    }
	
	@Spawns("magmaGun")
    public Entity newMagmaGun(SpawnData data) {
		TexturedGun magmaGun = new TexturedGunFactoryImpl().createMagmaGun();
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);        
        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(MAGMAGUN)
                .bbox(new HitBox(new Point2D(35,130), BoundingShape.box(160, 100)))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new PropComponent(new Image(magmaGun.getTextureMap().get(TLMSType.GUN))))
                .build();
    }

	@Spawns("machineGun")
    public Entity newMachineGun(SpawnData data) {
		TexturedGun machineGun = new TexturedGunFactoryImpl().createMachineGun();
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder(data)
                .type(MACHINEGUN)
                .bbox(new HitBox(new Point2D(35,130), BoundingShape.box(160, 100)))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new PropComponent(new Image(machineGun.getTextureMap().get(TLMSType.GUN))))
                .build();
    }
	
	@Spawns("firePowerUp")
	public Entity newPower(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);
		physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 80)));
		
		return entityBuilder(data)
				.type(TLMSType.FIREPOWER)
				.bbox(new HitBox(new Point2D(5,5), BoundingShape.circle(12)))
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new FirePowerComponent())
				.build();
	}

}