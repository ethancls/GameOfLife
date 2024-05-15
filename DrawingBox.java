import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;

/* COMMANDS TO COMPILE AND RUN
javac --module-path ./javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml DrawingBox.java
java --module-path ./javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml DrawingBox  
*/

public class DrawingBox extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int X = 40;
    private static final int Y = 20;
    private static final int Z = 40;
    private static final int SMALL_BOX_SIZE = 5;
    private boolean[][][] grid = new boolean[X][Y][Z];
    private boolean[][][] newGrid = new boolean[X][Y][Z];
    private int generation = 0;
    private Timeline timeline;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        SmartGroup group = new SmartGroup();
        initializeGrid();
        createGrid(group);

        Camera camera = new PerspectiveCamera();
        SubScene subScene = new SubScene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);

        Text generationCounter = new Text();
        generationCounter.setFill(Color.WHITE);
        generationCounter.setFont(Font.font(20));

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");

        startButton.setOnAction(event -> timeline.play());
        stopButton.setOnAction(event -> timeline.stop());

        HBox buttons = new HBox(10, startButton, stopButton);
        buttons.setTranslateX(10);
        buttons.setTranslateY(10);

        StackPane root = new StackPane();
        root.getChildren().addAll(subScene, generationCounter, buttons);
        StackPane.setAlignment(generationCounter, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setAlignment(buttons, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        double initialDistance = -2 * X * SMALL_BOX_SIZE;
        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(initialDistance);

        initMouseControl(group, scene);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case Z:
                    group.translateZProperty().set(group.getTranslateZ() - 100);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ() + 100);
                    break;
                default:
                    break;
            }
        });

        primaryStage.setTitle("3D Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        timeline = new Timeline(new KeyFrame(Duration.seconds(0.17), e -> {
            updateGrid();
            createGrid(group);
            generation++;
            generationCounter.setText("Generation: " + generation);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void initializeGrid() {
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                for (int z = 0; z < Z; z++) {
                    grid[x][y][z] = Math.random() < 0.1;  // Randomly set some cells to be alive
                }
            }
        }
    }

    private void updateGrid() {
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                for (int z = 0; z < Z; z++) {
                    int aliveNeighbors = countAliveNeighbors(x, y, z);
                    if (grid[x][y][z]) {
                        newGrid[x][y][z] = aliveNeighbors == 2 || aliveNeighbors == 3;
                    } else {
                        newGrid[x][y][z] = aliveNeighbors == 3;
                    }
                }
            }
        }
        boolean[][][] temp = grid;
        grid = newGrid;
        newGrid = temp;
    }

    private int countAliveNeighbors(int x, int y, int z) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    int nx = x + dx;
                    int ny = y + dy;
                    int nz = z + dz;
                    if (nx >= 0 && nx < X && ny >= 0 && ny < Y && nz >= 0 && nz < Z) {
                        if (grid[nx][ny][nz]) count++;
                    }
                }
            }
        }
        return count;
    }

    private void createGrid(SmartGroup group) {
        group.getChildren().removeIf(node -> node instanceof Box);
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                for (int z = 0; z < Z; z++) {
                    if (grid[x][y][z]) {
                        Box box = new Box(SMALL_BOX_SIZE, SMALL_BOX_SIZE, SMALL_BOX_SIZE);
                        PhongMaterial material = new PhongMaterial();
                        material.setDiffuseColor(Color.BLUE);
                        material.setSpecularColor(Color.LIGHTGRAY);
                        box.setMaterial(material);
                        box.setTranslateX(x * (SMALL_BOX_SIZE) - (X * (SMALL_BOX_SIZE) / 2));
                        box.setTranslateY(y * (SMALL_BOX_SIZE) - (Y * (SMALL_BOX_SIZE) / 2));
                        box.setTranslateZ(z * (SMALL_BOX_SIZE) - (Z * (SMALL_BOX_SIZE) / 2));
                        group.getChildren().add(box);
                    }
                }
            }
        }
    }

    private void initMouseControl(SmartGroup group, Scene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group {

        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
