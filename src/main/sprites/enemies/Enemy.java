package main.sprites.enemies;

import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Interface which must be implemented by every <em>Enemy</em> in game.
 * All Enemies must also be added to <em>ENEMIES</em> ArrayList in <b>SpriteHandler</b> class.
 */
public interface Enemy {

    /**
     * Enemy Logic. Executed by <em>GameLoop</em> class.
     */
    public void update();
    
    /**
     * Removes enemy from parent and frees up memory resources.
     * Must be called only when the enemy cycle has been completed.
     */
    public abstract void destroy();
    
    /**
     * Action to perform if Mario stomps the Enemy.
     */
    public abstract void completeCycle();
    
    /**
     * @return <em>True</em> if the Enemy has completed its cycle (died or fell in pit); otherwise <em>False</em>.
     */
    public abstract boolean getCycleStatus();
    
    /**
     * Action to perform if enemy touches Mario (<em>This check must be included in this code block</em>).<br>
     * This must include any checks necessary to ensure that the collision between player and enemy satisfied whatever conditions to kill player.
     */
    public abstract void attemptAttack();
    
    /**
     * Action to perform if hit by Mario's fireball.
     */
    public abstract void fireballHit();
    
    /**
     * Used to check if the enemy should be included in collision detection/logic execution loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which Enemy resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    public abstract int getZone();

    /**
     * @return ImageView containing enemy image on stage.
     */
    public abstract ImageView getSpriteFrame();

    /**
     * Returns the Array containing SVGPath Collision Data of Enemy. The element-specific data is as follows:<br>
     * Element 0: Top<br>
     * Element 1: Right<br>
     * Element 2: Bottom<br>
     * Element 3: Left<br>
     * Element 4: Hit Box<br>
     * Any more consecutive arrays would contain special condition-specific collision data (if any).
     * It is possible for members of array to be null (for example, if you only need to check for top and right collisions, make others null).
     * @return Array containing collision data for enemy.
     */
    public abstract SVGPath[] getSpriteBoundsArray(); 
    
}
