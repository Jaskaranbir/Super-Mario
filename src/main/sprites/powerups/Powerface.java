package main.sprites.powerups;

import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import main.Main;
import static main.Main.REL_HEIGHT;
import main.SpriteHandler;
import main.sprites.Sprite;
import main.sprites.blocks.StageBlock;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>Powerface</em> powerup (after consuming which Mario can shoot fireballs; originally Fire-Flower in original Mario).<br>
 * <b>Associated Class</b>: PowerTile
 */
public class Powerface extends Sprite implements Powerup {

    // The block from which powerup comes out.
    // Since this is a static powerup, we don't need to check everything for collisions
    private final StageBlock collidingBlock;

    // The amount of force by which powerup should be pushed out of brick
    private final float pushThreshold;
    
    // Check if star cycle is complete (fallen into pit or consumed by Mario)
    private boolean completeCycle;
    // If powerup was consumed by Mario
    private boolean consumedStats;
    // If the powerup has exited the tile, begin the usual logic execution
    private boolean tileExit;
    
    /**
     * Constructs a single Powerface.
     * @param collidingBlock The block from which powerup comes out. Being static powerup, we don't need to check every element in game for collisions.
     * @param xPos The x-Position of powerup
     * @param yPos The y-Position of powerup
     * @param powerSize The size of powerup.
     */
    public Powerface(StageBlock collidingBlock, float xPos, float yPos, float powerSize) {
        super("M 0,0 L 0,0 " + powerSize + ",0 " + powerSize + "," + powerSize + ", 0," + powerSize + ", 0,0", xPos, yPos, 0, 0, new Image("res/sprites/stage/powerups/madface.png", powerSize, powerSize, true, true, true));
        pushThreshold = (float) 0.5 * REL_HEIGHT;
        this.collidingBlock = collidingBlock;
    }

    /**
     * Powerup Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        // Push powerup out of tile till its hit box is colliding with tile
        if (!tileExit) {
            yPos -= pushThreshold;
            updatePosition();
            // "false" marks that we are not checking powerup for collision with player, but with StageBlock provided in constructor
            if (!collide(false))
                tileExit = true;
            return;
        }
        
        // "true" marks that we are checking powerup collision with player instead
        if(collide(true)){
            completeCycle = true;
            consumedStats = true;
            Main.updateScore(1000);
        }
    }

    /**
     * Checks if Powerup has completed its cycle (fell into pit or consumed by Mario).
     * @return <em>True</em> if the cycle is complete, otherwise <em>False</em>.
     */
    @Override
    public boolean completeCycle() {
        return completeCycle;
    }

    /**
     * Checks if Powerup was consumed by Mario.
     * @return <em>True</em> if the Powerup was consumed, otherwise <em>False</em>.
     */
    @Override
    public boolean getPowerupConsumedStats() {
        return consumedStats;
    }
    
    /**
     * Checks if powerup collided with whatever element chosen out of player or StageBlock provided in constructor.
     * @param forPlayer Check if collision check is for player. Change to false if its check for StageBLock.
     * @return The collision status of powerup with that specific object.
     */
    private boolean collide(boolean forPlayer) {
        Shape intersection = SVGPath.intersect(super.getSpriteBounds(), (forPlayer ? SpriteHandler.getCurrentCast().get(1).getSpriteBoundsArray()[5] : collidingBlock.getSpriteBounds()));
        return intersection.getBoundsInParent().getWidth() != -1;
    }

}
