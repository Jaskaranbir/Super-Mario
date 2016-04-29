package main.sprites.specials;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static main.Main.REL_HEIGHT;
import main.sprites.Sprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the <em>Aura</em> (that Mario gets after 2nd level powerup and after which he can shoot fireballs).<br>
 * <b>Associated Class</b>: Mario
 */
public class Aura extends Sprite {
    
    // Parent in which Aura resides, should be same as Mario's parent
    private final Group parent;
    // The wind effects during initial setting of aura (the blue-ish lines)
    private final ImageView envEffectsView;
    // Sprites for wind effects above
    private final Image[] windEffects;
    
    // To control aura animation
    private int frameControl;
    
    // The default aura height (or maximum aura height)
    private final float auraHeight;
    // Change in aura height during initial aura effects
    // Used to display growing aura effect in beginning
    // Starts from zero and increases till "auraHeight"
    private final float auraHeightDelta;
    
    // Used to adjust y-Position of aura during "growing width" sequence to compensate for change in width
    private float effectiveYPos;
    // To bring player in center of aura horizontally
    private final float xPosOffset;
    // To bring player at ideal position in aura vertically
    private final float yPosOffset;
    
    public boolean auraStateComplete;
    
    /**
     * Constructs a single aura.
     * @param parent The parent in which the aura resides (should be same as Mario's parent).
     * @param auraHeight The maximum height of aura (starts from 0 and increases till this height).
     * @param auraWidth Aura's width (since it is set to 0 width since we are maintaining aspect ratio of aura sprites (see aura sprites initialization in <em>Main</em> class)).
     * @param windEffects The wind effects during initial setting of aura (the blue-ish lines).
     * @param sprites Sprites for aura animation.
     */
    public Aura(Group parent, float auraHeight, float auraWidth, Image[] windEffects, Image... sprites) {
        // Since we are not checking for aura collisions with anything
        super("", 0, 0, 0, 0, sprites);
        this.parent = parent;
        this.auraHeight =  auraHeight;
        this.windEffects = windEffects;
        spriteFrame.setFitHeight(0);
        spriteFrame.setFitWidth(auraWidth);
        envEffectsView = new ImageView();
        xPosOffset = 15  * REL_HEIGHT;
        auraHeightDelta = (float) (2.5 * REL_HEIGHT);
        yPosOffset = (float) (20.7 * REL_HEIGHT);
        // So that during initialization, aura will not be visible to player until its location is exactly same as player
        effectiveYPos = -200;
    }

    /**
     * Runs aura logic along with updating Aura's position to be same as Mario's position.
     * @param xPos x-Position of player (Mario).
     * @param yPos y-Position of player (Mario). 
     */
    public void update(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        
        // Aura growth effect during initialization
        if(spriteFrame.getFitHeight() < auraHeight){
            spriteFrame.setFitHeight(spriteFrame.getFitHeight() + auraHeightDelta);
            this.yPos = (float) (yPos + auraHeight - spriteFrame.getFitHeight());
            updatePosition();
        }
        
        
        if(!auraStateComplete){
            // Marks that player has just consumed powerup and aura initialization stage is running
            if(effectiveYPos == -200){
                effectiveYPos = yPos;
                parent.getChildren().add(envEffectsView);
                envEffectsView.setTranslateX(xPos - xPosOffset);
                envEffectsView.setTranslateY(effectiveYPos - yPosOffset);
            }
            displayEffect();
            return;
        }
        
        this.update();
    }

    /**
     * <b>This method should NOT be called in this class.</b><br>
     * Please call the overloaded version of this method and supply the player coordinates to that method instead.
     */
    @Override
    public void update() {
        // The Aura animation
        if(frameControl++ == 0)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(0));
        else if(frameControl == 8)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(1));
        else if(frameControl == 16)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(2));
        else if(frameControl == 24){
            super.getSpriteFrame().setImage(super.getPlayerStates().get(3));
            frameControl = 0;
        }
        
        if(auraStateComplete){
            updatePosition();
            // The aura sound
            if(!main.Main.auraLoop.isPlaying())
                main.Main.auraLoop.play(0.6);
        }
    }
    
    /**
     * Updates position of aura on stage.
     * Needs to be called manually in aura logic (the <em>update</em> method).
     */
    @Override
    public void updatePosition(){
        spriteFrame.setTranslateX(xPos - xPosOffset);
        spriteFrame.setTranslateY(yPos - yPosOffset);
    }
    
    private void displayEffect(){
        // Initial Wind effect (the blue lines) when aura is just consumed by player.
        if(frameControl % 2 == 0){
            envEffectsView.setImage(windEffects[frameControl / 2]);
            if(frameControl % 8 == 0)
                super.getSpriteFrame().setImage(super.getPlayerStates().get(frameControl / 8));
            // Remove effect after completion
            else if(frameControl == 28){
                frameControl = 0;
                auraStateComplete = true;
                effectiveYPos = 0;
                parent.getChildren().remove(envEffectsView);
                return;
            }
        }
        frameControl++;
    }
    
    /**
     * Changes White Aura to Golden Aura and marks Invincibility mode for player.<br>
     * This should only be called when player consumes <em>ImmunityStar</em> powerup.
     */
    public void enableSuperAura(){
        auraStateComplete = false;
        spriteFrame.setFitHeight(0);
        main.Main.auraLoop.stop();
        frameControl = 0;
        effectiveYPos = 0;
        for(int i = 0; i < 4; i++)
            playerStates.remove(0);
    }
    
}
