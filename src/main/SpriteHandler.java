package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import main.sprites.Sprite;
import main.sprites.blocks.StageBlock;
import main.sprites.enemies.Enemy;
import main.sprites.specials.PlayerPower;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Wednesday, April 21, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining Data Sets for the Sprites in game.<br>
 * Used to retrieve/remove and perform other operations such as <b>logic execution</b> with Sprites.
 */
public class SpriteHandler {
    
    // Easy access; since a lot of classes use these.
    private static final ArrayList<Enemy> ENEMIES = new ArrayList<>();
    private static final ArrayList<PlayerPower> POWERS = new ArrayList<>();
    
    // First element of ArrayList is the Stage Floor.
    // Second element is Mario (player).
    // Third element is Flag Pole.
    // (count still starts from zero, ofourse)
    private static final ArrayList<Sprite> MAIN_CAST = new ArrayList<>();
    
    private static final ArrayList<StageBlock> STAGE_ELEMENTS = new ArrayList<>();
    private static final ArrayList<Enemy> CURRENT_ENEMIES = new ArrayList<>();
    private static final HashSet<StageBlock> CURRENT_STAGE_ELEMENTS = new HashSet<>();
    
    /**
     * To get the main Sprites: The Player (Mario), Stage Floor, Flag Pole.<br>
     * Only Mario is dynamic Sprite, rest are static.<br>
     * Sprites that do not belong to any of the Interfaces must be accessed from here.
     * @return Main Sprites: The Player (Mario), Stage Floor, Flag Pole.
     */
    public static ArrayList<Sprite> getCurrentCast(){
        return MAIN_CAST;
    }
    
    /**
     * Returns all enemies. Whether active (whose logic is currently being executed) or inactive.
     * @return List containing all Enemy Sprites.
     */
    public static ArrayList<Enemy> getAllEnemyCast(){
        return ENEMIES;
    }
    
    /**
     * Returns all Stage Elements (StageBLock: Bricks and Tiles). Whether active (whose logic is currently being executed) or inactive.
     * @return List containing active StageBlock Sprites. 
     */
    public static ArrayList<StageBlock> getAllStageElements(){
        return STAGE_ELEMENTS;
    }
    
    /**
     * Returns only active (whose logic is currently being executed) enemies.
     * @return List containing active Enemy Sprites.
     */
    public static ArrayList<Enemy> getEnemyCast(){
        return CURRENT_ENEMIES;
    }
    
    /**
     * Returns all powers in motion (executed by player).
     * @return All Powers executed by player.
     */
    public static ArrayList<PlayerPower> getPlayerPowerList(){
        return POWERS;
    }
    
    /**
     * Returns only active (whose logic is currently being executed) StageBlocks (tiles and bricks).
     * @return Set containing active StageBlock Sprites.
     */
    public static HashSet<StageBlock> getStageElements(){
        return CURRENT_STAGE_ELEMENTS;
    }

    /**
     * Adds all Stage Elements (StageBLock: Bricks and Tiles) specified to global List (where both active and inactive StageBlocks exist).<br>
     * Activity (whose Logic is currently being executed) or inactivity is decided from <em>stageZone</em> property of StageBlock.
     * @param elements List containing StageBlock Sprites (must Implement <b>StageBlock</b> Interface). 
     */
    public static void setAllStageElements(StageBlock... elements){
        STAGE_ELEMENTS.addAll(Arrays.asList(elements));
    }
    
    /**
     * Adds all Enemies specified to global List (where both active and inactive Enemies exist).<br>
     * Activity (whose Logic is currently being executed) or inactivity is decided from <em>stageZone</em> property of Enemy.
     * @param enemies List containing Enemy Sprites (must Implement <b>Enemy</b> Interface). 
     */
    public static void setEnemyCast(Enemy... enemies){
        ENEMIES.addAll(Arrays.asList(enemies));
    }
        
    /**
     * Adds Sprite to a separate list. Only Sprites that do not implement any of the Interfaces shall be added.
     * @param sprites List of Sprites to add to List (must extend <b>Sprite</b> abstract class).
     */
    public static void addToCurrentCast(Sprite... sprites){
        MAIN_CAST.addAll(Arrays.asList(sprites));
    }
    
}
