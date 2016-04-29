package main.sprites;

import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import main.Main;
import static main.Main.HEIGHT;
import static main.Main.MARIOSCALEY_b;
import static main.Main.MARIOSCALEY_s;
import static main.Main.MARIO_SCALE_DIFF;
import static main.Main.REL_HEIGHT;
import static main.Main.STAGEWIDTH;
import static main.Main.WIDTH;
import main.SpriteHandler;
import main.sprites.blocks.StageBlock;
import main.sprites.enemies.Enemy;
import main.sprites.specials.Aura;
import main.sprites.specials.PlayerPower;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining Mario (player).
 */
public class Mario extends Sprite {

    //--------This class does not extend MotionStageSprite class because we need more control
    //--------And MotionStageSprite class is not intended for player
    
    // State 0: Small Mario
    // State 1: Big Mario
    // State 2: Super Mario
    private int MARIO_STATE;

    public final Main sMario;
    private Aura aura;

    // Collision data for small Mario
    private final String[] SVGData_s;
    // Collision data for big Mario
    private final String[] SVGData_b;
    
    private final float runVel;
    private final float sprintVel;
    private final float groundRunAccel;
    private final float groundSprintAccel;
    // Amount by which player should be 
    private final float yDec;
    // Set Mario slipping SPrite when speed exceeds this
    private final float frictionThreshold;
    private final float friction;
    private final float groundCollVel;
    // Amount of deceleration when player stops controlling Mario and speed is high enough
    private final float playerStopDecel;
    private final float jumpVel;
    // To limit maximum Velocity
    private final float maxVelExceedThreshold;
    // How much Mario should move along x-axis till the stage starts scrolling and Mario stays stationary (with animation still playing).
    private final float stageMoveTh;
    // To bring jump effect in corerct position relative to Mario
    private final float jumpEffectXTH;
    private float jumpEffectYTH;
    private float jumpTrailYTH;
    
    // Jump Effects default height
    private final float jumpTrailDefHeight;
    // Amount by which jump effect's height should increase
    private final float jumpTrailHeightDelta;
    // Amount of rebound when Mario stomps enemy
    private final float enemyHitBounce;
    // Need to set gravity to zero during final Flag Pole Sequence. Hence this to set it back to normal.
    private final float tempGravity;
    
    private float gravity;
    // If Mario collides from sides, this pushes him back
    private float sideCollidePush;
    // In case mario tries to go out of screen bounds using slipping
    // (run fast and friction takes a while to make you stop)
    private float outBoundThrowback;
    private float maxVel;
    // To adjust frame rates between sprinting and running/walking
    private float maxFrame;
    // Current ordinate of stage
    private float stageCoordinate;
    
    // Stage zone in which Mario curerntly resides. Check StageFloorBase class for more details on zones.
    public static int collidingZone;
    // To control animation of Mario
    private int frameLimiter;
    // Used to alternate between different sprites to show Walking animation
    private int switchFoot;
    // To control animation when Mario grows from big to small and other states
    private int stateFrame;
    
    // Global opacity of effects (jump effects, powerup light effects)
    private double opacity;
    
    // If God Mode should be enabled (only enabled after taking Invincibilty Star)
    public static boolean godMode;
    
    // Cache the calculations when Mario is standing idle. To save performance.
    private boolean isIdle;
    private boolean isFireballShot;
    private boolean isRightCollided;
    private boolean isLeftCollided;
    private boolean isBottomCollided;
    // Break jump if collided with something on top while jumping
    private boolean breakJump;
    
    // Disallow player controlling Mario if he is curerntly changing state
    private boolean midStateChange;
    // If Aura should be enabled (only enabled after second level powerup (powerface) is consumed)
    private boolean enableAura;
    // If speed is enough to apply significant friction
    private boolean frictionApp;
    // If Mario has entered castle
    private boolean endSeqFinish;
    // MIf Mario has been killled by an Enemy and his death (incineration) Sequence is still running
    private boolean isDeathSeqRunning;
    // If Mario is currently jumping (to prevent repeated jump keystrokes)
    private boolean isJumpCycleRunning;
    // If Flag Pole final sequence is in progress
    private boolean playFlagPoleSeq;
    
    /**
     * Constructs a single Mario.
     * @param sMario The instance of Main class from where SPrites data is taken.
     * @param SVGData_s Collision Data for small Mario.
     * @param SVGData_b Collision Data for Grown Mario.
     * @param xPos Starting x-Position of Mario.
     * @param yPos Starting y-Position of Mario.
     * @param xVel Starting x-Velocity of Mario.
     * @param yVel Starting y-Velocity of Mario.
     * @param sprites Sprites images for Mario.
     */
    public Mario(Main sMario, String[] SVGData_s, String[] SVGData_b, float xPos, float yPos, float xVel, float yVel, Image... sprites) {
        super(SVGData_s, xPos, yPos + 109, xVel, yVel, sprites);
        this.sMario = sMario;
        this.SVGData_s = SVGData_s;
        this.SVGData_b = SVGData_b;
        this.aura = sMario.getAura();

        runVel = 2.5f * REL_HEIGHT;
        sprintVel = 3.5f * REL_HEIGHT;
        gravity = 4.9f * REL_HEIGHT;
        tempGravity = gravity;
        jumpVel = yVel;
        yDec = REL_HEIGHT - (0.14f * REL_HEIGHT);
        frictionThreshold = 2.51f * REL_HEIGHT;
        friction = 0.5f * REL_HEIGHT;
        groundRunAccel = 0.01f * REL_HEIGHT;
        groundSprintAccel = 0.04f * REL_HEIGHT;
        groundCollVel = 0.23f * REL_HEIGHT;
        playerStopDecel = 0.1f * REL_HEIGHT;
        outBoundThrowback = 2 * REL_HEIGHT;
        maxVelExceedThreshold = maxVel + 0.5f;
        sideCollidePush = 2 * REL_HEIGHT;
        stageMoveTh = 0.6f * WIDTH;
        jumpEffectXTH = 17 * REL_HEIGHT;
        jumpEffectYTH = 12.85f * REL_HEIGHT;
        jumpTrailYTH = 7.25f * REL_HEIGHT;
        jumpTrailDefHeight = 8.9f * REL_HEIGHT;
        jumpTrailHeightDelta = 2.07f * REL_HEIGHT;
        enemyHitBounce = 5.97f * REL_HEIGHT;
    }
    
    /**
     * Mario Logic. Run by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if(isDeathSeqRunning)
            return;
        else if (midStateChange) {
            switchStates();
            return;
        }

        if (!isIdle) {
            // Caching the status of collision of sides with anything
            isRightCollided = sideCollide(1);
            isLeftCollided = sideCollide(3);

            if (sMario.isSprinting()) {
                maxVel = sprintVel;
                maxFrame = 3;
            } else {
                maxVel = runVel;
                maxFrame = 5;
            }
        }

        //---------------------- Mario X-Movement
        
        // Move Mario to right as required
        if (sMario.isRight() && !isRightCollided) {
            if (endSeqFinish && this.isInsideCastle()) {
                spriteFrame.setImage(null);
                spriteFrame = null;
                aura.getSpriteFrame().setImage(null);
                aura = null;
                if (main.Main.auraLoop.isPlaying())
                    main.Main.auraLoop.stop();
                sMario.getGameLoop().stop(false);
                return;
            }
            
            isIdle = false;
            if (xVel > frictionThreshold && super.getSpriteFrame().getScaleX() == -1) {
                frictionApp = true;
                super.getSpriteFrame().setImage(super.getPlayerStates().get(5));
                frameLimiter = -6;
            } else
                frictionApp = false;

            // Make Mario face right if facing left
            if (super.getSpriteFrame().getScaleX() != 1)
                super.getSpriteFrame().setScaleX(1);
            
            // Start scrolling stage instead of Mario if Mario is at the specific threshold point. Else move Mario.
            if (xPos < stageMoveTh || stageCoordinate <= -STAGEWIDTH) {
                xPos += xVel;
            } else if (stageCoordinate > -STAGEWIDTH) {
                stageCoordinate -= xVel;
                sMario.getStageContainer().setTranslateX(stageCoordinate);
                if (sMario.getJumpTrail().getOpacity() > 0) {
                    sMario.getJumpEffect().setTranslateX(sMario.getJumpEffect().getTranslateX() - xVel);
                    sMario.getJumpTrail().setTranslateX(sMario.getJumpTrail().getTranslateX() - xVel);
                    for (int i = 0; i < SpriteHandler.getPlayerPowerList().size(); i++) {
                        PlayerPower p = SpriteHandler.getPlayerPowerList().get(i);
                        if(p.getSpriteFrame() != null)
                            p.getSpriteFrame().setTranslateX(p.getSpriteFrame().getTranslateX() - xVel);
                    }
                }
            }
            applyFriction();
        }
        // Mario left movement
        else if (sMario.isLeft() && !isLeftCollided && xPos > 0) {
            isIdle = false;
            if (xVel > frictionThreshold && super.getSpriteFrame().getScaleX() == 1) {
                frictionApp = true;
                super.getSpriteFrame().setImage(super.getPlayerStates().get(5));
                frameLimiter = -6;
            } else
                frictionApp = false;
            
            // Make Mario face left if facing right
            if (super.getSpriteFrame().getScaleX() != -1)
                super.getSpriteFrame().setScaleX(-1);
            
            xPos -= xVel;
            applyFriction();
        } else if (xPos < 0)
            xPos += outBoundThrowback;
        else if (!isIdle && !isJumpCycleRunning)
            if (isRightCollided)
                xPos -= sideCollidePush;
            else if (isLeftCollided)
                xPos += sideCollidePush;

        // Check if player is trying to shoot fireball and if Mario has adequate powerup for shooting fireballs and limit Fireball count to two at a time
        if (sMario.isShootingFireball() && SpriteHandler.getPlayerPowerList().size() < 2 && MARIO_STATE > 1) {
            main.Main.fireballRelease.play(0.4);
            SpriteHandler.getPlayerPowerList().add(sMario.genFireball(xPos, yPos, (int) spriteFrame.getScaleX()));
            isFireballShot = true;
            isIdle = false;
        } else
            isFireballShot = false;

        if (!isIdle || sMario.isJumping()) {
            if ((sMario.isLeft() && sMario.isRight()))
                // Stop Mario if player pressed right and left button at same time
                xVel = 0;
            else if ((!sMario.isLeft() && !sMario.isRight() && !sMario.isUp() && !sMario.isDown())) {
                if (isFireballShot) {
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(3));
                    // To show Mario's "fireball shooting hand" sprite
                    frameLimiter = -12;
                } else if (!isJumpCycleRunning && frameLimiter > -1)
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(0));

                // Slow down player speed if high enough
                if (xVel > yDec) {
                    if (xVel > frictionThreshold)
                        super.getSpriteFrame().setImage(super.getPlayerStates().get(5));
                        
                    xVel -= playerStopDecel;
                    if (super.getSpriteFrame().getScaleX() == 1 && !isRightCollided)
                        xPos += xVel;
                    else if (!isLeftCollided)
                        xPos -= xVel;
                    
                    isIdle = false;
                } else if (yVel == jumpVel)
                    // Player has touched ground after jumping and is not doing anything for now
                    isIdle = true;

                if (frameLimiter < 0) {
                    isIdle = false;
                    frameLimiter++;
                }

            }
            // Mario walking/running/sprint animation
            else if (frameLimiter++ == maxFrame && !frictionApp && !sMario.isUp() && !sMario.isDown()) {
                if (isJumpCycleRunning)
                    // Apply jump sprite
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(4));
                else if (switchFoot == 0) {
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(3));
                    switchFoot++;
                } else if (switchFoot == 1) {
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(2));
                    switchFoot++;
                } else if (switchFoot == 2) {
                    super.getSpriteFrame().setImage(super.getPlayerStates().get(1));
                    switchFoot = 0;
                }
                frameLimiter = 0;
            }

            if (frameLimiter > maxFrame)
                frameLimiter = 0;

            //-----------------------Mario Y-Movement
            
            // Check if Mario is in air
            if (!bottomCollide(isJumpCycleRunning, false)) {
                yPos += gravity;
                isIdle = false;
                
                // Play end sequence
                if (!endSeqFinish && (playFlagPoleSeq || (collidingZone == 3 && flagPoleHit()))) {
                    if (!playFlagPoleSeq) {
                        SpriteHandler.getAllEnemyCast().clear();
                        SpriteHandler.getEnemyCast().clear();
                        SpriteHandler.getAllStageElements().clear();
                        SpriteHandler.getStageElements().clear();
                        playFlagPoleSeq = true;
                    }
                    playFlagPoleSeq();
                    return;
                }
            } else if (sMario.isJumping()) {
                // If player just triggered jump, play jump effect
                super.getSpriteFrame().setImage(super.getPlayerStates().get(4));
                isJumpCycleRunning = true;
                isIdle = false;
                opacity = 1;
                Main.pJump.play();
                sMario.getJumpEffect().setTranslateX(xPos - jumpEffectXTH);
                sMario.getJumpEffect().setTranslateY(yPos + jumpEffectYTH);

                sMario.getJumpTrail().setTranslateX(xPos - jumpEffectXTH);
                sMario.getJumpTrail().setTranslateY(yPos + jumpTrailYTH);
                sMario.getJumpTrail().setFitHeight(jumpTrailDefHeight);
            } else
                // If player sinks in game geometry by any chance, pull him up
                while (bottomCollide(false, true)) {
                    yPos -= groundCollVel;
                    // Slower down x-Velocity for no reason
                    if (xVel != friction)
                        xVel = friction;
                    
                    for (int i = 0; i < spriteBoundsArray.length; i++)
                        spriteBoundsArray[i].setTranslateY(yPos);
                        
                    spriteFrame.setTranslateY(yPos);
                    if (isIdle)
                        isIdle = false;
                }

            if (isJumpCycleRunning && !breakJump) {
                // Keep reducing jump speed after jumping until gravity takes over to make player fall
                yVel -= yDec;
                yPos -= (yVel + gravity);
            }

            playJumpTrailEffect();
        }

        // To make Mario fly.
        // Only intended for testing purposes.
        // (However the controls for up/down movement should be enabled in Main class first)
        if (sMario.isUp()) {
            yPos -= 8.9f * REL_HEIGHT;
            isIdle = false;
        } else if (sMario.isDown()) {
            yPos += REL_HEIGHT;
            isIdle = false;
        }

        if (enableAura)
            aura.update(xPos, yPos);

        if (isIdle) {
            playJumpTrailEffect();
            return;
        }

        // Mario fell into pit
        if (yPos > HEIGHT) {
            playPitFallSeq();
            return;
        }

        // If Mario is jumping, check for top collision and break jump if required
        if (isJumpCycleRunning)
            topCollide();

        updatePosition();
    }

    /**
     * Applies friction as required by Speed.
     */
    private void applyFriction() {
        // If speed goes too low, set it to minimum speed
        // (which coincidentally came out to be same as friction value)
        if (xVel < maxVel) {
            if (xVel < friction)
                xVel = friction;
            
            // Apply acceleration if required till Mario is not colliding and is within terminal speed limits
            if ((sMario.isLeft() && !isLeftCollided) || (sMario.isRight() && !isRightCollided)) {
                xVel += groundRunAccel;
                if (sMario.isSprinting())
                    xVel += groundSprintAccel;
            }
            
        } else if (xVel > maxVelExceedThreshold) {
            xVel -= friction;
        }
    }

    /**
     * Checks if Mario's top is colliding with something.
     * @return <em>True</em> if top is colliding, else <em>False</em>.
     */
    private boolean topCollide() {
        // Since basically StageBlocks is only thing Mario's top collide is considered with by default
        for (StageBlock sprite : SpriteHandler.getStageElements()) {
            Shape intersection = SVGPath.intersect(getSpriteBoundsArray()[0], sprite.getSpriteBounds());
            if (intersection.getBoundsInParent().getWidth() != -1) {
                if (!Main.bump.isPlaying())
                    Main.bump.play();
                    
                sprite.collide();
                breakJump = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Mario's side is colliding with something.
     * @return <em>True</em> if side is colliding, else <em>False</em>.
     */
    private boolean sideCollide(int scale) {
        Shape intersection;
        
        // Check if side is colliding with Stage Floor
        for (int i = 0; i < 4; i++) {
            intersection = SVGPath.intersect(getSpriteBoundsArray()[scale], sMario.getStageFloorBase().getSpriteBoundsArray()[i]);
            if (intersection.getBoundsInParent().getWidth() != -1)
                return true;
        }

        // Else check if side is colliding with StageBlocks
        for (StageBlock sprite : SpriteHandler.getStageElements()) {
            intersection = SVGPath.intersect(getSpriteBoundsArray()[scale], sprite.getSpriteBounds());
            if (intersection.getBoundsInParent().getWidth() != -1)
                return true;
        }

        return false;
    }

    /**
     * Checks if Mario's bottom is colliding with something. 
     * @param initJump If [;ayer is trying to jump. Used to make sure hat jump velocity remains adequate.
     * @param checkBaseTH If we need to check for player sinking into ground geometry to pull him back up.
     * @return <em>True</em> if side is colliding, else <em>False</em>.
     */
    private boolean bottomCollide(boolean initJump, boolean checkBaseTH) {
        Shape intersection;
        boolean collided = false;

        // Check collisions with Stage Floors
        // (remember that our stage is divided into 4 zones, check StageFloorClass)
        for (int i = 0; i < 4; i++) {
            intersection = SVGPath.intersect(getSpriteBoundsArray()[checkBaseTH ? 4 : 2], sMario.getStageFloorBase().getSpriteBoundsArray()[i]);
            collided = intersection.getBoundsInParent().getWidth() != -1;
            if (collided) {
                if (collidingZone != i) {
                    collidingZone = i;
                    sMario.getStageFloorBase().setActiveFloor(i);
                }
                break;
            }
        }

        // Otherwise check for collisions with StageBlocks
        if (!collided) {
            for (StageBlock s : SpriteHandler.getStageElements()) {
                intersection = SVGPath.intersect(getSpriteBoundsArray()[checkBaseTH ? 4 : 2], s.getSpriteBounds());
                collided = intersection.getBoundsInParent().getWidth() != -1;
                if (collided)
                    break;
            }

            // Otherwise check for collision with enemies
            if(!collided)
                for (int i = 0 ; i < SpriteHandler.getEnemyCast().size(); i++) {
                    Enemy e = SpriteHandler.getEnemyCast().get(i);
                    if (e.getSpriteBoundsArray() == null || e.getSpriteBoundsArray()[5] == null)
                        continue;
                
                    intersection = SVGPath.intersect(getSpriteBoundsArray()[2], e.getSpriteBoundsArray()[5]);
                    if (intersection.getBoundsInParent().getWidth() != -1) {
                        Main.updateScore(1000);
                        if (!godMode) {
                            yVel = enemyHitBounce;
                            yPos -= yVel;
                            isJumpCycleRunning = true;
                            e.completeCycle();
                        }
                        break;
                    }
                }
        }

        if (collided && initJump && !checkBaseTH) {
            yVel = jumpVel;
            isJumpCycleRunning = false;
            breakJump = false;
        }
        
        if(!checkBaseTH)
            isBottomCollided = collided;

        return collided;
    }

    /**
     * Check if Mario has hit the Flag Pole
     * @return <em>True</em> if Mario is colliding, else <em>False</em>.
     */
    private boolean flagPoleHit() {
        // Remember that Flag Pole is second element in MAIN_CAST ArrayList in SpriteHandler class (this is defined in Main class)
        Shape intersection = SVGPath.intersect(spriteBoundsArray[5], SpriteHandler.getCurrentCast().get(2).getSpriteBounds());
        return intersection.getBoundsInParent().getWidth() != -1;
    }

    /**
     * Check if Mario has entered Castle.
     * @return <em>True</em> if Mario is entered, else <em>False</em>.
     */
    private boolean isInsideCastle() {
        Shape intersection = SVGPath.intersect(spriteBoundsArray[5], SpriteHandler.getCurrentCast().get(0).getSpriteBoundsArray()[4]);
        return intersection.getBoundsInParent().getWidth() != -1;
    }

    /**
     * Updates position of Mario on stage.
     * Needs to be called manually in Mario logic (the <em>update</em> method).
     */
    @Override
    public void updatePosition() {
        for (int i = 0; i < spriteBoundsArray.length; i++) {
            spriteBoundsArray[i].setTranslateX(xPos);
            spriteBoundsArray[i].setTranslateY(yPos);
        }
        
        spriteFrame.setTranslateX(xPos);
        spriteFrame.setTranslateY(yPos);
    }

    /**
     * Used when Mario consumes a powerup. This disabled Player being able to control Mario while Mario's state is changing due to powerup.
     */
    public void setMidGrowth() {
        midStateChange = true;
    }

    /**
     * Update Collision data for Mario.
     * @param SVGData The collision data for Mario.
     */
    private void updateSVGArrContent(String[] SVGData) {
        // Used when Mario grows from small to big
        for (int i = 0; i < SVGData.length; i++)
            super.getSpriteBoundsArray()[i].setContent(SVGData[i]);
    }

    /**
     * Returns Mario's state as:<br>
     * State 0: Small Mario<br>
     * State 1: Big Mario<br>
     * State 2: Super Mario
     * @return Mario's State.
     */
    public int getMarioState() {
        return MARIO_STATE;
    }

    /**
     * Explicitely set Mario's state. Does not guarantee that Mario's state change animation will be played. <b>Use carefully</b>.
     * @param state Mario's State.
     */
    public void setSetMarioState(int state) {
        // Not intended to be greater than 2.
        MARIO_STATE = state;
    }

    /**
     * Used to switch to next Mario State when Mario consumes powerup.
     */
    public void switchStates() {
        // If Mario was in middle of jump while he consumed powerup, wee need to remove jump effects
        if (sMario.getJumpTrail().getOpacity() > 0) {
            sMario.getJumpTrail().setOpacity(0);
            sMario.getJumpEffect().setOpacity(0);
            opacity = 1;
        }

        // The Light effect that appears upon consuming powerup
        if (opacity > 0) {
            float decFactor = (float) (yPos / (opacity * jumpTrailHeightDelta));
            sMario.getPowerEffect(MARIO_STATE).setFitHeight(sMario.getPowerEffect(MARIO_STATE).getFitHeight() + decFactor);
            sMario.getPowerEffect(MARIO_STATE).setOpacity(opacity);
            sMario.getPowerEffect(MARIO_STATE).setTranslateY(sMario.getPowerEffect(MARIO_STATE).getTranslateY() - decFactor);
            // Switch Mario Sprites
            if (MARIO_STATE > 1 && playerStates.size() > 12 && opacity < 0.5)
                removeStates(6);
                
            opacity -= 0.016;
            if (enableAura)
                aura.update(xPos, yPos);
                
        } else if (enableAura) {
            sMario.removePowerupEffect();
            midStateChange = false;
            MARIO_STATE++;
            return;
        }

        // If Mario just consumed second or higher level powerups
        if (MARIO_STATE > 0) {
            if (MARIO_STATE >= 1 && !enableAura) {
                spriteFrame.setImage(playerStates.get(0));
                setAura();
                sMario.addPowerupEffect(xPos - jumpEffectXTH, (float) (191.02 * REL_HEIGHT), MARIO_STATE);
            }
            return;
        }

        // If Mario consumed first level powerup, play Mario growing animation
        if (stateFrame++ == 0) {
            jumpEffectYTH += MARIO_SCALE_DIFF;
            jumpTrailYTH += MARIO_SCALE_DIFF;
            yPos -= (MARIO_SCALE_DIFF * 1.25);
            spriteFrame.setTranslateY(yPos);
            sMario.addPowerupEffect(xPos - jumpEffectXTH, (float) (191.02 * REL_HEIGHT), MARIO_STATE);
        } else if (stateFrame == 6)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(12));
        else if (stateFrame == 16)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(0));
        else if (stateFrame == 28)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(12));
        else if (stateFrame == 38)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(0));
        else if (stateFrame == 50)
            super.getSpriteFrame().setImage(super.getPlayerStates().get(12));
        else if (stateFrame == 62) {
            super.getSpriteFrame().setImage(super.getPlayerStates().get(6));
            stateFrame = 0;
            this.updateSVGArrContent(SVGData_b);
            midStateChange = false;
            isIdle = false;
            sMario.removePowerupEffect();
            removeStates(6);
            playerStates.remove(6);
            MARIO_STATE++;
        }
    }

    /**
     * Sets Aura around Mario.
     */
    public void setAura() {
        sMario.getRoot().getChildren().add(aura.getSpriteFrame());
        Main.initAura.play();
        enableAura = true;
        aura.getSpriteFrame().toFront();
    }

    /**
     * To change White Aura to Golden Aura.
     */
    public void resetAura() {
        sMario.getRoot().getChildren().remove(aura.getSpriteFrame());
        enableAura = false;
        aura.enableSuperAura();
        removeStates(6);
        godMode = true;
    }

    /**
     * Plays Mario's jump effect sequence.
     */
    private void playJumpTrailEffect() {
        // The Global Opacity must be greater than 0. So it doesn't always play this effect.
        if (opacity > 0) {
            sMario.getJumpEffect().setOpacity(opacity);
            sMario.getJumpTrail().setOpacity(opacity);
            sMario.getJumpTrail().setFitHeight(sMario.getJumpTrail().getFitHeight() + (opacity * jumpTrailHeightDelta));
            sMario.getJumpTrail().setTranslateY(sMario.getJumpTrail().getTranslateY() - (opacity * jumpTrailHeightDelta));
            opacity -= 0.06;
        }
    }

    /**
     * The final Sequence played when Mario collides with Flag Pole.
     */
    private void playFlagPoleSeq() {
        // Since we won't be needing outBoundThrowBack variable, lets use it for controlling this sequence
        if (outBoundThrowback != 0) {
            if(spriteFrame.getScaleX() == -1)
                spriteFrame.setScaleX(1);
            
            // Make Mario go invincible mode if he is not already so
            if (MARIO_STATE <= 2) {
                MARIO_STATE = 1;
                opacity = 1;
                setMidGrowth();
                resetAura();
                updateSVGArrContent(SVGData_b);
                while (playerStates.size() > 12)
                    playerStates.remove(0);
                    
            }
            Main.auraLoop.play();
            spriteFrame.setImage(playerStates.get(0));
            gravity = sideCollidePush = xVel = yVel = outBoundThrowback = 0;
            sMario.removeEvents();
            sMario.getRoot().getChildren().add(sMario.getElevateEffectView());
            sMario.getElevateEffectView().setPreserveRatio(true);
        }

        // The Blue effect below Mario
        sMario.getElevateEffectView().setTranslateX(xPos - (MARIOSCALEY_s * 1.5));
        sMario.getElevateEffectView().setTranslateY(yPos + (MARIOSCALEY_b * 0.26));
        aura.update(xPos, yPos);
        
        if (yPos >= (41 * REL_HEIGHT))
            if (sideCollidePush++ < 10)
                // Make Mario stand Idle for a short duration
                spriteFrame.setImage(playerStates.get(11));
            else {
                // Instant transmission sequence
                main.Main.teleport.play();
                while (yPos >= (41 * REL_HEIGHT)) {
                    xPos -= 1;
                    yPos -= 1;
                    updatePosition();
                }
                spriteFrame.setImage(playerStates.get(0));
                sideCollidePush = -60;
            }
        else {
            if (sideCollidePush < 0) {
                sideCollidePush++;
                return;
            }
            
            // The yellowish light that appears in Mario's hands before Fireball
            if (sideCollidePush == 0) {
                sMario.getRoot().getChildren().add(sMario.getFlagDestroyEffectView());
                sMario.getFlagDestroyEffectView().setTranslateX(xPos - (22.4 * REL_HEIGHT));
                sMario.getFlagDestroyEffectView().setTranslateY(yPos - (11.2 * REL_HEIGHT));
            }

            // Mario Fireball Charge and Shoot sequence
            if (sideCollidePush < 177) {
                switch ((int) sideCollidePush++) {
                    case 6:
                        main.Main.energyCharge.play();
                        spriteFrame.setImage(playerStates.get(6));
                        // Compensate location for change in Sprite Width
                        spriteFrame.setTranslateX(xPos - (6.0 / 3392.0 * STAGEWIDTH));
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[0]);
                        break;
                    case 12:
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[1]);
                        break;
                    case 18:
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[2]);
                        break;
                    case 24:
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[3]);
                        break;
                    case 30:
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[4]);
                        break;
                    case 36:
                        sMario.getFlagDestroyEffectView().setImage(sMario.getFlagDestroyEffects()[5]);
                        Main.gameBGM.stop();
                        Main.flagPoleSeq_bgm.play();
                        break;
                    case 42:
                        sMario.getRoot().getChildren().remove(sMario.getFlagDestroyEffectView());
                        break;

                    case 63:
                        spriteFrame.setImage(playerStates.get(7));
                        break;
                    case 80:
                        spriteFrame.setImage(playerStates.get(8));
                        break;
                    case 126:
                        spriteFrame.setImage(playerStates.get(9));
                        break;
                    case 156:
                        spriteFrame.setImage(playerStates.get(10));
                        Main.energyRelease.play();
                        SpriteHandler.getPlayerPowerList().add(sMario.genFireballBig(xPos, yPos));
                        break;
                    case 176:
                        spriteFrame.setImage(playerStates.get(0));
                        spriteFrame.setTranslateX(spriteFrame.getTranslateX() + (6.0 / 3392.0 * STAGEWIDTH));
                        break;
                }
            } else if (SpriteHandler.getPlayerPowerList().isEmpty()) {
                // Fireball has hit the Flag Pole
                gravity = tempGravity;
                playFlagPoleSeq = false;
                sMario.getRoot().getChildren().remove(sMario.getElevateEffectView());
                SpriteHandler.getCurrentCast().get(0).getSpriteBoundsArray()[3].setContent(sMario.switchStagePath());
                sMario.setRight();
                endSeqFinish = true;
            }
        }
    }

    /**
     * Sequence played when Mario falls in Pit.
     */
    private void playPitFallSeq() {
        if (xVel != 0) {
            xVel = 0;
            sideCollidePush = 0;
            sMario.removeEvents();
            spriteFrame.setTranslateY(HEIGHT + 20);
        } else if (sideCollidePush > -1 && sideCollidePush++ > 50) {
            Main.marioPitFall.play();
            sideCollidePush = -1;
        } else if (sideCollidePush == -1 && !Main.marioPitFall.isPlaying())
            sMario.getGameLoop().stop(true);
    }

    /**
     * Removes old state sprites for Mario so new state sprites can be applied.
     * @param count The number of old state sprites to remove.
     */
    private void removeStates(int count) {
        for (int i = 0; i < count; i++)
            playerStates.remove(0);
    }
    
    /**
     * Used to mark start of death sequence of Mario.
     */
    public void setDeathSeqRunning(){
        // So player can't control Mario during death animation effects/
        isDeathSeqRunning = true;
    }

    /**
     * Check if Mario is in air.
     * @return <em>False</em> if Mario is in air; otherwise <em>True</em>.
     */
    public boolean isBottomCollided(){
        return isBottomCollided;
    }
    
    /**
     * Used to remove Aura for Mario (Mario must have atleast second level powerup for this to take effect).
     */
    public void removeAura(){
        // Currently used to transition aura to Golden state from white state
        enableAura = false;
        sMario.getRoot().getChildren().remove(aura.getSpriteFrame());
        aura = null;
    }
    
}
