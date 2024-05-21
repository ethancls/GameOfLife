import java.util.Random;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

// 3D
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class Main {

    public static void main(String[] args) {
        int rows = 50;
        int cols = 50;
        Random random = new Random();
        STRUCT_Grid_ND grid = new STRUCT_Grid_ND(rows, cols);

        for (int i = 0; i < rows * cols * 0.5; i++) {
            // grid.getCell(random.nextInt(rows), random.nextInt(cols)).setValue(true);
        }

        for (int i = -4; i < 2; i++) {
            for (int j = -4; j < -2; j++) {
                grid.getCell(rows / 2 + j, cols / 2 + i).setCellValue(true);
            }
        }

        for (int i = -1; i < 5; i++) {
            for (int j = -4; j < -2; j++) {
                grid.getCell(rows / 2 + i, cols / 2 + j).setCellValue(true);
            }
        }

        for (int i = -4; i < 2; i++) {
            for (int j = -4; j < -2; j++) {
                grid.getCell(rows / 2 - j, cols / 2 - i).setCellValue(true);
            }
        }

        for (int i = -1; i < 5; i++) {
            for (int j = -4; j < -2; j++) {
                grid.getCell(rows / 2 - i, cols / 2 - j).setCellValue(true);
            }
        }

        GFX_GrilleGraphique Grid_2D = new GFX_GrilleGraphique(grid.getDimensions()[0], grid.getDimensions()[1], 12);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid.getCell(i, j).getCellValue()) {
                    Grid_2D.colorierCase(i, j, Color.BLACK);
                }
            }
        }

        class SimulationPanel extends JPanel implements KeyListener {
            private boolean simulationRunning = false;

            public SimulationPanel() {
                addKeyListener(this);
                setFocusable(true);
                requestFocusInWindow();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 's') {
                    simulationRunning = true;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            public boolean isSimulationRunning() {
                return simulationRunning;
            }
        }

        SimulationPanel panel = new SimulationPanel();
        Grid_2D.add(panel);
        Grid_2D.revalidate();

        class GridWrapper {
            private STRUCT_Grid_ND grid;
            public int GENERATIONS = 0;

            public GridWrapper(STRUCT_Grid_ND grid) {
                this.grid = grid;
            }

            public STRUCT_Grid_ND getGrid() {
                return grid;
            }

            public void setGrid(STRUCT_Grid_ND grid) {
                this.grid = grid;
            }
        }

        run3DSimulation(grid);

        int GENERATIONS = 0;
        final GridWrapper gridWrapper = new GridWrapper(grid);
        Timer timer = new Timer(200, e -> {
            if (panel.isSimulationRunning()) {
                System.out.println("GENERATIONS: " + gridWrapper.GENERATIONS);
                gridWrapper.setGrid(run2DSimulationStep(gridWrapper.getGrid(), Grid_2D));
                gridWrapper.GENERATIONS++;

            }
        });

        timer.start();

        
    }

    private static STRUCT_Grid_ND run2DSimulationStep(STRUCT_Grid_ND grid, GFX_GrilleGraphique Grid_2D) {
        int rows = grid.getDimensions()[0];
        int cols = grid.getDimensions()[1];

        STRUCT_Grid_ND new_grid = new STRUCT_Grid_ND(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
               
                if (new Rule2D().isAlive(grid, i, j)) {
                    new_grid.getCell(i, j).setCellValue(true);
                } else {
                    new_grid.getCell(i, j).setCellValue(false);
                }

            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (new_grid.getCell(i, j).getCellValue()) {
                    Grid_2D.colorierCase(i, j, Color.BLACK);
                } else {
                    Grid_2D.colorierCase(i, j, Color.WHITE);
                }
            }
        }

        return new_grid;
    }

    private static void run3DSimulation(STRUCT_Grid_ND grid) 
    {
        GFX_Cube cube = new GFX_Cube(grid);
        Application.launch(GFX_Cube.class);
    }
}
