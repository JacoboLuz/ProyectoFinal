import java.util.ArrayList;
import java.util.Random;
public class GrafoDirigidoAciclico{
    int cantidadVertices=0;
    Random random=new Random();
    private ArrayList<String> nombresVertices=new ArrayList<>();

    /*
    Constructor que recibe argumento n que define la cantidad de vertices
    del grafo, donde
    n=vertices
    opc= define si se asigna de 0 a n-1 o de forma aleatoria
     */
    public GrafoDirigidoAciclico(int n, int opc){
        cantidadVertices=n;
        if(opc==1){ //0 a n-1
            int i=0;
            do{
                nombresVertices.add(String.valueOf(i));
                i++;
            }while(nombresVertices.size()<cantidadVertices);
        }else if(opc==2){ //numeros aleatorios
            valoresNumericosAleatorios();
        }else{ //letras aleatorias
            if(cantidadVertices<=26) {
                valoresLetrasAleatorios();
            }else{
                //no permitir acción
            }
        }

    }

    //Constructor que recibe solamente n
    public GrafoDirigidoAciclico(int n){
        cantidadVertices=n;
    }


    //constructor vacío que crea un grafo de 4 vertices con números aleatorios
    public GrafoDirigidoAciclico() {
        cantidadVertices=4;
        valoresNumericosAleatorios();
    }


    /*
    Permiten crear los valores aleatorios en base a la cantidad de vertices
    que se tienen
     */
    public void valoresNumericosAleatorios() {
        Random random = new Random();
        while (nombresVertices.size() < cantidadVertices) {
            int num = random.nextInt(cantidadVertices);
            String n = String.valueOf(num);
            if (!nombresVertices.contains(n)) {
                nombresVertices.add(n);
                System.out.println(n);
            }
        }
    }

    public void valoresLetrasAleatorios() {
        String[] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        while (nombresVertices.size() < cantidadVertices) {
            int letra = random.nextInt(26);
            String letraGenerada = abecedario[letra];
            if (!nombresVertices.contains(letraGenerada)) {
                nombresVertices.add(letraGenerada);
                System.out.println(letraGenerada);
            }
        }
    }


    /*
    METODOS DE ACCESO
     */

    public int gradoDeEntrada(int i){
        return 0;
    }

    public int gradoDeSalida(int i){
        return 0;
    }
    public int cuantasAristasHay(){
        return 0;
    }
    public boolean adyacente(int i,int j){
        return false;
    }
    public boolean conectados(int i,int j){
        return false;
    }
    public String topologicalSort(){
        return "";
    }
    public boolean tieneCiclos(){
        return false;
    }
    public String mostrarEsructura(){
        return "";
    }


    /*
    METODOS PARA ESTABLECER VALORES
     */

    public boolean insertarArista(int i,int j){
        return false;
    }
    public void eliminarAristas(){

    }







}
