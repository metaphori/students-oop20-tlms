package collisions;


import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.io.IOException;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;

import com.almasb.fxgl.entity.Entity;
import components.PlayerComponent;
import components.TextureComponent;
import components.ComponentUtils;
import controller.ScoreControllerImpl;
import model.TLMSType;
import model.score.JsonScore;
import javafx.util.Duration;
import model.PlayerPowerUp;
import model.Player;
import model.PlayerColor;
import model.PlayerImpl;
import model.PlayerPowerUpProxy;
import model.PlayerTexture;

/**
 * @version 2.2
 * Manages collisions between players and zombies
 */

public class PlayerZombieCollision extends CollisionHandler{

	public PlayerZombieCollision(TLMSType player, TLMSType zombie) {
		super(player, zombie);
	}

	@Override
	public void onCollisionBegin(Entity player, Entity zombie) {
		
		player.getComponent(ComponentUtils.HEALTH_COMPONENT).damage(zombie.getComponent(ComponentUtils.DAMAGING_COMPONENT).getDamage());
	
		PlayerPowerUp playerPowerUp = new PlayerPowerUpProxy(player);

		if(player.getComponent(ComponentUtils.PLAYER_COMPONENT).getPlayer().getColor()==PlayerColor.RED) {
			playerPowerUp.transformation(PlayerColor.BLUE, 400, player.getComponent(ComponentUtils.PLAYER_COMPONENT).getPlayer().getHealt(), 1);
			
			getGameTimer().runOnceAfter(() -> {
				PlayerTexture playerTexture = new PlayerTexture();
				player.removeComponent(ComponentUtils.PLAYERTEXTURE_COMPONENT);  
				player.addComponent(new TextureComponent(playerTexture.getTextureBlue().getTextureMap()));		
			}, Duration.seconds(0.8));
		}
    	
		
    	player.getComponent(ComponentUtils.PLAYER_COMPONENT).attacked();
    	zombie.getComponent(ComponentUtils.TEXTURE_COMPONENT).setAttacking(true);
    	
    	inc("playerLife", -0.1);
		
		System.out.println("Il player ha vita: " + player.getComponent(ComponentUtils.HEALTH_COMPONENT).getValue());
			
	
		if(player.getComponent(ComponentUtils.PLAYER_COMPONENT).isDead()) {
			getGameTimer().runOnceAfter(() -> {			
			    	player.removeFromWorld();
					System.out.println("Hai perso!");
					try {
						new ScoreControllerImpl().updateScore(
								new JsonScore(getWorldProperties().intProperty("score").get())
						);
					} catch (IOException e) {
						e.printStackTrace();
					}
					gameOver();		
	    	}, Duration.seconds(1.7));
		}
	}
	
	private void gameOver() {
        getDialogService().showMessageBox("Game Over. Press OK to exit", getGameController()::exit);
    }
}
