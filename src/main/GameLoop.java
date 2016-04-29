package main;

import javafx.animation.AnimationTimer;
import main.sprites.Mario;
import main.sprites.blocks.StageBlock;
import main.sprites.enemies.Enemy;
import main.sprites.specials.PlayerPower;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Wednesday, April 21, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * GameLoop is a base class that plays the motion and dynamics of every object in game.<br>
 * Also responsible for deciding active elements in game.<br>
 * Loop runs at 60FPS (under ideal resource allocation).
 */
public class GameLoop extends AnimationTimer {

    private final Main game;
    private final Mario player;

    /**
     * Constructs a new Loop to run the game.
     * @param game An instance of Main class that contains all defined resources.
     */
    public GameLoop(Main game) {
        this.game = game;
        this.player = game.getPlayer();
    }

    /**
     * Loop that executes the Sprites' code and decides active elements.<br>
     * Active Elements here mean the Sprites which would be considered for tasks
     * such as Collision Detection or executing their logic.
     * @param now The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
     */
    @Override
    public void handle(long now) {

        // The main player, Mario
        player.update();

        for (StageBlock tile : SpriteHandler.getStageElements()) {
            tile.update();
        }
        
        // Powers released by player, such as fireballs (normal and big)
        for (int i = 0; i < SpriteHandler.getPlayerPowerList().size(); i++) {
            PlayerPower power = SpriteHandler.getPlayerPowerList().get(i);
            if (power.isReadyForRemoval()) {
                SpriteHandler.getPlayerPowerList().remove(power);
                break;
            }
            power.update();
        }

        for (int i = 0; i < SpriteHandler.getEnemyCast().size(); i++) {
            Enemy e = SpriteHandler.getEnemyCast().get(i);
            if (e.getCycleStatus()) {
                e.destroy();
                SpriteHandler.getEnemyCast().remove(e);
                SpriteHandler.getAllEnemyCast().remove(e);
                i--;
                break;
            }
            e.update();
        }
    
    }

    /**
     * Stops the Logic Execution and goes to display final ending sequence
     * based on whether player failed or succeeded in completed stage.
     * @param playerDied If player died while trying to complete stage.
     */
    public void stop(boolean playerDied) {
        game.setGameOverUI(playerDied);
        super.stop();
    }

}
