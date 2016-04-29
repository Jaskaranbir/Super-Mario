package main.sprites.powerups;

import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Interface which must be implemented by every <em>Powerup</em> in game.
 * All Powerups must also be added to <em>POWERS</em> ArrayList in <b>SpriteHandler</b> class.
 */
public interface Powerup {
    
    /**
     * Powerup Logic. Executed by <em>GameLoop</em> class.
     */
    public void update();
    
    /**
     * Check whether the powerup has completed cycle (by getting consumed by player or falling into pit, if its motion powerup).
     * @return <em>True</em> if the powerup has completed cycle, else <em>False</em>.
     */
    public abstract boolean completeCycle();
    
    /**
     * Check if powerup was consumed by player (mario).
     * @return <em>True</em> if consumed, otherwise <em>False</em>.
     */
    public abstract boolean getPowerupConsumedStats();
    
    /**
     * @return ImageView containing powerup image on stage.
     */
    public abstract ImageView getSpriteFrame();
    
    /**
     * Returns the Array containing SVGPath Collision Data of Powerup. The element-specific data is as follows:<br>
     * Element 0: Top<br>
     * Element 1: Right<br>
     * Element 2: Bottom<br>
     * Element 3: Left<br>
     * Element 4: Hit Box<br>
     * Any more consecutive arrays would contain special condition-specific collision data (if any).
     * It is possible for members of array to be null (for example, if you only need to check for top and right collisions, make others null).
     * @return Array containing collision data for powerup. Returns null if Powerup is a stationary sprite.
     */
    public abstract SVGPath[] getSpriteBoundsArray();
    
    /**
     * @return Collision data for powerup. Returns null if the powerup is a motion sprite.
     */
    public abstract SVGPath getSpriteBounds();
    
}
