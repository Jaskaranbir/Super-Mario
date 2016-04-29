package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.R;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.sprites.FlagPole;
import main.sprites.Mario;
import main.sprites.StageFloorBase;
import main.sprites.blocks.Brick;
import main.sprites.blocks.CoinTile;
import main.sprites.blocks.PowerTile;
import main.sprites.blocks.SecretBlock;
import main.sprites.blocks.StageBlock;
import main.sprites.blocks.StarBrick;
import main.sprites.enemies.Enemy;
import main.sprites.enemies.Goomba;
import main.sprites.specials.Aura;
import main.sprites.specials.Fireball;
import main.sprites.specials.FireballBig;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Main class where resources are defined and base objects are created.<br>
 * Also contains methods for generating various stage elements.
 */
public class Main extends Application {

    public static int WIDTH, HEIGHT;
    public static int STAGEWIDTH;
    public static int enemyCount;
    public static float REL_HEIGHT;
    public static float ASPECT_LENGTH;
    public static float MARIOSCALEX_s;
    public static float MARIOSCALEY_s;
    public static float MARIOSCALEY_b;
    public static float MARIO_SCALE_DIFF;

    // Menu sounds
    public static AudioClip menuM;
    public static AudioClip menuSelection;
    public static AudioClip menuNav;
    public static AudioClip gameBGM;
    public static AudioClip flagPoleSeq_bgm;

    // Player sounds
    public static AudioClip pJump;
    public static AudioClip bump;
    public static AudioClip initAura;
    public static AudioClip auraLoop;
    
    public static AudioClip marioDeath;
    public static AudioClip coinHit;
    public static AudioClip fireballRelease;
    public static AudioClip fireballHit;
    public static AudioClip fireballIncinerate;
    public static AudioClip godModeHit;
    public static AudioClip marioPitFall;
    public static AudioClip teleport;

    public static AudioClip powerup_appear;
    public static AudioClip powerupTaken;

    public static AudioClip goombaHit;
    public static AudioClip goombaAttack;

    public static AudioClip energyCharge;
    public static AudioClip energyRelease;
    public static AudioClip energyImpact;

    private static Label scoresDisplay;
    private static Label coinCount;

    private Scene scene;
    private Group root;
    // Container in which all game elements including stage floor are added for easy scrolling of stage.
    private Group stageContainer;
    private StackPane menuContainer;
    private HBox buttonContainer;
    private HBox coinsDisplay;
    private HBox pauseLabelBox;
    private VBox quitDialog;

    private GameLoop gameLoop;
    private Mario player;
    private StageFloorBase stageFloorBase;
    private Aura aura;
    private FlagPole flag;

    private float auraScaleY;
    private float auraScaleX;

    private float fireballScaleX;
    private float fireballScaleY;

    // Tracks current status of menu
    // Used to help navigating menu using keyboard
    private int menuStatus;

    private Image splash;
    private Image pauseSplash;
    private Image gameOverSplash;
    private Image creditsImg;

    // For more control over each Sprite Image, array has not been used.
    // And to bask in glory of number of states our main Character has!
    private Image mario0;
    private Image mario1;
    private Image mario2;
    private Image mario3;
    private Image mario4;
    private Image mario5;
    private Image mario6;
    private Image mario7;
    private Image mario8;
    private Image mario9;
    private Image mario10;
    private Image mario11;
    private Image mario12;
    private Image mario13;
    private Image mario14;
    private Image mario15;
    private Image mario16;
    private Image mario17;
    private Image mario18;
    private Image mario19;
    private Image mario20;
    private Image mario21;
    private Image mario22;
    private Image mario23;
    private Image mario24;

    private Image fireBall;
    private Image fireballb;

    private Image floorBase;
    private Image brick0;

    private Image tile0;
    private Image tile1;
    private Image tile2;
    private Image tile3;
    private Image tile4;

    private Image goomba0;
    private Image goomba1;
    private Image goomba2;
    private Image goomba3;

    // Image for hidden tile (the one up tile)
    private Image yolo;
    
    private Image[] auraSprites;
    private Image[] auraWindEffects;

    private Image[] flagPoleSprites;
    private Image[] flagDesEffects;

    private Image[] fireballBlast;

    private Image[] invinciStarSprites;

    private Image[] hitCoins;
    private Image[] powerupEffect;

    private Image[] playerDeathSprites;
    
    // Just so we don't have to create new Image[] everytime a StarBrick is generated
    private Image[] starBrickSprites;

    private ImageView splashView;
    private ImageView creditsView;
    private ImageView scoresView;

    // The coin displayed top-right corner with coin count
    private ImageView statsCoin;
    
    private ImageView jumpEffect;
    private ImageView jumpTrail;
    private ImageView powerupEffectView;

    private ImageView elevateEffectView;
    private ImageView flagDestroyEffectView;

    private Label stats;
    private Label pauseLabel;
    private Label quitConfirm;

    private Button play;
    private Button hScore;
    private Button credits;
    private Button quit;
    private Button quitY;
    private Button quitN;

    // Used to control the player movement
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean sprint;
    private boolean shootFireball;
    private boolean allowFireball;

    @Override
    public void start(Stage primaryStage) {
        readStats();
        loadImageAssets();
        loadAudioAssets();
        initUIComponents();
        setCurrentCast();
        initEvents();

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        menuM.play();
        coinsDisplay.setTranslateX(WIDTH - coinsDisplay.getWidth());
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads all the Image resources of game.
     */
    private void loadImageAssets() {
        splash = new Image(getClass().getResource("/res/splash.png").toString(), WIDTH, HEIGHT, true, true, true);
        pauseSplash = new Image(getClass().getResource("/res/pause.png").toString(), WIDTH, HEIGHT, false, true, true);
        gameOverSplash = new Image(getClass().getResource("/res/gameOver_splash.png").toString(), 0, HEIGHT, true, false, true);
        creditsImg = new Image(getClass().getResource("/res/credits.png").toString(), WIDTH, HEIGHT, true, true, true);

        MARIOSCALEY_s = (float) ASPECT_LENGTH - 5;
        MARIOSCALEX_s = (float) (12.0 / 16 * MARIOSCALEY_s);
        MARIOSCALEY_b = ((float) (31.0 * ASPECT_LENGTH / 16.0)) - 10;
        MARIO_SCALE_DIFF = MARIOSCALEY_b - MARIOSCALEY_s;
        mario0 = new Image(getClass().getResource("/res/sprites/mario/0.png").toString(), (int) (12.0 / 16 * MARIOSCALEY_s), MARIOSCALEY_s, true, false, true);
        mario1 = new Image(getClass().getResource("/res/sprites/mario/1.png").toString(), (int) (12.0 / 15 * ((int) (15.0 * REL_HEIGHT) - 5)), (int) (15.0 * REL_HEIGHT) - 5, true, false, true);
        mario2 = new Image(getClass().getResource("/res/sprites/mario/2.png").toString(), (int) (15.0 / 16 * MARIOSCALEY_s), MARIOSCALEY_s, true, false, true);
        mario3 = new Image(getClass().getResource("/res/sprites/mario/3.png").toString(), (int) (15.0 / 16 * MARIOSCALEY_s), MARIOSCALEY_s, true, false, true);
        mario4 = new Image(getClass().getResource("/res/sprites/mario/4.png").toString(), MARIOSCALEY_s, MARIOSCALEY_s, true, false, true);
        mario5 = new Image(getClass().getResource("/res/sprites/mario/5.png").toString(), (int) (13.0 / 16 * MARIOSCALEY_s), MARIOSCALEY_s, true, false, true);

        mario6 = new Image(getClass().getResource("/res/sprites/mario/6.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario7 = new Image(getClass().getResource("/res/sprites/mario/7.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario8 = new Image(getClass().getResource("/res/sprites/mario/8.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario9 = new Image(getClass().getResource("/res/sprites/mario/9.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario10 = new Image(getClass().getResource("/res/sprites/mario/10.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario11 = new Image(getClass().getResource("/res/sprites/mario/11.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);

        mario12 = new Image(getClass().getResource("/res/sprites/mario/12.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);

        mario13 = new Image(getClass().getResource("/res/sprites/mario/13.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario14 = new Image(getClass().getResource("/res/sprites/mario/14.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario15 = new Image(getClass().getResource("/res/sprites/mario/15.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario16 = new Image(getClass().getResource("/res/sprites/mario/16.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario17 = new Image(getClass().getResource("/res/sprites/mario/17.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);
        mario18 = new Image(getClass().getResource("/res/sprites/mario/18.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);

        mario19 = new Image(getClass().getResource("/res/sprites/mario/19.png").toString(), MARIOSCALEY_s + (6.0 / 3392.0 * STAGEWIDTH), MARIOSCALEY_b, true, false, true);
        mario20 = new Image(getClass().getResource("/res/sprites/mario/20.png").toString(), MARIOSCALEY_s + (6.0 / 3392.0 * STAGEWIDTH), MARIOSCALEY_b, true, false, true);
        mario21 = new Image(getClass().getResource("/res/sprites/mario/21.png").toString(), MARIOSCALEY_s + (6.0 / 3392.0 * STAGEWIDTH), MARIOSCALEY_b, true, false, true);
        mario22 = new Image(getClass().getResource("/res/sprites/mario/22.png").toString(), MARIOSCALEY_s + (6.0 / 3392.0 * STAGEWIDTH), MARIOSCALEY_b, true, false, true);
        mario23 = new Image(getClass().getResource("/res/sprites/mario/23.png").toString(), MARIOSCALEY_s + (6.0 / 3392.0 * STAGEWIDTH), MARIOSCALEY_b, true, false, true);
        mario24 = new Image(getClass().getResource("/res/sprites/mario/24.png").toString(), MARIOSCALEY_s, MARIOSCALEY_b, true, false, true);

        playerDeathSprites = new Image[8];
        for (int i = 0; i < 8; i++) {
            playerDeathSprites[i] = new Image(getClass().getResource("/res/sprites/mario/actions/death/" + i + ".png").toString(), 0, MARIOSCALEY_b, true, true, true);
        }

        tile0 = new Image(getClass().getResource("/res/sprites/stage/tiles/tile0.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, false, true);
        tile1 = new Image(getClass().getResource("/res/sprites/stage/tiles/tile1.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, false, true);
        tile2 = new Image(getClass().getResource("/res/sprites/stage/tiles/tile2.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, false, true);
        tile3 = new Image(getClass().getResource("/res/sprites/stage/tiles/tile_collide.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, true, true);
        tile4 = new Image(getClass().getResource("/res/sprites/stage/tiles/tile_aftercollide.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, false, true);

        brick0 = new Image(getClass().getResource("/res/sprites/stage/brick.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, false, false, true);

        hitCoins = new Image[4];
        hitCoins[0] = new Image(getClass().getResource("/res/sprites/stage/hit_coins/0.png").toString(), ASPECT_LENGTH - 3, ASPECT_LENGTH - 3, false, false, false);
        hitCoins[1] = new Image(getClass().getResource("/res/sprites/stage/hit_coins/1.png").toString(), ASPECT_LENGTH - 3, ASPECT_LENGTH - 3, false, false, false);
        hitCoins[2] = new Image(getClass().getResource("/res/sprites/stage/hit_coins/2.png").toString(), ASPECT_LENGTH - 3, ASPECT_LENGTH - 3, false, false, false);
        hitCoins[3] = new Image(getClass().getResource("/res/sprites/stage/hit_coins/3.png").toString(), ASPECT_LENGTH - 3, ASPECT_LENGTH - 3, false, false, false);

        floorBase = new Image(getClass().getResource("/res/sprites/stage/stage.png").toString(), 0, HEIGHT, true, false, true);

        auraSprites = new Image[8];
        auraScaleY = (float) (MARIOSCALEY_b + (21 * REL_HEIGHT));
        auraScaleX = (float) (MARIOSCALEY_s + (31.1 * REL_HEIGHT));
        for (int i = 0; i < 8; i++) {
            auraSprites[i] = new Image(getClass().getResource("/res/sprites/mario/actions/aura/a" + i + ".png").toString(), 0, 0, true, true, true);
        }
        auraWindEffects = new Image[15];
        for (int i = 0; i < 15; i++) {
            auraWindEffects[i] = new Image(getClass().getResource("/res/sprites/mario/actions/aura/envEffect_" + i + ".png").toString(), auraScaleX, 0, true, true, true);
        }

        jumpEffect = new ImageView(new Image(getClass().getResource("/res/sprites/mario/actions/jump_floorEffect.png").toString(), 41 * REL_HEIGHT, 5.2 * REL_HEIGHT, false, true, true));
        jumpEffect.setOpacity(0);
        jumpTrail = new ImageView(new Image(getClass().getResource("/res/sprites/mario/actions/jump_trailw.png").toString(), 41 * REL_HEIGHT, 9.3 * REL_HEIGHT, false, true, true));
        jumpTrail.setOpacity(0);

        powerupEffect = new Image[3];
        powerupEffect[0] = new Image(getClass().getResource("/res/sprites/mario/actions/powerup_take_1.png").toString(), 41 * REL_HEIGHT, 9.3 * REL_HEIGHT, false, true, true);
        powerupEffect[1] = new Image(getClass().getResource("/res/sprites/mario/actions/powerup_take_2.png").toString(), 41 * REL_HEIGHT, 9.3 * REL_HEIGHT, false, true, true);
        powerupEffect[2] = new Image(getClass().getResource("/res/sprites/mario/actions/powerup_take_3.png").toString(), 41 * REL_HEIGHT, 9.3 * REL_HEIGHT, false, true, true);
        powerupEffectView = new ImageView();

        yolo = new Image(getClass().getResource("/res/sprites/stage/powerups/yolo.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH * (48.0 / 123), true, true, true);

        fireballScaleX = (float) (11.7 * REL_HEIGHT);
        fireballScaleY = (float) (11.2 * REL_HEIGHT);
        fireBall = new Image(getClass().getResource("/res/sprites/mario/actions/fireball/fireball.png").toString(), 0, fireballScaleY, true, true, true);

        fireballb = new Image(getClass().getResource("/res/sprites/mario/actions/fireball/fireballb.png").toString(), 0, fireballScaleY + 20, true, true, true);

        fireballBlast = new Image[12];
        for (int i = 0; i < 12; i++) {
            fireballBlast[i] = new Image(getClass().getResource("/res/sprites/mario/actions/fireball/blast_" + i + ".png").toString(), fireballScaleX, 0, true, true, true);
        }

        flagPoleSprites = new Image[6];
        for (int i = 0; i < 5; i++) {
            flagPoleSprites[i] = new Image(getClass().getResource("/res/sprites/stage/flagPole/" + i + ".png").toString(), 0, 172 * REL_HEIGHT, true, true, false);
        }

        invinciStarSprites = new Image[23];
        for (int i = 0; i < 23; i++) {
            invinciStarSprites[i] = new Image(getClass().getResource("/res/sprites/stage/powerups/invinci_star/" + i + ".png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, true, true, false);
        }

        goomba0 = new Image(getClass().getResource("/res/sprites/enemies/goomba/0.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, true, true, false);
        goomba1 = new Image(getClass().getResource("/res/sprites/enemies/goomba/1.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, true, true, false);
        goomba2 = new Image(getClass().getResource("/res/sprites/enemies/goomba/2.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, true, true, false);
        goomba3 = new Image(getClass().getResource("/res/sprites/enemies/goomba/3.png").toString(), ASPECT_LENGTH, ASPECT_LENGTH, true, true, false);

        Image elevateEffect = new Image(getClass().getResource("/res/sprites/mario/actions/elevate_effect.png").toString(), MARIOSCALEY_s * 4, 0, true, true, false);
        elevateEffectView = new ImageView(elevateEffect);

        flagDesEffects = new Image[6];
        for (int i = 0; i < 6; i++) {
            flagDesEffects[i] = new Image(getClass().getResource("/res/sprites/mario/actions/flagDestroy_effect/" + i + "s.png").toString(), MARIOSCALEY_s * 4, 0, true, true, false);
        }
        flagDestroyEffectView = new ImageView();
        
        statsCoin = new ImageView(new Image(getClass().getResource("/res/coinsDisplay_icon.gif").toString(), 0, 6.36 * REL_HEIGHT, true, true, false));
    }

    /**
     * Loads all the Sound resources of game.
     */
    private void loadAudioAssets() {
        menuM = new AudioClip(getClass().getResource("/res/sounds/menu.mp3").toString());
        menuSelection = new AudioClip(getClass().getResource("/res/sounds/menuSelect.wav").toString());
        menuNav = new AudioClip(getClass().getResource("/res/sounds/menuChange.wav").toString());
        gameBGM = new AudioClip(getClass().getResource("/res/sounds/bgm.mp3").toString());

        pJump = new AudioClip(getClass().getResource("/res/sounds/pJump.mp3").toString());
        bump = new AudioClip(getClass().getResource("/res/sounds/bump.wav").toString());
        godModeHit = new AudioClip(getClass().getResource("/res/sounds/godModeHit.wav").toString());
        energyCharge = new AudioClip(getClass().getResource("/res/sounds/energyCharge.mp3").toString());
        energyRelease = new AudioClip(getClass().getResource("/res/sounds/energyBeamRelease.wav").toString());
        energyImpact = new AudioClip(getClass().getResource("/res/sounds/energyImpact.wav").toString());
        teleport = new AudioClip(getClass().getResource("/res/sounds/teleport.wav").toString());
        marioPitFall = new AudioClip(getClass().getResource("/res/sounds/marioPitFall.wav").toString());
        marioDeath = new AudioClip(getClass().getResource("/res/sounds/marioDeath.mp3").toString());

        coinHit = new AudioClip(getClass().getResource("/res/sounds/coinHit.mp3").toString());
        powerup_appear = new AudioClip(getClass().getResource("/res/sounds/powerup_appear.wav").toString());
        powerupTaken = new AudioClip(getClass().getResource("/res/sounds/powerup_taken.wav").toString());

        initAura = new AudioClip(getClass().getResource("/res/sounds/auraInit.mp3").toString());
        auraLoop = new AudioClip(getClass().getResource("/res/sounds/auraLoop.mp3").toString());

        fireballRelease = new AudioClip(getClass().getResource("/res/sounds/fireball_release.mp3").toString());
        fireballHit = new AudioClip(getClass().getResource("/res/sounds/fireballHit.mp3").toString());
        fireballIncinerate = new AudioClip(getClass().getResource("/res/sounds/fireballIncinerate.wav").toString());

        goombaHit = new AudioClip(getClass().getResource("/res/sounds/goombaHit.wav").toString());
        goombaAttack = new AudioClip(getClass().getResource("/res/sounds/goombaAttack.mp3").toString());

        flagPoleSeq_bgm = new AudioClip(getClass().getResource("/res/sounds/flagPoleSeq_bgm.mp3").toString());
    }

    /**
     * Initializes game elements and adds them to respective containers.
     */
    private void initUIComponents() {
        root = new Group();
        stageContainer = new Group();
        menuContainer = new StackPane();
        quitDialog = new VBox(16);
        quitDialog.setAlignment(Pos.CENTER);

        menuContainer.setId("menuContainer");

        scene = new Scene(root, WIDTH, HEIGHT, Color.rgb(107, 140, 255));

        play = new Button("PLAY");
        hScore = new Button("STATS");
        credits = new Button("CREDITS");
        quit = new Button("QUIT");
        quitY = new Button("YES");
        quitN = new Button("NO");

        HBox quitOptions = new HBox(12, quitY, quitN);
        quitOptions.setAlignment(Pos.CENTER);

        quitConfirm = new Label("Are you sure you want to quit?");
        quitConfirm.setId("quitConfirmLbl");
        quitConfirm.setStyle("-fx-font-size: " + (9.955 * REL_HEIGHT));
        quitDialog.getChildren().addAll(quitConfirm, quitOptions);
        quitDialog.setId("quitDialog");
        quitDialog.setVisible(false);

        buttonContainer = new HBox(22, play, hScore, credits, quit);
        buttonContainer.setAlignment(Pos.BOTTOM_CENTER);
        buttonContainer.setPadding(new Insets(0, 0, (REL_HEIGHT * 20.0), 0));

        splashView = new ImageView(splash);
        creditsView = new ImageView(creditsImg);
        creditsView.setVisible(false);

        scoresView = new ImageView(new Image(getClass().getResource("/res/score_bg.png").toString(), WIDTH, HEIGHT, true, false, false));
        scoresView.setVisible(false);

        scoresDisplay = new Label("SCORE\n0000000");
        scoresDisplay.setStyle("-fx-font-size: " + (5.6 * REL_HEIGHT) + "px");
        scoresDisplay.setVisible(false);
        scoresDisplay.setId("scoresDisplayLabel");

        pauseLabel = new Label("Game Paused");
        pauseLabel.setVisible(false);
        pauseLabel.setStyle("-fx-text-fill: white");
        pauseLabelBox = new HBox(pauseLabel);
        pauseLabelBox.setId("pauseLbl");
        pauseLabelBox.setAlignment(Pos.TOP_CENTER);
        pauseLabelBox.setPadding(new Insets((REL_HEIGHT * 20.0), 0, 0, 0));

        coinCount = new Label("x0");
        coinsDisplay = new HBox(5, statsCoin, coinCount);
        coinCount.setStyle("-fx-font-size: " + (5.6 * REL_HEIGHT) + "px");
        coinsDisplay.setVisible(false);
        coinCount.setId("scoresDisplayLabel");

        menuContainer.setPrefSize(WIDTH, HEIGHT);
        menuContainer.getChildren().addAll(quitDialog, splashView, scoresView, stats, creditsView, buttonContainer, pauseLabelBox);
        stageFloorBase = new StageFloorBase(
                new String[]{
                    // First Stage Zone
                    " M " + RelCoordinates.getRelCoords(768, 136)
                    + " L " + RelCoordinates.getRelCoords(768, 136, 768, 148)
                    + RelCoordinates.getRelCoords(768, 148, 766, 160)
                    + RelCoordinates.getRelCoords(766, 160, 766, 200)
                    + RelCoordinates.getRelCoords(766, 200, 914, 200)
                    + RelCoordinates.getRelCoords(914, 200, 914, 160)
                    + RelCoordinates.getRelCoords(914, 160, 912, 148)
                    + RelCoordinates.getRelCoords(912, 148, 912, 136)
                    + RelCoordinates.getRelCoords(912, 136, 944, 136)
                    + RelCoordinates.getRelCoords(944, 136, 944, 148)
                    + RelCoordinates.getRelCoords(944, 148, 942, 160)
                    + RelCoordinates.getRelCoords(942, 160, 942, 200)
                    + RelCoordinates.getRelCoords(942, 200, 1104, 200)
                    + RelCoordinates.getRelCoords(1104, 200, 1104, 224)
                    + RelCoordinates.getRelCoords(1104, 224, 0, 224)
                    + RelCoordinates.getRelCoords(0, 224, 0, 200)
                    + RelCoordinates.getRelCoords(0, 200, 450, 200)
                    + RelCoordinates.getRelCoords(450, 200, 450, 190)
                    + RelCoordinates.getRelCoords(450, 190, 448, 181)
                    + RelCoordinates.getRelCoords(448, 181, 448, 168)
                    + RelCoordinates.getRelCoords(448, 168, 480, 168)
                    + RelCoordinates.getRelCoords(480, 168, 480, 181)
                    + RelCoordinates.getRelCoords(480, 181, 478, 187)
                    + RelCoordinates.getRelCoords(478, 187, 478, 200)
                    + RelCoordinates.getRelCoords(478, 200, 610, 200)
                    + RelCoordinates.getRelCoords(610, 200, 610, 176)
                    + RelCoordinates.getRelCoords(610, 176, 608, 164)
                    + RelCoordinates.getRelCoords(608, 164, 608, 152)
                    + RelCoordinates.getRelCoords(608, 152, 640, 152)
                    + RelCoordinates.getRelCoords(640, 152, 640, 164)
                    + RelCoordinates.getRelCoords(640, 164, 638, 176)
                    + RelCoordinates.getRelCoords(638, 176, 638, 200)
                    + RelCoordinates.getRelCoords(638, 200, 738, 200)
                    + RelCoordinates.getRelCoords(738, 200, 738, 160)
                    + RelCoordinates.getRelCoords(738, 160, 736, 148)
                    + RelCoordinates.getRelCoords(736, 148, 736, 136)
                    + RelCoordinates.getRelCoords(736, 136, 768, 136) + " Z",
                    // Second Stage Zone
                    " M " + RelCoordinates.getRelCoords(1376, 200)
                    + " L " + RelCoordinates.getRelCoords(1376, 200, 1376, 224)
                    + RelCoordinates.getRelCoords(1376, 224, 1136, 224)
                    + RelCoordinates.getRelCoords(1136, 224, 1136, 200)
                    + RelCoordinates.getRelCoords(1136, 200, 1376, 200) + " Z",
                    // Third Stage Zone
                    " M " + RelCoordinates.getRelCoords(2208, 136)
                    + " L " + RelCoordinates.getRelCoords(2208, 136, 2208, 200)
                    + RelCoordinates.getRelCoords(2208, 200, 2240, 200)
                    + RelCoordinates.getRelCoords(2240, 200, 2240, 136)
                    + RelCoordinates.getRelCoords(2240, 136, 2256, 136)
                    + RelCoordinates.getRelCoords(2256, 136, 2256, 152)
                    + RelCoordinates.getRelCoords(2256, 152, 2272, 152)
                    + RelCoordinates.getRelCoords(2272, 152, 2272, 168)
                    + RelCoordinates.getRelCoords(2272, 168, 2288, 168)
                    + RelCoordinates.getRelCoords(2288, 168, 2288, 184)
                    + RelCoordinates.getRelCoords(2288, 184, 2304, 184)
                    + RelCoordinates.getRelCoords(2304, 184, 2304, 200)
                    + RelCoordinates.getRelCoords(2304, 200, 2368, 200)
                    + RelCoordinates.getRelCoords(2368, 200, 2368, 184)
                    + RelCoordinates.getRelCoords(2368, 184, 2384, 184)
                    + RelCoordinates.getRelCoords(2384, 184, 2384, 168)
                    + RelCoordinates.getRelCoords(2384, 168, 2400, 168)
                    + RelCoordinates.getRelCoords(2400, 168, 2400, 152)
                    + RelCoordinates.getRelCoords(2400, 152, 2416, 152)
                    + RelCoordinates.getRelCoords(2416, 152, 2416, 136)
                    + RelCoordinates.getRelCoords(2416, 136, 2448, 136)
                    + RelCoordinates.getRelCoords(2448, 136, 2448, 224)
                    + RelCoordinates.getRelCoords(2448, 224, 1424, 224)
                    + RelCoordinates.getRelCoords(1424, 224, 1424, 200)
                    + RelCoordinates.getRelCoords(1424, 200, 2144, 200)
                    + RelCoordinates.getRelCoords(2144, 200, 2144, 184)
                    + RelCoordinates.getRelCoords(2144, 184, 2160, 184)
                    + RelCoordinates.getRelCoords(2160, 184, 2160, 168)
                    + RelCoordinates.getRelCoords(2160, 168, 2176, 168)
                    + RelCoordinates.getRelCoords(2176, 168, 2176, 152)
                    + RelCoordinates.getRelCoords(2176, 152, 2192, 152)
                    + RelCoordinates.getRelCoords(2192, 152, 2192, 136)
                    + RelCoordinates.getRelCoords(2192, 136, 2208, 136) + " Z",
                    // Fourth Stage Zone
                    "M " + RelCoordinates.getRelCoords(3040, 72)
                    + " L " + RelCoordinates.getRelCoords(3040, 72, 3040, 200)
                    + RelCoordinates.getRelCoords(3040, 200, 3392, 200)
                    + RelCoordinates.getRelCoords(3168, 200, 3168, 184)
                    + RelCoordinates.getRelCoords(3168, 184, 3184, 184)
                    + RelCoordinates.getRelCoords(3184, 184, 3184, 200)
                    + RelCoordinates.getRelCoords(3184, 200, 3392, 200)
                    + RelCoordinates.getRelCoords(3392, 200, 3392, 224)
                    + RelCoordinates.getRelCoords(3392, 224, 2480, 224)
                    + RelCoordinates.getRelCoords(2480, 224, 2480, 136)
                    + RelCoordinates.getRelCoords(2480, 136, 2496, 136)
                    + RelCoordinates.getRelCoords(2496, 136, 2496, 152)
                    + RelCoordinates.getRelCoords(2496, 152, 2512, 152)
                    + RelCoordinates.getRelCoords(2512, 152, 2512, 168)
                    + RelCoordinates.getRelCoords(2512, 168, 2528, 168)
                    + RelCoordinates.getRelCoords(2528, 168, 2528, 184)
                    + RelCoordinates.getRelCoords(2528, 184, 2544, 184)
                    + RelCoordinates.getRelCoords(2544, 184, 2544, 200)
                    + RelCoordinates.getRelCoords(2544, 200, 2610, 200)
                    + RelCoordinates.getRelCoords(2610, 200, 2610, 190)
                    + RelCoordinates.getRelCoords(2610, 190, 2608, 181)
                    + RelCoordinates.getRelCoords(2608, 181, 2608, 168)
                    + RelCoordinates.getRelCoords(2608, 181, 2608, 168)
                    + RelCoordinates.getRelCoords(2608, 168, 2641, 168)
                    + RelCoordinates.getRelCoords(2641, 168, 2641, 183)
                    + RelCoordinates.getRelCoords(2641, 183, 2638, 183)
                    + RelCoordinates.getRelCoords(2638, 183, 2638, 200)
                    + RelCoordinates.getRelCoords(2638, 200, 2866, 200)
                    + RelCoordinates.getRelCoords(2866, 200, 2866, 190)
                    + RelCoordinates.getRelCoords(2866, 190, 2864, 181)
                    + RelCoordinates.getRelCoords(2864, 181, 2864, 168)
                    + RelCoordinates.getRelCoords(2864, 168, 2896, 168)
                    + RelCoordinates.getRelCoords(2896, 168, 2896, 181)
                    + RelCoordinates.getRelCoords(2896, 181, 2894, 187)
                    + RelCoordinates.getRelCoords(2894, 187, 2894, 200)
                    + RelCoordinates.getRelCoords(2894, 200, 2896, 200)
                    + RelCoordinates.getRelCoords(2896, 200, 2896, 184)
                    + RelCoordinates.getRelCoords(2896, 184, 2912, 184)
                    + RelCoordinates.getRelCoords(2912, 184, 2912, 168)
                    + RelCoordinates.getRelCoords(2912, 168, 2928, 168)
                    + RelCoordinates.getRelCoords(2928, 168, 2928, 152)
                    + RelCoordinates.getRelCoords(2928, 152, 2944, 152)
                    + RelCoordinates.getRelCoords(2944, 152, 2944, 136)
                    + RelCoordinates.getRelCoords(2944, 136, 2960, 136)
                    + RelCoordinates.getRelCoords(2960, 136, 2960, 120)
                    + RelCoordinates.getRelCoords(2960, 120, 2976, 120)
                    + RelCoordinates.getRelCoords(2976, 120, 2976, 104)
                    + RelCoordinates.getRelCoords(2976, 104, 2992, 104)
                    + RelCoordinates.getRelCoords(2992, 104, 2992, 88)
                    + RelCoordinates.getRelCoords(2992, 88, 3008, 88)
                    + RelCoordinates.getRelCoords(3008, 88, 3008, 72)
                    + RelCoordinates.getRelCoords(3008, 72, 3040, 72) + " Z",
                    // Fifth Stage Zone
                    "M " + RelCoordinates.getRelCoords(3277, 200)
                    + " L " + RelCoordinates.getRelCoords(3277, 100)
                }
                , floorBase);

        aura = new Aura(root, auraScaleY, auraScaleX, auraWindEffects, auraSprites);

        player = new Mario(this, new String[]{
            "M " + 2.0 * REL_HEIGHT + "," + -5.0 * REL_HEIGHT + " L " + (MARIOSCALEX_s - (2.0 * REL_HEIGHT)) + "," + -5.0 * REL_HEIGHT, //Top
            "M " + (MARIOSCALEX_s) + "," + 2.0 * REL_HEIGHT + " L " + (MARIOSCALEX_s) + "," + (MARIOSCALEY_s - (3.0 * REL_HEIGHT)), //Right
            "M " + (1.3 * REL_HEIGHT) + "," + MARIOSCALEY_s + " L " + (MARIOSCALEX_s - (1.3 * REL_HEIGHT)) + "," + (MARIOSCALEY_s), //Bottom
            "M 0," + 2.0 * REL_HEIGHT + " L 0," + (MARIOSCALEY_s - (3.0 * REL_HEIGHT)), //Left
            "M " + (MARIOSCALEX_s - (5.0 * REL_HEIGHT)) + "," + (MARIOSCALEY_s - 1) + " L " + (5.0 * REL_HEIGHT) + "," + (MARIOSCALEY_s - 1), //Bottom Floor Collision Threshold
            "M 0,0 L " + MARIOSCALEX_s + ",0 " + MARIOSCALEX_s + "," + (MARIOSCALEY_s - (2.24 * REL_HEIGHT)) + " 0," + (MARIOSCALEY_s - (2.24 * REL_HEIGHT)) + " Z"}, // Hit Box
                new String[]{
                    "M " + 2.0 * REL_HEIGHT + "," + -5.0 * REL_HEIGHT + " L " + (MARIOSCALEY_s - (2.0 * REL_HEIGHT)) + "," + -5.0 * REL_HEIGHT, //Top
                    "M " + (MARIOSCALEY_s) + "," + 2.0 * REL_HEIGHT + " L " + (MARIOSCALEY_s) + "," + (MARIOSCALEY_b - (3.0 * REL_HEIGHT)), //Right
                    "M " + (1.3 * REL_HEIGHT) + "," + MARIOSCALEY_b + " L " + (MARIOSCALEY_s - (1.3 * REL_HEIGHT)) + "," + (MARIOSCALEY_b), //Bottom
                    "M 0," + 2.0 * REL_HEIGHT + " L 0," + (MARIOSCALEY_b - (3.0 * REL_HEIGHT)), //Left
                    "M " + (MARIOSCALEY_s - (5.0 * REL_HEIGHT)) + "," + (MARIOSCALEY_b - 1) + " L " + (5.0 * REL_HEIGHT) + "," + (MARIOSCALEY_b - 1), //Bottom Floor Collision Threshold
                    "M 0,0 L " + MARIOSCALEY_s + ",0 " + MARIOSCALEY_s + "," + (MARIOSCALEY_b - (2.24 * REL_HEIGHT)) + " 0," + (MARIOSCALEY_b - (2.24 * REL_HEIGHT)) + " Z"}, // Hit Box
                RelCoordinates.getRelX(20), -200, (float) 1.5 * REL_HEIGHT, (float) (12.0 * REL_HEIGHT), mario0, mario1, mario2, mario3, mario4, mario5, mario6, mario7, mario8, mario9, mario10, mario11, mario12, mario13, mario14, mario15, mario16, mario17, mario18, mario19, mario20, mario21, mario22, mario23, mario24
        );

        player.getSpriteFrame().setVisible(false);
        
        starBrickSprites = new Image[]{brick0, tile4};

        gameLoop = new GameLoop(this);

        flag = new FlagPole(
                "M " + RelCoordinates.getRelCoords(35, 11)
                + " L " + RelCoordinates.getRelCoords(35, 11, 35, 172)
                + RelCoordinates.getRelCoords(35, 172, 21, 172)
                + RelCoordinates.getRelCoords(21, 172, 21, 11)
                + RelCoordinates.getRelCoords(21, 11, 35, 11),
                RelCoordinates.getRelX((int) (3175 - REL_HEIGHT)), RelCoordinates.getRelY(28), flagPoleSprites);

        stageContainer.getChildren()
                .addAll(stageFloorBase.getSpriteFrame(), stageFloorBase.getSpriteBoundsArray()[0], stageFloorBase.getSpriteBoundsArray()[1], stageFloorBase.getSpriteBoundsArray()[2], stageFloorBase.getSpriteBoundsArray()[3], stageFloorBase.getSpriteBoundsArray()[4], flag.getSpriteBounds(), flag.getSpriteFrame());

        root.getChildren()
                .addAll(
                        stageContainer,
                        menuContainer,
                        player.getSpriteBoundsArray()[0], player.getSpriteBoundsArray()[1], player.getSpriteBoundsArray()[2], player.getSpriteBoundsArray()[3], player.getSpriteBoundsArray()[4], player.getSpriteBoundsArray()[5],
                        player.getSpriteFrame(), jumpEffect, jumpTrail, scoresDisplay, coinsDisplay);

        root.getStylesheets().add(getClass().getResource("/main/Styles.css").toString());

        setMainMenuFocus(true);
    }

    /**
     * Called during final flag pole sequence.<br>
     * Used to disable player from controlling Mario.
     */
    public void removeEvents() {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
        up = down = left = right = sprint = jump = shootFireball = false;
    }

    /**
     * Initializes key and action event of game.<br>
     * Also defines player controls.
     */
    private void initEvents() {
        // MenuStats codes:
        // 0: Start Screen
        // 1: Game currently being played
        // 2: Pause Screen Visible
        // 3: Pause Screen and Credits Screen Visible
        // 4: Quit Dialog Visible
        // 5: Pause Screen and High Scores Visible
        setSelectionSounds(play, hScore, credits, quit, quitY, quitN);
        play.setOnAction((ActionEvent) -> {
            menuSelection.play();
            menuM.stop();
            gameBGM.play(0.2);
            creditsView.setVisible(false);
            scoresView.setVisible(false);
            stats.setVisible(false);
            menuStatus = 1;
            root.getChildren().remove(getMenuContainer());
            gameLoop.start();
            if (!play.getText().equals("RESUME")) {
                play.setText("RESUME");
                splashView.setImage(pauseSplash);
                menuContainer.setStyle("-fx-background-color: rgba(0,0,0,0)");
                player.getSpriteFrame().setVisible(true);
            }
            scoresDisplay.setVisible(true);
            coinsDisplay.setVisible(true);
        });
        hScore.setOnAction((ActionEvent) -> {
            menuSelection.play();
            creditsView.setVisible(false);
            scoresView.setVisible(true);
            stats.setVisible(true);
            scoresView.toFront();
            stats.toFront();
            buttonContainer.toFront();
            pauseLabelBox.toFront();
            menuStatus = 5;
        });
        credits.setOnAction((ActionEvent) -> {
            menuSelection.play();
            menuStatus = 3;
            scoresView.setVisible(false);
            stats.setVisible(false);
            splashView.setVisible(true);
            creditsView.setVisible(true);
            buttonContainer.toFront();
        });
        quit.setOnAction((ActionEvent) -> {
            menuSelection.play();
            setMainMenuFocus(false);
            quitDialog.setVisible(true);
            quitDialog.toFront();
            menuStatus = 4;
        });
        quitY.setOnAction((ActionEvent) -> {
            menuSelection.play();
            System.exit(0);
        });
        quitN.setOnAction((ActionEvent) -> {
            menuSelection.play();
            setMainMenuFocus(true);
            quitDialog.setVisible(false);
        });

        scene.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ESCAPE:
                    switch (menuStatus) {
                        case 1:
                            menuStatus = 2;
                            root.getChildren().add(getMenuContainer());
                            getMenuContainer().toFront();
                            gameLoop.stop();
                            gameBGM.stop();
                            menuM.play();
                            pauseLabel.setVisible(true);
                            scoresDisplay.setVisible(false);
                            coinsDisplay.setVisible(false);
                            break;
                        case 2:
                            if (!play.getText().equals("RESUME"))
                                return;
                                
                            root.getChildren().remove(getMenuContainer());
                            menuStatus = 1;
                            creditsView.setVisible(false);
                            scoresView.setVisible(false);
                            pauseLabel.setVisible(false);
                            stats.setVisible(false);
                            menuM.stop();
                            gameBGM.play(0.2);
                            scoresDisplay.setVisible(true);
                            coinsDisplay.setVisible(true);
                            gameLoop.start();
                            break;
                        case 3:
                            creditsView.setVisible(false);
                            menuStatus = 2;
                            break;
                        case 4:
                            quitDialog.setVisible(false);
                            setMainMenuFocus(true);
                            quitDialog.toBack();
                            menuStatus = 2;
                            break;
                        case 5:
                            scoresView.setVisible(false);
                            stats.setVisible(false);
                            menuStatus = 2;
                            break;
                    }
                    break;
                case SPACE:
                    jump = true;
                    break;
                case SHIFT:
                    sprint = true;
                    break;
                // Uncomment to enable mario to fly and stuff
//                case UP:
//                case W:
//                    up = true;
//                    break;
//
//                case DOWN:
//                case S:
//                    down = true;
//                    break;

                case LEFT:
                case A:
                    left = true;
                    break;

                case RIGHT:
                case D:
                    right = true;
                    break;
                case R:
                    if (!allowFireball) {
                        allowFireball = true;
                        shootFireball = true;
                    }
            }

        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            switch (e.getCode()) {
//                case UP:
//                case W:
//                    up = false;
//                    break;
//
//                case DOWN:
//                case S:
//                    down = false;
//                    break;

                case LEFT:
                case A:
                    left = false;
                    break;

                case RIGHT:
                case D:
                    right = false;
                    break;

                case SPACE:
                    jump = false;
                    break;

                case SHIFT:
                    sprint = false;
                    break;
                case R:
                    allowFireball = false;
                    shootFireball = false;
            }
        });

    }

    /**
     * Sets the Score of top-left label shown during gameplay.
     * @param score Amount by which existing score should be incremented.
     */
    public static void updateScore(int score) {
        // Remove all non digit characters so we can get a valid integer and add to it
        score += Integer.parseInt(scoresDisplay.getText().replaceAll("[^0-9]", ""));
        String output = "";
        while ((score + output).length() < 7) {
            output += "0";
        }
        output += score;
        scoresDisplay.setText("SCORE\n" + output);
    }

    /**
     * Sets the Coin count of top-left label shown during gameplay.
     */
    public static void updateCoins() {
        // Remove all non digit characters so we can get a valid integer and add to it
        int coins = Integer.parseInt(coinCount.getText().replaceAll("[^0-9]", ""));
        coinCount.setText("x" + ++coins);
    }

    /**
     * Read stats and config from file.
     * Game Window size is also set here.
     */
    private void readStats() {
        stats = new Label();
        stats.setVisible(false);
        stats.setId("statsLabel");
        try {
            File file = new File("stats.dat");
            if (!file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("0:0:0");
                bw.close();
                fw.close();
            }
            FileReader fr = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);

            String[] statsArrayStr = br.readLine().split(":");
            br.close();
            fr.close();
            int[] statsArray = new int[3];
            for (int i = 0; i < 3; i++) {
                statsArray[i] = Integer.parseInt(statsArrayStr[i]);
            }
            stats.setText("Stats:\n\nHigh Scores: " + statsArray[0] + "\nTotal Coins Collected: " + statsArray[1] + "\nTotal Enemies Killed: " + statsArray[2]);
        } catch (Exception e) {
            stats.setText("Stats:\n\nHigh Scores: 0\nTotal Coins Collected: 0\nTotal Enemies Killed: 0");
            try {
                FileWriter fw = new FileWriter(new File("stats.dat").getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("0:0:0");
                bw.close();
                fw.close();
            } catch (Exception e1) {
                System.out.println("Cannot create file. Make sure you have access rights.");
            }
        }

        try {
            File file = new File("size_config.dat");
            if (!file.exists()) {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("1000:600");
                bw.close();
                fw.close();
            }
            FileReader fr = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);

            String[] statsArray = br.readLine().split(":");
            br.close();
            fr.close();
            int[] sizes = new int[2];
            sizes[0] = Integer.parseInt(statsArray[0]);
            sizes[1] = Integer.parseInt(statsArray[1]);

            if (sizes[0] > 1920 || sizes[1] < 600) {
                sizes[0] = 1000;
            }
            if (sizes[1] > 1080 || sizes[1] < 300) {
                sizes[1] = 600;
            }

            WIDTH = sizes[0];
            HEIGHT = sizes[1];
        } catch (Exception e) {
            WIDTH = 1000;
            HEIGHT = 600;
            try {
                FileWriter fw = new FileWriter(new File("size_config.dat").getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("1000:600");
                bw.close();
                fw.close();
            } catch (IOException ex) {
                System.out.println("Cannot create file. Make sure you have access rights.");
            }
        }

        REL_HEIGHT = (float) (HEIGHT / 224.0);
        STAGEWIDTH = ((int) (3392.0 * REL_HEIGHT)) - WIDTH;
        ASPECT_LENGTH = (float) (16.0 * REL_HEIGHT);
    }

    /**
     * Writes the final score and enemy kill count and coins to file.
     */
    public void writeStats() {
        try {
            File file = new File("stats.dat");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String[] curStats = stats.getText().split("\n");

            int score = Integer.parseInt(scoresDisplay.getText().replaceAll("[^0-9]", ""));
            int coins = Integer.parseInt(coinCount.getText().replaceAll("[^0-9]", ""));

            bw.write((Integer.parseInt(curStats[2].substring(curStats[2].length() - 1)) + score) + ":" + (Integer.parseInt(curStats[3].substring(curStats[3].length() - 1)) + coins) + ":" + (Integer.parseInt(curStats[4].substring(curStats[4].length() - 1)) + enemyCount));
            bw.close();
            fw.close();
        } catch (Exception ex) {
            System.out.println("Error Occurred while saving Stats. Please make sure you have Access rights. The Stats will not be changed. The error was: " + ex.getMessage() + "\nStackTrace is as Follows:\n");
            ex.printStackTrace();
        }
    }

    /**
     * Generate the light effect when Mario takes powerup.
     * @param x The x-Location of player
     * @param y The y-Location of player
     * @param marioState The Mario's state. Used to decide color of effect.
     */
    public void addPowerupEffect(double x, double y, int marioState) {
        root.getChildren().add(powerupEffectView);
        powerupEffectView.setImage(powerupEffect[marioState]);
        powerupEffectView.toFront();
        powerupEffectView.setTranslateX(x);
        powerupEffectView.setTranslateY(y);
        powerupEffectView.setFitHeight(9.3 * REL_HEIGHT);
    }

    /**
     * Removes the powerup effect.
     * Called by Mario class after completion of effect.
     */
    public void removePowerupEffect() {
        root.getChildren().remove(powerupEffectView);
    }

    /**
     * To switch stage style after flag has been destroyed.
     * So that player doesn't collide with flag base-block while going into castle.
     * Only Stage Zone 4 data is changed. All other zones remain same.
     * @return The updated Stage Zone 4 collision data after flag has been destroyed.
     */
    public String switchStagePath() {
        return "M " + RelCoordinates.getRelCoords(3040, 72)
                + " L " + RelCoordinates.getRelCoords(3040, 72, 3040, 200)
                + RelCoordinates.getRelCoords(3040, 200, 3392, 200)
                + RelCoordinates.getRelCoords(3392, 200, 3392, 224)
                + RelCoordinates.getRelCoords(3392, 224, 2480, 224)
                + RelCoordinates.getRelCoords(2480, 224, 2480, 136)
                + RelCoordinates.getRelCoords(2480, 136, 2496, 136)
                + RelCoordinates.getRelCoords(2496, 136, 2496, 152)
                + RelCoordinates.getRelCoords(2496, 152, 2512, 152)
                + RelCoordinates.getRelCoords(2512, 152, 2512, 168)
                + RelCoordinates.getRelCoords(2512, 168, 2528, 168)
                + RelCoordinates.getRelCoords(2528, 168, 2528, 184)
                + RelCoordinates.getRelCoords(2528, 184, 2544, 184)
                + RelCoordinates.getRelCoords(2544, 184, 2544, 200)
                + RelCoordinates.getRelCoords(2544, 200, 2610, 200)
                + RelCoordinates.getRelCoords(2610, 200, 2610, 190)
                + RelCoordinates.getRelCoords(2610, 190, 2608, 181)
                + RelCoordinates.getRelCoords(2608, 181, 2608, 168)
                + RelCoordinates.getRelCoords(2608, 181, 2608, 168)
                + RelCoordinates.getRelCoords(2608, 168, 2641, 168)
                + RelCoordinates.getRelCoords(2641, 168, 2641, 183)
                + RelCoordinates.getRelCoords(2641, 183, 2638, 183)
                + RelCoordinates.getRelCoords(2638, 183, 2638, 200)
                + RelCoordinates.getRelCoords(2638, 200, 2866, 200)
                + RelCoordinates.getRelCoords(2866, 200, 2866, 190)
                + RelCoordinates.getRelCoords(2866, 190, 2864, 181)
                + RelCoordinates.getRelCoords(2864, 181, 2864, 168)
                + RelCoordinates.getRelCoords(2864, 168, 2896, 168)
                + RelCoordinates.getRelCoords(2896, 168, 2896, 181)
                + RelCoordinates.getRelCoords(2896, 181, 2894, 187)
                + RelCoordinates.getRelCoords(2894, 187, 2894, 200)
                + RelCoordinates.getRelCoords(2894, 200, 2896, 200)
                + RelCoordinates.getRelCoords(2896, 200, 2896, 184)
                + RelCoordinates.getRelCoords(2896, 184, 2912, 184)
                + RelCoordinates.getRelCoords(2912, 184, 2912, 168)
                + RelCoordinates.getRelCoords(2912, 168, 2928, 168)
                + RelCoordinates.getRelCoords(2928, 168, 2928, 152)
                + RelCoordinates.getRelCoords(2928, 152, 2944, 152)
                + RelCoordinates.getRelCoords(2944, 152, 2944, 136)
                + RelCoordinates.getRelCoords(2944, 136, 2960, 136)
                + RelCoordinates.getRelCoords(2960, 136, 2960, 120)
                + RelCoordinates.getRelCoords(2960, 120, 2976, 120)
                + RelCoordinates.getRelCoords(2976, 120, 2976, 104)
                + RelCoordinates.getRelCoords(2976, 104, 2992, 104)
                + RelCoordinates.getRelCoords(2992, 104, 2992, 88)
                + RelCoordinates.getRelCoords(2992, 88, 3008, 88)
                + RelCoordinates.getRelCoords(3008, 88, 3008, 72)
                + RelCoordinates.getRelCoords(3008, 72, 3040, 72) + " Z";
    }

    //----------------GENERATORS-------------------------
    //NOTE: Powerups are generated in classes of their respective containers (for example ImmunityStar will be generated in StarBrick class).
    
    
    /**
     * To generate new Brick element in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>STAGE_ELEMENTS</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of brick. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @return The generated Brick element with specific position supplied.
     */
    public Brick genBrick(float[] coords, int stageZone) {
        return new Brick(stageZone, coords[0], coords[1], brick0);
    }

    /**
     * To generate new Coin Tile element in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>STAGE_ELEMENTS</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of tile. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @return The generated Tile element with specific position supplied.
     */
    public CoinTile genTile(float[] coords, int stageZone) {
        return new CoinTile(stageZone, coords[0], coords[1], (float) 1.5 * REL_HEIGHT, hitCoins, stageContainer, tile0, tile1, tile2, tile3, tile4);
    }
    
    /**
     * To generate new Power Tile element in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>STAGE_ELEMENTS</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of Power Tile. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @return The generated Tile element with specific position supplied.
     */
    public PowerTile genPowerTile(float[] coords, int stageZone) {
        return new PowerTile(player, stageZone, coords[0], coords[1], (float) 1.0 * REL_HEIGHT, (float) 3.0 * REL_HEIGHT, stageContainer, tile0, tile1, tile2, tile3, tile4);
    }

    /**
     * To generate new Star Brick (which generates invincibility star) element in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>STAGE_ELEMENTS</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of Star Brick. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @return The generated Star Brick element with specific position supplied.
     */
    public StarBrick genStarBrick(float[] coords, int stageZone) {
        return new StarBrick(player, stageZone, stageContainer, coords[0], coords[1], (float) 1.0 * REL_HEIGHT, (float) 3.0 * REL_HEIGHT, starBrickSprites, invinciStarSprites);
    }
    
    /**
     * To generate new Secret Block (which generates yolo image by default) element in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>STAGE_ELEMENTS</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of Secret Block. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @return The generated Secret Block element with specific position supplied.
     */
    public SecretBlock genSecretBlock(float[] coords, int stageZone) {
        return new SecretBlock(player, stageContainer, stageZone, coords[0], coords[1], ASPECT_LENGTH, tile4, yolo);
    }

    /**
     * To generate new fireball power from player when requested by user.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>POWERS</em> list and to the <em>stageContainer</em>.
     * @param xPos the x-Position of player. Used to set the x-starting point of fireball.
     * @param yPos the y-Position of player. Used to set the y-starting point of fireball.
     * @param direction the direction in which player is facing (1 or right and -1 for left.<br>
     * Using <em>getScale</em> method of ImageView class on <em>spriteFrame</em> of player. Used to set the direction of fireball.
     * @return The generated Fireball element with specific position supplied.
     */
    public Fireball genFireball(float xPos, float yPos, int direction) {
        String fireballBounds = "M 0,0 L " + fireballScaleX + " ,0" + " " + fireballScaleX + "," + fireballScaleY + " 0," + fireballScaleY + " Z";
        float fireballXVel = 2.24f * REL_HEIGHT;
        return new Fireball(root, fireballBounds, xPos, yPos, fireballXVel, direction, fireBall, fireballBlast);
    }

    /**
     * To generate new bigger fireball power from player when requested by user.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>POWERS</em> list and to the <em>stageContainer</em>.
     * <b>For now it is only used when flag pole sequence is being run.</b>
     * @param xPos the x-Position of player. Used to set the x-starting point of fireball.
     * @param yPos the y-Position of player. Used to set the y-starting point of fireball.
     * @return The generated FireballBig element with specific position supplied.
     */
    public FireballBig genFireballBig(float xPos, float yPos) {
        String fireballBounds = "M 0,0 L " + fireballScaleX + " ,0" + " " + fireballScaleX + "," + fireballScaleY + " 0," + fireballScaleY + " Z";
        float fireballXVel = 2.24f * REL_HEIGHT;
        return new FireballBig(root, fireballBounds, xPos, yPos, fireballXVel, fireballb, fireballBlast);
    }

    /**
     * To generate new Goomba enemy in stage.
     * The generated element must be added into <b>SpriteHandler</b> class's <em>ENEMIES</em> list and to the <em>stageContainer</em>.
     * @param coords Coordinates of brick. Use <b>RelCoordinates</b> class's <em>getRelCoordsArr</em> method to generate this.
     * @param stageZone Define the stage zone in which element is present from 0-4. Each stage zone is separated by a pit.
     * @param checkBlocks If the Goomba's collision logic code should include checking collisions with <em>StageBlock</em> elements. Includes brick, tiles and other blocks.
     * @return The generated Goomba enemy with specific position supplied.
     */
    public Goomba genGoomba(float[] coords, int stageZone, boolean checkBlocks) {
        return new Goomba(stageContainer, gameLoop, player, stageZone, checkBlocks, new String[]{
            null, // Top; not required
            "M " + ASPECT_LENGTH + ",0 L " + ASPECT_LENGTH + "," + (ASPECT_LENGTH - 6), // Right
            "M 0," + ASPECT_LENGTH + " L " + ASPECT_LENGTH + "," + ASPECT_LENGTH, // Bottom
            "M 0,0 L 0," + (ASPECT_LENGTH - 6), // Left
            "M 20," + (ASPECT_LENGTH - 1) + " L " + (ASPECT_LENGTH - 20) + "," + (ASPECT_LENGTH - 1), // Bottom Penetration Threshold
            "M 0,0 L 0,0 " + ASPECT_LENGTH + ",0 " + ASPECT_LENGTH + "," + ASPECT_LENGTH + " 0," + ASPECT_LENGTH + " Z" // Hit Box
        }, coords[0], coords[1] - ASPECT_LENGTH, 0.4f * REL_HEIGHT, 4.9f * REL_HEIGHT, playerDeathSprites, goomba0, goomba1, goomba2, goomba3);
    }

    //----------------GETTERS----------------------------
    
    /**
     * @return The main Parent which contains all elements of game including menus, stage, elements and characters.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * @return The container containing Menu elements.
     */
    public StackPane getMenuContainer() {
        return menuContainer;
    }

    /**
     * @return The Floor of stage.
     */
    public StageFloorBase getStageFloorBase() {
        return stageFloorBase;
    }
    
    /**
     * @return The parent containing stage elements: <em>Tiles, Bricks, Enemies, Stage Floor</em> etc.
     */
    public Group getStageContainer() {
        return stageContainer;
    }

    /**
     * @return The main loop which controls the game. The rate of loop is 60fps by default.
     */
    public GameLoop getGameLoop() {
        return gameLoop;
    }

    /**
     * @return The main <em>Player</em> object (Mario).
     */
    public Mario getPlayer() {
        return player;
    }

    /**
     * @return The Aura object (aura is added to player after second stage powerup is taken).
     */
    public Aura getAura() {
        return aura;
    }

    /**
     * @return The Image Array containing Flag Burning Sprites.
     */
    public Image[] getFlagDestroyEffects() {
        return flagDesEffects;
    }

    /**
     * @return The ImageView used as container for <em>Flag Destruction Effect</em>
     */
    public ImageView getFlagDestroyEffectView() {
        return flagDestroyEffectView;
    }

    /**
     * @return The blue base effect added to player (Mario) during flag destruction sequence by default.
     */
    public ImageView getElevateEffectView() {
        return elevateEffectView;
    }

    /**
     * @return The brown concentric ellipse effect added when player jumps.
     */
    public ImageView getJumpEffect() {
        return jumpEffect;
    }

    /**
     * @return The white trail-lines effect added when player jumps.
     */
    public ImageView getJumpTrail() {
        return jumpTrail;
    }

    /**
     * The light effect when Mario takes powerup
     * @param marioState The Mario's state of growth (increases with powerups) to determine color of effect.
     * @return THe light effect.
     */
    public ImageView getPowerEffect(int marioState) {
        return powerupEffectView;
    }

    /**
     * @return If <b>UP</b> button is pressed by user.
     */
    public boolean isUp() {
        return up;
    }

    /**
     * @return If <b>DOWN</b> button is pressed by user.
     */
    public boolean isDown() {
        return down;
    }

    /**
     * @return If <b>LEFT</b> button is pressed by user.
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * @return If <b>RIGHT</b> button is pressed by user.
     */
    public boolean isRight() {
        return right;
    }

    /**
     * @return If <b>JUMP</b> button is pressed by user.
     */
    public boolean isJumping() {
        return jump;
    }

    /**
     * To stop the player's jump in mid air. Used when player collides with something in mid air.
     */
    public void stopJump() {
        jump = false;
    }

    /**
     * @return If <b>SPRINT</b> button is pressed by user.
     */
    public boolean isSprinting() {
        return sprint;
    }

    /**
     * Allows to shoot fireballs after pressing and releasing shoot fireball button.
     * @return If <b>FIREBALL</b> button is pressed by user and player is in valid condition to shoot fireball.
     */
    public boolean isShootingFireball() {
        // prevents from fireball object overflow due to holding fireball shoot button.
        if (shootFireball) {
            // Do not allow player to shoot fireball until button is released and pressed again.
            shootFireball = false;
            return true;
        }
        return false;
    }

    //----------------SETTERS----------------------------
    
    /** This is called during finishing of flag pole sequence
     * To make player go into castle
     */
    public void setRight() {
        right = true;
    }

    /**
     * Sets menu sounds (button-hover and button-selection).
     * @param options The buttons which are part of menu and to which sound effects should be applied.
     */
    private void setSelectionSounds(Button... options) {
        for (Button option : options) {
            option.setOnMouseEntered((MouseEvent) -> {
                menuNav.play();
            });
            option.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        menuNav.play();
                    }
                }
            });
        }
    }

    /**
     * So keyboard menu navigation doesn't select unrequired elements
     */
    private void setMainMenuFocus(boolean value) {
        play.setFocusTraversable(value);
        hScore.setFocusTraversable(value);
        credits.setFocusTraversable(value);
        quit.setFocusTraversable(value);
        quitY.setFocusTraversable(!value);
        quitN.setFocusTraversable(!value);
        if (value) {
            play.requestFocus();
        } else {
            quitN.requestFocus();
        }
    }

    /**
     * Sets initial game elements like the <em>StageBlocks</em> (Bricks, Tiles etc), <em>Enemies</em>.<br>
     * Also adds main Characters and elements (StageFloor, Mario, FlagPole) to <b>MAIN_CAST</b> list of <b>SpriteHandler</b> class.
     * 
     */
    private void setCurrentCast() {
        //<editor-fold defaultstate="collapsed" desc="Stage Blocks">
        SpriteHandler.setAllStageElements(
                genTile(RelCoordinates.getRelCoordsArr(256, 136), 0), //0

                genPowerTile(RelCoordinates.getRelCoordsArr(336, 136), 0),
                
                genTile(RelCoordinates.getRelCoordsArr(368, 136), 0), //2
                genTile(RelCoordinates.getRelCoordsArr(352, 72), 0), //3

                genPowerTile(RelCoordinates.getRelCoordsArr(1248, 136), 1),

                genTile(RelCoordinates.getRelCoordsArr(1504, 72), 2), //5
                genTile(RelCoordinates.getRelCoordsArr(1696, 136), 2), //6
                genTile(RelCoordinates.getRelCoordsArr(1744, 136), 2), //7
                genTile(RelCoordinates.getRelCoordsArr(1792, 136), 2), //8
                genPowerTile(RelCoordinates.getRelCoordsArr(1744, 72), 2),
                
                genPowerTile(RelCoordinates.getRelCoordsArr(1744, 72), 2),
                
                genTile(RelCoordinates.getRelCoordsArr(2064, 72), 2), //10

                genTile(RelCoordinates.getRelCoordsArr(2720, 136), 3), //11

                genBrick(RelCoordinates.getRelCoordsArr(320, 136), 0), //12
                genBrick(RelCoordinates.getRelCoordsArr(352, 136), 0), //13
                genBrick(RelCoordinates.getRelCoordsArr(384, 136), 0), //14

                genBrick(RelCoordinates.getRelCoordsArr(1232, 136), 1), //15
                genBrick(RelCoordinates.getRelCoordsArr(1264, 136), 1), //16
                genBrick(RelCoordinates.getRelCoordsArr(1280, 72), 1), //17
                genBrick(RelCoordinates.getRelCoordsArr(1296, 72), 1), //18
                genBrick(RelCoordinates.getRelCoordsArr(1312, 72), 1), //19
                genBrick(RelCoordinates.getRelCoordsArr(1328, 72), 1), //20
                genBrick(RelCoordinates.getRelCoordsArr(1344, 72), 1), //21
                genBrick(RelCoordinates.getRelCoordsArr(1360, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1376, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1392, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1456, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1472, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1488, 72), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1504, 136), 1),
                genBrick(RelCoordinates.getRelCoordsArr(1600, 136), 1),
                
                genStarBrick(RelCoordinates.getRelCoordsArr(1616, 136), 2),
               
                genBrick(RelCoordinates.getRelCoordsArr(1888, 136), 2),
                genBrick(RelCoordinates.getRelCoordsArr(1936, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(1952, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(1968, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(1984, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(2048, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(2080, 72), 2),
                genBrick(RelCoordinates.getRelCoordsArr(2064, 136), 2),
                genBrick(RelCoordinates.getRelCoordsArr(2080, 136), 2),
                genBrick(RelCoordinates.getRelCoordsArr(2688, 136), 3),
                genBrick(RelCoordinates.getRelCoordsArr(2704, 136), 3),
                genBrick(RelCoordinates.getRelCoordsArr(2736, 136), 3),
                
                genSecretBlock(RelCoordinates.getRelCoordsArr(1025, 136), 0)
        );

        for (StageBlock s : SpriteHandler.getAllStageElements()) {
            if (s.getZone() == 0) {
                SpriteHandler.getStageElements().add(s);
            }
            stageContainer.getChildren().addAll(s.getSpriteFrame(), s.getSpriteBounds());
        }
        //</editor-fold>

        SpriteHandler.setEnemyCast(
                genGoomba(RelCoordinates.getRelCoordsArr(329, 200), 0, false),
                genGoomba(RelCoordinates.getRelCoordsArr(645, 200), 0, false),
                genGoomba(RelCoordinates.getRelCoordsArr(842, 200), 0, false),
                genGoomba(RelCoordinates.getRelCoordsArr(866, 200), 0, false),
                genGoomba(RelCoordinates.getRelCoordsArr(1277, 72), 1, true),
                genGoomba(RelCoordinates.getRelCoordsArr(1306, 72), 1, true),
                genGoomba(RelCoordinates.getRelCoordsArr(1519, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(1541, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(1689, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(1973, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(1997, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(2029, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(2053, 200), 2, false),
                genGoomba(RelCoordinates.getRelCoordsArr(2771, 200), 3, false),
                genGoomba(RelCoordinates.getRelCoordsArr(2793, 200), 3, false)
        );
        for (Enemy s : SpriteHandler.getAllEnemyCast()) {
            if (s.getZone() == 0) {
                SpriteHandler.getEnemyCast().add(s);
            }
            stageContainer.getChildren().addAll(s.getSpriteFrame(), s.getSpriteBoundsArray()[1], s.getSpriteBoundsArray()[2], s.getSpriteBoundsArray()[3], s.getSpriteBoundsArray()[4], s.getSpriteBoundsArray()[5]);
        }

        SpriteHandler.addToCurrentCast(stageFloorBase, player, flag);
    }

    /**
     * Sets game-end UI based on whether player has completed stage or died before completing.
     * Called by GameLoop class.
     * @param playerDied If player died before completing stage,
     */
    public void setGameOverUI(boolean playerDied) {
        if (playerDied) {
            buttonContainer.getChildren().remove(0, 3);
            splashView.setImage(gameOverSplash);
            root.getChildren().add(getMenuContainer());
            getMenuContainer().toFront();
        } else {
            quitConfirm.setText("Congrats. Game Completed.\nYou can quit the game now\nor choose \"No\" and use Alt+F4 to quit.\n\nDo you want to quit now?");
            root.getChildren().add(quitDialog);
            quitDialog.setPrefSize(WIDTH, HEIGHT);
            quitDialog.setVisible(true);
            quitDialog.toFront();
        }

        // Write scores to file
        writeStats();
    }

}
