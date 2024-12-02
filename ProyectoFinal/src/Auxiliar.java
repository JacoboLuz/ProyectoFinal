import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Auxiliar {
    public Auxiliar(int maxVertices, int[][] matrizAdyacencia, ArrayList<String> nombresVertices, ArrayList<int[]> aristas) {
        GrafoDirigidoAciclicoGUI grafo = new GrafoDirigidoAciclicoGUI(maxVertices,matrizAdyacencia,nombresVertices,aristas);
        JOptionPane.showMessageDialog(null, "Se ha cargado con exito.");
        grafo.setVisible(true);
    }
}
