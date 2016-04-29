package main.sprites;

import javafx.scene.image.Image;
import static main.Main.REL_HEIGHT;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Wednesday, April 21, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the ending-stage FlagPole Sprite.
 */
public class FlagPole extends Sprite {
    
    // To control the animation
    private int frameCounter;
    
    // Height that is applied to Pole after each Tick
    private float height;
    
    // Change in Height. This exists to save calculation at every Tick.
    private float heightDelta;
    
    /**
     * Constructs a new <b>FlagPole</b> Sprite.
     * @param SVGData The Collision Data in SVG Format.
     * @param poleXPos X-Position of pole in Parent.
     * @param poleYPos Y-Position of pole in Parent.
     * @param sprites Pole States (currently programmed for use of total 5 states).
     */
    public FlagPole(String SVGData, float poleXPos, float poleYPos, Image... sprites) {
        super(SVGData, poleXPos, poleYPos, 0, 0, sprites);
        
        // Adjust X-Location in accordance to Sprite Image Width.
        spriteFrame.setTranslateX(poleXPos - (24 * REL_HEIGHT));
        spriteBounds.setTranslateX(poleXPos - (24 * REL_HEIGHT));
        
        // Set to -1 to mark inital start
        frameCounter--;
        height = 172 * REL_HEIGHT;
        heightDelta = 1.12f * REL_HEIGHT;
    }

    
    /**
     * Method defining execution logic for FlagPole.
     * This method is called by <em>FireballBig</em> class.
     */
    @Override
    public void update() {
        if(spriteFrame == null)
            return;
        
        // Reduce height of Pole till its zero along with burn animation
        if(height > 0){
            spriteFrame.setFitHeight(height);
            height-=heightDelta;
            spriteFrame.setTranslateY(spriteFrame.getTranslateY() + heightDelta);
            
            // See the reason it was set to -1?
            switch(frameCounter++){
                case 0 : spriteFrame.setImage(playerStates.get(1)); break;
                case 10 : spriteFrame.setImage(playerStates.get(2)); break;
                case 20 : spriteFrame.setImage(playerStates.get(3)); break;
                case 30 : spriteFrame.setImage(playerStates.get(4)); frameCounter = -1; break;
            }
        }
        // Discard resources once pole height is zero
        else{
            spriteFrame.setImage(null);
            spriteFrame = null;
            playerStates = null;
            spriteBounds = null;
            xPos = yPos = height = heightDelta = 0;
        }
    }
    
}
