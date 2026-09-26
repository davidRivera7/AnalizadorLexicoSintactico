/** AnalizadorSintáctico.java
 *  Tecnológico Nacional de México en León Campus 1
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Autómatas I
 *  ------------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 *  ------------------------------------------------------
 *  Clase AnalizadorSintáctico que implementa el análisis 
 *  sintáctico de un archivo de código de prueba; utiliza
 *  una pila para gestionar los símbolos derivados de las
 *  producciones gramaticales y de la matriz predictiva para 
 *  guiar el proceso de análisis basándose en los tokens 
 *  proporcionados por el Analizador Léxico;
 * 
 *  El análisis sigue el método LL(1), que requiere una 
 *  tabla predictiva para decidir las producciones a aplicar 
 *  según el símbolo actual y el siguiente token en la entrada;
 *  Este enfoque permite validar si el código fuente cumple 
 *  con la gramática especificada identificando errores 
 *  sintácticos.
 */
package analizadorSintactico;

import java.io.IOException;
import java.util.Formatter;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.LecturaArchivo;
import analizadorLexico.Nodo;
import gramatica.Gramatica;
import stack.NodoPila;
import stack.Pila;

public class AnalizadorSintactico {
    private Pila p;
    private Gramatica g;
    private int [][] predict;
    private AnalizadorLexico anLex;
    private LecturaArchivo reader;
    private String línea;
    private int numLínea;

    /**
     * Constructor del Analizador Sintáctico
     * Inicializa los componentes
     * necesarios para realizar el análisis
     * sintáctico de un archivo fuente,
     * configurando la gramática, la matriz 
     * predictiva y el analizador léxico.
     *
     * @param gramatica Ruta al archivo que contiene la definición de la
     *                  gramática (basada en la enviada a ustedes).
     * @param archivo Ruta al archivo de código fuente a analizar.
     * @throws IOException Si ocurre un error al leer los archivos necesarios.
     */
    public AnalizadorSintactico(String gramatica, String archivo) throws IOException {
        this.p = new Pila();                                                    
        this.g = new Gramatica(gramatica);                                      
        this.predict = new int[][]{
                       /*
                            Se deberá asegurar la correcta implementación
                            de la matriz predictiva proporcionada
                            junto con la gramática a utilizar en este
                            proyecto de analizadores léxico y sintáctico.
                        */ 
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//programa
            { 0, 2, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 2, 2, 0, 0, 0, 0},//lista_sent
            { 0, 3, 0, 4, 0, 0, 3, 0, 0, 3, 0, 0, 0, 3, 3, 0, 0, 0, 0},//sent_final
            { 0, 6, 0, 0, 0, 0, 7, 0, 0, 8, 0, 0, 0, 5, 5, 0, 0, 0, 0},//sentencia
            { 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//lista_id
            { 0, 0, 0, 0, 11, 0, 0, 0, 11, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0},//id_final
            { 0, 12, 0, 0, 0, 0, 0, 12, 0, 0, 0, 12, 12, 0, 0, 0, 0, 0, 0},//lista_expr
            { 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0},//lista_exprfinal
            { 0, 15, 0, 0, 0, 0, 0, 15, 0, 0, 0, 15, 15, 0, 0, 0, 0, 0, 0},//expresion
            { 0, 0, 0, 0, 17, 0, 0, 0, 17, 0, 17, 0, 0, 0, 0, 16, 16, 16, 16},//expr_final
            { 0, 19, 0, 0, 0, 0, 0, 18, 0, 0, 0, 20, 21, 0, 0, 0, 0, 0, 0},//expr_arit
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 23, 0, 0, 0, 0},//tipo
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 25, 26, 27},//operador
            { 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}//inicio
                       };                         
        System.out.println("\nMatriz predictiva:");
        Formatter fmt = new Formatter();//creando fmt para darle un formato
        fmt.format("%17s", "No Term.");
        for (int i = 0; i < g.getTerminales().length; i++)
            fmt.format("%8s", g.getTerminales()[i]);                    
        fmt.format("\n", null);
        
        for (int i = 0; i < g.getNoTerminales().length; i++) {                        
            fmt.format("\n%17s", g.getNoTerminales()[i] + ": ");            
            for (int j = 0; j < predict[i].length; j++) 
                fmt.format("%8s", predict[i][j]);                           
            fmt.format("\n", null);
        }                                        
        System.out.println(fmt);
        
        System.out.println("\nCorrida paso a paso del análisis Léxico-Sintáctico:");
        this.anLex = new AnalizadorLexico();                                    
        this.reader = new LecturaArchivo(archivo);                              
        this.línea = this.reader.leerLinea().trim();                            
        this.numLínea = 1;                                                     
        this.llDriver();
    }

    /**
     * Este método sigue el algoritmo de análisis sintáctico dirigido por 
     * tabla predictiva para derivar el código fuente de acuerdo con la 
     * gramática especificada,manejando errores sintácticos según 
     * aparezcan y se encuentren.
     */
    public void llDriver() throws IOException {
        int errCont = 0;  //- Contador para errores sintácticos encontrados.
        //- Inicia la pila con el símbolo inicial de la gramática.
        p.push(g.getSimboloInicial());
        //- define a x como el elemento en la cima de la pila sin removerlo.
        String x = p.peek(); 
        //- define a a para que sea el primer token del analizador léxico.
        String a = anLex.AFN(línea, numLínea).getLexema();        
        // Continúa mientras la pila no esté vacía y no haya errores léxicos.
        while (!p.estaVacía() && anLex.getErrores().estaVacia()) {                      
            // Imprime el estado actual del análisis. imprimir(x, a);                                                             
            imprimir(x, a);
            //- Verifica si el símbolo en la cima de la pila es un no terminal. 
            //  ... if (g.buscarSimbolo(g.getNoTerminales(), x, g.getNoTerminales().length)) {                          
            if (g.buscarSimbolo(g.getNoTerminales(), x, g.getNoTerminales().length)) {
            //- Calcula el número de producción a derivar tomando en cuanta la
            //- relación de x con a en la tabla predictiva ... predict[posiciónNoTerminal(x)][categoría(a)];            
            //- Si existe una producción aplicable, realiza la derivación.
            //-    if (prod != 0) {                                                        
                if (predict[posiciónNoTerminal(x)][categoría(a)] != 0) {
                    //- Removiendo el símbolo no terminal de la pila.
                    p.pop();
                    //- e insertando los lados derechos de la producción indicada... insertarLadosDerechos(prod - 1);                                    
                    insertarLadosDerechos(predict[posiciónNoTerminal(x)][categoría(a)] - 1);
                    //- Actualiza a x como el símbolo en la cima de la pila.
                    x = p.peek();
                } else {
                    //- Si no existe una producción aplicable
                    System.out.println("ERROR SINTÁCTICO en la línea " + numLínea); //Informa un error sintáctico.
                    errCont++;//Incrementa el contador de errores.
                    break;
                }                                              
            } else {
                //- Si el símbolo en la parte alta de la pila es un terminal
                //- y el elemento a coincide con la parte alta de la pila                                           
                if (x.equals(a)//son exactamente iguales  x = pila --no tiene clasificacion y a = token obtenemos "clasificacion"
                        || (categoría(a) == 1 && x.equals("id"))
                        || (categoría(a) == 11 && x.equals("enteros"))
                        || (categoría(a) == 12 && x.equals("reales")) ) {
                    //- lo saca de la pila
                    p.pop();
                    //- y pide un nuevo token                    
                    a = nuevoToken(a);
                    //- además de actualizar a x como el elemento de la pila sin sacarlo de ella
                    x = p.peek();
                } else {
                    //- Si el elemento a no coincide con la parte alta de la pila
                    System.out.println("ERROR SINTÁCTICO en la línea " + numLínea);//Informa un error sintáctico si no coincide.
                    errCont++;//Incrementa el contador de errores.
                    break;                
                }                                                                            
            }            
        }
        imprimirFinal(errCont, x, a);//Imprime el resultado final del análisis.
    }

    //- Determina la categoría del token actual basándose en su código de token.
    public int categoría(String a) {
        if (línea != null) {                         //- Verifica que la línea actual no esté vacía para evitar errores al procesar.
            Nodo token = anLex.AFN(línea, numLínea); //- Utiliza el analizador léxico para obtener el token actual y su código.
            switch (token.getCódigoDeToken()) {      //- Evalúa el código del token para clasificarlo en categorías comunes usadas en el análisis sintáctico.
                case 261: return  1;                 //- Código para identificadores.                    
                case 259: return 11;                 //- Código para literales enteras.
                case 260: return 12;                 //- Código para literales reales.
                default : break;//- Para cualquier otro código de token, no se modifica.                     
            }
        }
        
        for (int i = 0; i < g.getTerminales().length; i++) 
            if (a.equals(g.getTerminales()[i])) 
                return i;
        
        return -1;//- Si el token no necesita categorización especial o la línea es nula, devuelve el lexema original.
        //- Corregir el valor del retorno para que asegure el valor de la columna correcta en la matriz predictiva.
    }

    int posiciónNoTerminal(String lexema){
        /*
            Se deberá implementar un ciclo para iterar sobre
            el conjunto de símbolos no terminales y devolver
            el índice o posición del lexema en ese conjunto,
            o -1 si el lexema no está en él.
        */
        for (int i = 0; i < g.getNoTerminales().length; i++) 
            if (lexema.equals(g.getNoTerminales()[i])) 
                return i;                    
        
        return -1;
    }
    
    // Inserta los símbolos del lado derecho de una producción gramatical en la pila, en orden inverso.
    public void insertarLadosDerechos(int numProd) {
        String ladoDerecho = g.getLadoDerecho()[numProd].trim();                //- Elimina espacios innecesarios y prepara el lado derecho de la producción especificada.
        String[] simbolos = new String[g.getLadoDerecho().length * 2];          //- Crea un arreglo para almacenar temporalmente los símbolos del lado derecho.
        int contador = 0;                                                       //- Contador para los símbolos almacenados.
        int inicio = 0;                                                         //- Índice de inicio para cada símbolo.
        int fin = 0;                                                            //- Índice de fin para cada símbolo.
        while (fin <= ladoDerecho.length()) {                                   //- Recorre la cadena del lado derecho para extraer símbolos.
            // Detecta el fin de un símbolo o de la cadena.
            if (fin == ladoDerecho.length() || ladoDerecho.charAt(fin) == ' ' 
                    || ladoDerecho.charAt(fin) == ';'
                    || ladoDerecho.charAt(fin) == ',' 
                    || ladoDerecho.charAt(fin) == '(' 
                    || ladoDerecho.charAt(fin) == ')'){
                if (inicio != fin) {                                            //- Si se encontró un símbolo válido (inicio no es igual a fin y no es el símbolo especial 'ε').
                    String simbolo = ladoDerecho.substring(inicio, fin);
                    if (!simbolo.equals("ε")) {
                        simbolos[contador++] = simbolo;                         //- Almacena el símbolo en el arreglo y aumenta el contador.
                    }
                }
                if (fin != ladoDerecho.length()&&ladoDerecho.charAt(fin)!=' '){ //- Incluye delimitadores como símbolos válidos, excepto los espacios.
                    simbolos[contador++]=String.valueOf(ladoDerecho.charAt(fin));
                }
                inicio = fin + 1;                                               //- Prepara el inicio del próximo símbolo.
            }
            fin++;                                                              //- Incrementa 'fin' para continuar la búsqueda de símbolos.
        }
        for (int i = contador - 1; i >= 0; i--)                                 //- Ahora añadimos los símbolos a la pila en orden inverso.
            p.push(simbolos[i]);        
    }

    // Obtiene el siguiente token del archivo de entrada y actualiza la 
    // línea actual que está siendo analizada.
    public String nuevoToken(String tokenActual) throws IOException {
        String nuevoToken = "";                                                 //- Inicializa el nuevo token como una cadena vacía.                             
        if (línea != null && línea.length() > tokenActual.length()) {           //- Verifica si la línea actual tiene contenido suficiente para extraer un nuevo token después del actual.
            línea = línea.substring(tokenActual.length()).trim();               //- Actualiza la línea actual eliminando el token procesado.
            nuevoToken = anLex.AFN(línea, numLínea).getLexema();                //- Utiliza el analizador léxico para obtener el lexema del nuevo token.
            anLex.generarTablaSimbolos(anLex.AFN(línea, numLínea));             //- Actualiza la tabla de símbolos con el nuevo token.
        } else {
            línea = reader.leerLinea();                                         //- Lee la siguiente línea del archivo si la actual se ha consumido completamente.
            if (línea != null) {                                                //- Verificar que la línea leída no sea null antes de hacer trim para eliminar espacios.
                línea = línea.trim();
                numLínea++;                                                     //- Incrementa el contador de líneas.
                nuevoToken = anLex.AFN(línea, numLínea).getLexema();            //- Obtiene el lexema del primer token de la nueva línea.
                anLex.generarTablaSimbolos(anLex.AFN(línea, numLínea));         //- Actualiza la tabla de símbolos con el nuevo token.
            }
        }
        if (línea == null) {                                                    //- Gestiona el caso en que no quedan más líneas para leer del archivo.
            nuevoToken = "$";                                                   //- Asigna $ para indicar EOF.
        }
        return nuevoToken;                                                      //- Retorna el nuevo token extraído o el símbolo de fin de archivo.
    }                

    /**
     * Imprime el estado actual del análisis sintáctico, incluyendo el símbolo
     * actual en la pila, el token actual procesado, la producción derivada si
     * corresponde, y el estado actual de la pila.
     */
    public void imprimir(String x, String a) {
        System.out.println("--------------------------------");
        System.out.println("Símbolo actual en X: " + x);
        System.out.println("Token actual en A: " + a);
        System.out.println("--------------------------------");
        System.out.println("Contenido actual de la pila:");
        NodoPila temp = p.getTop();                                             //- Obtiene el nodo superior de la pila para iterar sobre él.
        while (temp != null) {
            System.out.println(temp.getDato());                                 //- Imprime el dato del nodo actual en la pila.
            temp = temp.getSiguiente();                                         //- Avanza al siguiente nodo en la pila.
        }
        // Si el símbolo actual en la pila es un no terminal, imprimir la producción aplicable.
        if (g.buscarSimbolo(g.getNoTerminales(), x, g.getNoTerminales().length)) {
            int prod = predict[posiciónNoTerminal(x)][categoría(a)];
            if (prod != 0) {
                System.out.println("--------------------------------");
                System.out.println("Producción Actual de Derivación:");
                System.out.println(prod + ".-" + g.getTodaGramatica()[prod - 1]);   //- Muestra la producción que se está aplicando según la matriz predictiva.
                System.out.println("--------------------------------");
            }
        }
        System.out.println("--------------------------------");
        System.out.println("     Tabla de símbolos");
        System.out.println("--------------------------------");
        anLex.imprimirTablas(anLex.getTablaSímbolos());                             //- Utiliza el método del analizador léxico para imprimir la tabla de símbolos.
        System.out.println("--------------------------------");
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    /**
     * Imprime el mensaje final del análisis sintáctico, reportando si el
     * análisis fue exitoso o si se encontraron errores. También cierra el
     * archivo de entrada después de completar el análisis.
     */
    public void imprimirFinal(int errCont, String x, String a) throws IOException {
        if (anLex.getErrores().estaVacia()) {                                           //- Verifica si no hay errores léxicos reportados.
            if (errCont < 1) {                                                          //- Si no se contaron errores sintácticos, se procede a imprimir el estado final.
                imprimir(x, a);                                                         //- Utiliza el método imprimir para mostrar el estado actual de la pila y del análisis.
                System.out.println("LA PILA ESTÁ VACÍA");
                System.out.println("EL PROGRAMA ES LÉXICA Y SINTÁCTICAMENTE CORRECTO");
            }
        } else {
            System.out.println("ERROR LÉXICO: " + a + " en la línea " + numLínea);      //- En caso de errores léxicos, imprime el mensaje de error con detalles.
        }
        reader.cerrarArchivo();                                                         //- Cierra el archivo de entrada para liberar recursos.
    }
        
}