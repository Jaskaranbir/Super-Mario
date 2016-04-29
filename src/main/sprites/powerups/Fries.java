package main.sprites.powerups;

import javafx.scene.image.Image;
import main.Main;
import static main.Main.HEIGHT;
import static main.Main.REL_HEIGHT;
import main.sprites.MotionStageSprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>Fries</em> powerup (after consuming which Mario grows; originally Mushroom in original Mario).<br>
 * <b>Associated Class</b>: PowerTile
 */
public class Fries extends MotionStageSprite implements Powerup {

    // Check if powerup cycle is complete (fallen into pit or consumed by Mario)
    private boolean cycleStats;
    
    // If the powerup has exited the tile, begin the usual logic execution
    private boolean tileExit;
    // If powerup was consumed by Mario
    private boolean powerup_consumed;
    
    // We need to slow xVel while powerup is in air; then set it back to original once it touches ground again
    // Just a different optional gameplay style
    private final float tempXVel;
    // The amount of force by which powerup should be pushed out of brick
    private final float pushThreshold;

    /**
     * Constructs a single ImmunityStar.
     * @param stageZone Stage zone in which the brick resides. See class <em>StageFloorBase</em>.
     * @param xPos The x-Position of Star.
     * @param yPos The y-Position of Star.
     * @param xVel The xVel of Star.
     * @param yVel The yVel of Star.
     * @param powerSize The size of powerup.
     */
    public Fries(int stageZone, float xPos, float yPos, float xVel, float yVel, float powerSize) {
        super(new String[]{
            null, // Top; in this case we do not need to check for top collisions, however they can be implemented if required
            "M " + powerSize + ",0 L " + powerSize + "," + (powerSize - (2 * REL_HEIGHT)), // Right
            null, null,null, // Bottom, Left and Bottom pushup threshold (to push powerup if it sinks into stage geometry beyond that point) -> Don't required
            "M 0,0 L 0,0 " + powerSize + ",0 " + powerSize + "," + powerSize + ", 0," + powerSize + ", 0,0" // Hit Box
        }, stageZone, xPos, yPos - 15, xVel, yVel, new Image("res/sprites/stage/powerups/fries.png", powerSize, powerSize, false, true, false));
        tempXVel = xVel;
        pushThreshold = (float) 0.5 * REL_HEIGHT;
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
            if (!rightCollide(true))
                tileExit = true;
            return;
        }

        // If powerup falls into pit
        if (yPos > HEIGHT) {
            cycleStats = true;
            return;
        }

        // Apply gravitational force if powerup is in mid-air
        if (!custCollide(5, true)) {
            yPos += yVel;
            // We slow down speed if its mid-air. Just for a little improved game-play
            xVel = 1;
        } else {
            // Set back speed once it has collided with ground
            if(xVel == 1)
                xVel = tempXVel;
            // Reverse direction if collided with right side
            if (rightCollide(true))
                xVel = -xVel;
        }

        // Check if Mario consumed powerup
        if (checkPlayerCollided()) {
            Main.powerupTaken.play();
            Main.updateScore(1000);
            powerup_consumed = true;
            cycleStats = true;
        }

        xPos += xVel;
        updatePosition();
    }

    /**
     * Checks if Powerup has completed its cycle (fell into pit or consumed by Mario).
     * @return <em>True</em> if the cycle is complete, otherwise <em>False</em>.
     */
    @Override
    public boolean completeCycle() {
        return cycleStats;
    }
    
    /**
     * Checks if Powerup was consumed by Mario.
     * @return <em>True</em> if the Powerup was consumed, otherwise <em>False</em>.
     */
    @Override
    public boolean getPowerupConsumedStats() {
        return powerup_consumed;
    }

}
