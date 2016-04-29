package main.sprites.enemies;

import javafx.scene.Group;
import javafx.scene.image.Image;
import main.GameLoop;
import static main.Main.ASPECT_LENGTH;
import static main.Main.HEIGHT;
import static main.Main.REL_HEIGHT;
import main.SpriteHandler;
import main.sprites.Mario;
import main.sprites.MotionStageSprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the Goomba enemy.
 */
public class Goomba extends MotionStageSprite implements Enemy {
    
    private Group parent;
    private Mario mario;
    // To be able to stop gameloop and display end game sequence if player dies from this enemy
    private GameLoop gameLoop;
    
    // Death sprites for player (Mario); NOT the enemy
    private Image[] playerDeathSprites;
    
    // Counter to control Goomba animation
    private int frameCounter;
    // Stage Zone in which brick resides. See StageFloorBase class.
    private int zone;
    
    // Default height of Enemy; used to play shrinking effect when hit with player's (mario) fireball.
    private float frameHeight;
    // Amount by which height should decrease when hit by player's fireball.
    private float fbEffectDelta;
    
    // Will this enemy collide with StageBlocks (tile, brick)?
    // This then allows to check for collisions with blocks, which are otherwise disregarded due to performance
    private boolean checkBlockCollision;
    // If the cycle of this enemy completed (either by getting killed by Mario or by falling into pit)
    private boolean cycleComplete;
    // If the death sequence of this sprite is currently running
    // (So it doesn't move while animations (such as shrinking when hit by fireball) are being played).
    private boolean runDeathSeq;
    // If the enemy has sucessfully killed mario, stop all other animations and just play Mario's incineration animation
    private boolean marioDeathSeqRunning;
    // If the goomba was hit by Mario's fireball
    private boolean fireballHit;
    
    /**
     * Constructs a single Goomba enemy at specified location in stage.
     * @param parent The parent in which which the Goomba should reside.
     * @param gameLoop An instance of <em>GameLopp</em> class. Used to stop the main loop upon death of player (Mario).
     * @param mario The player (Mario). Used to check various interactions and play resultant logic.
     * @param stageZone Stage zone in which the Goomba resides. See class <em>StageFloorBase</em>.
     * @param checkBlockCollision Will this enemy collide with StageBlocks (tile, brick)? This then allows to check for collisions with blocks, which are otherwise disregarded due to performance.
     * @param SVGData The Collision Data for enemy.
     * @param xPos The x-Location of enemy.
     * @param yPos The y-Location of enemy.
     * @param xVel The x-Velocity of enemy.
     * @param yVel The y-Velocity of enemy.
     * @param playerDeathSprites Sprites to play player death animation (when Mario dies by this enemy).
     * @param sprites Sprites for Goomba's animations (0 and 1: For walking animation; 2: When Goomba dies by player).
     */
    public Goomba(Group parent, GameLoop gameLoop, Mario mario, int stageZone, boolean checkBlockCollision, String[] SVGData, float xPos, float yPos, float xVel, float yVel, Image[] playerDeathSprites, Image... sprites) {
        super(SVGData, stageZone, xPos, yPos, xVel, yVel, sprites);
        this.checkBlockCollision = checkBlockCollision;
        this.parent = parent;
        spriteBoundsArray[5].setTranslateX(xPos);
        spriteFrame.setTranslateX(xPos);
        spriteFrame.setTranslateY(yPos);
        frameHeight = ASPECT_LENGTH;
        fbEffectDelta = (float) (1.86 * REL_HEIGHT);
        this.mario = mario;
        zone = stageZone;
        this.gameLoop = gameLoop;
        this.playerDeathSprites = playerDeathSprites;
    }
    
    /**
     * Goomba Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        
        // If Goomba has killed player (Mario); play Mario death animation sequence
        if (marioDeathSeqRunning) {
            if (frameCounter % 10 == 0)
                if (frameCounter < 71)
                    mario.getSpriteFrame().setImage(playerDeathSprites[frameCounter / 10]);
                else if (frameCounter == 80)
                    mario.getSpriteFrame().setImage(null);
                
                else if (frameCounter == 120){
                    main.Main.gameBGM.stop();
                    main.Main.goombaAttack.play();
                    while (main.Main.goombaAttack.isPlaying()) {
                        // Wait for attack sound to finish before displaying game over screen
                    }
                    gameLoop.stop(true);
                }
            frameCounter++;
            return;
        }
        
        // If Goomba was killed by player
        if (runDeathSeq) {
            // If goomba was hit by player's fireball; play Goomba shrink animation
            if (fireballHit)
                if (frameHeight > 0) {
                    // Shrink Goomba and compensate y-Location as per reduced height
                    spriteFrame.setFitHeight(frameHeight);
                    spriteFrame.setTranslateY(spriteFrame.getTranslateY() + fbEffectDelta);
                    frameHeight -= fbEffectDelta;
                } else cycleComplete = true;
            
            // If Goomba was stomped by player and not hit by mario
            // Display goomba stomped sprite
            else if (frameCounter++ == 40)
                cycleComplete = true;
            
            return;
        }
        
        // Goomba walking animation
        if (frameCounter++ == 14)
            spriteFrame.setImage(playerStates.get(0));
        else if (frameCounter == 28) {
            spriteFrame.setImage(playerStates.get(1));
            frameCounter = 0;
        }
        
        // Apply Gravitational force if Goomba is in air
        if (!bottomCollide(checkBlockCollision))
            yPos += yVel;
        else
            // Otherwise if Goomba touches floor and in case it sinks in floor geometry, pull it up
            while (custCollide(4, checkBlockCollision)) {
                yPos -= 1;
                for (int i = 0; i < spriteBoundsArray.length; i++) {
                    if (spriteBoundsArray[i] == null)
                        continue;
                    spriteBoundsArray[i].setTranslateY(yPos);
                }
                spriteFrame.setTranslateY(yPos);
            }
        
        // If Goomba falls into pit
        if (yPos > HEIGHT)
            cycleComplete = true;
        
        // Change Goomba's direction if it collides from left or right side to some part of stage
        if (leftCollide(checkBlockCollision) || rightCollide(checkBlockCollision))
            xVel = -xVel;
            
        xPos -= xVel;
        updatePosition();
        attemptAttack();
    }
    
    /**
     * @return <em>True</em> if the Goomba has completed its cycle (died or fell in pit); otherwise <em>False</em>.
     */
    @Override
    public boolean getCycleStatus() {
        return cycleComplete;
    }
    
    /**
     * Action to perform if Mario stomps the Enemy.
     */
    @Override
    public void completeCycle() {
        // Begin playing Goomba's death sequence
        if (!runDeathSeq) {
            main.Main.goombaHit.play(0.7);
            runDeathSeq = true;
            main.Main.enemyCount++;
            spriteFrame.setImage(playerStates.get(2));
            // Remove collision box for this GOomba so mario can no longer interact with it once the Goomba is killed
            spriteBoundsArray[5] = null;
            frameCounter = 0;
        }
    }
    
    /**
     * Removes enemy from parent and frees up memory resources.
     * Must be called only when the enemy cycle has been completed.
     */
    @Override
    public void destroy() {
        for (int i = 0; i < spriteBoundsArray.length; i++)
            if (spriteBoundsArray[i] != null)
                parent.getChildren().remove(spriteBoundsArray[i]);
        
        parent.getChildren().remove(spriteFrame);
        parent = null;
        spriteFrame = null;
        spriteBoundsArray = null;
        xPos = yPos = xVel = yVel = frameCounter = 0;
        frameHeight = fbEffectDelta = zone = 0;
        mario = null;
        playerStates = null;
        playerDeathSprites = null;
        gameLoop = null;
        checkBlockCollision = runDeathSeq = cycleComplete = false;
    }
    
    /**
     * Action to perform if enemy touches Mario.
     */
    @Override
    public void attemptAttack() {
        // If Mario actually collided with Goomba
        if (checkPlayerCollided())
            
            // If Mario has God Mode on (took the ImmunityStar powerup)
            if (Mario.godMode) {
                main.Main.godModeHit.play(0.7);
                fireballHit();
            } else if (mario.isBottomCollided()) {
                // Otherwise set to play incineration sequence for Mario if Goomba killed it
                frameCounter = 0;
                marioDeathSeqRunning = true;
                // So player can no longer control Mario if Mario was killed by Goomba
                mario.setDeathSeqRunning();
                
                // Remove all other enemies from collision detection loop except current enemy
                // (since game is practically over now and donly current enemy's logic loop will be used to execute any more remaining sequences)
                for (int i = 0; i < SpriteHandler.getEnemyCast().size(); i++) {
                    Enemy e = SpriteHandler.getEnemyCast().get(i);
                    if (e != this) {
                        SpriteHandler.getEnemyCast().remove(e);
                        i--;
                    }
                }
                
                // If its small Mario, we need to compensate for height difference in incineration effect image sprites
                if (mario.getMarioState() == 0)
                    mario.getSpriteFrame().setTranslateY(mario.getSpriteFrame().getTranslateY() - main.Main.MARIO_SCALE_DIFF);
                else if(main.Main.auraLoop.isPlaying()){
                    main.Main.auraLoop.stop();
                    mario.removeAura();
                }
                main.Main.marioDeath.play();
            }
    }
    
    /**
     * Action to perform if Goomba is hit by Mario's fireball.
     */
    @Override
    public void fireballHit() {
        runDeathSeq = fireballHit = true;
        // Remove collision box for this GOomba so mario can no longer interact with it once the Goomba is killed
        parent.getChildren().remove(spriteBoundsArray[5]);
        spriteBoundsArray[5] = null;
        spriteFrame.setImage(playerStates.get(3));
        main.Main.enemyCount++;
    }
    
    /**
     * Used to check if the enemy should be included in collision detection and logic execution loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which enemy resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }
    
}
