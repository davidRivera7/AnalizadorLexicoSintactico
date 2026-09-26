/** Gramatica.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Autómatas I
 * -------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 * -------------------------------------------------
 *  La clase Gramática  está diseñada para cargar, 
 *  analizar y manipular las reglas de una gramática 
 *  definida en un archivo de texto
 *
 *  Esta clase automatiza la extracción de la 
 *  gramática dada, así como de símbolos terminales 
 *  y no terminales, y la separación de la parte 
 *  derecha de las reglas gramaticales.
 */
package gramatica;

import java.io.IOException;

import analizadorLexico.LecturaArchivo;

public class Gramatica {

    private String[] todaGramática, 
                     ladoDerecho, 
                     terminales, 
                     noTerminales; 
    private String   símboloInicial;                                         

    /**
     * Constructor que carga y procesa la gramática desde un archivo.
     *
     * @param rutaArchivo La ruta al archivo de texto que contiene la gramática.
     * @throws IOException Si ocurre un error durante la lectura del archivo.
     */
    public Gramatica(String rutaArchivo) throws IOException {
        todaGramática = new String[generarTamaño(rutaArchivo)];                         
        ladoDerecho = new String[todaGramática.length];
//        System.out.println("Lectura y procesamiento de la gramatica");
        lecturaYprocesaGramática(rutaArchivo);
    }

    private void lecturaYprocesaGramática(String rutaArchivo) throws IOException{
        // Lectura y procesamiento de la gramática desde el archivo.        
        leerArchivo(rutaArchivo);                                               
        generarLadoDerecho(todaGramática);                                      
        generarNoTerminales(todaGramática);                                     
        generarTerminales(ladoDerecho);                                        
        símboloInicial = obtenerSímboloInicial();                   
        // System.out.println("Contenido de estructuras:");
        // System.out.println("Símbolos Terminales:");        
        // for (String t : terminales) 
        //     System.out.println(t);                        
        
        // System.out.println("\nSímbolos No Terminales:");
        // for (String nT : noTerminales) 
        //     System.out.println(nT);
        
        // System.out.println("\nLados Derechos:");
        // for (String lD : ladoDerecho) 
        //     System.out.println(lD);                        
    }

    /**
     * Calcula y devuelve el número de líneas en el archivo de gramática.
     * Se utiliza para determinar el tamaño de los arreglos que almacenarán
     * las producciones gramaticales.
     */
    public int generarTamaño(String rutaArchivo) throws IOException {
        LecturaArchivo archivo = new LecturaArchivo(rutaArchivo);               
        String línea;                                                           
        int tamaño = 0;    
        
        // - Escribir el código necesario para el cálculo requerido
        línea = archivo.leerLinea();
        while (línea != null) {
            tamaño++;
            línea = archivo.leerLinea();
        }        
        archivo.cerrarArchivo();
        
        return tamaño;                                                          
    }        

    public void leerArchivo(String rutaArchivo) throws IOException {
//        System.out.println("Abriendo archivo");
//        System.out.print("Leyendo archivo que contiene la gramatica y ");
//        System.out.println("almacenamiento de producciones en arreglo 'todaGramatica'...");
        LecturaArchivo archivo = new LecturaArchivo(rutaArchivo);               
        String línea;                                                           
        int numLínea = 0;                            
        
        while ((línea = archivo.leerLinea()) != null) {                         
//            System.out.println("Lectura linea " + (numLínea + 1) + ", contenido: " + línea);
            generarGramática(línea, numLínea);                                  
            numLínea++;                                                         
        }
        archivo.cerrarArchivo();                                                
//        System.out.println("Cerrando archivo...");
    }

    /**
     * Procesa una línea individual de texto para extraer la gramática y
     * almacenarla en un arreglo; este método se asegura de comenzar la
     * extracción desde el primer carácter alfabético, ignorando cualquier
     * carácter no alfabético al principio de la línea.
     */
    public void generarGramática(String línea, int numLínea) {
        // Escriba el código adecuado para el funcionamiento del método        
        todaGramática[numLínea] = "";
        int i = 0;
        while (i < línea.length() && !esLetra(línea.charAt(i)))//- avanzamos iterador mientras el comienzo de la produccion no sea un  carácter alfabético
            i++;           
        
        for (int j = i; j < línea.length(); j++) 
            todaGramática[numLínea] += línea.charAt(j);//- concatenamos el resto de la produccion        
        
//        System.out.println("todaGramatica[" + numLínea + "] --> " + línea);
    }        

    /**
     * Extrae el lado derecho de cada producción en la gramática y lo almacena
     * en un arreglo; este método analiza las cadenas proporcionadas en el
     * arreglo lista, buscando el carácter especial '→' que delimita el lado
     * izquierdo del lado derecho en cada regla gramatical.
     */
    public void generarLadoDerecho(String [] lista) {
//        System.out.println("\nAlmacenando en estructura 'ladoDerecho' el lado derecho de cada produccion");
        for (int i = 0; i < lista.length; i++) {                                
            int actual = lista[i].indexOf('\u2192');                                        
            if (actual != -1) { // - Corrija la condicional                                                
                ladoDerecho[i] = lista[i].substring(actual + 2);                
//                System.out.println("ladoDerecho[" + i + "] --> " + ladoDerecho[i]);
            } else                                                            
                ladoDerecho[i] = "";
        }
    }

    /**
     * Extrae y almacena todos los símbolos no terminales sin repetir de un
     * conjunto de producciones gramaticales; los símbolos no terminales son
     * identificados como las palabras antes del primer espacio en cada
     * producción.
     */
    public void generarNoTerminales(String[] lista) {
//        System.out.println("\nGenerando estructura 'noTerminales' que almacena simbolos no terminales sin repetir de las producciones" );
        String[] listaTemp = new String[lista.length];                          
        int contador = 0;                      
        
        for (int i = 0; i < lista.length; i++) {                                
            int apt = 0;                                                        
            while (lista[i].charAt(apt) != ' ' && apt < lista[i].length())
                apt++;
            String simb = lista[i].substring(0, apt);
//            System.out.println("Simbolo identificado en linea " + (i + 1) + ": " + simb);
            if (!buscarSimbolo(listaTemp, simb, contador)) {                    
                // - Complete el código para almacenar el símbolo terminal                        
                listaTemp[contador] = simb;
                contador++;    
//                System.out.println("Simbolo '" + simb + "' no se ha registrado anteriormente");
//                System.out.println("almacenando en noTerminales[" + (contador - 1) + "] --> " + simb);
            }
        }
        noTerminales = new String[contador];                                    
        System.arraycopy(listaTemp, 0, noTerminales, 0, contador);              
    }

    /**
     * Extrae y almacena todos los símbolos terminales únicos desde las partes
     * derechas de las producciones gramaticales; un símbolo terminal es
     * cualquier token que no es reconocido como no terminal y que aparece en 
     * el lado derecho de la gramática.
     */
    public void generarTerminales(String[] lista) {
//        System.out.println("\nGenerando estructura 'terminales' que almacena simbolos terminales sin repetir de las producciones");
        String[] listaTemp = new String[lista.length * 2];                      
        int contador = 0;                                                       
        
        for (String ladoDerecho : lista) { //-recorremos todos los lados derechos                                    
            int inicio = 0;                                                     
            int fin = 0;
            ladoDerecho = ladoDerecho.trim();//-obteniendo lado derecho quitando espacios antes y despues                                   
            while (fin <= ladoDerecho.length()) {//-recorriendo cada caracter ladoDerecho                               
                // Condición para determinar el final de un token o la cadena.
                if (fin == ladoDerecho.length()    || 
                    ladoDerecho.charAt(fin) == ' ' || 
                    ladoDerecho.charAt(fin) == ';' || 
                    ladoDerecho.charAt(fin) == ',' || 
                    ladoDerecho.charAt(fin) == '(' || 
                    ladoDerecho.charAt(fin) == ')') {
                    
                    if (fin < ladoDerecho.length() - 1) {                       
                        char nextChar = ladoDerecho.charAt(fin + 1);            
                        if ((ladoDerecho.charAt(fin) == '=' && nextChar == '=') 
                                || (ladoDerecho.charAt(fin) == '<' && 
                                    nextChar == '>')) {
                            fin++;                                              
                        }
                    }//avanzamos si tenemos '==' o '<>'
                    if (inicio != fin) {//--podemos tener: ladoDerecho.charAt(fin) == ' '                                        
                        String simbolo = ladoDerecho.substring(inicio, fin);//-registrando palabra encontrada    
//                        System.out.println("Identificacion de simbolo: " + simbolo);
                        if (!simbolo.isEmpty() && 
                            !simbolo.equals("ε") && 
                            !buscarSimbolo(noTerminales, simbolo, noTerminales.length) && //- si no es 'no terminal'
                            !buscarSimbolo(listaTemp, simbolo, contador)) { //- verificar que no se repitan los terminales                            
                            listaTemp[contador] = simbolo;                      
                            contador++;//numero de terminales encontrados, nos movemos una posicion adelante para guardar el siguiente terminal (si lo hay)                                         
//                            System.out.println("agregando en terminales[" + (contador - 1) + "] --> " + simbolo);
                        }
                    }
                    if (fin != ladoDerecho.length() && ladoDerecho.charAt(fin) != ' ') {//-verificar que no terminemos de procesar la linea y caracter actual no sea espacio ////--podemos tener: un caracter especial
                        String simbolo = String.valueOf(ladoDerecho.charAt(fin));//-transformando char a string                              
//                        System.out.println("Identificacion de simbolo: " + simbolo);
                        if (!simbolo.equals("ε") && 
                            !buscarSimbolo(noTerminales, simbolo, noTerminales.length) 
                            && !buscarSimbolo(listaTemp, simbolo, contador)) {
                            listaTemp[contador] = simbolo;                                                      
                            contador++;                                                                         
//                            System.out.println("agregando en terminales[" + (contador - 1) + "] --> " + simbolo);
                        }
                    }
                    fin++;                                                      
                    inicio = fin;//-reestablecemos indices para procesar nueva palabra                                               
                } else
                    fin++;//-avanzamos de manera que se va identificando una palabra completa                                                      
            }
        }
        terminales = new String[contador];                                      
        System.arraycopy(listaTemp, 0, terminales, 0, contador);                
    }

    /**
     * Busca un símbolo específico en un arreglo de cadenas hasta un índice
     * dado; este método es utilizado para verificar si un símbolo ya ha sido
     * agregado a una lista, ayudando a evitar duplicados en las listas de
     * símbolos no terminales y terminales.
     */
    public boolean buscarSimbolo(String [] lista, String símbolo, int length) {                               
        if (símbolo == null)
            return false;
        
        for (int i = 0; i < length; i++) 
            if (símbolo.equals(lista[i]))                 
                return true;        
                                            
        return false;
        // Corrija la línea anterior para asegurar el funcionamiento del método
    }

    //Imprime cada elemento de un arreglo de cadenas en la consola.
    public void imprimirLista(String[] lista) {
        // Escriba el código del método para la impresión
        for (String cadena : lista) 
            System.out.println(cadena);        
    }

    // Determina si un caracter específico es una letra del alfabeto.
    //
    public boolean esLetra(char act) {
        return (act >= 'a' && act <= 'z') || (act >= 'A' && act <= 'Z');
        // Corrija la línea anterior para asegurar el funcionamiento del método
    }

    /**
     * Identifica y devuelve el símbolo inicial de la gramática (el símbolo no
     * terminal que aparece una vez en el lado izquierdo de las producciones y
     * nunca en el lado derecho).
     */
    public String obtenerSímboloInicial() {
//        System.out.println("\nObteniendo 'simbolo inicial' con apoyo de la estructura 'noTerminales'");
        /*
            Escriba el código adecuado para determinar cuál es el símbolo 
            inicial de la gramática.
           
        */
        //-solamente verificaremos si aparece del lado derecho el 'no terminal' que aparezca una vez en el lado izquierdo.
        for (String noTerminal : noTerminales)//-recorremos todos los simbolos 'no terminales' 
            if (contarAparicionesLadoIzquierdo(noTerminal) == 1) {//- verificamos si el simbolo 'no terminal actual' existe solo una vez del lado izq de la produccion
                
//                System.out.println("Verificando si '" + noTerminal + "' aparece en lado derecho...");                
                if (!apareceEnLadoDerecho(noTerminal)) {//- verificamos que el simbolo 'no terminal actual' no exista del lado derecho de la produccion
                    símboloInicial = noTerminal;//- inicializamos variable 'símboloInicial'
//                    System.out.println("'" + noTerminal + "' NO aparece en lado derecho, por lo tanto es el SIMBOLO INICIAL");
                    return noTerminal;//- retornamos 'símboloInicial'
                }
//                System.out.println("Aparicion en lado derecho, descartando '" + noTerminal + "' como simbolo inicial");
            }
                            
        return null;//- nulo si no encontramos un simbolo 'no terminal' en la gramatica                                                            
    }

    // Verifica si un símbolo dado aparece en el lado derecho de alguna producción.
    private boolean apareceEnLadoDerecho(String simbolo) {
        for (String produccion : ladoDerecho) {//-recorriendo todos los lados derechos de la produccion                                 
                int inicio = 0;                                                 
                int fin = inicio;                                               
                while (fin <= produccion.length()) {//-verificando produccion actual
                    
                    if (fin == produccion.length() || produccion.charAt(fin) == ' ' || produccion.charAt(fin) == ';'
                            || produccion.charAt(fin) == ',' || produccion.charAt(fin) == '(' || produccion.charAt(fin) == ')') {//-verificamos posible palabra encontrada (estos caracteres practicamente son los que separan palabras en las producciones)
                        String token = produccion.substring(inicio, fin).trim();//-obtenemos palabra sin espacios antes ni despues
                        if (token.equals(simbolo)) {                            
                            return true; //-aparicion de simbolo en lado derecho                                       
                        }
                        fin++;                                                  
                        inicio = fin;//-establecemos indices para seguir buscando palabra                                           
                    } else {
                        fin++;//-se sigue juntando palabra a analizar (palabra que se obtendra con inicio y fin)
                    }
                }
        }
        return false;//- no existe 'simbolo' en el lado derecho de las producciones                                                           
    }

    /**
     * Cuenta cuántas veces un símbolo no terminal aparece en el lado izquierdo
     * de las producciones.
     */
    private int contarAparicionesLadoIzquierdo(String simbolo) {
                
        int cuenta = 0;
        for (String produccion : todaGramática) {//-recorremos cada produccion de la gramatica                              
            String ladoIzquierdo = produccion.split(" → ")[0].trim();//-con split obtenemos un arreglo de 'n + 1' elementos donde n = " → " que es la separacion que divide el string. la posicion 0 del arreglo sera el lado izquierdo de  " → " y con trim obtenemos el string sin espacios antes ni despues            
            if (ladoIzquierdo.equals(simbolo)) {                               
                cuenta++;//-contamos la aparicion de 'simbolo' que se encuantra del lado izq de la produccion                                                    
            }
        }
        /*
            Corrija el código anterior para asegurar que la cuenta
            de apariciones de un no terminal en el lado izquierdo
            de las producciones.
        */
//        System.out.println("Apariciones de '" + simbolo + "' en el lado izquierdo: " + cuenta);
        return cuenta;                                                       
    }

    // Métodos get para devolver las listas y el símbolo inicial.
    // Corrija el código que se presenta a continuación para
    // asegurar el correcto funcionamiento de los métodos de acceso.
    public String[] getTodaGramatica() {
        return todaGramática;
    }

    public String[] getLadoDerecho() {
        return ladoDerecho;
    }

    public String[] getTerminales() {
        return terminales;
    }

    public String[] getNoTerminales() {
        return noTerminales;
    }

    public String getSimboloInicial() {
        return símboloInicial;
    }
}