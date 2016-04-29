package main.sprites.blocks;

import javafx.scene.image.Image;
import main.sprites.Sprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the Brick Elements in game.
 */
public class Brick extends Sprite implements StageBlock{
    
    // To change brick states
    private int counter;
    
    // Stage Zone in which brick resides. See StageFloorBase class.
    private final int zone;
    
    // Amount of bounce when player hits brick
    private boolean bounce;
        
    /**
     * Constructs a single brick.
     * @param stageZone The stage zone in which Brick resides. Please see class <b>StageFloorBase</b> for details for zones.
     * @param xPos x-Location of brick.
     * @param yPos y-Location of brick.
     * @param sprite The image to be displayed as brick.
     */
    public Brick(int stageZone, float xPos, float yPos,  Image sprite) {
        super(BLOCK_COLLISION_DATA, xPos, yPos, 0, 0, sprite);
        zone = stageZone;
    }

    /**
     * Brick Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if(!bounce)
            return;
        
        // Bouncy effect for brick when player hits it.
        counter++;
        if(counter == 1)
            super.getSpriteFrame().setTranslateY(super.getSpriteFrame().getTranslateY() - BOUNCE_HEIGHT);
        else if(counter == 5){
            super.getSpriteFrame().setTranslateY(super.getSpriteFrame().getTranslateY() + BOUNCE_HEIGHT);
            counter = 0;
            bounce = false;
        }
    }

    /**
     * This marks that player has collided with brick.
     * The brick logic will not run unless this condition takes place.
     */
    @Override
    public void collide() {
        bounce = true;
    }

    /**
     * Used to check if the brick should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which Brick resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }
    
}
