import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.security.Guard;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GrafoDirigidoAciclicoGUI extends JFrame {
    private JPanel panelControl, panelDibujo, panelRepresentaciones;
    private JButton btnCrearVertice, btnCrearArista, btnCargarGrafo, btnGuardarGrafo, btnOrdenTopologico, btnEliminarAristas;
    private JTextArea areaOrdenTopologico;
    private JTable tablaMatrizAdyacencia, tablaListaAdyacencia;
    private int maxVertices;
    private int nuevoMaxVertices;
    private int numVertices = 0;
    private int[][] matrizAdyacencia;
    private ArrayList<ArrayList<Integer>> listaAdyacencia;
    private ArrayList<String> nombresVertices;
    private ArrayList<String> nuevosNombresVertices;
    private ArrayList<Point> vertices;
    private ArrayList<int[]> aristas;
    private final int TAMANIO_VERTICE = 40;


    public GrafoDirigidoAciclicoGUI(int maxVertices, int[][]matrizAdyacencia,ArrayList<String> nombresVertices,ArrayList<int[]>aristas) {
        this.maxVertices = maxVertices;
        this.matrizAdyacencia = matrizAdyacencia;
        this.listaAdyacencia = new ArrayList<>();
        this.nombresVertices = nombresVertices;
        this.vertices = new ArrayList<>();
        this.aristas = aristas;

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
        btnOrdenTopologico.setEnabled(false);
        btnOrdenTopologico.addActionListener(e->ordenTopologico());
        btnGuardarGrafo.addActionListener(e->guardarGrafo());
        btnCargarGrafo.addActionListener(e->cargarGrafo());
        for (int i = 0; i <=maxVertices; i++) {
            for(int j=0;j<=maxVertices;j++){
                tablaMatrizAdyacencia.setValueAt(0, i, j);
            }
        }
        crearVerticesCargados(maxVertices,nombresVertices);
        System.out.println(maxVertices);
        System.out.println(nombresVertices);
    }


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
        btnOrdenTopologico.setEnabled(false);
        btnOrdenTopologico.addActionListener(e->ordenTopologico());
        btnGuardarGrafo.addActionListener(e->guardarGrafo());
        btnCargarGrafo.addActionListener(e->cargarGrafo());
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
        btnOrdenTopologico.setEnabled(false);
        btnOrdenTopologico.addActionListener(e->ordenTopologico());
        btnGuardarGrafo.addActionListener(e->guardarGrafo());
        btnCargarGrafo.addActionListener(e->cargarGrafo());

        for (int i = 0; i <= maxVertices; i++) {
            for(int j=0;j<=maxVertices;j++){
                tablaMatrizAdyacencia.setValueAt(0, i, j);
            }
        }
        crearVerticesAleatorios();
    }


    public GrafoDirigidoAciclicoGUI(int maxVertices,int opc) {
        this.maxVertices = maxVertices;
        this.matrizAdyacencia = new int[maxVertices+1][maxVertices+1];
        this.listaAdyacencia = new ArrayList<>();
        this.nombresVertices = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.aristas = new ArrayList<>();

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
        btnOrdenTopologico.setEnabled(false);
        btnOrdenTopologico.addActionListener(e->ordenTopologico());
        btnGuardarGrafo.addActionListener(e->guardarGrafo());
        btnCargarGrafo.addActionListener(e->cargarGrafo());

        for (int i = 0; i <= maxVertices; i++) {
            for(int j=0;j<=maxVertices;j++){
                tablaMatrizAdyacencia.setValueAt(0, i, j);
            }
        }
        if(opc==0){
            valoresNumericosAleatorios();
        }else if(opc==1){
            valoresLetrasAleatorios();
        }
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

    private void ordenTopologico(){
        areaOrdenTopologico.setText("Orden topológico: \n"+topologicalSort());
        System.out.println(mostrarEstructura());
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
            btnOrdenTopologico.setEnabled(true);
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

        btnOrdenTopologico.setEnabled(false);
        areaOrdenTopologico.setText("");
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
                    int opc = JOptionPane.showOptionDialog(null, "Seleccione una opción:",
                            "Tipo de grafos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, new Object[]{"Numericos", "Letras", "Personalizado"}, "Numericos");
                    int valor = Integer.parseInt(valorStr);
                    if (opc == 0) {
                        grafoGUI = new GrafoDirigidoAciclicoGUI(valor, opc);
                    }else if(opc==1){
                        if(valor>26){
                            valor=4;
                            JOptionPane.showMessageDialog(null, "Valor no válido. Se usarán valores aleatorios.");
                            grafoGUI = new GrafoDirigidoAciclicoGUI();
                        }else{
                            grafoGUI = new GrafoDirigidoAciclicoGUI(valor, opc);
                        }
                    }else{
                        grafoGUI = new GrafoDirigidoAciclicoGUI(valor);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Valor no válido. Se usarán valores aleatorios.");
                    grafoGUI = new GrafoDirigidoAciclicoGUI();
                }
            }
            grafoGUI.setSize(800, 600);
            grafoGUI.setVisible(true);
        });


    }

    /*
    Permiten crear los valores aleatorios en base a la cantidad de vertices
    que se tienen
     */
    public void valoresNumericosAleatorios() {
            Random random = new Random();

            int width = panelDibujo.getWidth();
            int height = panelDibujo.getHeight();

            if (width <= 0 || height <= 0) {
                width = 500;
                height = 500;
            }
            int centroX = width / 2;
            int centroY = height / 2;

            int distancia = 200;

            for (int i = 0; i < maxVertices; i++) {
                double angulo = (i * 2 * Math.PI) / maxVertices;
                int x = (int) (centroX + distancia * Math.cos(angulo));
                int y = (int) (centroY + distancia * Math.sin(angulo));

                String nombreVertice = String.valueOf(random.nextInt(100));
                nombresVertices.add(nombreVertice);
                listaAdyacencia.add(new ArrayList<>());

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

    public void valoresLetrasAleatorios() {
        String[] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z"};
        Random random = new Random();

        int width = panelDibujo.getWidth();
        int height = panelDibujo.getHeight();

        if (width <= 0 || height <= 0) {
            width = 500;
            height = 500;
        }
        int centroX = width / 2;
        int centroY = height / 2;

        int distancia = 200;

        for (int i = 0; i < maxVertices;) {
            double angulo = (i * 2 * Math.PI) / maxVertices;
            int x = (int) (centroX + distancia * Math.cos(angulo));
            int y = (int) (centroY + distancia * Math.sin(angulo));

            int letra = random.nextInt(26);
            String letraGenerada = abecedario[letra];
            if (!nombresVertices.contains(letraGenerada)) {
                nombresVertices.add(letraGenerada);
                listaAdyacencia.add(new ArrayList<>());
                vertices.add(new Point(x, y));
                numVertices++;

                tablaListaAdyacencia.setValueAt(letraGenerada, i, 0);

                for (int j = 0; j < maxVertices; j++) {
                    matrizAdyacencia[i][j] = 0;
                    matrizAdyacencia[j][i] = 0;
                }

                tablaMatrizAdyacencia.setValueAt(letraGenerada, i + 1, 0);
                tablaMatrizAdyacencia.setValueAt(letraGenerada, 0, i + 1);

                i++;
            }
            }
        repaint();
    }




    /*
    METODOS DE ACCESO
     */

    public int gradoDeEntrada(int i){
        if (i < 0 || i >= maxVertices) {
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        int gradoEntrada = 0;
        for (int u = 0; u < maxVertices; u++) {
            if (matrizAdyacencia[u][i] != 0) {
                gradoEntrada++;
            }
        }
        return gradoEntrada;
    }

    public int gradoDeSalida(int i){
        if (i < 0 || i >= maxVertices) {
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        int gradoSalida= 0;
        for (int u = 0; u < maxVertices; u++) {
            if (matrizAdyacencia[i][u] != 0) {
                gradoSalida++;
            }
        }
        return gradoSalida;
    }

    public int cuantasAristasHay(){
        int numAristas = 0;

        for (int i = 0; i < maxVertices; i++) {
            for (int j = 0; j < maxVertices; j++) {
                if (matrizAdyacencia[i][j] != 0) {
                    numAristas++;
                }
            }
        }
        return numAristas;
    }

    public boolean adyacente(int i,int j){
        if(i<0||j<0||i>=maxVertices||j>=maxVertices){
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        return matrizAdyacencia[i][j] != 0;
    }


    public boolean conectados(int i,int j){
        if(i<0||j<0||i>=maxVertices||j>=maxVertices){
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        if(i==j){
            return false;
        }
        Queue<Integer> cola=new LinkedList<>();
        boolean[] valores=new boolean[maxVertices];
        cola.add(i);
        valores[i]=true;

        while(!cola.isEmpty()){
            int actual=cola.poll();//elimina y regresa el elemento superior
            for(int x=0;x<maxVertices;x++){
                //solo si el valor fue encontrado pero no había sido visitado
                if(matrizAdyacencia[actual][x]!=0 && valores[x]==false){
                    if(x==j){ //si se encontró el valor de j
                        return true;
                    }
                    valores[x]=true; //aseguramos que ya "visitamos" el valor
                    cola.add(x);
                }
            }
        }
        return false;
    }


    public String topologicalSort(){
            int[] gradoEntrada = new int[maxVertices];

            for (int i = 0; i < maxVertices; i++) {
                gradoEntrada[i] = gradoDeEntrada(i);
            }

            PriorityQueue<Integer> colaPrioridad = new PriorityQueue<>(Comparator.reverseOrder());

            for (int i = 0; i < maxVertices; i++) {
                if (gradoEntrada[i] == 0) {
                    colaPrioridad.add(i);
                }
            }

            String ordenTopologico = "";

            int cont = 0;
            while (!colaPrioridad.isEmpty()) {
                int vertice = colaPrioridad.poll();

                if (cont > 0) {
                    ordenTopologico += "-";
                }
                ordenTopologico += nombresVertices.get(vertice);

                for (int i = 0; i < maxVertices; i++) {
                    if (adyacente(vertice, i)) {
                        gradoEntrada[i]--;
                        if (gradoEntrada[i] == 0) {
                            colaPrioridad.add(i);
                        }
                    }
                }

                cont++;
            }
            return ordenTopologico;
    }

    public String mostrarEstructura(){
        String texto=" ";
        for(int i = 0; i < maxVertices; i++){
            texto+=" "+nombresVertices.get(i);
        }
        texto+="\n";

        for(int i = 0; i < maxVertices; i++){
            texto+=nombresVertices.get(i)+" ";
            for(int j = 0; j < maxVertices; j++){
                texto+=" "+matrizAdyacencia[i][j];
            }
            texto +="\n";
        }
        return texto;
    }


    //Metodo que permite guardar el grafo en el archivo
    public void guardarGrafo() {
        File archivo = new File("grafosGuardados.txt");
        if (archivo.exists() && verificar(archivo)) {
            JOptionPane.showMessageDialog(this, "El grafo ya había sido guardado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo,true))) {
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            escritor.write("Fecha y hora de creación: " + fechaHora + "\n");
            escritor.write("Max Vértices: " + this.maxVertices + "\n");

            escritor.write("Matriz de Adyacencia:\n");
            for (int i = 0; i < this.maxVertices; i++) {
                for (int j = 0; j < this.maxVertices; j++) {
                    escritor.write(this.matrizAdyacencia[i][j] + " ");
                }
                escritor.write("\n");
            }
            escritor.write("Lista de Vértices: \n");
            for (String vertice : this.nombresVertices) {
                escritor.write(vertice + "\n");
            }

            escritor.write("Aristas: \n");
            for (int[] arista : this.aristas) {
                escritor.write("[" + arista[0] + " -> " + arista[1] + "]\n");
            }

            JOptionPane.showMessageDialog(null, "Su grafo ha sido guardado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean verificar(File archivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith("Matriz de Adyacencia:")) {
                    for (int i = 0; i < this.maxVertices; i++) {
                        linea = lector.readLine();
                        String[] valores = linea.trim().split(" ");
                        for (int j = 0; j < this.maxVertices; j++) {
                            if (Integer.parseInt(valores[j]) != this.matrizAdyacencia[i][j]) {
                                return false;
                            }
                        }
                    }
                    for (String vertice : this.nombresVertices) {
                        linea = lector.readLine();
                        if (!linea.equals(vertice)) {
                            return false;
                        }
                    }
                    for (int[] arista : this.aristas) {
                        linea = lector.readLine();
                        if (!linea.equals(arista)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cargarGrafo(){
            List<String> grafos = listarGrafos();
            if (grafos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay grafos guardados para mostrar.", "EROR", JOptionPane.ERROR_MESSAGE);
            } else {
                String seleccion = (String) JOptionPane.showInputDialog(
                        null,
                        "Seleccione un grafo para cargar:",
                        "Cargar Grafo",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        grafos.toArray(),
                        grafos.get(0)
                );

                if (seleccion != null) {
                    int indice = Integer.parseInt(seleccion.split(" ")[1]) - 1;
                    leerArchivo(indice);
                }
        }

    }
    public void leerArchivo(int indice) {
        File archivo = new File("grafosGuardados.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo de grafos guardados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int grafoActual = -1;
            boolean leyendoMatriz = false;
            boolean leyendoVertices = false;
            boolean leyendoAristas = false;

            int[][] nuevaMatrizAdyacencia = null;
            ArrayList<String> nuevosNombresVertices = new ArrayList<>();
            ArrayList<int[]> nuevasAristas = new ArrayList<>();
            int nuevoMaxVertices = 0;

            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith("Fecha y hora de creación: ")) {
                    grafoActual++;
                    if (grafoActual > indice) break;
                }

                if (grafoActual == indice) {
                    if (linea.startsWith("Max Vértices: ")) {
                        nuevoMaxVertices = Integer.parseInt(linea.replace("Max Vértices: ", "").trim());
                        nuevaMatrizAdyacencia = new int[nuevoMaxVertices][nuevoMaxVertices];
                    } else if (linea.startsWith("Matriz de Adyacencia:")) {
                        leyendoMatriz = true;
                        leyendoVertices = false;
                        leyendoAristas = false;
                    } else if (linea.startsWith("Lista de Vértices:")) {
                        leyendoMatriz = false;
                        leyendoVertices = true;
                        leyendoAristas = false;
                    } else if (linea.startsWith("Aristas:")) {
                        leyendoMatriz = false;
                        leyendoVertices = false;
                        leyendoAristas = true;
                    } else if (leyendoMatriz) {
                        String[] valores = linea.trim().split(" ");
                        for (int i = 0; i < valores.length; i++) {
                            nuevaMatrizAdyacencia[nuevosNombresVertices.size()][i] = Integer.parseInt(valores[i]);
                        }
                    } else if (leyendoVertices) {
                        nuevosNombresVertices.add(linea.trim());
                    } else if (leyendoAristas) {
                        String[] arista = linea.replace("[", "").replace("]", "").split(" -> ");
                        nuevasAristas.add(new int[]{Integer.parseInt(arista[0]), Integer.parseInt(arista[1])});
                    }
                }
            }
            this.nuevoMaxVertices=nuevoMaxVertices;
            this.nuevosNombresVertices=nuevosNombresVertices;
            Auxiliar nuevaVentana = new Auxiliar(nuevoMaxVertices, nuevaMatrizAdyacencia, nuevosNombresVertices, nuevasAristas);
            System.out.println("Nuevo Max Vértices: " + nuevoMaxVertices);
            System.out.println("Nombres de Vértices: " + nuevosNombresVertices);

            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el grafo. El archivo puede estar corrupto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> listarGrafos() {
        List<String> grafos = new ArrayList<>();
        File archivo = new File("grafosGuardados.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null, "No se encontraron grafos guardados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return grafos;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String fechaHora = null;
            int grafoId = 1;

            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith("Fecha y hora de creación: ")) {
                    fechaHora = linea.replace("Fecha y hora de creación: ", "").trim();
                }

                if (linea.startsWith("Max Vértices: ")) {
                    grafos.add("Grafo " + grafoId + " / Fecha: " + fechaHora);
                    grafoId++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grafos;
    }
    private void crearVerticesCargados(int nuevoMaxVertices,ArrayList<String> nuevosNombresVertices) {

        if (nuevoMaxVertices == 0 || nuevosNombresVertices.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay datos cargados para los vértices.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int width = panelDibujo.getWidth();
        int height = panelDibujo.getHeight();

        if (width <= 0 || height <= 0) {
            width = 500;
            height = 500;
        }
        int centroX = width / 2;
        int centroY = height / 2;

        int distancia = 200;

        for (int i = 0; i < maxVertices; i++) {
            double angulo = (i * 2 * Math.PI) / maxVertices;
            int x = (int) (centroX + distancia * Math.cos(angulo));
            int y = (int) (centroY + distancia * Math.sin(angulo));

            String nombreVertice = nuevosNombresVertices.get(i);
            System.out.println(nombreVertice);
            nombresVertices.add(nombreVertice);
            listaAdyacencia.add(new ArrayList<>());


            vertices.add(new Point(x, y));

            numVertices++;

            tablaListaAdyacencia.setValueAt(nombreVertice, i, 0);

            for (int j = 0; j < nuevoMaxVertices; j++) {
                matrizAdyacencia[i][j] = 0;
                matrizAdyacencia[j][i] = 0;
            }

            tablaMatrizAdyacencia.setValueAt(nombreVertice, i + 1, 0);
            tablaMatrizAdyacencia.setValueAt(nombreVertice, 0, i + 1);
        }

        repaint();
    }



}

