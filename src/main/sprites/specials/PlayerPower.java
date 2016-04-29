package main.sprites.specials;

import javafx.scene.image.ImageView;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Interface which must be implemented by every <em>PlayerPower</em> element.
 * These include: <b>Fireballs</b> (of any type).
 * All PlayerPowers must also be added to <em>POWERS</em> ArrayList in <b>SpriteHandler</b> class.
 */
public interface PlayerPower {
    
    /**
     * Power Logic. Executed by <em>GameLoop</em> class.
     */
    public abstract void update();
    
    /**
     * @return ImageView containing power image (sprite) on stage.
     */
    public abstract ImageView getSpriteFrame();
    
    /**
     * If the power has already collided with something/completed its purpose, it should be set to be <em>readyForRemoval</em> for cleaning it up by game-loop.
     * @return <em>True</em> if power has collided/completed its purpose; otherwise <em>false</em>.
     */
    public abstract boolean isReadyForRemoval();
    
}
