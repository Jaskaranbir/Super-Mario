package main.sprites.blocks;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;
import main.sprites.Sprite;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Class defining the Coin Tiles (tiles generating coins upon hit) in game.
 */
public class CoinTile extends Sprite implements StageBlock {

    private Group parent;
    
    private final int zone;

    // The idle tile animation controller before player hits it
    private int tileLoop;
    
    // Coin animation delay controller
    private int frame;
    
    // Coin height controller
    private int counter;
    
    // Current y-position of Coin
    private float cYPos;
    
    // Coin sprites to craete rotating animation
    private Image[] hitCoins;
    
    // Imageview containing coin
    public ImageView coinView;
    
    // To allow coin generation only once
    private boolean wasHit;
    // Force by which coin is pulled back into tile
    private boolean coinGravity;
    // Check if the coin has been generated and tile has completed the job
    private boolean hitStatus;

    /**
     * Constructs a single coin tile.
     * @param stageZone The stage zone in which Coin Tile resides. Please see class <b>StageFloorBase</b> for details for zones.
     * @param xPos x-Location of tile.
     * @param yPos y-Location of tile.
     * @param coinBounceVelocity Initial bounce velocity of coin.
     * @param coinSprites Coin Sprites to create rotating animation.
     * @param parent The parent in which the Tile resides. Used to add/remove coin.
     * @param sprites The brick Sprites for idle animation before player hits tile.
     */    
    public CoinTile(int stageZone, float xPos, float yPos, float coinBounceVelocity, Image[] coinSprites, Group parent, Image... sprites) {
        super(BLOCK_COLLISION_DATA, xPos, yPos, 0, coinBounceVelocity, sprites);
        this.hitCoins = coinSprites;
        coinView = new ImageView();
        this.parent = parent;
        this.parent.getChildren().add(coinView);
        coinView.setTranslateX(xPos);
        cYPos = yPos;
        zone = stageZone;
    }
    
    /**
     * Tile Logic. Executed by <em>GameLoop</em> class.
     */
    @Override
    public void update() {
        if (hitStatus)
            return;

        // Initial idle tile animation before player hits tile
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

        // Play coin animation once tile is hit by player
        if (wasHit) {
            this.getSpriteFrame().setImage(playerStates.get(4));
            frame++;
            switch (frame) {
                case 1:
                    this.spriteFrame.setTranslateY(this.spriteFrame.getTranslateY() - BOUNCE_HEIGHT);
                    Main.coinHit.play();
                    break;
                case 5:
                    this.spriteFrame.setTranslateY(this.spriteFrame.getTranslateY() + BOUNCE_HEIGHT);
                    break;
                case 10:
                    Main.updateScore(100);
                    Main.updateCoins();
                    coinView.setImage(hitCoins[0]);
                    break;
                case 25:
                    coinView.setImage(hitCoins[1]);
                    break;
                case 35:
                    coinView.setImage(hitCoins[2]);
                    coinGravity = true;
                    break;
                case 60:
                    coinView.setImage(hitCoins[3]);
                    break;
            }

            // Coin bounce effect
            if (counter++ < 70) {
                if (coinGravity) {
                    cYPos += yVel;
                } else {
                    cYPos -= yVel;
                }
            } else {
                // Remove stuff after completion.
                coinView.setImage(null);
                hitStatus = true;
                parent.getChildren().remove(coinView);
            }

            coinView.setTranslateY(cYPos);
            if(hitStatus){
                coinView = null;
                hitCoins = null;
                parent = null;
                coinGravity = false;
                xPos = yPos = xVel = yVel = cYPos = counter = frame = tileLoop = 0;
            }
        }
    }

    /**
     * This marks that player has collided with tile.
     * The coin will not bounce unless collision takes place.
     */
    @Override
    public void collide() {
        wasHit = true;
    }
   
    /**
     * Used to check if the tile should be included in collision detection loop.
     * The zone must match the zone in which player resides for inclusion in collision detection.
     * @return The stage zone in which CoinTile resides. Please see class <b>StageFloorBase</b> for details on zones.
     */
    @Override
    public int getZone() {
        return zone;
    }
}
