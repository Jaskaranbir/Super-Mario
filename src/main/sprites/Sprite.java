package main.sprites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Wednesday, April 21, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * The base class that must be inherited by every Sprite of game.
 */
public abstract class Sprite {

    // Stores different player states to swith to during gameplay
    protected List<Image> playerStates = new ArrayList<>();
    
    // The main ImageVIew to display a single player state
    protected ImageView spriteFrame;
    
    // Sprite collision data. Intended for stationary sprites
    protected SVGPath spriteBounds;
    
    // Sprite collision data for motion sprites
    // Should always have SVG Data in order-> top, right, bottom, left, collision box, anything else
    // See Mario class for more detailed implementation
    protected SVGPath[] spriteBoundsArray;

    protected float xPos;
    protected float yPos;
    protected float xVel;
    protected float yVel;

    /**
     * Creates a single stationary sprite with given data. Intended for <em>StageBlocks</em> such as bricks, tiles etc.
     * @param SVGData The SVG Collision Data of sprite.
     * @param xPos x-Position of Sprite.
     * @param yPos y-Position of Sprite.
     * @param xVel x-Velocity of Sprite.
     * @param yVel y-Velocity of SPrite.
     * @param sprites Images containing Sprite States
     */
    public Sprite(String SVGData, float xPos, float yPos, float xVel, float yVel, Image... sprites) {
        this(xPos, yPos, xVel, yVel, sprites);
        spriteBounds = new SVGPath();
        spriteBounds.setContent(SVGData);
        spriteBounds.setFill(Color.TRANSPARENT);
        spriteBounds.setTranslateY(yPos);
        spriteBounds.setTranslateX(xPos);
    }

    /**
     * Creates a single motion sprite with given data. Intended for moving sprites such as enemies, some powerups etc.
     * @param SVGData The SVG Collision Data of sprite (should be of form top, right, bottom, left, collision box, anything else.
     * @param xPos x-Position of Sprite.
     * @param yPos y-Position of Sprite.
     * @param xVel x-Velocity of Sprite.
     * @param yVel y-Velocity of SPrite.
     * @param sprites Images containing Sprite States
     */
    public Sprite(String[] SVGData, float xPos, float yPos, float xVel, float yVel, Image... sprites) {
        this(xPos, yPos, xVel, yVel, sprites);
        spriteBoundsArray = new SVGPath[SVGData.length];
        // It is possible to not use some SVGPath collision areas in array.
        for (int i = 0; i < SVGData.length; i++)
            if(SVGData[i] != null){
                spriteBoundsArray[i] = new SVGPath();
                spriteBoundsArray[i].setContent(SVGData[i]);
                spriteBoundsArray[i].setStroke(Color.TRANSPARENT);
                spriteBoundsArray[i].setFill(Color.TRANSPARENT);
            }
    }
    
    /**
     * Used internally to create common functions between motion and stationary sprite.
     * @param xPos x-Position of Sprite.
     * @param yPos y-Position of Sprite.
     * @param xVel x-Velocity of Sprite.
     * @param yVel y-Velocity of SPrite.
     * @param sprites Images containing Sprite States
     */
    private Sprite(float xPos, float yPos, float xVel, float yVel, Image... sprites){
        if(sprites != null)
            spriteFrame = new ImageView(sprites[0]);
        playerStates.addAll(Arrays.asList(sprites));
        spriteFrame.setTranslateX(xPos);
        spriteFrame.setTranslateY(yPos);
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    /**
     * The method which executes sprite logic. Called in <em>GameLoop</em>
     * (but exceptions can be made, such as being called from logic of another sprite class).
     */
    public abstract void update();

    /**
     * Updates position of Sprite on stage.
     * Needs to be called manually in sprite logic (the <em>update</em> method.
     * Will cause NullPointerException if sprite is motion sprite.
     * Motion Sprites must implement their own logic for position updates.
     * See <b>MotionStageSprite</b> class for implementation for MotionSprites.
     */
    public void updatePosition() {
        spriteFrame.setTranslateX(xPos);
        spriteFrame.setTranslateY(yPos);

        spriteBounds.setTranslateX(xPos);
        spriteBounds.setTranslateY(yPos);
    }

    /**
     * @return ArrayList containing sprite states.
     */
    public List<Image> getPlayerStates() {
        return playerStates;
    }

    /**
     * @return ImageView containing sprite image on stage.
     */
    public ImageView getSpriteFrame() {
        return spriteFrame;
    }

    /**
     * @return SVGPath collision data of stationary sprite.
     * Returns null if sprite is a motion sprite.
     */
    public SVGPath getSpriteBounds() {
        return spriteBounds;
    }

    /**
     * @return SVGPath collision data array of motion sprite.
     * Returns null if sprite is a stationary sprite.
     */
    public SVGPath[] getSpriteBoundsArray() {
        return spriteBoundsArray;
    }

    /**
     * @return x-Position of Sprite
     */
    public float getxPos() {
        return xPos;
    }

    /**
     * @return y-Position of Sprite
     */
    public float getyPos() {
        return yPos;
    }

    /**
     * @return x-Velocity of Sprite
     */
    public float getxVel() {
        return xVel;
    }

    /**
     * @return y-Velocity of Sprite
     */
    public float getyVel() {
        return yVel;
    }
    
    

}
