import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class GFX_GrilleGraphique extends JPanel {
    private int largeur, hauteur, taille_case;
    private JFrame window = new JFrame();
    private STRUCT_Grid_ND grid;

    private List<ColoredPoint> casesAColorier;

    public GFX_GrilleGraphique(STRUCT_Grid_ND grid, int largeur, int hauteur, int taille_case) {
        this.grid = grid;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.taille_case = taille_case;
        casesAColorier = new ArrayList<>(25);

        window.setSize((largeur + 2) * taille_case, (hauteur + 4) * taille_case);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = (int) ((e.getX() - taille_case) / (taille_case));
                int row = (int) ((e.getY() - taille_case) / (taille_case));
                if (col >= 0 && col < largeur && row >= 0 && row < hauteur) {
                    try {
                        grid.getCell(col, row).setCellValue(!grid.getCell(col, row).getCellValue());
                        if (grid.getCell(col, row).getCellValue()) {
                            System.out.println("<Cell>" + col + "," + row + "</Cell>");
                            colorierCase(col, row, Color.BLACK);
                        } else {
                            colorierCase(col, row, Color.WHITE);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Error: Invalid cell");
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (ColoredPoint fillCell : casesAColorier) {
            int cellX = taille_case + (fillCell.getPoint().x * taille_case);
            int cellY = taille_case + (fillCell.getPoint().y * taille_case);
            g.setColor(fillCell.getColor());
            g.fillRect(cellX, cellY, taille_case, taille_case);
        }

        g.setColor(Color.BLACK);
        g.drawRect(taille_case, taille_case, largeur * taille_case, hauteur * taille_case);

        for (int i = taille_case; i <= largeur * taille_case; i += taille_case) {
            g.drawLine(i, taille_case, i, hauteur * taille_case + taille_case);
        }

        for (int i = taille_case; i <= hauteur * taille_case; i += taille_case) {
            g.drawLine(taille_case, i, largeur * taille_case + taille_case, i);
        }
    }

    public void colorierCase(int x, int y, Color color) {
        casesAColorier.add(new ColoredPoint(new Point(x, y), color));
        repaint();
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    private static class ColoredPoint {
        private Point point;
        private Color color;

        public ColoredPoint(Point point, Color color) {
            this.point = point;
            this.color = color;
        }

        public Point getPoint() {
            return point;
        }

        public Color getColor() {
            return color;
        }
    }
}
