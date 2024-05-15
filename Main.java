import java.util.Random;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
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
        Grid_ND grid = new Grid_ND(rows, cols);

        for (int i = 0; i < rows * cols * 0.5; i++)
         {
         grid.getCell(random.nextInt(rows), random.nextInt(cols)).setValue(true);
         }
         

         /*for(int i = -4; i < 2; i++)
         {
                for(int j = -4; j < -2; j++)
                {
                    grid.getCell(rows/2 + j, cols/2 + i).setValue(true);
                }
         }

         for(int i = -1; i < 5; i++)
         {
                for(int j = -4; j < -2; j++)
                {
                    grid.getCell(rows/2 + i, cols/2 + j).setValue(true);
                }
         }

         for(int i = -4; i < 2; i++)
         {
                for(int j = -4; j < -2; j++)
                {
                    grid.getCell(rows/2 - j, cols/2 - i).setValue(true);
                }
         }

         for(int i = -1; i < 5; i++)
         {
                for(int j = -4; j < -2; j++)
                {
                    grid.getCell(rows/2 - i, cols/2 - j).setValue(true);
                }
         }*/
         
         
         
         


        GrilleGraphique Grid_2D = new GrilleGraphique(grid.getDimensions()[0], grid.getDimensions()[1], 12);

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
            private Grid_ND grid;
            public int GENERATIONS = 0;

            public GridWrapper(Grid_ND grid) {
                this.grid = grid;
            }

            public Grid_ND getGrid() {
                return grid;
            }

            public void setGrid(Grid_ND grid) {
                this.grid = grid;
            }
        }

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

    private static Grid_ND run2DSimulationStep(Grid_ND grid, GrilleGraphique Grid_2D) {
        int rows = grid.getDimensions()[0];
        int cols = grid.getDimensions()[1];

        Grid_ND new_grid = new Grid_ND(rows, cols);

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                TreeNode compterG0 = new COMPTER(grid, "G0", i, j);
                TreeNode compterG8 = new COMPTER(grid, "G8*", i, j);

                // System.out.println("CompterG8 : " + "[" +i + ","+ j + "] | "
                // +compterG8.getValue());

                TreeNode eqG0_1 = new EQ(compterG0, new ConstNode(1)); // Mort ou vivant
                TreeNode EqG8_3 = new EQ(compterG8, new ConstNode(3)); // 3 voisins
                TreeNode EqG8_2 = new EQ(compterG8, new ConstNode(2)); // 2 voisins
                TreeNode Ou_2 = new OU(EqG8_2, EqG8_3); // 2 ou 3 voisins

                TreeNode siSupEqG8_4 = new SI(Ou_2, new ConstNode(1), new ConstNode(0)); // 2 ou 3 voisins ? 1 : 0
                TreeNode siEqG8_2 = new SI(EqG8_3, new ConstNode(1), new ConstNode(0)); // 3 voisins ? 1 : 0

                TreeNode mainSi = new SI(eqG0_1, siSupEqG8_4, siEqG8_2);

                int result = mainSi.getValue();
                if (result == 1) {
                    new_grid.getCell(i, j).setValue(true);
                } else {
                    new_grid.getCell(i, j).setValue(false);
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

    private static Grid_ND run3DSimulationStep(Grid_ND grid)
    {
        return null;
    }
}
