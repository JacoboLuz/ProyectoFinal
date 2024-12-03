/*
Proyecto Final Algoritmos
Jacobo Aldrete Luz María
Prieto Duarte Luis Fernando
 */
import javax.swing.*;
import java.util.ArrayList;

public class Auxiliar {
    public Auxiliar(int maximoDeVertices, int[][] matrizDeAdyacencia, ArrayList<String> listaDeLosVertices, ArrayList<int[]> listaDeLasAristas) {
        GrafoDirigidoAciclicoGUI grafoNuevo = new GrafoDirigidoAciclicoGUI( maximoDeVertices, matrizDeAdyacencia, listaDeLosVertices, listaDeLasAristas);
        JOptionPane.showMessageDialog(null, "Se a creado una GUI Auxiliar con exito");
        grafoNuevo.setVisible(true);
    }
}
