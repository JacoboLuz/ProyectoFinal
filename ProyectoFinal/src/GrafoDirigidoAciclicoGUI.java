import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.List;
import java.util.Random;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GrafoDirigidoAciclicoGUI extends JFrame {

    //
    private JPanel panelDeControl, panelDeDibujo, panelFormasDeMostrar;
    private JButton botonParaCrearVertices, botonParaCrearAristas, botonParaCargarGrafos, botonParaGuardarGrafos, botonParaOrdenarTopologicamente, botonParaEliminarAristas;
    private JTextArea areaDeTextoParaElTopologico;
    private JTable tablaParaLaMatrizDeAdyacencia, tablaParaLaListaDeAdyacencia;
    private int verticesMaximos;
    private int ContadorVertices = 0;
    private int[][] matrizDeAdyacencia;
    private ArrayList<ArrayList<Integer>> listaDeAdyacencia;
    private ArrayList<String> listaDeVertices;
    private ArrayList<String> nuevaListaDeVertices;
    private int nuevoMaximoDeVertices;
    private ArrayList<Point> listaVertices;
    private ArrayList<int[]> listaAristas;
    private GrafoDirigidoAciclico grafoAciclico;


    /*
        Metodo main del programa.
     */
    public static void main(String[] args) {
        //Preguntamos primero si deseamos generar los 4 numeros aleatorios o si deseamos solo ingresar los numeros de la lista Vertices
        SwingUtilities.invokeLater(() -> {
            int opcion = JOptionPane.showOptionDialog(null, "¿Quieres usar números aleatorios o un valor personalizado?",
                    "Seleccionar opción", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new Object[] {"Aleatorios", "Personalizado"}, "Aleatorios");

            GrafoDirigidoAciclicoGUI grafoGUI; //declaramos de una vez a la gui de Grafo
            if (opcion == 0) { //si la opción es Aleatoria
                grafoGUI = new GrafoDirigidoAciclicoGUI(); //directamente creamos con los 4 valores aleatorios
            } else { //de lo contrario
                String valorStr = JOptionPane.showInputDialog("Introduce un valor:");
                try {
                    //Preguntamos si creamos los valores aleatorios o si el usuario los insertará
                    int opc = JOptionPane.showOptionDialog(null, "Seleccione una opción:",
                            "Tipo de grafos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, new Object[]{"Numericos", "Letras", "Personalizado"}, "Numericos");
                    int valor = Integer.parseInt(valorStr);
                    if (opc == 0) { //en caso de ser numéricos, se inicia directamente
                        grafoGUI = new GrafoDirigidoAciclicoGUI(valor, opc);
                    }else if(opc==1){ //si es por medio de letras
                        if(valor>26){ //se compara para saber si el valor es mayor a 26
                            valor=4; //para saber si generar una interfaz diferente
                            JOptionPane.showMessageDialog(null, "Valor no válido. Se usarán valores aleatorios.");
                            grafoGUI = new GrafoDirigidoAciclicoGUI();
                        }else{//o si inicializamos la gui como corresponde
                            grafoGUI = new GrafoDirigidoAciclicoGUI(valor, opc);
                        }
                    }else{ //en caso de que sea personalizado, solo se le habla a la gui normal
                        grafoGUI = new GrafoDirigidoAciclicoGUI(valor);
                    }
                } catch (NumberFormatException e) { //en caso de error, solo iniciamos el constructor con 4 valores aleatorios
                    JOptionPane.showMessageDialog(null, "Valor no válido. Se usarán valores aleatorios.");
                    grafoGUI = new GrafoDirigidoAciclicoGUI();
                }
            }
            grafoGUI.setSize(800, 600);
            grafoGUI.setVisible(true);
        });


    }


    //Constructor extra necesario para generar una nueva GUI a la hora de cargar un grafo
    /*
        Es necesario ingresar:
            verticesMaximos, matrizDeAdyacencia, listaDeVertices, aristas
        Regresa:
            -
     */
    public GrafoDirigidoAciclicoGUI(int verticesMaximos, int[][]matrizDeAdyacencia,ArrayList<String> listaDeVertices,ArrayList<int[]>listaAristas) {
        this.verticesMaximos = verticesMaximos;
        this.matrizDeAdyacencia = matrizDeAdyacencia;
        this.listaDeAdyacencia = new ArrayList<>();
        this.listaDeVertices = listaDeVertices;
        this.listaVertices = new ArrayList<>();
        this.listaAristas = listaAristas;

        for (int i = 0; i < verticesMaximos; i++) {
            listaDeAdyacencia.add(new ArrayList<>());
        }

        setTitle("Interfaz para un Grafo Dirigido Acíclico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        panelDeControl = new JPanel(new FlowLayout());
        botonParaCrearVertices = new JButton("Crear Vértice");
        botonParaCrearAristas = new JButton("Crear Arista");
        botonParaCargarGrafos = new JButton("Cargar Grafo");
        botonParaGuardarGrafos = new JButton("Guardar Grafo");
        botonParaOrdenarTopologicamente = new JButton("Orden Topológico");
        botonParaEliminarAristas = new JButton("Eliminar Todas las Aristas");

        panelDeControl.add(botonParaCrearVertices);
        panelDeControl.add(botonParaCrearAristas);
        panelDeControl.add(botonParaCargarGrafos);
        panelDeControl.add(botonParaGuardarGrafos);
        panelDeControl.add(botonParaOrdenarTopologicamente);
        panelDeControl.add(botonParaEliminarAristas);

        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control de Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelDeControl);
        add(controlContainer, BorderLayout.NORTH);

        panelDeDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        panelDeDibujo.setBackground(Color.WHITE);

        JPanel dibujoContainer = new JPanel(new BorderLayout());
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Visualización del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDeDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        panelFormasDeMostrar = new JPanel(new GridLayout(1, 3));
        tablaParaLaMatrizDeAdyacencia = new JTable(verticesMaximos+1, verticesMaximos+1);
        tablaParaLaListaDeAdyacencia = new JTable(verticesMaximos, 1);
        areaDeTextoParaElTopologico = new JTextArea();
        areaDeTextoParaElTopologico.setEditable(false);
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaMatrizDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaListaDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(areaDeTextoParaElTopologico));

        JPanel representacionesContainer = new JPanel(new BorderLayout());
        representacionesContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Representaciones del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        representacionesContainer.add(panelFormasDeMostrar);
        add(representacionesContainer, BorderLayout.SOUTH);

        botonParaCrearVertices.addActionListener(e -> crearVertice());
        botonParaCrearAristas.addActionListener(e -> crearArista());
        botonParaEliminarAristas.addActionListener(e -> eliminarAristas());
        botonParaOrdenarTopologicamente.setEnabled(false);
        botonParaOrdenarTopologicamente.addActionListener(e->ordenTopologico());
        botonParaGuardarGrafos.addActionListener(e->guardarGrafo());
        botonParaCargarGrafos.addActionListener(e->cargarGrafo());

        for (int i = 0; i <=verticesMaximos; i++) {
            for(int j=0;j<=verticesMaximos;j++){
                tablaParaLaMatrizDeAdyacencia.setValueAt(0, i, j);
            }
        }
        crearVerticesCargados(verticesMaximos,listaDeVertices);
        System.out.println(verticesMaximos);
        System.out.println(listaDeVertices);
    }


    //Constructores solicitados

    /*
        Constructor que permite definir la cantidad de vértices del grafo y generar datos de forma aleatoria, donde:
        n=cantidad máxima de la lista Vertices
        opc=opción que permite saber si se eligió generar letras aleatorias o números
     */
    public GrafoDirigidoAciclicoGUI(int n,int opc) {
        //empezamos declarando los atributos
        this.verticesMaximos = n;
        this.matrizDeAdyacencia = new int[n+1][n+1];
        this.listaDeAdyacencia = new ArrayList<>();
        this.listaDeVertices = new ArrayList<>();
        this.listaVertices = new ArrayList<>();
        this.listaAristas = new ArrayList<>();

        setTitle("Interfaz para un Grafo Dirigido Acíclico"); //declaramos el título para la interfaz
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        panelDeControl = new JPanel(new FlowLayout()); //creamos un nuevo panel en el que agregar los botones
        botonParaCrearVertices = new JButton("Crear Vértice");
        botonParaCrearAristas = new JButton("Crear Arista");
        botonParaCargarGrafos = new JButton("Cargar Grafo");
        botonParaGuardarGrafos = new JButton("Guardar Grafo");
        botonParaOrdenarTopologicamente = new JButton("Orden Topológico");
        botonParaEliminarAristas = new JButton("Eliminar Todas las Aristas");
        panelDeControl.add(botonParaCrearVertices);
        panelDeControl.add(botonParaCrearAristas);
        panelDeControl.add(botonParaCargarGrafos);
        panelDeControl.add(botonParaGuardarGrafos);
        panelDeControl.add(botonParaOrdenarTopologicamente);
        panelDeControl.add(botonParaEliminarAristas);

        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelDeControl); //generamos un panel para poder visualizar los botones
        add(controlContainer, BorderLayout.NORTH);

        panelDeDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g); //dibujamos los grafos
            }
        };
        panelDeDibujo.setBackground(Color.WHITE);

        JPanel dibujoContainer = new JPanel(new BorderLayout()); //generamos un panel para poder mostrar el dibujo
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDeDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        panelFormasDeMostrar = new JPanel(new GridLayout(1, 3));
        tablaParaLaMatrizDeAdyacencia = new JTable(n+1, n+1);
        tablaParaLaListaDeAdyacencia = new JTable(n, 1);
        areaDeTextoParaElTopologico = new JTextArea();
        areaDeTextoParaElTopologico.setEditable(false);
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaMatrizDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaListaDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(areaDeTextoParaElTopologico));

        JPanel representacionesContainer = new JPanel(new BorderLayout()); //agregamos las representaciones de cada grafo como su tabla y la lista
        representacionesContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Grafo representados", TitledBorder.CENTER, TitledBorder.TOP));
        representacionesContainer.add(panelFormasDeMostrar);
        add(representacionesContainer, BorderLayout.SOUTH);

        //"unimos" los metodos con los botones
        botonParaCrearVertices.addActionListener(e -> crearVertice());
        botonParaCrearAristas.addActionListener(e -> crearArista());
        botonParaEliminarAristas.addActionListener(e -> eliminarAristas());
        botonParaOrdenarTopologicamente.setEnabled(false); //declaramos el boton como desactivado para evitar problemas
        botonParaOrdenarTopologicamente.addActionListener(e->ordenTopologico());
        botonParaGuardarGrafos.addActionListener(e->guardarGrafo());
        botonParaCargarGrafos.addActionListener(e->cargarGrafo());

        for (int i = 0; i <= n; i++) { //mostramos la matriz
            for(int j=0;j<=n;j++){
                tablaParaLaMatrizDeAdyacencia.setValueAt(0, i, j);
            }
        }
        if(opc==0){ //mandamos a llamar a su método correspondiente
            valoresNumericosAleatorios();
        }else if(opc==1){
            valoresLetrasAleatorios();
        }
    }


    /*
        Constructor con argumento n que define la cantidad de vertices
     */
    public GrafoDirigidoAciclicoGUI(int n) {
        //Parecido al constructor anteiror
        this.verticesMaximos = n;
        this.matrizDeAdyacencia = new int[n+1][n+1];
        this.listaDeAdyacencia = new ArrayList<>();
        this.listaDeVertices = new ArrayList<>();
        this.listaVertices = new ArrayList<>();
        this.listaAristas = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            listaDeAdyacencia.add(new ArrayList<>());
        }

        setTitle("Interfaz para un Grafo Dirigido Acíclico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        panelDeControl = new JPanel(new FlowLayout());
        botonParaCrearVertices = new JButton("Crear Vértice");
        botonParaCrearAristas = new JButton("Crear Arista");
        botonParaCargarGrafos = new JButton("Cargar Grafo");
        botonParaGuardarGrafos = new JButton("Guardar Grafo");
        botonParaOrdenarTopologicamente = new JButton("Orden Topológico");
        botonParaEliminarAristas = new JButton("Eliminar Todas las Aristas");

        panelDeControl.add(botonParaCrearVertices);
        panelDeControl.add(botonParaCrearAristas);
        panelDeControl.add(botonParaCargarGrafos);
        panelDeControl.add(botonParaGuardarGrafos);
        panelDeControl.add(botonParaOrdenarTopologicamente);
        panelDeControl.add(botonParaEliminarAristas);

        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control de Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelDeControl);
        add(controlContainer, BorderLayout.NORTH);

        panelDeDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        panelDeDibujo.setBackground(Color.WHITE);

        JPanel dibujoContainer = new JPanel(new BorderLayout());
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Visualización del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDeDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        panelFormasDeMostrar = new JPanel(new GridLayout(1, 3));
        tablaParaLaMatrizDeAdyacencia = new JTable(n+1, n+1);
        tablaParaLaListaDeAdyacencia = new JTable(n, 1);
        areaDeTextoParaElTopologico = new JTextArea();
        areaDeTextoParaElTopologico.setEditable(false);
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaMatrizDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaListaDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(areaDeTextoParaElTopologico));

        JPanel representacionesContainer = new JPanel(new BorderLayout());
        representacionesContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Representaciones del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        representacionesContainer.add(panelFormasDeMostrar);
        add(representacionesContainer, BorderLayout.SOUTH);

        botonParaCrearVertices.addActionListener(e -> crearVertice());
        botonParaCrearAristas.addActionListener(e -> crearArista());
        botonParaEliminarAristas.addActionListener(e -> eliminarAristas());
        botonParaOrdenarTopologicamente.setEnabled(false);
        botonParaOrdenarTopologicamente.addActionListener(e->ordenTopologico());
        botonParaGuardarGrafos.addActionListener(e->guardarGrafo());
        botonParaCargarGrafos.addActionListener(e->cargarGrafo());
        for (int i = 0; i <=n; i++) {
            for(int j=0;j<=n;j++){
                tablaParaLaMatrizDeAdyacencia.setValueAt(0, i, j);
            }
        }
    }


    /*
        Constructor vacío que genera 4 vertices con valores aleatorios numéricos
     */
    public GrafoDirigidoAciclicoGUI() {
        //parecido al metodo anterior
        this.verticesMaximos = 4;
        this.matrizDeAdyacencia = new int[verticesMaximos+1][verticesMaximos+1];
        this.listaDeAdyacencia = new ArrayList<>();
        this.listaDeVertices = new ArrayList<>();
        this.listaVertices = new ArrayList<>();
        this.listaAristas = new ArrayList<>();

        for (int i = 0; i < verticesMaximos; i++) {
            listaDeAdyacencia.add(new ArrayList<>());
        }

        setTitle("Interfaz para un Grafo Dirigido Acíclico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        panelDeControl = new JPanel(new FlowLayout());
        botonParaCrearVertices = new JButton("Crear Vértice");
        botonParaCrearAristas = new JButton("Crear Arista");
        botonParaCargarGrafos = new JButton("Cargar Grafo");
        botonParaGuardarGrafos = new JButton("Guardar Grafo");
        botonParaOrdenarTopologicamente = new JButton("Orden Topológico");
        botonParaEliminarAristas = new JButton("Eliminar Todas las Aristas");

        panelDeControl.add(botonParaCrearVertices);
        panelDeControl.add(botonParaCrearAristas);
        panelDeControl.add(botonParaCargarGrafos);
        panelDeControl.add(botonParaGuardarGrafos);
        panelDeControl.add(botonParaOrdenarTopologicamente);
        panelDeControl.add(botonParaEliminarAristas);

        JPanel controlContainer = new JPanel(new BorderLayout());
        controlContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Control de Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        controlContainer.add(panelDeControl);
        add(controlContainer, BorderLayout.NORTH);

        panelDeDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        panelDeDibujo.setBackground(Color.WHITE);

        JPanel dibujoContainer = new JPanel(new BorderLayout());
        dibujoContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Visualización del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        dibujoContainer.add(panelDeDibujo);
        add(dibujoContainer, BorderLayout.CENTER);

        panelFormasDeMostrar = new JPanel(new GridLayout(1, 3));
        tablaParaLaMatrizDeAdyacencia = new JTable(verticesMaximos+1, verticesMaximos+1);
        tablaParaLaListaDeAdyacencia = new JTable(verticesMaximos, 1);
        areaDeTextoParaElTopologico = new JTextArea();
        areaDeTextoParaElTopologico.setEditable(false);
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaMatrizDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(tablaParaLaListaDeAdyacencia));
        panelFormasDeMostrar.add(new JScrollPane(areaDeTextoParaElTopologico));


        JPanel representacionesContainer = new JPanel(new BorderLayout());
        representacionesContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Representaciones del Grafo", TitledBorder.CENTER, TitledBorder.TOP));
        representacionesContainer.add(panelFormasDeMostrar);
        add(representacionesContainer, BorderLayout.SOUTH);

        botonParaCrearVertices.addActionListener(e -> crearVertice());
        botonParaCrearAristas.addActionListener(e -> crearArista());
        botonParaEliminarAristas.addActionListener(e -> eliminarAristas());
        botonParaOrdenarTopologicamente.setEnabled(false);
        botonParaOrdenarTopologicamente.addActionListener(e->ordenTopologico());
        botonParaGuardarGrafos.addActionListener(e->guardarGrafo());
        botonParaCargarGrafos.addActionListener(e->cargarGrafo());

        for (int i = 0; i <= verticesMaximos; i++) {
            for(int j=0;j<=verticesMaximos;j++){
                tablaParaLaMatrizDeAdyacencia.setValueAt(0, i, j);
            }
        }
        crearVerticesAleatorios();
    }

    /*
        Metodo para mostrar en la GUI el orden topológico
     */
    private void ordenTopologico(){
        areaDeTextoParaElTopologico.setText("Orden topológico: \n"+topologicalSort());
        System.out.println(mostrarEstructura());
    }


    /*
        Método para generar vertices siempre y cuando no se haya excedido el tope de los mismos en el programa como tambien
        es el encargado de añadir los vertices tanto a lista de adyacencia como a la matriz de adyacencia.
     */
    private void crearVertice() {
        if (ContadorVertices >= verticesMaximos) {
            JOptionPane.showMessageDialog(this, "No se pueden agregar más vértices. Límite alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreVertice = JOptionPane.showInputDialog(this, "Ingrese el nombre del vértice:");

        if (nombreVertice == null || nombreVertice.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (listaDeVertices.contains(nombreVertice)) {
            JOptionPane.showMessageDialog(this, "Nombre de vértice ya ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        listaDeVertices.add(nombreVertice);
        listaDeAdyacencia.add(new ArrayList<>());
        tablaParaLaListaDeAdyacencia.setValueAt(nombreVertice, ContadorVertices, 0);

        for (int i = 0; i < verticesMaximos; i++) { //generamos la matriz de adyacencia
            matrizDeAdyacencia[ContadorVertices][i] = 0;
            matrizDeAdyacencia[i][ContadorVertices] = 0;
        }

        tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, ContadorVertices+1, 0); //guardamos en nuestra tabla el nombre del vertice
        tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, 0, ContadorVertices+1);
        agregarVerticeGrafico();//agregamos el vertice al metodo grafico
        ContadorVertices++;
        repaint();
    }


    //Encargado de pintar el vertice en la parte gráfica de la GUI
    private void agregarVerticeGrafico() {
        int centroX = panelDeDibujo.getWidth() / 2;
        int centroY = panelDeDibujo.getHeight() / 2;

        //calculamos la dirección para mostrar los  de forma gráfica
        double angulo = (ContadorVertices * 2 * Math.PI) / verticesMaximos;
        int distancia = 200;
        int x = (int) (centroX + distancia * Math.cos(angulo));
        int y = (int) (centroY + distancia * Math.sin(angulo));
        listaVertices.add(new Point(x, y));
    }

    /*
        Método encargado de crear una arista entre 2 vertices primero comprobando si dicha arista no generara
        algun ciclo en el programa, para luego ademas de añadir la arista modifica tambien tanto la matriz como la lista
        de adyacencia
     */
    private void crearArista() {
        String origen = JOptionPane.showInputDialog(this, "Ingrese el vértice origen:");
        String destino = JOptionPane.showInputDialog(this, "Ingrese el vértice destino:");

        if (origen == null || destino == null || !listaDeVertices.contains(origen) || !listaDeVertices.contains(destino)) {
            JOptionPane.showMessageDialog(this, "Vértices inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (origen.equals(destino)) {
            JOptionPane.showMessageDialog(this, "No se pueden crear aristas que conecten un vértice consigo mismo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int iOrigen = listaDeVertices.indexOf(origen);
        int iDestino = listaDeVertices.indexOf(destino);

        if (matrizDeAdyacencia[iOrigen][iDestino] == 1) {
            JOptionPane.showMessageDialog(this, "La arista ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (verificarCiclo(iOrigen, iDestino)) {
            JOptionPane.showMessageDialog(this, "La creación de un ciclo no está permitida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        matrizDeAdyacencia[iOrigen][iDestino] = 1; //guardamos un uno para la matriz
        listaAristas.add(new int[]{iOrigen, iDestino}); //agregamos una arista nueva

        ArrayList<Integer> adyacentes = listaDeAdyacencia.get(iOrigen); //tomamos el valor de origen
        if (!adyacentes.contains(iDestino)) { //en caso de que no se tenga ya el valor hacia el destino
            adyacentes.add(iDestino); //se almacena
            botonParaOrdenarTopologicamente.setEnabled(true); //se activa el orden topologico ya que se tiene una arista
        }

        tablaParaLaMatrizDeAdyacencia.setValueAt("1", iOrigen+1, iDestino +1);

        StringBuilder listaAdyacente = new StringBuilder();
        listaAdyacente.append(origen);

        ArrayList<Integer> lista = listaDeAdyacencia.get(iOrigen);
        for (int i = 0; i < lista.size(); i++) { //generamos la lista de adyacencia
            int verticeDestino = lista.get(i);
            listaAdyacente.append(" -> ");
            listaAdyacente.append(listaDeVertices.get(verticeDestino));
        }
        listaAdyacente.append("/0");

        tablaParaLaListaDeAdyacencia.setValueAt(listaAdyacente.toString(), iOrigen, 0);

        int x1 = listaVertices.get(iOrigen).x; int y1 = listaVertices.get(iOrigen).y;
        int x2 = listaVertices.get(iDestino).x; int y2 = listaVertices.get(iDestino).y;

        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x1, y1, x2, y2);

        repaint();
    }
    //Manda a llamar a un método responsable de verificar si ocurre un ciclo en el programa
    private boolean verificarCiclo(int origen, int destino) {
        boolean[] visitados = new boolean[listaDeVertices.size()];
        return hayCiclo(destino, origen, visitados);
    }
    //Método encargado de revisar si ocurre algun ciclo en el programa.
    private boolean hayCiclo(int vertice, int destino, boolean[] visitados) {
        if (visitados[vertice]) {
            return true;
        }
        visitados[vertice] = true;
        for (int vecino : listaDeAdyacencia.get(vertice)) {
            if (vecino == destino || hayCiclo(vecino, destino, visitados)) {
                return true;
            }
        }
        return false;
    }
    //Método encargado de borrar todas las aristas junto a las flechas dibujadas en el programa
    private void eliminarAristas() {
        listaAristas.clear(); //limpiamos las aristas

        for (int i = 0; i < verticesMaximos; i++) { //reiniciamos las vertices
            for (int j = 0; j < verticesMaximos; j++) {
                matrizDeAdyacencia[i][j] = 0;
            }
        }

        listaDeAdyacencia.clear();
        for (int i = 0; i < listaDeVertices.size(); i++) { //reiniciamos la lista de adyacencia
            listaDeAdyacencia.add(new ArrayList<>());
        }

        for (int i = 0; i < listaDeVertices.size(); i++) { //actualizamos las tablas
            tablaParaLaMatrizDeAdyacencia.setValueAt(listaDeVertices.get(i), i + 1, 0);
            tablaParaLaMatrizDeAdyacencia.setValueAt(listaDeVertices.get(i), 0, i + 1);
            tablaParaLaListaDeAdyacencia.setValueAt(listaDeVertices.get(i), i, 0);
            for (int j = 0; j < listaDeVertices.size(); j++) {
                tablaParaLaMatrizDeAdyacencia.setValueAt(0, i + 1, j + 1);
            }
        }
        botonParaOrdenarTopologicamente.setEnabled(false); //desactivamos el boton de orden topologico
        areaDeTextoParaElTopologico.setText("");
        repaint();
    }

    //Método encargado de dibujar los grafos de la GUI
    private void dibujarGrafo(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int[] arista : listaAristas) {
            Point origen = listaVertices.get(arista[0]);
            Point destino = listaVertices.get(arista[1]);
            System.out.println(arista[0]);
            System.out.println(arista[1]);
            dibujarFlecha(g2d, origen, destino);
        }

        for (int i = 0; i < listaVertices.size(); i++) {
            Point vertice = listaVertices.get(i);

            g2d.setColor(new Color(173, 216, 230));
            g2d.fillOval(vertice.x - 40 / 2, vertice.y - 40 / 2, 40, 40);

            g2d.setColor(Color.BLACK);
            FontMetrics metrics = g2d.getFontMetrics();
            String texto = listaDeVertices.get(i);

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

        double dx = destino.x - origen.x; double dy = destino.y - origen.y;
        double angle = Math.atan2(dy, dx);

        int base = 10; int altitud = 20; int desplazamiento = 15;

        int nuevoX = (int) (destino.x - desplazamiento * Math.cos(angle));
        int nuevoY = (int) (destino.y - desplazamiento * Math.sin(angle));

        int x1 = (int) (nuevoX - altitud * Math.cos(angle));
        int y1 = (int) (nuevoY - altitud * Math.sin(angle));
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

        int width = panelDeDibujo.getWidth();
        int height = panelDeDibujo.getHeight();

        if (width <= 0 || height <= 0) {
            width = 500;
            height = 500;
        }

        int distanciaX = width / 3;
        int distanciaY = height / 3;

        for (int i = 0; i < 4; i++) {
            String nombreVertice = String.valueOf(random.nextInt(100));
            listaDeVertices.add(nombreVertice);
            listaDeAdyacencia.add(new ArrayList<>());

            int x = (i % 2) * distanciaX + distanciaX / 2;
            int y = (i / 2) * distanciaY + distanciaY / 2;

            listaVertices.add(new Point(x, y));

            ContadorVertices++;

            tablaParaLaListaDeAdyacencia.setValueAt(nombreVertice, i, 0);

            for (int j = 0; j < verticesMaximos; j++) {
                matrizDeAdyacencia[i][j] = 0;
                matrizDeAdyacencia[j][i] = 0;
            }

            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, i + 1, 0);
            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, 0, i + 1);
        }

        repaint();
    }


    /*
        Permiten crear los valores aleatorios en base a la cantidad de vertices
        que se tienen
     */
    public void valoresNumericosAleatorios() {
        Random random = new Random(); //generamos un numero random

        int anchura = panelDeDibujo.getWidth();
        int altitud = panelDeDibujo.getHeight();

        if (anchura <= 0 || altitud <= 0) {
            anchura = 500; altitud = 500;
        }
        //calculamos la mitad
        int mitadEnX = anchura / 2;
        int mitadEnY = altitud / 2;

        int distancia = 200;

        for (int i = 0; i < verticesMaximos; i++) { //aumentamos i mientras sea menor a la cantidad de vertices
            double angulo = (i * 2 * Math.PI) / verticesMaximos; //calculamos el ángulo
            int x = (int) (mitadEnX + distancia * Math.cos(angulo));
            int y = (int) (mitadEnY + distancia * Math.sin(angulo));

            String nombreVertice = String.valueOf(random.nextInt(100)); //generamos un número aleatorio
            listaDeVertices.add(nombreVertice); //lo guardamos en la lista de nombres
            listaDeAdyacencia.add(new ArrayList<>());

            listaVertices.add(new Point(x, y));

            ContadorVertices++;

            tablaParaLaListaDeAdyacencia.setValueAt(nombreVertice, i, 0);
            for (int j = 0; j < verticesMaximos; j++) { //generamos la matriz de adyacencia
                matrizDeAdyacencia[i][j] = 0;
                matrizDeAdyacencia[j][i] = 0;
            }
            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, i + 1, 0);
            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, 0, i + 1);
        }
        repaint();
    }

    public void valoresLetrasAleatorios() {
        String[] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z"}; //Guardamos las letras del abecedario en una matriz
        Random random = new Random();

        //aplicando una logica parecida a la anterior
        int anchura = panelDeDibujo.getWidth();
        int altitud = panelDeDibujo.getHeight();

        if (anchura <= 0 || altitud <= 0) {
            anchura = 500; altitud = 500;
        }
        int mitadEnX = anchura / 2;
        int mitadEnY = altitud / 2;

        int distancia = 200;

        for (int i = 0; i < verticesMaximos;) {
            double angulo = (i * 2 * Math.PI) / verticesMaximos;
            int x = (int) (mitadEnX + distancia * Math.cos(angulo));
            int y = (int) (mitadEnY + distancia * Math.sin(angulo));

            int letra = random.nextInt(26);
            String letraGenerada = abecedario[letra]; //tomamos una letra de la matriz
            if (!listaDeVertices.contains(letraGenerada)) { //verificamos si la letra ya estaba generada
                listaDeVertices.add(letraGenerada); //guardamos y mostramos solo si no se tiene ya
                listaDeAdyacencia.add(new ArrayList<>());
                listaVertices.add(new Point(x, y));
                ContadorVertices++;

                tablaParaLaListaDeAdyacencia.setValueAt(letraGenerada, i, 0);

                for (int j = 0; j < verticesMaximos; j++) {
                    matrizDeAdyacencia[i][j] = 0;
                    matrizDeAdyacencia[j][i] = 0;
                }

                tablaParaLaMatrizDeAdyacencia.setValueAt(letraGenerada, i + 1, 0);
                tablaParaLaMatrizDeAdyacencia.setValueAt(letraGenerada, 0, i + 1);

                i++; //aumentamos i
            }
        }
        repaint();
    }


    /*
    METODOS DE ACCESO
     */

    public int gradoDeEntrada(int i){
        if (i < 0 || i >= verticesMaximos) {
            throw new IllegalArgumentException("Índice fuera de rango."); //lanzamos mensaje de excepción si el indice está fuera de rango
        }
        int gradoEntrada = 0;
        for (int u = 0; u < verticesMaximos; u++) {
            if (matrizDeAdyacencia[u][i] == 1) { //si se tiene un uno en la matriz
                gradoEntrada++; //se aumenta el grado
            }
        }
        return gradoEntrada;
    }

    public int gradoDeSalida(int i){
        if (i < 0 || i >= verticesMaximos) {
            throw new IllegalArgumentException("Índice fuera de rango.");//lanzamos mensaje de excepción si el indice está fuera de rango
        }
        int gradoSalida= 0;
        for (int u = 0; u < verticesMaximos; u++) {
            if (matrizDeAdyacencia[i][u] == 1) { //si se tiene un uno en la matriz
                gradoSalida++; //se aumenta el grado
            }
        }
        return gradoSalida;
    }

    public int cuantasAristasHay(){
        int numAristas = 0;
        for (int i = 0; i < verticesMaximos; i++) {
            for (int j = 0; j < verticesMaximos; j++) {
                if (matrizDeAdyacencia[i][j] == 1) { //si se tiene un uno en la matriz
                    numAristas++; //se aumenta la cantidad de aristas
                }
            }
        }
        return numAristas;
    }

    public boolean adyacente(int i,int j){
        if(i<0||j<0||i>=verticesMaximos||j>=verticesMaximos){
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        return matrizDeAdyacencia[i][j] == 1;
    }


    public boolean conectados(int i,int j){
        if(i<0||j<0||i>=verticesMaximos||j>=verticesMaximos){
            throw new IllegalArgumentException("Índice fuera de rango.");
        }
        if(i==j){
            return false;
        }
        Queue<Integer> cola=new LinkedList<>();
        boolean[] valores=new boolean[verticesMaximos];
        cola.add(i);
        valores[i]=true;

        while(!cola.isEmpty()){
            int actual=cola.poll();//elimina y regresa el elemento superior
            for(int x=0;x<verticesMaximos;x++){
                //solo si el valor fue encontrado pero no había sido visitado
                if(matrizDeAdyacencia[actual][x]!=0 && valores[x]==false){
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
        int[] gradoEntrada = new int[verticesMaximos];

        for (int i = 0; i < verticesMaximos; i++) {
            gradoEntrada[i] = gradoDeEntrada(i);
        }

        PriorityQueue<Integer> colaPrioridad = new PriorityQueue<>(Comparator.reverseOrder());

        for (int i = 0; i < verticesMaximos; i++) {
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
            ordenTopologico += listaDeVertices.get(vertice);

            for (int i = 0; i < verticesMaximos; i++) {
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
        for(int i = 0; i < verticesMaximos; i++){
            texto+=" "+listaDeVertices.get(i);
        }
        texto+="\n";

        for(int i = 0; i < verticesMaximos; i++){
            texto+=listaDeVertices.get(i)+" ";
            for(int j = 0; j < verticesMaximos; j++){
                texto+=" "+matrizDeAdyacencia[i][j];
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
            metodoPrueba();
            return;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo,true))) {
            String horaYFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            escritor.write("Fecha y hora de creación: " + horaYFecha + "\n");
            escritor.write("Maximo de vertices: " + this.verticesMaximos + "\n");

            escritor.write("Matriz de Adyacencia:\n");
            for (int i = 0; i < this.verticesMaximos; i++) {
                for (int j = 0; j < this.verticesMaximos; j++) {
                    escritor.write(this.matrizDeAdyacencia[i][j] + " ");
                }
                escritor.write("\n");
            }
            escritor.write("Lista de Vértices: \n");
            for (String vertice : this.listaDeVertices) {
                escritor.write(vertice + "\n");
            }

            escritor.write("Aristas: \n");
            for (int[] arista : this.listaAristas) {
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
                    for (int i = 0; i < this.verticesMaximos; i++) {
                        linea = lector.readLine();
                        String[] valores = linea.trim().split(" ");
                        for (int j = 0; j < this.verticesMaximos; j++) {
                            if (Integer.parseInt(valores[j]) != this.matrizDeAdyacencia[i][j]) {
                                return false;
                            }
                        }
                    }
                    for (String vertice : this.listaDeVertices) {
                        linea = lector.readLine();
                        if (!linea.equals(vertice)) {
                            return false;
                        }
                    }
                    for (int[] arista : this.listaAristas) {
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
            JOptionPane.showMessageDialog(null, "No hay grafos guardados para mostrar.", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        grafoAciclico= new GrafoDirigidoAciclico(verticesMaximos, 1);
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
                    if (linea.startsWith("Maximo de vertices: ")) {
                        nuevoMaxVertices = Integer.parseInt(linea.replace("Maximo de vertices: ", "").trim());
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
            this.nuevoMaximoDeVertices=nuevoMaximoDeVertices;
            this.nuevaListaDeVertices =nuevosNombresVertices;
            Auxiliar nuevaVentana = new Auxiliar(nuevoMaxVertices, nuevaMatrizAdyacencia, nuevosNombresVertices, nuevasAristas);
            System.out.println("Nuevo Maximo de vertices: " + nuevoMaxVertices);
            System.out.println("Nombres de Vértices: " + nuevosNombresVertices);

            dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el grafo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> listarGrafos() {
        List<String> grafos = new ArrayList<>();
        File archivo = new File("grafosGuardados.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(null, "No se encontraron grafos en el archivo", "Error", JOptionPane.INFORMATION_MESSAGE);
            return grafos;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String horaYFecha = null;
            int grafoId = 1;

            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith("Fecha y hora de creación: ")) {
                    horaYFecha = linea.replace("Fecha y hora de creación: ", "").trim();
                }

                if (linea.startsWith("Maximo de vertices: ")) {
                    grafos.add("Grafo " + grafoId + " / Fecha: " + horaYFecha);
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
            JOptionPane.showMessageDialog(null, "No hay datos para los vértices.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int width = panelDeDibujo.getWidth();
        int height = panelDeDibujo.getHeight();

        if (width <= 0 || height <= 0) {
            width = 500;
            height = 500;
        }
        int centroX = width / 2;
        int centroY = height / 2;

        int distancia = 200;

        for (int i = 0; i < verticesMaximos; i++) {
            double angulo = (i * 2 * Math.PI) / verticesMaximos;
            int x = (int) (centroX + distancia * Math.cos(angulo));
            int y = (int) (centroY + distancia * Math.sin(angulo));

            String nombreVertice = nuevosNombresVertices.get(i);
            System.out.println(nombreVertice);
            listaDeVertices.add(nombreVertice);
            listaDeAdyacencia.add(new ArrayList<>());


            listaVertices.add(new Point(x, y));

            ContadorVertices++;

            tablaParaLaListaDeAdyacencia.setValueAt(nombreVertice, i, 0);

            for (int j = 0; j < nuevoMaxVertices; j++) {
                matrizDeAdyacencia[i][j] = 0;
                matrizDeAdyacencia[j][i] = 0;
            }

            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, i + 1, 0);
            tablaParaLaMatrizDeAdyacencia.setValueAt(nombreVertice, 0, i + 1);
        }

        repaint();
    }

    public void metodoPrueba() {
        GrafoDirigidoAciclico grafo = new GrafoDirigidoAciclico(4, 1);
        grafo.insertarArista(0, 1);
        grafo.insertarArista(1, 2);
        grafo.insertarArista(2, 4);
        System.out.println(grafo.gradoDeEntrada(2));
        System.out.println(grafo.gradoDeSalida(1));
        System.out.println(grafo.cuantasAristasHay());
        System.out.println(grafo.adyacente(1, 2));
        System.out.println(grafo.conectados(0, 3));
        System.out.println(grafo.tieneCiclos());
        System.out.println(grafo.topologicalSort());
        grafo.eliminarAristas();
        System.out.println(grafo.mostrarEsructura());
    }

}

