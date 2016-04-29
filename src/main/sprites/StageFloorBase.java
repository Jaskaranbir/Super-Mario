package main.sprites;

import javafx.scene.image.Image;
import main.SpriteHandler;
import main.sprites.blocks.StageBlock;
import main.sprites.enemies.Enemy;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the Stage's Floor in game.
 */
public class StageFloorBase extends Sprite  {

    /**
     * Constructs a single floor base for the stage.
     * @param SVGData The SVGPath Collision Data for floor.
     * @param sprites Image for Floor base.
     */
    public StageFloorBase(String[] SVGData, Image... sprites) {
        super(SVGData, 0, 0, 0, 0, sprites);
    }

    /**
     * Removes or Adds the StageElements (StageBlocks, Enemies) based on which zone the player resides on.
     * Made for better performance of game so unrequired collision checks and logic executions could be prevented.
     * Also helps in enemy logic so enemies won't drop from stage by the time player (Mario) reaches them.
     * Called by logic from <em>Mario</em> class.
     * @param zone The current active zone of player.
     */
    public void setActiveFloor(int zone) {
        // Check and add/remove Enemies from executing their logic.
        for(int i = 0; i < SpriteHandler.getAllEnemyCast().size(); i++){
            Enemy e = SpriteHandler.getAllEnemyCast().get(i);
            // Remove enemy if player has went ahead two zones; since enemy will not be visible or required at that time.
            if(e.getZone() == zone - 2){
                SpriteHandler.getEnemyCast().remove(e);
                SpriteHandler.getAllEnemyCast().remove(i);
                i--;
            }
            // Add Enemy to current logic if player is on same zone.
            else if (e.getZone() == zone && !SpriteHandler.getEnemyCast().contains(e))
                SpriteHandler.getEnemyCast().add(e);
        }
        
        // Check and add/remove StageBlocks (bricks, tiles etc) from executing their logic.
        for(int i = 0; i < SpriteHandler.getAllStageElements().size(); i++){
            StageBlock s = SpriteHandler.getAllStageElements().get(i);
            // Remove StageBlock if player has went ahead two zones; since StageBlock will not be visible or required at that time.
            if(s.getZone() == zone - 2){
                SpriteHandler.getStageElements().remove(s);
                SpriteHandler.getAllStageElements().remove(i);
                i--;
            }
            // Add StageBlock to current logic if player is on same zone.
            else if (s.getZone() == zone && !SpriteHandler.getStageElements().contains(s))
                SpriteHandler.getStageElements().add(s);
        }
    }

    /**
     * This method is a No-Op for this class.
     */
    @Override
    public void update() {}

}
