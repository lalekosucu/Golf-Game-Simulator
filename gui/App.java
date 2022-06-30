/* This class is used to create the graphical user interface of the game.
 */

package gui;

import javafx.util.Duration;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;

import terrain.Terrain;
import terrain.TerrainBuilder;
import simulators.PlayBot;
import physicsEngine.PhysicsEngine;
import physicsEngine.BallCoordinates;
import odeSolvers.ODESolver;
import bots.Bot;
import bots.BotFactory;

public class App extends Application implements Observer{
    private static final int instance = 1;
    private static final String botType = "hillclimbing";
    // Constants
    private static final int noDecimals = 3;
    
    // Variables for the game logic.
    private Terrain terrain;
    private PhysicsEngine engine;
    private double[] ballCoordinates;
    private int hits;
    
    // Variables for the GUI.
    Scene scene;
    Group root;
    Group r;
    Box ground;
    Box pole;
    Box flag;
    Sphere ball;
    Circle lake;
    Circle sand;
    Rectangle lake1;
    Rectangle panel;
    private double mouseX;
    private double mouseZ;
    private double mouseAngleZ = 0;
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);
    
    Label xcoord;
    Label ycoord;
    Label hit;
    
    Scene hitScene;
    Group hroot;
    TextField velocityX;
    TextField velocityY;

    Scene endScene;
    Group root1;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Sets all the variables for the game logic.
        BallCoordinates.addObserver(this);
        terrain = TerrainBuilder.build(instance);
        engine = new PhysicsEngine(terrain);
        PlayBot botEngine = new PlayBot(botType, terrain);
        
        // Sets the starting position of the ball.
        hits = 0;
        ballCoordinates = TerrainBuilder.getStartingPosition(instance);
        
        // Sets the camera of the 3D GUI.
        final PerspectiveCamera cam = new PerspectiveCamera();
        cam.setFieldOfView(50);
        cam.setFarClip(9000);
        cam.setNearClip(0.1);
        cam.getTransforms().addAll(new Rotate(50,Rotate.X_AXIS),new Translate(-500,-200,-200));
        
        // Creates the scene of the 3D GUI.
        root = new Group();
        scene = new Scene(root, 1000, 700, true);  
        scene.setFill(Color.LIGHTBLUE);   
    	  scene.setCamera(cam);
        
        // Adds all the objects to the golf course.
        addBall();
        addTarget();
        addTrees();      
        repaintGrass();
        displayHeight();
        displayResults();
        
        // Creates a screen in which the player can enter the speed at which he/she wants to hit the ball.
        hroot = new Group();
        hitScene = new Scene(hroot, 400, 150, Color.LIGHTBLUE);
        Label dLabel = new Label("VELOCITY OF HIT IN X AXIS:  ");
        dLabel.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 15));
        dLabel.setTextFill(Color.BLACK);
        dLabel.setTranslateX(25);
        dLabel.setTranslateY(25);
        hroot.getChildren().add(dLabel);

        velocityX = new TextField("");
        velocityX.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 15));
        velocityX.setTranslateX(225);
        velocityX.setTranslateY(25);
        velocityX.setMaxWidth(150);
        hroot.getChildren().add(velocityX);

        Label vLabel = new Label("VELOCITY OF HIT IN Y AXIS: ");
        vLabel.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 15));
        vLabel.setTextFill(Color.BLACK);
        vLabel.setTranslateX(25);
        vLabel.setTranslateY(60);
        hroot.getChildren().add(vLabel);

        velocityY = new TextField("");
        velocityY.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 15));
        velocityY.setTranslateX(225);
        velocityY.setTranslateY(60);
        velocityY.setMaxWidth(150);
        hroot.getChildren().add(velocityY);

        Button hButton = new Button("HIT BALL!");
        hButton.setOnAction(e -> {
            primaryStage.setScene(scene);
            double vx = Double.parseDouble(velocityX.getText());
            double vy = Double.parseDouble(velocityY.getText());
            double[] stateVector = {ballCoordinates[0], ballCoordinates[1], vx, vy};
            engine.getNextCoordinates(stateVector);
        });
        hButton.setTranslateX(165);
        hButton.setTranslateY(105);
        hroot.getChildren().add(hButton);
        
        // Sets the main game on the primaryStage.
    	  primaryStage.setScene(scene);
    	  primaryStage.setTitle("Crazy Putting!");
    	  primaryStage.setResizable(false);
    	  primaryStage.show();

        mouseControl(root, scene, primaryStage);
        
        // Sets the key controls.
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                hits++;
                String code = key.getCode().toString();
                // When ENTER is pressed the user can enter vx and vy.
                if (code.equals("ENTER")){
                   primaryStage.setScene(hitScene);
                }
                // When SPACE is pressed the bot will play one move.
                else if (code.equals("SPACE")){
                   PlayBot playBot = new PlayBot(botType, terrain);
                   playBot.play(ballCoordinates);
                }
                else if (code.equals("N")){
                   PlayBot playBot = new PlayBot(botType, terrain);
                   playBot.playWithNoise(ballCoordinates);
                }
            }
        });
    }
    
    // Settings for the control of the mouse.
    private void mouseControl(Group group, Scene scene, Stage stage) {
        Rotate rotateZ;
        group.getTransforms().addAll( rotateZ = new Rotate(0, Rotate.Z_AXIS));
        rotateZ.angleProperty().bind(angleZ);

        scene.setOnMousePressed(event -> {
            mouseZ = event.getSceneY();
            mouseAngleZ = angleZ.get();
        });

        scene.setOnMouseDragged(event -> {
            angleZ.set(mouseAngleZ + (mouseX - event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double scrollAmount = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + scrollAmount);
        });
    }
    
    // This method will be called when the coordinates of the ball change.
    public void update() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), ball);
        tt.setFromX(ballCoordinates[0]*tileSize/scalingFactor);
        tt.setFromY(ballCoordinates[1]*tileSize/scalingFactor);
        
        // The new coordinates are retrieved.
        ballCoordinates = BallCoordinates.getCoordinates();
        updateResults();
        
        // Moves the ball to the new coordinates.
        tt.setToX(ballCoordinates[0]*tileSize/scalingFactor);
        tt.setToY(ballCoordinates[1]*tileSize/scalingFactor);
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }
    
    // Adds the ball to the GUI.    
    public void addBall() {
        ball = new Sphere(7.5);
        ball.setTranslateX(ballCoordinates[0]*tileSize/scalingFactor);
        ball.setTranslateY(ballCoordinates[1]*tileSize/scalingFactor);
        ball.setTranslateZ(-10);
        ball.setMaterial(new PhongMaterial(Color.WHITE));
        root.getChildren().add(ball);
    }
    
    // Adds the Target to the GUI.
    public void addTarget() {
        double[] targetCoordinates = terrain.getTargetCoordinates();
        
        // Adds the pole of the target.
        Box pole = new Box();
        pole.setWidth(3); 
        pole.setHeight(3);   
        pole.setDepth(75);
        pole.setTranslateX(targetCoordinates[0]*tileSize/scalingFactor);
        pole.setTranslateY(targetCoordinates[1]*tileSize/scalingFactor);
        pole.setTranslateZ(-30);
        root.getChildren().add(pole);
        
        // Adds the flag of the target.
        Box flag = new Box();
        flag.setWidth(15); 
        flag.setHeight(5);  
        flag.setDepth(15);
        flag.setTranslateX(targetCoordinates[0]*tileSize/scalingFactor-9);
        flag.setTranslateY(targetCoordinates[1]*tileSize/scalingFactor);
        flag.setTranslateZ(-60);
        flag.setMaterial(new PhongMaterial(Color.RED));
        root.getChildren().add(flag);
    }
    
    // Adds all the trees of the terrain.
    public void addTrees() {
        double[][] treeCoordinates = terrain.getTreeCoordinates();
        for (int i = 0; i < treeCoordinates.length; i++) {
            addTree(treeCoordinates[i][0], treeCoordinates[i][1]);
        }
    }
    
    // Adds a tree to the GUI.
    public void addTree(double x, double y){
        PhongMaterial sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseColor(Color.FORESTGREEN);
        Sphere sphere = new Sphere(35);
        sphere.setMaterial(sphereMaterial);
        sphere.getTransforms().add(new Translate(x*tileSize/scalingFactor,y*tileSize/scalingFactor,-70));
        Cylinder cylinder = new Cylinder();
        cylinder.setRadius(10);
        cylinder.setHeight(60);
        PhongMaterial cylinderMaterial = new PhongMaterial();
        cylinderMaterial.setDiffuseColor(Color.SADDLEBROWN);
        cylinder.setTranslateX(x*tileSize/scalingFactor);
        cylinder.setTranslateY(y*tileSize/scalingFactor);
        cylinder.setTranslateZ(-30);
        cylinder.getTransforms().addAll(new Rotate(90,Rotate.X_AXIS));
        cylinder.setMaterial(cylinderMaterial);
        root.getChildren().add(cylinder);
        root.getChildren().addAll(sphere); 
    }
    
    // The constants used to create the surface of the GUI.
    public static final int tileSize = 5;
    public static final int noTiles = 200;
    public static final double scalingFactor = 0.2;
    public double heighestPoint = 0;
    
    // Adds the surface to the GUI.
    public void repaintGrass(){
        // Gets the highest point of all the tiles.
        for(double x = (-noTiles/2)*scalingFactor; x < (noTiles/2)*scalingFactor; x += scalingFactor) {    
            for(double y = (-noTiles/2)*scalingFactor; y < (noTiles/2)*scalingFactor; y += scalingFactor) {
                double height = terrain.getHeight(x, y);
                if (height > heighestPoint) {
                   heighestPoint = height;
                }
            }
        }
        
        // Adds all the tiles with the proper color.
        for(double x = -noTiles/2; x < noTiles/2; x++) {    
            for(double y = -noTiles/2; y < noTiles/2; y++) {
                Color color = terrain.getColor(x*scalingFactor, y*scalingFactor, heighestPoint);
                Box box = new Box(tileSize,tileSize ,1);
                box.setTranslateX(x*tileSize);
                box.setTranslateY(y*tileSize);
                box.setMaterial(new PhongMaterial(color));
                root.getChildren().add(box);
            }
        }
    }
    
    // Displays the value of the heighest point on the surface.
    public void displayHeight(){
        Label h = new Label("Highest Point: " + roundToDecimals(heighestPoint));
        h.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 10));
        h.setTextFill(Color.WHITE);
        h.setTranslateX(50);
        h.setTranslateY(400);
        h.setTranslateZ(-5);
        root.getChildren().add(h);
    }

    // Displays the coordinates and the number of hits on the surface.
    public void displayResults(){
        // Displays the x-coordinate on the surface.
        xcoord = new Label("X Coordinate: " + roundToDecimals(ballCoordinates[0]));
        xcoord.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 10));
        xcoord.setTextFill(Color.WHITE);
        xcoord.setTranslateX(150);
        xcoord.setTranslateY(400);
        xcoord.setTranslateZ(-5);
        root.getChildren().add(xcoord);
        
        // Displays the y-coordinate on the surface.
        ycoord = new Label("Y Coordinate: " + roundToDecimals(ballCoordinates[1]));
        ycoord.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 10));
        ycoord.setTextFill(Color.WHITE);
        ycoord.setTranslateX(250);
        ycoord.setTranslateY(400);
        ycoord.setTranslateZ(-5);
        root.getChildren().add(ycoord);
        
        // Displays the number of hits on the surface.
        hit = new Label("Number of Hits: " + roundToDecimals(hits));
        hit.setFont(Font.font("MONTSERRAT", FontWeight.BOLD, FontPosture.REGULAR, 10));
        hit.setTextFill(Color.WHITE);
        hit.setTranslateX(350);
        hit.setTranslateY(400);
        hit.setTranslateZ(-5);
        root.getChildren().add(hit);
    }
    
    // Updates the result that are displayed on the surface.
    public void updateResults(){
        xcoord.setText("X Coordinate: " + roundToDecimals(ballCoordinates[0]));
        ycoord.setText("Y Coordinate: " + roundToDecimals(ballCoordinates[1]));
        hit.setText("Number of Hits: " + roundToDecimals(hits));
    }
    
    // Rounds a double value d to noDecimals decimals.
    private static double roundToDecimals(double d) {
       int multiplier = (int)Math.pow(10, noDecimals);
       int newValue = (int)(d * multiplier);
       return (double)newValue/multiplier;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}