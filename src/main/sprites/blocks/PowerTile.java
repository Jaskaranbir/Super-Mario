package main.sprites.blocks;

import javafx.scene.Group;
import javafx.scene.image.Image;
import main.Main;
import static main.Main.REL_HEIGHT;
import main.sprites.Mario;
import main.sprites.Sprite;
import main.sprites.powerups.Fries;
import main.sprites.powerups.Powerface;
import main.sprites.powerups.Powerup;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the Power Tiles (tiles generating powerups upon hit) in game.<br>
 * <b>Associated Classes</b>: Fries | Powerface
 */
public class PowerTile extends Sprite implements StageBlock {

    private Group parent;
    // The powerup to be displayed. Depends on MARIO_STATE (see Mario class)
    private Powerup power;
    public Mario mario;
    
    // The idle tile animation controller before player hits it
    private int tileLoop;
    
    // To switch between various hit stages
    // (0: Tile not Hit by Mario)
    // (1: Generate the correct powerup according to MARIO_STATE (see class Mario))
    // (2: When the power is completely out of block and ready to be consumed by player)
    // (3: When powerup has been consumed by player; so it can be removed from scene and resources can be cleared)
    private int hitStats;
    // Height of power; aspect ratio adjust automatically
    private int pHeight;
    // Stage zone in which the tile resides. See class StageFloorBase
    private final int zone;

    /**
     * Constructs a single Power Tile (tile which generates powerups).
     * @param mario The player (Mario).
     * @param stageZone Stage zone in which the tile resides. See class <em>StageFloorBase</em>.
     * @param xPos The x-Position of tile.
     * @param yPos The y-Position of tile.
     * @param xVel The xVel of Powerup (in case its motion powerup, like fries).
     * @param yVel The yVel (or gravity) of powerup.
     * @param parent The parent which contains the tile.
     * @param sprites The Tile sprites for animation effects.
     */
    public PowerTile(Mario mario, int stageZone, float xPos, float yPos, float xVel, float yVel, Group parent, Image... sprites) {
        super(BLOCK_COLLISION_DATA, xPos, yPos, 0, 0, sprites);
        this.parent = parent;
        this.mario = mario;
        pHeight = (int) ((18.0 * REL_HEIGHT) + 0.5);
        power = new Fries(stageZone, xPos, yPos, xVel, yVel, pHeight);
        zone = stageZone;
    }

    /**
     * Tile Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        // Return if tile has been hit
        if (hitStats == 3)
            return;

        // Initial idle tile animation before player hits tile
        if(hitStats == 0){
            switch (tileLoop) {
                case 10:
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(0));
                    break;
                case 30:
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(1));
                    break;
                case 40:
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(2));
                    tileLoop = 0;
                    break;
            }
            tileLoop++;
        }
        
        // Tile bounce effect once player collides with tile for first time
        else if(tileLoop < 30)
            if(tileLoop++ == 1)
                this.getSpriteFrame().setTranslateY(this.getSpriteFrame().getTranslateY() - BOUNCE_HEIGHT);
            else if(tileLoop == 5)
                this.getSpriteFrame().setTranslateY(this.getSpriteFrame().getTranslateY() + BOUNCE_HEIGHT);

        // Play powerup execution logic
        // (note that the else if condition below executes earlier than this)
        if(hitStats == 2){
            this.getSpriteFrame().toFront();
            // Power Logic Executed here----------------------------------------------
            // Till it gets consumed
            power.update();
        }
        
        // Create powerup according to MARIO_STATE (see Mario class) and add it to parent
        else if (hitStats == 1) {
            this.spriteFrame.setImage(playerStates.get(4));
            Main.updateScore(100);
            if(mario.getMarioState() == 0)
                parent.getChildren().addAll(power.getSpriteFrame(), power.getSpriteBoundsArray()[1], power.getSpriteBoundsArray()[5]);
            else{
                power = new Powerface(this, xPos, yPos, pHeight - (2 * REL_HEIGHT));
                parent.getChildren().addAll(power.getSpriteFrame(), power.getSpriteBounds());
            }
            hitStats = 2;
            tileLoop = 0;
        }
        
        // Remove stuff if powerup is consumed
        if(power.completeCycle()){
            hitStats = 3;
            // Modify Mario's power/state according to powerup and MARIO_STATE
            if(power.getPowerupConsumedStats() && mario.getMarioState() <= 1)
                mario.setMidGrowth();
            if(mario.getMarioState() == 0)
                // If this is first powerup, it has to be mushroom, hence remove multiple collision data array
                // (because the second level powerup after mushroom is stationary and hence has only one collision box)
                parent.getChildren().removeAll(power.getSpriteFrame(), power.getSpriteBoundsArray()[1], power.getSpriteBoundsArray()[5]);
            // Since after mushroom, its stationary powerup and thus doesn't need to have collision detection for sides
            // Hence only one hit box
            else parent.getChildren().removeAll(power.getSpriteFrame(), power.getSpriteBounds());
            pHeight = 0;
            power = null;
            parent = null;
            mario = null;
        }
    }

    /**
     * This marks that player has collided with tile.
     * The coin will not bounce unless collision takes place.
     */
    @Override
    public void collide() {
        if(hitStats == 0){
            hitStats = 1;
            Main.powerup_appear.play();
        }
    }

    /**
     * Used to check if the tile should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which Coin Tile resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }
        
}
