package main.sprites.blocks;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;
import static main.Main.REL_HEIGHT;
import main.sprites.Mario;
import main.sprites.Sprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the SecretBlock (supposedly one-up block in original game but "YOLO" block in this game).
 */
public class SecretBlock extends Sprite implements StageBlock {

    // Parent in which the block resides
    private Group parent;
    // Stage zone in which the tile resides. See class StageFloorBase
    private final int zone;
    
    // Tile texture after it has been hit
    private final Image hitState;
    // ImageView for the "YOLO" text
    private ImageView yoloView;
   
    // Check if tile has already been hit
    private boolean wasHit;
    
    /**
     * Constructs a single SecretBlock (tile which generates one-ups in original game, but "YOLO" image in this game).
     * @param player The player (Mario).
     * @param parent The Parent in which the block resides.
     * @param stageZone Stage zone in which the tile resides. See class <em>StageFloorBase</em>.
     * @param xPos The x-Position of block.
     * @param yPos The y-Position of tile.
     * @param yoloWidth The default width of YOLO Image.
     * This is required because width starts from 0 by default (because height is zero by default and hence java Image sets width to zero according to aspect ratio as well).
     * @param hitState The Block Sprites array containing (after hit tile image as first and yolo image as second element).
     */
    public SecretBlock(Mario player, Group parent, int stageZone, float xPos, float yPos, float yoloWidth, Image... hitState) {
        super(BLOCK_COLLISION_DATA, xPos, yPos, 0, 0, (Image) null);
        this.hitState = hitState[0];
        yoloView = new ImageView(hitState[1]);
        yoloView.setFitWidth(yoloWidth);
        this.parent = parent;
        zone = stageZone;
    }
    
    /**
     * Block Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if(yoloView != null && wasHit){
            yPos -= 1;
            // Increase width and height by some value
            // And move image by 0.5 pixels to left every time it increases width to compensate for increased width making image align more to right
            // Also keep decreasing opacity (opacity here is to to check if the cycle has completed)
            yoloView.setTranslateY(yPos);
            yoloView.setFitWidth(yoloView.getFitWidth() + 1);
            yoloView.setTranslateX(yoloView.getTranslateX() - 0.5);
            yoloView.setFitHeight(yoloView.getFitWidth() * 48.0/123);
            yoloView.setOpacity(yoloView.getOpacity() - 0.003);
            
            // Remove effect once its cycle has completed
            if(yoloView.getOpacity() <= 0){
                parent.getChildren().remove(yoloView);
                yoloView = null;
                parent = null;
                xPos = 0;
                yPos = 0;
            }
        }
    }

    /**
     * This marks that player has collided with block.
     * The block logic will not run unless this condition takes place.
     */
    @Override
    public void collide() {
        if(!wasHit){
            Main.updateScore(500);
            main.Main.powerup_appear.play();
            super.getSpriteFrame().setImage(hitState);
            parent.getChildren().add(yoloView);
            spriteFrame.toFront();
            yoloView.setTranslateX(xPos - (0.746 * REL_HEIGHT));
            yoloView.setTranslateY(yPos);
            wasHit = true;
        }
    }
    
    /**
     * Used to check if the block should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which Block resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }
    
}
