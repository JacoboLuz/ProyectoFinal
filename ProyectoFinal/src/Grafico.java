/*
CODIGO BASE. NO TERMINADO Y NO FUNCIONA COMO DEBE DE FUNCIONAR.
Revisar:
-La forma en la que se muestran los grafos. Cambiarlo para que dejen de ponerse unos sobre otros.
-Como implementar las uniones entre cada grafo
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Grafico extends JFrame {
    private JPanel panelDibujo;
    private ArrayList<Point> vertices = new ArrayList<>();
    private ArrayList<int[]> aristas = new ArrayList<>();
    private final int TAMANIO_VERTICE = 35;

    public Grafico() {
        setTitle("Generador de Grafos Incremental");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelControles = new JPanel();
        JButton botonAgregar = new JButton("Agregar Vértice");
        panelControles.add(botonAgregar);
        add(panelControles, BorderLayout.NORTH);

        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        add(panelDibujo, BorderLayout.CENTER);

        botonAgregar.addActionListener(e -> {
            agregarVertice();
            repaint();
        });

        panelDibujo.addMouseListener(new MouseAdapter() {
            Point inicio = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                Point click = e.getPoint();
                for (int i = 0; i < vertices.size(); i++) {
                    if (click.distance(vertices.get(i)) < TAMANIO_VERTICE / 2) {
                        if (inicio == null) {
                            inicio = vertices.get(i);
                        } else {
                            aristas.add(new int[]{vertices.indexOf(inicio), i});
                            inicio = null;
                            repaint();
                        }
                        break;
                    }
                }
            }
        });
    }

    private void agregarVertice() {
        int centroX = panelDibujo.getWidth() / 2;
        int centroY = panelDibujo.getHeight() / 2;

        int numVertices = vertices.size();
        double angulo = numVertices * Math.PI / 6;
        int distancia = 100; //cambia este valor para que exista una mayor distancia entre los valores

        int x = (int) (centroX + distancia * Math.cos(angulo));
        int y = (int) (centroY + distancia * Math.sin(angulo));
        vertices.add(new Point(x, y));
    }


    private void dibujarGrafo(Graphics g) {
        g.setColor(Color.BLACK);
        for (int[] arista : aristas) {
            Point p1 = vertices.get(arista[0]);
            Point p2 = vertices.get(arista[1]);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        for (int i = 0; i < vertices.size(); i++) {
            Point vertice = vertices.get(i);
            g.setColor(Color.BLUE);
            g.fillOval(vertice.x - TAMANIO_VERTICE / 2, vertice.y - TAMANIO_VERTICE / 2, TAMANIO_VERTICE, TAMANIO_VERTICE);

            g.setColor(Color.WHITE);
            String valor = String.valueOf(i + 1); //cambiar para que sea el valor asignado al vertice
            int anchoTexto = g.getFontMetrics().stringWidth(valor);
            int altoTexto = g.getFontMetrics().getHeight();
            g.drawString(valor, vertice.x - anchoTexto / 2, vertice.y + altoTexto / 4);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Grafico ventana = new Grafico();
            ventana.setVisible(true);
        });
    }
}
