package main.sprites;

import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import main.SpriteHandler;
import main.sprites.blocks.StageBlock;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Wednesday, April 21, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * The class intended to be inherited by motion sprites of game (enemies, moving
 * powerups etc).
 */
public abstract class MotionStageSprite extends Sprite {

    // The zone in which the sprite exists.
    // Each stage zone is separated by a pit with total of 4 zones.
    private final int zone;

    /**
     * Creates a single motion sprite with given data. Intended for moving
     * sprites such as enemies, some powerups etc.
     *
     * @param SVGData The SVG Collision Data of sprite. Must be in order of top, right, bottom, left, hit box, anything else.
     * @param stageZone The stage zone in which Sprite will exist. Each stage zone is separated by a pit with total of 4 zones.
     * @param xPos x-Position of Sprite.
     * @param yPos y-Position of Sprite.
     * @param xVel x-Velocity of Sprite.
     * @param yVel y-Velocity of SPrite.
     * @param sprites Images containing Sprite States
     */
    public MotionStageSprite(String[] SVGData, int stageZone, float xPos, float yPos, float xVel, float yVel, Image... sprites) {
        super(SVGData, xPos, yPos, xVel, yVel, sprites);
        zone = stageZone;
    }

    /**
     * If Sprite collided with player (Mario).
     * Checks by using fifth element (hit box) in SVGPath Array (logical count starting from one).
     * @return <em>True</em> if player (Mario) collided with the Sprite,
     * otherwise <em>False</em>.
     */
    public boolean checkPlayerCollided() {
        Shape intersection = SVGPath.intersect(getSpriteBoundsArray()[5], SpriteHandler.getCurrentCast().get(1).getSpriteBoundsArray()[5]);
        return intersection.getBoundsInParent().getWidth() != -1;
    }

    /**
     * If the top of Sprite collided with any of <em>StageBlocks</em> (bricks, tiles etc).
     * Checks by using very first element in SVGPath Array (logical count starting from one).
     * @return <em>True</em> if top collided, else <em>False</em>.
     */
    public boolean topCollide() {
        for (StageBlock block : SpriteHandler.getStageElements()) {
            Shape intersection = SVGPath.intersect(super.getSpriteBoundsArray()[0], block.getSpriteBounds());
            if (intersection.getBoundsInParent().getWidth() != -1)
                return true;
        }
        
        return false;
    }

    /**
     * If the right of Sprite collided.
     * Checks by using second element in SVGPath Array (logical count starting from one).
     * @param checkElements Check <em>StageBlock</em> (tiles, bricks etc) for collision too along with stage floor.
     * @return <em>True</em> if right collided, else <em>False</em>.
     */
    public boolean rightCollide(boolean checkElements) {
        return collide(1, checkElements);
    }

    /**
     * If the bottom of Sprite collided.
     * Checks by using third element in SVGPath Array (logical count starting from one).
     * @param checkElements Check <em>StageBlock</em> (tiles, bricks etc) for collision too along with stage floor.
     * @return <em>True</em> if bottom collided, else <em>False</em>.
     */
    public boolean bottomCollide(boolean checkElements) {
        return collide(2, checkElements);
    }

    /**
     * If the right of Sprite collided.
     * Checks by using fourth element in SVGPath Array (logical count starting from one).
     * @param checkElements Check <em>StageBlock</em> (tiles, bricks etc) for collision too along with stage floor.
     * @return <em>True</em> if right collided, else <em>False</em>.
     */
    public boolean leftCollide(boolean checkElements) {
        return collide(3, checkElements);
    }

    public boolean custCollide(int scale, boolean checkElements) {
        return collide(scale, checkElements);
    }

    /**
     * Use any custom element in SVGPath collision array to check for collisions between stage floor and/or <em>StageBlock</em>.
     * @param scale The element location in SVGPath Collision Data array (starting count from zero).
     * @param checkElements If the other <em>StageBlock</em> elements (bricks, tiles etc) should be included in collision check.
     * @return <em>True</em> if collision occurred, else <em>False</em>.
     */
    private boolean collide(int scale, boolean checkElements) {
        Shape intersection;
        // Check Stage Floor for collision
        intersection = SVGPath.intersect(spriteBoundsArray[scale], SpriteHandler.getCurrentCast().get(0).getSpriteBoundsArray()[zone]);
        if (intersection.getBoundsInParent().getWidth() != -1)
            return true;
        
        // If did not collide with stage floor and check elements is enabled, check StageBlocks.
        if (checkElements)
            for (StageBlock block : SpriteHandler.getStageElements()) {
                intersection = SVGPath.intersect(super.getSpriteBoundsArray()[scale], block.getSpriteBounds());
                if (intersection.getBoundsInParent().getWidth() != -1)
                    return true;
            }

        return false;
    }

    /**
     * Updates position of Sprite on stage.
     * Needs to be called manually in sprite logic (the <em>update</em> method).
     */
    @Override
    public void updatePosition() {
        super.getSpriteFrame().setTranslateX(xPos);
        super.getSpriteFrame().setTranslateY(yPos);

        for (int i = 0; i < spriteBoundsArray.length; i++)
            if (spriteBoundsArray[i] != null){
                spriteBoundsArray[i].setTranslateX(xPos);
                spriteBoundsArray[i].setTranslateY(yPos);
            }
    }

}
