package main.sprites.specials;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import main.SpriteHandler;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>Fireball-Big</em> power. This is implemented into game for only the final Flag-Pole Sequence for now and cannot be used by Mario during normal gameplay.<br>
 * <b>Associated Class</b>: Mario
 */
public class FireballBig extends Fireball {
    
    // If the fireball has hit the flag pole or not
    private boolean flagPoleHit;
    
    /**
     * Constructs a single Big Fireball.
     * @param root The parent in which the fireball should reside.
     * @param SVGData SVGPath collision data for Fireball.
     * @param xPos x-Position of player (Mario).
     * @param yPos y-Position of player (Mario).
     * @param xVel x-Velocity of Fireball.
     * @param sprite The Sprite Image for Fireball.
     * @param blastSprites Sprites for Fireball blast animation after Fireball collides.
     */
    public FireballBig(Group root, String SVGData, float xPos, float yPos, float xVel, Image sprite, Image... blastSprites) {
        super(root, SVGData, xPos, yPos, xVel, 1, sprite, blastSprites);
    }
    
    /**
     * Fireball Logic. Executed by <em>GameLoop</em> class (since FIreball Big is also a <em>Power</em> interface's implementation).
     */
    @Override
    public void update(){
        // Check if fireball has hit the Flag Pole and then remove it
        if(flagPoleHit){
            SpriteHandler.getCurrentCast().get(2).update();
            if(SpriteHandler.getCurrentCast().get(2).getSpriteFrame() == null)
                isReadyForRemoval = true;
            return;
        }
        super.update();
        // If Fireball collided with Flag Pole
        if(super.getCollisionStatus()){
            // Play the impact Sound
            if(!flagPoleHit){
                main.Main.energyImpact.play();
                flagPoleHit = true;
            }
            
            // Clear up resources to free memory
            super.dispose();
            isReadyForRemoval = false;
            // Start playing the Flag Pole burning Sequence
            // (as guessed, element #2 is Flag Pole in that ArrayList; see main class' InitUIComponenets method)
            SpriteHandler.getCurrentCast().get(2).update();
        }
    }
    
    /**
     * Check Fireball's collision with Flag Pole
     * @return The collision status (<em>true</em> if collision occurred; otherwise <em>false</em>) of Fireball.
     */
    @Override
    protected boolean checkCollision(){
        Shape intersection = SVGPath.intersect(SpriteHandler.getCurrentCast().get(2).getSpriteBounds(), spriteBounds);
        return intersection.getBoundsInParent().getWidth() != -1;
    }
    
}
