import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class GrafoDirigidoAciclicoGUI extends JFrame {
    private JPanel panelControl, panelDibujo, panelRepresentaciones;
    private JButton btnCrearVertice, btnCrearArista, btnCargarGrafo, btnGuardarGrafo, btnOrdenTopologico, btnEliminarAristas;
    private JTextArea areaOrdenTopologico;
    private JTable tablaMatrizAdyacencia, tablaListaAdyacencia;
    private int maxVertices;
    private int numVertices = 0;
    private int[][] matrizAdyacencia;
    private ArrayList<ArrayList<Integer>> listaAdyacencia;
    private ArrayList<String> nombresVertices;
    private ArrayList<Point> vertices;
    private ArrayList<int[]> aristas;
    private final int TAMANIO_VERTICE = 40;

    public GrafoDirigidoAciclicoGUI(int maxVertices) {
        this.maxVertices = maxVertices;
        this.matrizAdyacencia = new int[maxVertices][maxVertices];
        this.listaAdyacencia = new ArrayList<>();
        this.nombresVertices = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.aristas = new ArrayList<>();

        for (int i = 0; i < maxVertices; i++) {
            listaAdyacencia.add(new ArrayList<>());
        }

        setTitle("Grafo Dirigido Acíclico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        panelControl = new JPanel(new FlowLayout());
        btnCrearVertice = new JButton("Crear Vértice");
        btnCrearArista = new JButton("Crear Arista");
        btnCargarGrafo = new JButton("Cargar Grafo");
        btnGuardarGrafo = new JButton("Guardar Grafo");
        btnOrdenTopologico = new JButton("Orden Topológico");
        btnEliminarAristas = new JButton("Eliminar Todas las Aristas");

        panelControl.add(btnCrearVertice);
        panelControl.add(btnCrearArista);
        panelControl.add(btnCargarGrafo);
        panelControl.add(btnGuardarGrafo);
        panelControl.add(btnOrdenTopologico);
        panelControl.add(btnEliminarAristas);

        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control de Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelControl);
        add(controlContainer, BorderLayout.NORTH);

        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        panelDibujo.setBackground(Color.WHITE);

        JPanel dibujoContainer = new JPanel(new BorderLayout());
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Visualización del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        panelRepresentaciones = new JPanel(new GridLayout(1, 3));
        tablaMatrizAdyacencia = new JTable(maxVertices, maxVertices);
        tablaListaAdyacencia = new JTable(maxVertices, 1);
        areaOrdenTopologico = new JTextArea();
        areaOrdenTopologico.setEditable(false);
        panelRepresentaciones.add(new JScrollPane(tablaMatrizAdyacencia));
        panelRepresentaciones.add(new JScrollPane(tablaListaAdyacencia));
        panelRepresentaciones.add(new JScrollPane(areaOrdenTopologico));

        JPanel representacionesContainer = new JPanel(new BorderLayout());
        representacionesContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Representaciones del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        representacionesContainer.add(panelRepresentaciones);
        add(representacionesContainer, BorderLayout.SOUTH);

        btnCrearVertice.addActionListener(e -> crearVertice());
        btnCrearArista.addActionListener(e -> crearArista());
        btnEliminarAristas.addActionListener(e -> eliminarAristas());
    }

    private void crearVertice() {
        if (numVertices >= maxVertices) {
            JOptionPane.showMessageDialog(this, "No se pueden agregar más vértices. Límite alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreVertice = JOptionPane.showInputDialog(this, "Ingrese el nombre del vértice:");
        if (nombreVertice == null || nombreVertice.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombresVertices.contains(nombreVertice)) {
            JOptionPane.showMessageDialog(this, "El vértice ya existe. Ingrese un nombre único.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nombresVertices.add(nombreVertice);
        listaAdyacencia.add(new ArrayList<>());

        tablaListaAdyacencia.setValueAt(nombreVertice, numVertices, 0);

        for (int i = 0; i < maxVertices; i++) {
            matrizAdyacencia[numVertices][i] = 0;
            matrizAdyacencia[i][numVertices] = 0;
        }

        tablaMatrizAdyacencia.setValueAt(nombreVertice, numVertices, 0);

        agregarVerticeGrafico();
        numVertices++;
        repaint();
    }

    private void agregarVerticeGrafico() {
        int centroX = panelDibujo.getWidth() / 2;
        int centroY = panelDibujo.getHeight() / 2;

        double angulo = (numVertices * 2 * Math.PI) / maxVertices;
        int distancia = 200;
        int x = (int) (centroX + distancia * Math.cos(angulo));
        int y = (int) (centroY + distancia * Math.sin(angulo));
        vertices.add(new Point(x, y));
    }

    private void crearArista() {
        String origen = JOptionPane.showInputDialog(this, "Ingrese el vértice origen:");
        String destino = JOptionPane.showInputDialog(this, "Ingrese el vértice destino:");
        if (origen == null || destino == null || !nombresVertices.contains(origen) || !nombresVertices.contains(destino)) {
            JOptionPane.showMessageDialog(this, "Vértices inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int indexOrigen = nombresVertices.indexOf(origen);
        int indexDestino = nombresVertices.indexOf(destino);

        matrizAdyacencia[indexOrigen][indexDestino] = 1;
        aristas.add(new int[]{indexOrigen, indexDestino});
        repaint();
    }

    private void eliminarAristas() {
        aristas.clear();
        matrizAdyacencia = new int[maxVertices][maxVertices];
        repaint();
    }

    private void dibujarGrafo(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int[] arista : aristas) {
            Point origen = vertices.get(arista[0]);
            Point destino = vertices.get(arista[1]);
            dibujarFlecha(g2d, origen, destino);
        }

        for (int i = 0; i < vertices.size(); i++) {
            Point vertice = vertices.get(i);

            g2d.setColor(new Color(173, 216, 230));
            g2d.fillOval(vertice.x - TAMANIO_VERTICE / 2, vertice.y - TAMANIO_VERTICE / 2, TAMANIO_VERTICE, TAMANIO_VERTICE);

            g2d.setColor(Color.BLACK);
            FontMetrics metrics = g2d.getFontMetrics();
            String texto = nombresVertices.get(i);

            int textoAncho = metrics.stringWidth(texto);
            int textoAltura = metrics.getHeight();
            int textoX = vertice.x - textoAncho / 2;
            int textoY = vertice.y + textoAltura / 4;

            g2d.drawString(texto, textoX, textoY);
        }
    }

    private void dibujarFlecha(Graphics2D g2d, Point origen, Point destino) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(origen.x, origen.y, destino.x, destino.y);

        double dx = destino.x - origen.x;
        double dy = destino.y - origen.y;
        double angle = Math.atan2(dy, dx);

        int base = 10;
        int altura = 20;
        int desplazamiento = 15;

        int nuevoX = (int) (destino.x - desplazamiento * Math.cos(angle));
        int nuevoY = (int) (destino.y - desplazamiento * Math.sin(angle));

        int x1 = (int) (nuevoX - altura * Math.cos(angle));
        int y1 = (int) (nuevoY - altura * Math.sin(angle));
        int x2 = (int) (x1 - base * Math.sin(angle));
        int y2 = (int) (y1 + base * Math.cos(angle));
        int x3 = (int) (x1 + base * Math.sin(angle));
        int y3 = (int) (y1 - base * Math.cos(angle));

        int[] xPoints = {nuevoX, x2, x3};
        int[] yPoints = {nuevoY, y2, y3};
        g2d.fillPolygon(xPoints, yPoints, 3); // Triángulo sólido
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GrafoDirigidoAciclicoGUI grafoGUI = new GrafoDirigidoAciclicoGUI(10);
            grafoGUI.setSize(800, 600);
            grafoGUI.setVisible(true);
        });
    }
}
