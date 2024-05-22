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

    private int offsetX = 0, offsetY = 0;
    private double zoomFactor = 1.0;

    private Point lastDragPoint = null;

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
                int col = (int) ((e.getX() - offsetX) / (taille_case * zoomFactor));
                int row = (int) ((e.getY() - offsetY) / (taille_case * zoomFactor));
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

            @Override
            public void mousePressed(MouseEvent e) {
                lastDragPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastDragPoint = null;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastDragPoint != null) {
                    int deltaX = e.getX() - lastDragPoint.x;
                    int deltaY = e.getY() - lastDragPoint.y;
                    offsetX += deltaX;
                    offsetY += deltaY;
                    lastDragPoint = e.getPoint();
                    repaint();
                }
            }
        });

        // Adding key listener for zooming with 'z' and 's' keys
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'z') {
                    zoomFactor = Math.min(zoomFactor * 1.1, 5.0);
                    repaint();
                } else if (e.getKeyChar() == 's') {
                    zoomFactor = Math.max(zoomFactor / 1.1, 0.2);
                    repaint();
                }
            }
        });
    }

    public void updateDimensions(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        window.setSize((largeur + 2) * taille_case, (hauteur + 4) * taille_case);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.setVisible(true);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(offsetX, offsetY);
        g2d.scale(zoomFactor, zoomFactor);

        for (ColoredPoint fillCell : casesAColorier) {
            int cellX = taille_case + (fillCell.getPoint().x * taille_case);
            int cellY = taille_case + (fillCell.getPoint().y * taille_case);
            g2d.setColor(fillCell.getColor());
            g2d.fillRect(cellX, cellY, taille_case, taille_case);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawRect(taille_case, taille_case, largeur * taille_case, hauteur * taille_case);

        for (int i = taille_case; i <= largeur * taille_case; i += taille_case) {
            g2d.drawLine(i, taille_case, i, hauteur * taille_case + taille_case);
        }

        for (int i = taille_case; i <= hauteur * taille_case; i += taille_case) {
            g2d.drawLine(taille_case, i, largeur * taille_case + taille_case, i);
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
