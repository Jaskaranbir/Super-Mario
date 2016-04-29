package main.sprites.blocks;

import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;
import static main.Main.ASPECT_LENGTH;
import static main.Main.REL_HEIGHT;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Interface which must be implemented by every <em>StageBlock</em> element.
 * These include: <b>Bricks</b> and <b>Tiles</b> (of any type).
 * All StageBlocks must also be added to <em>STAGE_ELEMENTS</em> ArrayList in <b>SpriteHandler</b> class.
 */
public interface StageBlock {

    /**
     * Collision data for StageBlock Elements. This is by assuming all blocks are of same size (which they are supposed to be).
     */
    public static final String BLOCK_COLLISION_DATA = "M 0,0 L 0,0 " + ASPECT_LENGTH + ",0 " + ASPECT_LENGTH + "," + ASPECT_LENGTH + " 0," + ASPECT_LENGTH + " Z";
    /**
     * Bounce height of StageBlock when player (Mario) hits the block.
     */
    public static final float BOUNCE_HEIGHT = 3f * REL_HEIGHT;
    
    /**
     * Block Logic. Executed by <em>GameLoop</em> class.
     */
    public abstract void update();
    
    /**
     * This marks that player has collided with block.
     * The major block logic should not be run unless this condition is satisfie.
     */
    public abstract void collide();
    
    /**
     * @return ImageView containing block image on stage.
     */
    public abstract ImageView getSpriteFrame();
    
    /**
     * @return SVGPath collision data of block.
     */    
    public abstract SVGPath getSpriteBounds();
    
    /**
     * Used to check if the block should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which StageBlock resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    public abstract int getZone();
    
}
