package main.sprites.specials;

import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import main.Main;
import static main.Main.REL_HEIGHT;
import static main.Main.WIDTH;
import main.SpriteHandler;
import main.sprites.Mario;
import main.sprites.Sprite;
import main.sprites.blocks.StageBlock;
import main.sprites.enemies.Enemy;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>Fireball</em> power.<br>
 * <b>Associated Class</b>: Mario
 */
public class Fireball extends Sprite implements PlayerPower {

    //-------------Every protected object is used in only FireballBig class---------------------
    
    // Parent in which Fireball resides (should be same as Mario)
    private Group parent;
    // The Stage's Floor/Base
    // This is reqquired so we don't have to browse through indexes of MAIN_CAST ArrayList (in SpriteHandler class) over and over; just a way of caching
    private Sprite stageFloorBase;
    
    // If the fireball goes out of screen bounds or hits some object
    protected boolean isReadyForRemoval;
    // If the firebal has already collided with something and the blast sequence is currently running
    private boolean initBlast;
    // If fireball collided with something
    private boolean collided;
    
    // To control Fireball animation
    private int frameControl;
    
    // Acceleration
    private float accel;
    // Blast Sprites have lesser width, this is to compensate the x-Position of Fireball for lesser width
    private final float BLAST_THX = (float) (0.858 * REL_HEIGHT);
    
    /**
     * Constructs a single Fireball.
     * @param parent The parent in which the fireball should reside.
     * @param SVGData SVGPath collision data for Fireball.
     * @param xPos x-Position of player (Mario).
     * @param yPos y-Position of player (Mario).
     * @param xVel x-Velocity of Fireball.
     * @param direction Direction in which Fireball is shot (is same as Mario's direction at time of shooting). <b>-1</b> for left and <b>1</b> for right.
     * @param sprite The Sprite Image for Fireball.
     * @param blastSprites Sprites for Fireball blast animation after Fireball collides.
     */
    public Fireball(Group parent, String SVGData, float xPos, float yPos, float xVel, int direction, Image sprite, Image... blastSprites) {
        super(SVGData, xPos, yPos+(8.57f * REL_HEIGHT), direction == 1 ? xVel : -xVel, 0, sprite);
        playerStates.addAll(Arrays.asList(blastSprites));
        this.parent = parent;
        parent.getChildren().addAll(spriteBounds, spriteFrame);
        accel = (float) (0.186 * REL_HEIGHT) * (direction == 1 ? 1 : -1);
        spriteFrame.setScaleX(direction);
        this.stageFloorBase = SpriteHandler.getCurrentCast().get(1);
    }

    /**
     * Fireball Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        // If collided, run blast sequence
        if(initBlast){
            playBlastSeq();
            return;
        }
        xVel += accel;
        xPos += xVel;
        spriteFrame.setTranslateX(xPos);
        spriteBounds.setTranslateX(xPos);
        
        // Check for collisions and apply dynamics
        collided = checkCollision();
        if(collided){
            // Set positive or negative acceleration based on direction
            spriteFrame.setTranslateX(xPos + (spriteFrame.getFitWidth()/BLAST_THX * (accel < 0 ? -1 : 1)));
            if(main.Main.fireballRelease.isPlaying())
                main.Main.fireballRelease.stop();
            initBlast = true;
        }
    }
    
    /**
     * Checks if fireball has collided with something.
     * @return <em>True</em> if Fireball has collided, otherwise <em>False</em>.
     */
    protected boolean checkCollision(){
        // FIreball is out of screen bounds
        if(xPos > WIDTH + 500 || xPos < 0)
            return true;
        
        Shape intersection;
       
        // Check collision with Stage's Floor
        intersection = SVGPath.intersect(spriteBounds, stageFloorBase.getSpriteBoundsArray()[Mario.collidingZone]);
        if(intersection.getBoundsInParent().getWidth() != -1){
            main.Main.fireballHit.play();
            return true;
        }
        
        // Check collision with enemies
        for(int i = 0; i < SpriteHandler.getEnemyCast().size(); i++){
            Enemy e = SpriteHandler.getEnemyCast().get(i);
            if(e.getSpriteBoundsArray()[5] != null){
                intersection = SVGPath.intersect(spriteBounds, e.getSpriteBoundsArray()[5]);
                if(intersection.getBoundsInParent().getWidth() != -1){
                    main.Main.fireballIncinerate.play();
                    e.fireballHit();
                    Main.updateScore(1000);
                    return true;
                }
            }
        }
        
        // Check collisions with StageBlocks
        for(StageBlock block : SpriteHandler.getStageElements()){
            intersection = SVGPath.intersect(spriteBounds, block.getSpriteBounds());
            if(intersection.getBoundsInParent().getWidth() != -1){
                main.Main.fireballHit.play();
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Plays the blast sequence of FIreball when it collides with something
     */
    private void playBlastSeq(){
        if(frameControl % 2 ==0){
            spriteFrame.setImage(playerStates.get((frameControl / 2) + 1));
            if(frameControl == 22){
                dispose();
                isReadyForRemoval = true;
                return;
            }
        }
        frameControl++;
            
    }
    
    /**
     * Removes fireball and frees up resources after collision.
     */
    protected void dispose(){
        parent.getChildren().removeAll(spriteFrame, spriteBounds);
        spriteFrame = null;
        spriteBounds = null;
        playerStates = null;
        parent = null;
        stageFloorBase = null;
        xPos = yPos = xVel = accel = frameControl = 0;
        initBlast = false;
        isReadyForRemoval = true;
    }
    
    /**
     * If the Fireball has collided and resources are freed, and the Fireball object is ready to be removed from game-loop.
     * @return <em>True</em> if Fireball is ready to be removed, otherwise <em>False</em>.
     */
    @Override
    public boolean isReadyForRemoval(){
        return isReadyForRemoval;
    }
    
    /**
     * Check if Fireball has collided with something.
     * @return <em>True</em> if Fireball has collided, otherwise <em>False</em>.
     */
    protected boolean getCollisionStatus(){
        return collided;
    }
    
}
