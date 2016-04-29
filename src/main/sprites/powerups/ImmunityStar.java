package main.sprites.powerups;

import javafx.scene.image.Image;
import main.Main;
import static main.Main.ASPECT_LENGTH;
import static main.Main.HEIGHT;
import static main.Main.REL_HEIGHT;
import main.sprites.Mario;
import main.sprites.MotionStageSprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>ImmunityStar</em> (after consuming which Mario becomes immune to enemies) in game.<br>
 * <b>Associated Class</b>: StarBrick
 */
public class ImmunityStar extends MotionStageSprite implements Powerup {

    private final float jumpVel;
    // The amount of force by which star should be pushed out of brick
    private final float pushThreshold;
    private final float gravity;
    
    // To control frames for Star animation
    private int frameControl;
    // Initial jump velocity is higher than usual, hence the check. Values are as follows:
    // 0: Star has just exitted the brick. Set high jump velocity.
    // 1: Star touched ground for first time after exiting the brick.
    // 2: Star has touched ground. Set low jump velocity.
    private int initJump;
    
    // If the star has exited the tile, begin the usual logic execution
    private boolean tileExit;
    // Check if star cycle is complete (fallen into pit or consumed by Mario)
    private boolean cycleStats;
    // If star was consumed by Mario
    private boolean powerup_consumed;

    /**
     * Constructs a single ImmunityStar
     * @param mario The player (Mario).
     * @param stageZone Stage zone in which the brick resides. See class <em>StageFloorBase</em>.
     * @param xPos The x-Position of Star.
     * @param yPos The y-Position of Star.
     * @param xVel The xVel of Star.
     * @param yVel The yVel of Star.
     * @param sprites The sprites for star animation effects.
     */
    public ImmunityStar(Mario mario, int stageZone, float xPos, float yPos, float xVel, float yVel, Image... sprites) {
        super(new String[]{
            // SVG Collision Data
            "M " + ASPECT_LENGTH + "," + ASPECT_LENGTH + " L " + ASPECT_LENGTH + "," + ASPECT_LENGTH, //Top
            "M " + ASPECT_LENGTH + ", 0" + " L " + ASPECT_LENGTH + "," + (ASPECT_LENGTH - (7.46 * REL_HEIGHT)), //Right
            "M " + (ASPECT_LENGTH - (3.73 * REL_HEIGHT)) + "," + ASPECT_LENGTH + " L " + (3.73 * REL_HEIGHT) + "," + ASPECT_LENGTH, //Bottom
            "M 0,0 " + " L 0," + (ASPECT_LENGTH - (7.46 * REL_HEIGHT)), //Left
            "M " + (7.46 * REL_HEIGHT) + "," + (ASPECT_LENGTH-1) + " L " + (ASPECT_LENGTH - (7.46 * REL_HEIGHT)) + "," + (ASPECT_LENGTH-1), // Bottom Penetration Threshold: Used to pull up sprite in case it sinks into ground geometry
            "M 0,0 L 0,0 " + ASPECT_LENGTH + ",0 " + ASPECT_LENGTH + "," + ASPECT_LENGTH + ", 0," + ASPECT_LENGTH + ", 0,0", //Hit Box
        }, stageZone, xPos, yPos, xVel, yVel, sprites);

        super.getSpriteFrame().toFront();
        pushThreshold = 0.5f * REL_HEIGHT;
        gravity = REL_HEIGHT;
        jumpVel = -9.6f * REL_HEIGHT;
    }

    /**
     * Star Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if (!tileExit) {
            yPos -= pushThreshold;
            updatePosition();
            // If hit box is no longer colliding with tile, it means out star has exited the tile
            if (!custCollide(4, true))
                tileExit = true;
            return;
        }
        
        // Set initial jump velocity higher than usual (when star has just exitted the brick)
        if(initJump == 0){
            yVel -= 14.93f * REL_HEIGHT;
            xVel += REL_HEIGHT;
            initJump++;
        }
        // Set jump velocity back to normal once the star has touched the ground after exiting from brick
        else if(initJump == 2) {
            yVel += 14.93f * REL_HEIGHT;
            xVel -= REL_HEIGHT;
            initJump++;
        }

        // Star rotation animation
        if (frameControl % 3 == 0) {
            spriteFrame.setImage(playerStates.get(frameControl / 3));
            if (frameControl == 66)
                frameControl = 0;
        }
        frameControl++;

        // Apply gravitational force to star if its in air
        if (!bottomCollide(false)) {
            // If star falls into pit
            if (yPos > HEIGHT) {
                cycleStats = true;
                return;
            }
            yVel += gravity;
            yPos += yVel;
        } else {
            // Bounce star if it touches ground
            yVel = jumpVel;
            yPos += yVel;
            
            // Pull up star if it sinks into grond geometry
            while(custCollide(4, false)){
                yPos--;
                for (int i = 0; i < spriteBoundsArray.length; i++)
                    if(spriteBoundsArray[i] != null)
                        spriteBoundsArray[i].setTranslateY(yPos);
                spriteFrame.setTranslateY(yPos);
            }
            
            if(initJump == 1)
                initJump++;
        }

        // Change Star direction if it collides from sides
        if (rightCollide(false) || leftCollide(false))
            xVel = -xVel;

        // Check if Mario consumed star
        if (checkPlayerCollided()) {
            Main.updateScore(1500);
            powerup_consumed = true;
            cycleStats = true;
        }

        xPos += xVel;
        updatePosition();
    }

    /**
     * Checks if Star has completed its cycle (fell into pit or consumed by Mario).
     * @return <em>True</em> if the cycle is complete, otherwise <em>False</em>.
     */
    @Override
    public boolean completeCycle() {
        return cycleStats;
    }

    /**
     * Checks if Star was consumed by Mario.
     * @return <em>True</em> if the Star was consumed, otherwise <em>False</em>.
     */
    @Override
    public boolean getPowerupConsumedStats() {
        return powerup_consumed;
    }

}
