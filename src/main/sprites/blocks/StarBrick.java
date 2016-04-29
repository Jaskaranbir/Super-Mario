package main.sprites.blocks;

import javafx.scene.Group;
import javafx.scene.image.Image;
import main.Main;
import static main.Main.REL_HEIGHT;
import main.sprites.Mario;
import main.sprites.Sprite;
import main.sprites.powerups.ImmunityStar;
import main.sprites.powerups.Powerup;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the StarBlock (which generates Immunity Star in game).
 * <b>Associated Class</b>: ImmunityStar
 */
public class StarBrick extends Sprite implements StageBlock {

    private Mario mario; 
    private Group parent;
    // The ImmunityStar
    private Powerup immu_star;
    
    // To switch between various hit stages
    // (0: Brick not Hit by Mario)
    // (1: Generate the ImmunityStar)
    // (2: Play star execution logic when the star is completely out of block and ready to be consumed by player)
    // (3: When star has been consumed by player; so it can be removed from scene and resources can be cleared)
    private int hitStats;
    // To display the bounce animation when player hits the brick
    private int tileLoop;
    // Stage zone in which the tile resides. See class StageFloorBase
    private final int zone;

    /**
     * Constructs a single Star Brick (brick which generates ImmunityStar).
     * @param mario The player (Mario).
     * @param stageZone Stage zone in which the brick resides. See class <em>StageFloorBase</em>.
     * @param parent The parent which contains the brick.
     * @param xPos The x-Position of brick.
     * @param yPos The y-Position of brick.
     * @param xVel The xVel of Star.
     * @param yVel The yVel of Star.
     * @param brickSprite Array having images for brick states (first element being default brick state and second element being brick state after player has collided).
     * @param starSprites The sprites for star animation effects.
     */
    public StarBrick(Mario mario, int stageZone, Group parent, float xPos, float yPos, float xVel, float yVel, Image[] brickSprite, Image... starSprites) {
        super(BLOCK_COLLISION_DATA, xPos, yPos, xVel, yVel, brickSprite);
        this.mario = mario;
        this.parent = parent;
        zone = stageZone;
        immu_star = new ImmunityStar(mario, stageZone, xPos, yPos, (float) (1.5 * REL_HEIGHT), 10, starSprites);
    }

    /**
     * Brick Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if (hitStats == 3)
            return;

        if (hitStats == 2)
            immu_star.update();

        else if (hitStats == 1) {
        // Bounce effect when player hits the Brick
            if (tileLoop < 30)
                if (tileLoop++ == 1)
                    this.getSpriteFrame().setTranslateY(this.getSpriteFrame().getTranslateY() - BOUNCE_HEIGHT);
                else if (tileLoop == 5)
                    this.getSpriteFrame().setTranslateY(this.getSpriteFrame().getTranslateY() + BOUNCE_HEIGHT);
            
            // Generate Star
            this.getSpriteFrame().setImage(playerStates.get(1));
            Main.updateScore(200);
            parent.getChildren().addAll(immu_star.getSpriteFrame(), immu_star.getSpriteBoundsArray()[1],immu_star.getSpriteBoundsArray()[2],immu_star.getSpriteBoundsArray()[3], immu_star.getSpriteBoundsArray()[4], immu_star.getSpriteBoundsArray()[5]);
            this.getSpriteFrame().toFront();
            hitStats = 2;
        }

        // Check if star has completed its cycle
        // (this includes falling in stage pit or getting consumed by player)
        if (immu_star.completeCycle()) {
            hitStats = 3;
            // If player has consumed powerup and player has atleast consumed level 1 powerup (fries), change player state accordingly
            if (immu_star.getPowerupConsumedStats() && mario.getMarioState() >= 1) {
                mario.setSetMarioState(2);
                // Set super aura (immunity)
                mario.resetAura();
                mario.setMidGrowth();
            }
            parent.getChildren().removeAll(immu_star.getSpriteFrame(), immu_star.getSpriteBoundsArray()[1],immu_star.getSpriteBoundsArray()[2],immu_star.getSpriteBoundsArray()[3], immu_star.getSpriteBoundsArray()[4], immu_star.getSpriteBoundsArray()[5]);
            immu_star = null;
            parent = null;
            tileLoop = 0;
            mario = null;
        }

    }

    /**
     * This marks that player has collided with tile.
     * The brick logic will not be run unless this condition is satisfied.
     */
    @Override
    public void collide() {
        if (hitStats == 0){
            hitStats = 1;
            Main.powerup_appear.play();
        }
    }

    /**
     * Used to check if the brick should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which Star Brick resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }

}
