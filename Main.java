
// 2D
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

// XML
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

// Tools
import java.util.Scanner;
import java.util.ResourceBundle.Control;

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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* COMMANDS TO COMPILE AND RUN
javac --module-path ./javafx/lib --add-modules javafx.controls,javafx.fxml Main.java
java --module-path ./javafx/lib --add-modules javafx.controls,javafx.fxml Main
*/

@SuppressWarnings("unused")
public class Main {

    static STRUCT_Grid_ND grid;
    static int[] cut = new int[2];
    static TOOLS_EvolutionRule evolutionRule;
    static int height = 500; //1D hauteur
    static int taille_case = 12;
    static int delay = 100;
    static Color color_vie = Color.BLACK;
    static Color color_mort = Color.WHITE;

    public static void main(String[] args)
        throws ParserConfigurationException, SAXException, NumberFormatException {
        TOOLS_ConfigLoader loader = new TOOLS_ConfigLoader();
        String filePath = loader.askForFilePath();
        if (filePath != null) {
            config(filePath);
        } else {
            config("./configs/Random2D.xml");
            System.out.println("Aucun fichier sélectionné, utilisation des valeurs par défaut.");
        }

        int[] dimensions = grid.getDimensions();
        if (dimensions.length == 3) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to see a 2D cut? (yes/no): ");
            String response = scanner.nextLine();
            scanner.close();

            if (!response.equalsIgnoreCase("no")) {
                run3DCutSimulation(grid, cut[1], cut[0]);
            } else {
                run3DSimulation(grid);
            }
        } else if (dimensions.length == 2) {
            run2DSimulation(grid);
        } else if (dimensions.length == 1) {
            run1DSimulation(grid);
        }
    }

    private static void run1DSimulation(STRUCT_Grid_ND grid) {
        int width = grid.getDimensions()[0];

        GFX_GrilleGraphique grid2D = new GFX_GrilleGraphique(grid, width, height, taille_case);

        // Initialize the first row
        for (int i = 0; i < width; i++) {
            if (grid.getCell(i).getCellValue()) {
                grid2D.colorierCase(i, 0, color_vie);
            } else {
                grid2D.colorierCase(i, 0, color_mort);
            }
        }

        GFX_Start controle = new GFX_Start();
        controle.setVisible(true);

        final TOOLS_GridWrapper gridWrapper = new TOOLS_GridWrapper(grid);
        Timer timer = new Timer(delay, e -> {
            if (controle.isSimulationRunning()) {
                //System.out.println("GENERATIONS: " + gridWrapper.GENERATIONS);
                gridWrapper.setGrid(run1DSimulationStep(gridWrapper.getGrid(), grid2D, gridWrapper.GENERATIONS + 1));
                gridWrapper.GENERATIONS++;
            }
        });

        timer.start();
    }

    private static STRUCT_Grid_ND run1DSimulationStep(STRUCT_Grid_ND grid, GFX_GrilleGraphique Grid_2D,
            int generation) {

        int width = grid.getDimensions()[0];

        STRUCT_Grid_ND new_grid = new STRUCT_Grid_ND(width);

        if (generation == height) {
            return null;
        }
        for (int i = 0; i < width; i++) {
            COMPTER.setSettings(grid, i);
            TOOLS_EvolutionRule.cursor = 0;
            int result = evolutionRule.createNode(TOOLS_EvolutionRule.ParseFile()).getValue();
            if (result == 1) {
                new_grid.getCell(i).setCellValue(true);
                Grid_2D.colorierCase(i, generation, color_vie);
            } else {
                new_grid.getCell(i).setCellValue(false);
                Grid_2D.colorierCase(i, generation, color_mort);
            }
        }

        return new_grid;
    }

    private static void run2DSimulation(STRUCT_Grid_ND grid) {
        GFX_GrilleGraphique Grid_2D = new GFX_GrilleGraphique(
                grid,
                grid.getDimensions()[0],
                grid.getDimensions()[1],
                taille_case);

        for (int i = 0; i < grid.getDimensions()[0]; i++) {
            for (int j = 0; j < grid.getDimensions()[1]; j++) {
                if (grid.getCell(i, j).getCellValue()) {
                    Color color = color_vie;
                    Grid_2D.colorierCase(i, j, color);
                } else {
                    Color color = color_mort;
                    Grid_2D.colorierCase(i, j, color);
                }
            }
        }

        GFX_Start controle = new GFX_Start();
        controle.setVisible(true);

        final TOOLS_GridWrapper gridWrapper = new TOOLS_GridWrapper(grid);
        Timer timer = new Timer(delay, e -> {
            if (controle.isSimulationRunning()) {
                //System.out.println("GENERATIONS: " + gridWrapper.GENERATIONS);
                gridWrapper.setGrid(
                        run2DSimulationStep(gridWrapper.getGrid(), Grid_2D));
                gridWrapper.GENERATIONS++;
            }
        });

        timer.start();
    }

    private static STRUCT_Grid_ND run2DSimulationStep(
            STRUCT_Grid_ND grid,
            GFX_GrilleGraphique Grid_2D) {
        int rows = grid.getDimensions()[0];
        int cols = grid.getDimensions()[1];

        STRUCT_Grid_ND new_grid = new STRUCT_Grid_ND(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                COMPTER.setSettings(grid, i, j);
                TOOLS_EvolutionRule.cursor = 0;
                int result = evolutionRule
                        .createNode(TOOLS_EvolutionRule.ParseFile())
                        .getValue();
                if (result == 1) {
                    new_grid.getCell(i, j).setCellValue(true);
                } else {
                    new_grid.getCell(i, j).setCellValue(false);
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (new_grid.getCell(i, j).getCellValue()) {
                    Grid_2D.colorierCase(i, j, color_vie);
                } else {
                    Grid_2D.colorierCase(i, j, color_mort);
                }
            }
        }

        return new_grid;
    }

    private static void run3DCutSimulation(
            STRUCT_Grid_ND grid,
            int value,
            int axis) {
        int[] dimensions = grid.getDimensions();
        int width, height;

        // Determine the dimensions of the 2D slice
        if (axis == 0) {
            width = dimensions[1];
            height = dimensions[2];
        } else if (axis == 1) {
            width = dimensions[0];
            height = dimensions[2];
        } else {
            width = dimensions[0];
            height = dimensions[1];
        }

        GFX_GrilleGraphique grid2D = new GFX_GrilleGraphique(
                grid,
                width,
                height,
                taille_case);

        // Extract and display the 2D slice
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean cellValue;
                if (axis == 0) {
                    cellValue = grid.getCell(value, i, j).getCellValue();
                } else if (axis == 1) {
                    cellValue = grid.getCell(i, value, j).getCellValue();
                } else {
                    cellValue = grid.getCell(i, j, value).getCellValue();
                }
                if (cellValue) {
                    grid2D.colorierCase(i, j, color_vie);
                }
            }
        }

        GFX_Start controle = new GFX_Start();
        controle.setVisible(true);

        final TOOLS_GridWrapper gridWrapper = new TOOLS_GridWrapper(grid);
        Timer timer = new Timer(delay, e -> {
            if (controle.isSimulationRunning()) {
                //System.out.println("GENERATIONS: " + gridWrapper.GENERATIONS);
                gridWrapper.setGrid(
                        run3DSimulationStep(
                                gridWrapper.getGrid(),
                                grid2D,
                                value,
                                axis));
                gridWrapper.GENERATIONS++;
            }
        });

        timer.start();
    }

    private static STRUCT_Grid_ND run3DSimulationStep(
            STRUCT_Grid_ND grid,
            GFX_GrilleGraphique Grid_2D,
            int value,
            int axis) {
        int rows = grid.getDimensions()[0];
        int cols = grid.getDimensions()[1];
        int depth = grid.getDimensions()[2];

        STRUCT_Grid_ND new_grid = new STRUCT_Grid_ND(rows, cols, depth);

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                for (int z = 0; z < depth; z++) {
                    COMPTER.setSettings(grid, x, y, z);
                    TOOLS_EvolutionRule.cursor = 0;
                    int result = evolutionRule
                            .createNode(TOOLS_EvolutionRule.ParseFile())
                            .getValue();
                    if (result == 1) {
                        new_grid.getCell(x, y, z).setCellValue(true);
                    } else {
                        new_grid.getCell(x, y, z).setCellValue(false);
                    }
                }
            }
        }

        int[] dimensions = grid.getDimensions();
        int width, height;

        // Determine the dimensions of the 2D slice
        if (axis == 0) {
            width = dimensions[1];
            height = dimensions[2];
        } else if (axis == 1) {
            width = dimensions[0];
            height = dimensions[2];
        } else {
            width = dimensions[0];
            height = dimensions[1];
        }

        // Extract and display the 2D slice
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean cellValue;
                if (axis == 0) {
                    cellValue = new_grid.getCell(value, i, j).getCellValue();
                } else if (axis == 1) {
                    cellValue = new_grid.getCell(i, value, j).getCellValue();
                } else {
                    cellValue = new_grid.getCell(i, j, value).getCellValue();
                }
                if (cellValue) {
                    Grid_2D.colorierCase(i, j, color_vie);
                } else {
                    Grid_2D.colorierCase(i, j, color_mort);
                }
            }
        }
        return new_grid;
    }

    private static void run3DSimulation(STRUCT_Grid_ND grid) {
        GFX_Cube cube = new GFX_Cube(grid);
        Application.launch(GFX_Cube.class);
    }

    public static void config(String path)
            throws ParserConfigurationException, SAXException {
        try {
            File file = new File(path);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            NodeList nList = document.getDocumentElement().getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    switch (eElement.getNodeName()) {
                        case "Dimension":
                            System.out.println(
                                    "Dimension : " + eElement.getTextContent());
                            break;
                        case "GridSize":
                            NodeList sizes = eElement.getElementsByTagName(
                                    "Size");
                            System.out.print("GridSize : ");
                            int[] gridSize = new int[sizes.getLength()];
                            for (int i = 0; i < sizes.getLength(); i++) {
                                gridSize[i] = Integer.parseInt(
                                        sizes.item(i).getTextContent());
                                System.out.print(
                                        sizes.item(i).getTextContent() + " ");
                            }
                            grid = new STRUCT_Grid_ND(gridSize);
                            System.out.println();
                            break;
                        case "Cut":
                            String[] parts = eElement
                                    .getTextContent()
                                    .split(" ");
                            int axis = -1;
                            switch (parts[0].toLowerCase()) {
                                case "x":
                                    cut[0] = 0;
                                    break;
                                case "y":
                                    cut[0] = 1;
                                    break;
                                case "z":
                                    cut[0] = 2;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Invalid axis. Use 'x', 'y', or 'z'.");
                            }
                            cut[1] = Integer.parseInt(parts[1]);
                            System.out.println(
                                    "Cut : " + eElement.getTextContent());
                            break;
                        case "CustomNeighborhoods":
                            NodeList neighborhoods = eElement.getElementsByTagName("Neighborhood");
                            for (int i = 0; i < neighborhoods.getLength(); i++) {
                                new TOOLS_NeighborhoodParser()
                                        .parseFile(
                                                neighborhoods.item(i).getTextContent());
                                System.out.println(
                                        "Neighborhood : " +
                                                neighborhoods.item(i).getTextContent());
                            }
                            System.out.println(
                                    new TOOLS_Neighborhoods().toString());
                            break;
                        case "EvolutionRule":
                            evolutionRule = new TOOLS_EvolutionRule(
                                    eElement.getTextContent(),
                                    false);
                            System.out.println(
                                    "EvolutionRule : " + eElement.getTextContent());
                            break;
                        case "InitialCells":
                            if (eElement
                                    .getElementsByTagName("Random")
                                    .getLength() > 0) {
                                NodeList k = eElement.getElementsByTagName(
                                        "Random");
                                grid.initializeCells(
                                        Integer.parseInt(k.item(0).getTextContent()));
                            } else {
                                NodeList cells = eElement.getElementsByTagName(
                                        "Cell");
                                for (int i = 0; i < cells.getLength(); i++) {
                                    String[] coordinates = cells
                                            .item(i)
                                            .getTextContent()
                                            .split(",");
                                    int[] cellCoordinates = new int[coordinates.length];
                                    for (int j = 0; j < coordinates.length; j++) {
                                        cellCoordinates[j] = Integer.parseInt(
                                                coordinates[j].trim());
                                    }
                                    grid
                                            .getCell(cellCoordinates)
                                            .setCellValue(true);
                                    System.out.println(
                                            "Cell : " +
                                                    cells.item(i).getTextContent());
                                }
                            }
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
