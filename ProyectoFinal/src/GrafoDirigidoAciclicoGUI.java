import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

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
        this.matrizAdyacencia = new int[maxVertices+1][maxVertices+1];
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
        tablaMatrizAdyacencia = new JTable(maxVertices+1, maxVertices+1);
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
        for (int i = 0; i <=maxVertices; i++) {
            for(int j=0;j<=maxVertices;j++){
                tablaMatrizAdyacencia.setValueAt(0, i, j);
            }
        }
    }

    public GrafoDirigidoAciclicoGUI() {
        this.maxVertices = 4;
        this.matrizAdyacencia = new int[maxVertices+1][maxVertices+1];
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
        tablaMatrizAdyacencia = new JTable(maxVertices+1, maxVertices+1);
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
        for (int i = 0; i <= maxVertices; i++) {
            for(int j=0;j<=maxVertices;j++){
                tablaMatrizAdyacencia.setValueAt(0, i, j);
            }
        }
        crearVerticesAleatorios();
    }



    //Método para generar vercices siempre y cuando no se haya excedido el tope de los mismos en el programa como tambien
    //es el encargado de añadir los vertices tanto a lista de adyacencia como a la matriz de adyacencia.
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

        tablaMatrizAdyacencia.setValueAt(nombreVertice, numVertices+1, 0);
        tablaMatrizAdyacencia.setValueAt(nombreVertice, 0, numVertices+1);
        agregarVerticeGrafico();
        numVertices++;
        repaint();
    }

    //Es el encargado de pintar el vertice en la parte gráfica de la GUI.
    private void agregarVerticeGrafico() {
        int centroX = panelDibujo.getWidth() / 2;
        int centroY = panelDibujo.getHeight() / 2;

        double angulo = (numVertices * 2 * Math.PI) / maxVertices;
        int distancia = 200;
        int x = (int) (centroX + distancia * Math.cos(angulo));
        int y = (int) (centroY + distancia * Math.sin(angulo));
        vertices.add(new Point(x, y));
    }

    //Es un método encargado de crear una arista entre 2 vertices primero comprobando si dicha arista no generara
    //algun ciclo en el programa, para luego ademas de añadir la arista modifica tambien tanto la matriz como la lista
    //de adyacencia
    private void crearArista() {
        String origen = JOptionPane.showInputDialog(this, "Ingrese el vértice origen:");
        String destino = JOptionPane.showInputDialog(this, "Ingrese el vértice destino:");

        if (origen == null || destino == null || !nombresVertices.contains(origen) || !nombresVertices.contains(destino)) {
            JOptionPane.showMessageDialog(this, "Vértices inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (origen.equals(destino)) {
            JOptionPane.showMessageDialog(this, "No se pueden crear aristas que conecten un vértice consigo mismo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int indexOrigen = nombresVertices.indexOf(origen);
        int indexDestino = nombresVertices.indexOf(destino);

        if (matrizAdyacencia[indexOrigen][indexDestino] == 1) {
            JOptionPane.showMessageDialog(this, "La arista ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (verificarCiclo(indexOrigen, indexDestino)) {
            JOptionPane.showMessageDialog(this, "La creación de un ciclo no está permitida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        matrizAdyacencia[indexOrigen][indexDestino] = 1;
        aristas.add(new int[]{indexOrigen, indexDestino});

        ArrayList<Integer> adyacentes = listaAdyacencia.get(indexOrigen);
        if (!adyacentes.contains(indexDestino)) {
            adyacentes.add(indexDestino);
        }

        tablaMatrizAdyacencia.setValueAt("1", indexOrigen+1, indexDestino+1);

        StringBuilder listaAdyacente = new StringBuilder();
        listaAdyacente.append(origen);

        ArrayList<Integer> lista = listaAdyacencia.get(indexOrigen);
        for (int i = 0; i < lista.size(); i++) {

            int verticeDestino = lista.get(i);

            listaAdyacente.append(" -> ");
            listaAdyacente.append(nombresVertices.get(verticeDestino));
        }
        listaAdyacente.append("/0");

        tablaListaAdyacencia.setValueAt(listaAdyacente.toString(), indexOrigen, 0);

        int x1 = vertices.get(indexOrigen).x;
        int y1 = vertices.get(indexOrigen).y;
        int x2 = vertices.get(indexDestino).x;
        int y2 = vertices.get(indexDestino).y;

        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);  // Puedes ajustar el color
        g2d.drawLine(x1, y1, x2, y2);

        repaint();
    }
    //Manda a llamar a un método responsable de verificar si ocurre un ciclo en el programa
    private boolean verificarCiclo(int origen, int destino) {
        boolean[] visitados = new boolean[nombresVertices.size()];
        return hayCiclo(destino, origen, visitados);
    }
    //Método encargado de revisar si ocurre algun ciclo en el programa.
    private boolean hayCiclo(int vertice, int destino, boolean[] visitados) {
        if (visitados[vertice]) {
            return true;
        }

        visitados[vertice] = true;

        for (int vecino : listaAdyacencia.get(vertice)) {
            if (vecino == destino || hayCiclo(vecino, destino, visitados)) {
                return true;
            }
        }

        return false;
    }
    //Método encargado de borrar todas las aristas junto a las flechas dibujadas en el programa
    private void eliminarAristas() {
        // Limpiar todas las aristas
        aristas.clear();

        // Reiniciar la matriz de adyacencia
        for (int i = 0; i < maxVertices; i++) {
            for (int j = 0; j < maxVertices; j++) {
                matrizAdyacencia[i][j] = 0;  // Reiniciar todos los valores a 0
            }
        }

        // Reiniciar la lista de adyacencia
        listaAdyacencia.clear();
        for (int i = 0; i < nombresVertices.size(); i++) {
            listaAdyacencia.add(new ArrayList<>());
        }

        // Actualizar las tablas de la interfaz gráfica
        for (int i = 0; i < nombresVertices.size(); i++) {
            tablaMatrizAdyacencia.setValueAt(nombresVertices.get(i), i + 1, 0);
            tablaMatrizAdyacencia.setValueAt(nombresVertices.get(i), 0, i + 1);
            tablaListaAdyacencia.setValueAt(nombresVertices.get(i), i, 0);
            for (int j = 0; j < nombresVertices.size(); j++) {
                tablaMatrizAdyacencia.setValueAt(0, i + 1, j + 1);
            }
        }

        repaint();
    }

    //Método encargado de dibujar los grafos de la GUI.
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
    //Método para dibujar la flecha de las aristas cuando se va de un vertice a otro.
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
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
    //Parte del método constructor de aleatorios
    private void crearVerticesAleatorios() {
        Random random = new Random();

        int width = panelDibujo.getWidth();
        int height = panelDibujo.getHeight();

        if (width <= 0 || height <= 0) {
            width = 500;
            height = 500;
        }

        int distanciaX = width / 3;
        int distanciaY = height / 3;

        for (int i = 0; i < 4; i++) {
            String nombreVertice = String.valueOf(random.nextInt(100));
            nombresVertices.add(nombreVertice);
            listaAdyacencia.add(new ArrayList<>());

            int x = (i % 2) * distanciaX + distanciaX / 2;
            int y = (i / 2) * distanciaY + distanciaY / 2;

            vertices.add(new Point(x, y));

            numVertices++;

            tablaListaAdyacencia.setValueAt(nombreVertice, i, 0);

            for (int j = 0; j < maxVertices; j++) {
                matrizAdyacencia[i][j] = 0;
                matrizAdyacencia[j][i] = 0;
            }

            tablaMatrizAdyacencia.setValueAt(nombreVertice, i + 1, 0);
            tablaMatrizAdyacencia.setValueAt(nombreVertice, 0, i + 1);
        }

        repaint();
    }


    //Método main del programa.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showOptionDialog(null, "¿Quieres usar números aleatorios o un valor personalizado?",
                    "Seleccionar opción", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new Object[] {"Aleatorios", "Personalizado"}, "Aleatorios");

            GrafoDirigidoAciclicoGUI grafoGUI;
            if (opcion == 0) {
                grafoGUI = new GrafoDirigidoAciclicoGUI();
            } else {
                String valorStr = JOptionPane.showInputDialog("Introduce un valor:");
                try {
                    int valor = Integer.parseInt(valorStr);
                    grafoGUI = new GrafoDirigidoAciclicoGUI(valor);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Valor no válido. Se usarán valores aleatorios.");
                    grafoGUI = new GrafoDirigidoAciclicoGUI();
                }
            }

            grafoGUI.setSize(800, 600);
            grafoGUI.setVisible(true);
        });
    }
}
