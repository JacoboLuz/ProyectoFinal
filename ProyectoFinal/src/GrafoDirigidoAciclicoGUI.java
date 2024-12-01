import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class GrafoDirigidoAciclicoGUI extends JFrame {
    private JPanel panelControl, panelDibujo, panelRepresentaciones;
    private JButton btnCrearVertice, btnCrearArista, btnCargarGrafo, btnGuardarGrafo, btnOrdenTopologico;
    private JTextArea areaOrdenTopologico;
    private JTable tablaMatrizAdyacencia, tablaListaAdyacencia;
    private int maxVertices;
    private int numVertices = 0;
    private int[][] matrizAdyacencia;
    private ArrayList<ArrayList<Integer>> listaAdyacencia;
    private ArrayList<String> nombresVertices; // Lista para almacenar los nombres de los vértices
    private static GrafoDirigidoAciclico grafoDirigidoAciclico;

    public GrafoDirigidoAciclicoGUI(int maxVertices) {
        this.maxVertices = maxVertices;
        this.matrizAdyacencia = new int[maxVertices][maxVertices];
        this.listaAdyacencia = new ArrayList<>();
        this.nombresVertices = new ArrayList<>(); // Inicializamos la lista de nombres de vértices

        for (int i = 0; i < maxVertices; i++) {
            listaAdyacencia.add(new ArrayList<>());
        }

        setTitle("Grafo Dirigido Acíclico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel de control
        panelControl = new JPanel();
        panelControl.setLayout(new FlowLayout());
        btnCrearVertice = new JButton("Crear Vértice");
        btnCrearArista = new JButton("Crear Arista");
        btnCargarGrafo = new JButton("Cargar Grafo");
        btnGuardarGrafo = new JButton("Guardar Grafo");
        btnOrdenTopologico = new JButton("Orden Topológico");
        panelControl.add(btnCrearVertice);
        panelControl.add(btnCrearArista);
        panelControl.add(btnCargarGrafo);
        panelControl.add(btnGuardarGrafo);
        panelControl.add(btnOrdenTopologico);
        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control de Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelControl);
        add(controlContainer, BorderLayout.NORTH);

        // Panel de dibujo
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        panelDibujo.setBackground(Color.WHITE);
        JPanel dibujoContainer = new JPanel(new BorderLayout());
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Visualización del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        // Panel de representaciones
        panelRepresentaciones = new JPanel();
        panelRepresentaciones.setLayout(new GridLayout(1, 3));
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

        // Agregar funcionalidad a los botones
        btnCrearVertice.addActionListener(e -> crearVertice());
        btnCrearArista.addActionListener(e->crearArista());
        btnCargarGrafo.addActionListener(e->cargarGrafo());
        btnGuardarGrafo.addActionListener(e->guardarGrafo());
        btnOrdenTopologico.addActionListener(e->ordenTopologico());
    }

    private void crearVertice() {
        if (numVertices >= maxVertices) {
            JOptionPane.showMessageDialog(this, "No se pueden agregar más vértices. Límite alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pedir al usuario el nombre o valor para el vértice
        String valorVertice = JOptionPane.showInputDialog(this, "Ingrese el nombre o valor para el vértice " + (numVertices + 1) + ":");

        // Validar la entrada
        if (valorVertice == null || valorVertice.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un valor para el vértice.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el nombre del vértice ya existe
        if (nombresVertices.contains(valorVertice)) {
            JOptionPane.showMessageDialog(this, "El valor del vértice ya existe. Ingrese otro valor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar el valor del vértice a la lista de nombres
        nombresVertices.add(valorVertice);

        numVertices++;

        // Agregar el valor del vértice a la lista de adyacencia
        listaAdyacencia.get(numVertices - 1).add(-1);  // Inicialmente sin conexiones (representado como -1 o cualquier valor que se desee)

        // Actualizar la tabla de matriz de adyacencia
        for (int i = 0; i < numVertices; i++) {
            tablaMatrizAdyacencia.setValueAt(0, numVertices - 1, i);  // Aseguramos que todos los valores son 0 (sin conexión)
            tablaMatrizAdyacencia.setValueAt(0, i, numVertices - 1);
        }

        // Colocar el valor del vértice en la posición (1, 0) en la matriz de adyacencia
        tablaMatrizAdyacencia.setValueAt(valorVertice, numVertices - 1, 0);

        // Actualizar la tabla de lista de adyacencia
        StringBuilder lista = new StringBuilder(valorVertice);  // Comenzar la lista con el nombre del vértice
        for (int v : listaAdyacencia.get(numVertices - 1)) {
            if (v != -1) { // Solo agregar valores válidos (no -1)
                lista.append(" ").append(v);
            }
        }
        tablaListaAdyacencia.setValueAt(lista.toString(), numVertices - 1, 0);

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(this, "Vértice " + valorVertice + " creado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void crearArista() {

    }

    private void cargarGrafo() {

    }

    private void guardarGrafo() {

    }

    private void ordenTopologico() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int maxVertices = solicitarMaxVertices();
            if (maxVertices > 0) {
                GrafoDirigidoAciclicoGUI gui = new GrafoDirigidoAciclicoGUI(maxVertices);
                grafoDirigidoAciclico=new GrafoDirigidoAciclico(maxVertices);
                gui.setVisible(true);
            }
        });
    }

    private static int solicitarMaxVertices() {
        String input = JOptionPane.showInputDialog(
                null,
                "Ingrese el número máximo de vértices para el grafo:",
                "Configuración Inicial",
                JOptionPane.PLAIN_MESSAGE
        );
        try {
            int maxVertices = Integer.parseInt(input);
            if (maxVertices > 0) {
                return maxVertices;
            } else {
                JOptionPane.showMessageDialog(null, "El número debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Introduzca un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }


}
