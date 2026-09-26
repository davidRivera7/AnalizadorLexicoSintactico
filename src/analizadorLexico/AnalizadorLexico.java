/** AnalizadorLéxico.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 *  -------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 *  -------------------------------------------------
 *  AnalizadorLéxico contiene el código para realizar
 *  el análisis léxico de un archivo de texto que a 
 *  su vez contiene el código del lenguaje propuesto;
 *  Este analizador implementa un autómata finito no
 *  determinista para identificar y clasificar tokens
 *  como: identificadores, números naturales, números
 *  de punto flotante sin signo, caracteres simples,
 *  palabras reservadas y detecta errores.
 */
package analizadorLexico;

import java.util.Formatter;

public final class AnalizadorLexico {
    private Lista tablaSímbolos, tablaTokens, errores;
    private String[][] palabrasReservadas = { 
        //- Esta tabla deberá concordar con el lenguaje en estudio
        //- Los pares(lexema, atributo) de abajo son puramente un
        //- ejemplo.
        {"class", "262"},
        {"read", "263"},
        {"write", "264"},
        {"int", "265"},
        {"float", "266"}       
    };    
   
    public AnalizadorLexico(){
        tablaSímbolos = new Lista();        
        //tablaTokens = new Lista();//instanciamos tablaTokens en constructor para posteriormente utilizarla en demas metodos      comentamos porque no se genera tabla de simbolos de todo el programa
        errores = new Lista();                                                  
    }
       
    /**
     * AFN procesa una línea de texto para identificar tokens.
     * emplea un conjunto de estados para reconocer los patrones
     * existentes en el texto.
     * @param línea, que es la línea de texto a procesar
     * @param numLínea, que indica el número de línea correspondiente.
     * @return el token generado como un nodo.
     */
    
    
    public Nodo AFN(String linea, int numLinea) {
        char actual;                       //- Carácter actual a analizar
        String palabra;                    //- Palabras clave y errores 
        int códigoDeToken = 0;             //- Atributo de un lexema
        int estado = 0;                    //- Estado del autómata, cero al inicio
        int índiceInicial, índiceFinal;    //- Índices para manejar substrings en la línea 
        boolean encontrado = false;
        Nodo token = null;                 //- Para contener un token como nodo
        
        índiceInicial = índiceFinal = 0; 
        
        //- Ciclo del AFN a ejecutar mientras el índice final no exceda la 
        //- longitud de la línea actual y no se haya encontrado un token
        while ((índiceFinal <= linea.length()) && !encontrado) {              
            if (índiceFinal < (linea.length()))      //- El índice final debe encontrarse dentro de los límites de la línea analizada
                actual = linea.charAt(índiceFinal);  //- Se accede al caracter a analizar
            else                                     //- Si el índice ha alcanzado el final de la línea, 
                actual = ' ';                        //- Se asigna un espacio ' ' a 'actual'
            
            //- En esta parte es donde se implementa el AFN para reconocer

            //- los componentes léxicos en la línea a analizar
            
            switch (estado) {
                case 0:                                   //- Estado 0: estado inicial del AFN para determinar qué tipo de token se estará reconociendo
                    if (actual >= '1' && actual <= '9') { //- Si el carácter actual es un dígito del 1 al 9, el autómata anticipa un número entero o de ´punto flotante
                        estado = 1;                       //- y cambia al estado indicado por el AFN diseñado
                        índiceFinal++;                    //- Incrementa el índice para leer el siguiente carácter
                    } else if (actual == '0') {           //- Si el carácter actual es 0, el autómata cambia
                        estado = 2;                       //- al estado que corresponda según su diseño
                        índiceFinal++;                    //- Avanza el índice para leer el siguiente carácter
                    } else {                              //- Ahora bien, si no es dígito, cambia al
                        estado = 7;                       //- estado 7 para intentar reconocer otros tipos de lexemas
                    }
                    break;

                case 1:                                   //- Estado 1: Este estado ayuda en la lectura de dígitos formar un número
                    if (actual >= '0' && actual <= '9') { //- Si el carácter actual es un dígito del 0 al 9, permanece en el estado 1
                        estado = 1;                       //- y avanza el índice para continuar leyendo más dígitos del número
                        índiceFinal++;                    //- Avanza el apuntador para incluir este dígito en el número actual
                    } else if ((int) actual == 46) {      //- Si el carácter actual es un punto decimal (ASCII 46), 
                        estado = 4;                       //- Cambia al estado 4 para manejar números de punto flotante
                        índiceFinal++;                    //- Avanza el índice para leer la parte decimal del número
                        //- Ahora bien, si el caracter actual no es dígito ni es un punto y tampoco
                        //- es un carácter simple o un espacio (ASCII 32), hay un error léxico y se cambia al estado -1 para manejarlo
                    } else if (actual != '=' && actual != '<' && !isSimpleChar(actual) && (int) actual != 32) {
                        estado = -1;
                    } else {                              //- Para cualquier otro carácter válido que pueda seguir a un número (espacios y caracteres simples), 
                        estado = 3;                       //- cambia al estado 3 para finalizar el token
                        índiceFinal--;                    //- Retrocede el apuntador para evaluar este carácter en el siguiente ciclo
                    }
                    break;

                case 2:                                   //- Estado 2: Maneja la secuencia después de encontrar un dígito '0' inicial
                    if ((int) actual == 46) {             //- Si el carácter actual es un punto (ASCII 46), 
                        estado = 4;                       //- cambia al estado 4 para comenzar a procesar la parte decimal del número (estado 4 no implementado aquí
                        índiceFinal++;                    //- Avanza el índice para leer los dígitos después del punto
                        // Si el carácter actual no es un carácter simple o un espacio (ASCII 32), indica un error léxico y cambia al estado -1 para manejarlo
                    } else if (actual != '=' && actual != '<' && !isSimpleChar(actual) && (int) actual != 32) {
                        estado = -1;
                    } else {                              //- Para cualquier otro carácter válido que pueda seguir a un 0 (espacios y caracteres simples), 
                        estado = 3;                       //- cambia al estado 3 para reconocer el token ya que 3 es un estado de aceptación
                        índiceFinal--;                    //- Retrocede el índice para evaluar este carácter en el siguiente ciclo de reconocimiento de lexema
                    }
                    break;

                case 3:                                   //- Estado 3: Finaliza la lectura de un token válido de número entero y lo registra
                    token = generarToken(linea,           //- Generar un token basado en el texto reconocido desde 'índiceInicial' hasta 'índiceFinal'
                                         índiceInicial, índiceFinal + 1, 259);  //- El atributo 259 es arbitrario para los números enteros                                        
                    encontrado = true;                    //- encontrado cambia a verdadero ´para detener el ciclo´ e iniciar para otro lexema                    
                    
                    //imprimiendo en consola numero de linea en que se encontro un token y los datos del token como lexema, codigo de token y clasificacion a la que pertenece. Pintando lexema y codigo con salidas de escape en color morado para su mejor identificacion.
//                    System.out.println("Linea " + numLínea + ", token identificado(lexema : " + "\u001B[35m" + token.getLexema() + "\u001B[0m" + ", codigo : " + "\u001B[35m" + token.getCódigoDeToken() + "\u001B[0m" + ", clasificacion : Números enteros)");
                    break;

                /*
                    
                    De manera similar se deberán implementar el resto de los casos
                    para considerar el resto de categorías léxicas en el programa.
                    
                    case  4:
                    case  5:
                    case  6:
                    .
                    .
                    .
                    case 22:
                    case 23:
                    
                    
                    
                */                        
                case 4://flotante 'empezar' llega como: digito.
                    if (actual >= '0' && actual <= '9') {//consideramos un flotante que se puede aceptar
                        estado = 5;//cambiamos a estado correspondiente donde se puede considerar generar un 'numero flotante'
                        índiceFinal++;//aumentamos iterador para leer el siguiente caracter
                        break;
                    }
                    if (índiceFinal == linea.length())//verificar si el error se da de una linea que no tiene mas caracteres a la derecha, por lo tanto restamos indice final ya que hasta ahi se encuentra el error localizado, porque se puede dar que 'actual' = ' ' e 'indice final' sea = 'linea.length' y por lo tanto ' ' no existe en la linea en realidad, por eso es necesario retroceder el 'indice final'.
                        índiceFinal--;
                    
                    estado = -1;//error lexico identificado porque se esperaba un digito.
                    break;
                    
                case 5://flotante 'seguir' llega como: digito.digito
                    if (actual >= '0' && actual <= '9') {
                        estado = 5;//nos quedamos almacenando numero flotante
                        índiceFinal++;//aumentamos iterador para leer el siguiente caracter
                        break;//evitamos continuar con instrucciones del 'case 5'
                    }
                    if (actual != '=' && actual != '<' && !isSimpleChar(actual) && (int) actual != 32) {//si a continuacion de un digito es un caracter que no es valido, cambiamos a estado para generar error identificado
                        estado = -1;//estado -1 para tratar el error
                        break;//evitamos continuar con instrucciones del 'case 5'
                    }
                    estado = 6;//cambiamos a estado para generar token encontrado
                    índiceFinal--;//retrocedemos posicion de fin para capturar solo la palabra aceptada
                    break;
                    
                case 6://flotante 'generar'                                                      
                    token = generarToken(linea,//- Generar un token basado en el texto reconocido desde 'índiceInicial' hasta 'índiceFinal'
                                         índiceInicial, índiceFinal + 1, 260);  //- El atributo 260 es arbitrario para los números enteros                                        
                    encontrado = true;//- encontrado cambia a verdadero ´para detener el ciclo´ e iniciar para otro lexema
                    
                    //imprimiendo en consola numero de linea en que se encontro un token y los datos del token como lexema, codigo de token y clasificacion a la que pertenece. Pintando lexema y codigo con salidas de escape en color morado para su mejor identificacion.
//                    System.out.println("Linea " + numLínea + ", token identificado(lexema : " + "\u001B[35m" + token.getLexema() + "\u001B[0m" + ", codigo : " + "\u001B[35m" + token.getCódigoDeToken() + "\u001B[0m" + ", clasificacion : Números de punto flotante)");
                    break;
                    
                case 7://'identificador' o 'palabra reservada' 'empieza'
                    if (actual >= 'a' && actual <= 'z') {//consideramos palabra que se puede aceptar
                        estado = 8;//nos movemos a estado correspondiente donde se puede generar un posible 'identificador' o 'palabra reservada'
                        índiceFinal++;//aumentamos indice para procesar siguiente caracter
                        break;//evitamos continuar con instrucciones del 'case 7'
                    }
                    estado = 10;//verificamos si es 'caracter simple' en estado 10
                    break;                    
                    
                case 8://'identificador' o 'palabra reservada' 'siguiente'
                    if ( (actual >= 'a' && actual <= 'z') || (actual >= '0' && actual <= '9') ||
                          actual == '@' ) {
                        estado = 8;//nos mantenemos generando palabra
                        índiceFinal++;//avanzamos indice para procesar siguiente caracter
                        break;//dejamos de procesar instrucciones de 'case 8'
                    }

                    if ((int) actual == 32 || isSimpleChar(actual)) {//verificamos si es carácter válido que pueda seguir a un 'identificador' o 'palabra reservada' (espacios o caracteres simples)
                        estado = 9;//cambiamos a estado 9 que verifica si puede generar el token
                        índiceFinal--;//retrocedemos 'indice final' para comprobar el posible token a aceptar
                        break;//dejamos de procesar instrucciones de 'case 8'
                    }
                    
                    estado = -1;//se detecta error por ser cualquier otro caracter no valido para un 'identificador' o 'palabra reservada'.
                    break;
                    
                case 9://'identificador' o 'palabra reservada' 'generar'
                    if (actual == '@') {//verificar que no termine con caracter no valido '@'
                        estado = -1;//se detecta error lexico
                        break;//dejamos de procesar instrucciones de 'case 9'
                    }
                    
                    palabra = extraePalabra(linea, índiceInicial, índiceFinal);//extraer palabra valida encontrada
                    if (esPalabraRes(linea, índiceInicial, índiceFinal)) {//verificar si es 'palabra reservada'                                                
                        
                        for (int i = 0; i < palabrasReservadas.length; i++) //buscamos 'palabra reservada' y obtenemos su 'códigoDeToken'
                            if (palabra.equals(palabrasReservadas[i][0])) {//verificar que palabra sea igual al lexema de la palabra reservada 'actual' en el arreglo de palabras reservadas.
                                códigoDeToken = Integer.parseInt(palabrasReservadas[i][1]);//obtenemos el codigo de token correspondiente
                                break;
                            }                                                                                                                           
                    } else 
                        códigoDeToken = 261;//definimos valor de atributo para la clasificacion de 'identificadores'
                    
                    token = generarToken(linea,//- Generar un token basado en el texto reconocido desde 'índiceInicial' hasta 'índiceFinal'
                                         índiceInicial, índiceFinal + 1, códigoDeToken);                     
                    encontrado = true;//- encontrado cambia a verdadero ´para detener el ciclo´ e iniciar para otro lexema
                    
                    //imprimiendo en consola numero de linea en que se encontro un token y los datos del token como lexema, codigo de token y clasificacion a la que pertenece. Pintando lexema y codigo con salidas de escape en color morado para su mejor identificacion.
//                    System.out.println("Linea " + numLínea + ", token identificado(lexema : " + "\u001B[35m" + token.getLexema() + "\u001B[0m" + ", codigo : " + "\u001B[35m" + token.getCódigoDeToken() + "\u001B[0m" + ", clasificacion : " + ((códigoDeToken == 261) ? "Identificadores" : "Palabras reservadas") + ")");
                    
                    generarTablaSimbolos(token);//agregando a la tabla de simbolos sin repetir
                    break;
                    
                case 10://verificar si es caracter simple
                    if (!isSimpleChar(actual)) {
                        estado = 11;//cambio a estado 11 para verificacion de espacio en blanco (ASCII 32)
                        break;
                    }
                    //si es caracter simple y generamos 'token'
                    token = generarToken(linea,//- Generar un token basado en el texto reconocido desde 'índiceInicial' hasta 'índiceFinal'
                                         índiceInicial, índiceFinal + 1, (int) actual);//obtenemos codigo ASCII del caracter identificado ya que ese sera su codigo de token.                     
                    encontrado = true;//- encontrado cambia a verdadero ´para detener el ciclo´ e iniciar para otro lexema
                    
                    //imprimiendo en consola numero de linea en que se encontro un token y los datos del token como lexema, codigo de token y clasificacion a la que pertenece. Pintando lexema y codigo con salidas de escape en color morado para su mejor identificacion.
//                    System.out.println("Linea " + numLínea + ", token identificado(lexema : " + "\u001B[35m" + token.getLexema() + "\u001B[0m" + ", codigo : " + "\u001B[35m" + token.getCódigoDeToken() + "\u001B[0m" + ", clasificacion : Caracteres simples)");
                    break;
                    
                case 11:
                    if ((int) actual == 32) {//verificar si actual es un espacio en blanco
                        índiceInicial++;//aumentamos indice inicial y final al siguiente caracter de la linea para seguir procesando lo faltante de la linea.
                        índiceFinal++;
                        estado = 0;//reiniciamos automata para procesar siguiente caracter de la linea de forma correcta.
                        break;//salimos del caso 11 para ya no seguir con las instrucciones debajo
                    }
                    estado = -1;//identificacion de error: caracter no conocido en el lenguaje
                    break;
                    
                default://- Estado default: Maneja cualquier caso no contemplado específicamente por los estados anteriores                                                            
                    palabra = extraePalabra(linea, índiceInicial, índiceFinal);//- Obtiene la palabra actual
                    
                    //condicion para revisar si lo que se encuentra a la derecha del "error actual localizado" tambien es afectado y se convierte en error. O si podemos generar el "error de inmediato". Por ejemplo en la palabra "Arbol", al comenzar con A mayuscula, lo que sigue de 'A' tambien se convertira en error, es decir, no separamos 'A' como error y dejamos 'rbol' para seguir procesando
                    if (actual != '@' && actual != 32) {//verificamos que el error no venga de los casos 9 (identificador que termina con @) y 4 de la forma: digito.' ' donde ' ' representa un espacio ya que para estos casos no es necesaro entrar a este bucle ya que son "errores que se generan de inmediato"                                                                                        
                        while ((índiceFinal + 1) != linea.length()) {//verificar que no hemos llegado al final de la linea
                            índiceFinal++;
                            actual = linea.charAt(índiceFinal);
                            
                            if (!isSimpleChar(actual) && actual != 32) //verificamos si los caracteres siguientes a nuestro "error" tambien formar parte de la palabra "erronea" identificada.
                                palabra += actual;//adjuntamos caracteres que se encuentren continuos a nuestro error
                            else 
                                break;//dejamos de comparar para ahora generar "error completo" identificado
                        }
                        
                    }
                    
                    índiceFinal = índiceInicial + palabra.length();//- Ajusta el índice al final de la palabra                                                            
                    token = generarError(linea, índiceInicial, índiceFinal, numLinea);//- Registra un error para la palabra no reconocida                                        
                    encontrado = true;//- Encontrado verdadero ´para detener el ciclo´ y reiniciar una nueva secuencia de reconocimiento                                        
                    
                    //imprimiendo en consola numero de linea en que se encontro un token clasificado como error y su lexema. Pintando lexema con salida de escape en color morado para su mejor identificacion y en color de fondo rojo con letras blancas la palabra "error".
//                    System.out.println("Linea " + numLínea + ", " + "\u001B[41m" + "\u001B[37m" + "error" + "\u001B[0m" + " identificado(lexema : " + "\u001B[35m" + token.getLexema() + "\u001B[0m" + ", clasificacion : Error)");
                    break;
            }
        }
                        
        return token;
    }  
    
    //- Método para generar un token
    public Nodo generarToken(String línea, int inicio, int fin, int códigoDeToken) {
        //- El código de este método se debe corregir para que la extracción
        //- del lexema y la generación del token sean correctas
        
        String lexema = extraePalabra(línea, inicio, --fin);//- Extraer el lexema de la línea de texto y restamos uno a fin para que metodo extraer palabra funcione correctamente
        Nodo n = new Nodo(códigoDeToken, lexema);//- Crear nuevo nodo con el lexema y el código del token
                        
        return n;//- Regresar el nodo creado
    }

    //- Método para registrar un error léxico en la lista de errores
    public Nodo generarError(String línea, int inicio, int fin, int numLínea){
        //- Este método es muy similar al de generarToken(), solamente que aquí
        //- se reconoce el segment de error, se agrega en la lista de errores y
        //- se retorna como nodo creado
        //- Escriban el código necesario
                        
        String palabraDeError = extraePalabra(línea, inicio, --fin);//- Extraer el segmento de texto (palabra) que causó el error y restamos uno a fin para que funcione correctamente el metodo extraer palabra        
        
        if (errores.buscar(palabraDeError))//verificar antes si ya existe un error igual en la tabla de errores.
            return new Nodo(numLínea, palabraDeError);//retornar error sin meter a la tabla de errores para evitar duplicado.
        
        errores.addNodo(palabraDeError, numLínea);//- Añadir un nuevo nodo a la lista de errores con la palabra y el número de línea       comentamos porque no se genera tabla de errores
        Nodo nodoError = generarToken(línea, inicio, fin + 1, numLínea);//- Crear el nuevo nodo con el segmento de texto y el código del token                
        return nodoError;//- Regresa el nodo creado
    }

    public void generarTablaErrores(Nodo n) {
        // Implementar código
        if (!errores.buscar(n.getLexema()))//- Verificamos que no exista el token 'n' en la 'tabla de errores'
            errores.addNodo(n.getLexema(), n.getCódigoDeToken());//- Agregamos un nuevo 'token' de error a la 'tabla de errores' con el 'lexema' y el 'código de token' de 'n'
    }

    //- Método para generar la tabla de símbolos a partir de los tokens identificados
    /*
        Documente todas las líneas del código de la generación de la tabla de símbolos
        de manera que se comprenda lo que sucde en el código proporcionado
    */
    public void generarTablaSimbolos(Nodo n) {//- Recibimos como argumento un nodo (token)
        Nodo temp = tablaSímbolos.getInicio();//- Apuntamos con 'temp' hacia el nodo 'inicial' de la lista de 'tabla de símbolos'
        
        if (temp == null && n.getCódigoDeToken() == 261)//- Verificamos si la 'tabla de simbolos' esta vacia y si la categoría léxica del 'token' que recibimos como argumento pertenece a los 'identificadores'
            tablaSímbolos.addNodo(n.getLexema(), n.getCódigoDeToken());//- Agregamos un primer identificador a la tabla de simbolos
         else 
            while (temp != null) {//- Permanecemos en el ciclo mientras que 'temp' apunte a un 'token' de la 'tabla de simbolos'
                if (!tablaSímbolos.buscar(n.getLexema()) && n.getCódigoDeToken() == 261)//- Verificamos si no existe un 'token' en la tabla de simbolos que contenga el lexema de 'n' y ademas si la categoría léxica de 'n' pertenece a los 'identificadores'
                    tablaSímbolos.addNodo(n.getLexema(), n.getCódigoDeToken());//- Agregamos un nuevo 'token' a la 'tabla de simbolos' con el 'lexema' y el 'código de token' de 'n'
                temp = temp.getSiguiente();//recorremos al siguiente token de la tabla de simbolos
            }        
    }
        
    //- Método para generar la tabla general de tokens identificados
    /*
        Documente todas las líneas del código de la generación de la tabla de tokens
        de manera que se comprenda lo que sucde en el código proporcionado
    */
    public void generarTablaTokens(Nodo n) {//- Recibimos como argumento un nodo (token)
        // Implementar código                
        if (!tablaTokens.buscar(n.getLexema()))//- Verificamos que no exista un token con el mismo lexema de 'n' en la 'tabla de tokens'
            tablaTokens.addNodo(n.getLexema(), n.getCódigoDeToken());//- Agregamos un nuevo 'token' a la 'tabla de tokens' con el 'lexema' y el 'código de token' de 'n'
    }

    //- Determina si un carácter simple es válido
    //- Corrija en caso de que su gramática considere otros símbolos terminales
    //- como caracteres simples
    public boolean isSimpleChar(char caracter) {
        return caracter == ';' || 
                caracter == '=' || //-Agregamos caracter simple '=' ya que forma parte de los caracteres simples de nuestro lenguaje y porque hemos usado este metodo en diferentes partes del codigo y queremos que en esas partes del codigo tambien se haga la comparacion de '=' aunque en algunas partes del codigo este signo '=' se compare de forma independiente antes de entrar a este metodo (pero hemos decidido no borrar esa comparacion independiente de '=' para no modificar el codigo que se nos proporciono previamente)
                caracter == ',' || caracter == '+' || caracter == '-' || 
                caracter == '/' || caracter == '*'|| caracter == '(' || 
                caracter == ')' || caracter == '@' || caracter == '{' || 
                caracter == '}';
    } // Si no está en los casos, retorna false.
       
    //- Determina si un segmento de texto es una palabra reservada
    public boolean esPalabraRes(String linea, int inicio, int fin) {
        //- Extraiga la palabra completa desde la posición 'inicio' hasta 'fin' usando el método 'extraerPalabra'
        String palabra = extraePalabra(linea, inicio, fin);                       
        //- Itere sobre el array de palabras reservadas para verificar si la palabra extraída coincide con alguna de ellas
        for (int i = 0; i < palabrasReservadas.length; i++) 
            if (palabra.equals(palabrasReservadas[i][0]))//comparando 'lexemas' de la nueva palabra extraida identificada con los lexemas del arreglo de palabras reservadas para decidir si la palabra nueva es identificador o es palabra reservada
                return true;//- Retorne true si la palabra extraída es una palabra reservada            
                    
        return false;
    }
        
    //- Extrae una palabra o segmento de una línea de texto, comenzando en un índice especificado
    public String extraePalabra(String linea, int inicio, int fin) {        
        String palabra = "";
        for (int i = inicio; i <= fin; i++)//iterando desde inicio hasta fin  
            palabra += linea.charAt(i);//considerar cada caracter recorrido de inicio a fin como una palabra
                
        return palabra; //- Corrija el código según se necesita
    }   
            
    /*
        Por favor, realicen una crítica detallada del método imprimirTablas que
        se proporciona a continuación.
    Crítica: 
    Recibe una lista 'l' como argumento para 'imprimir' la informacion de sus 'tokens'
    por consola como "LEXEMA", "ATRIBUTO" y "CATEGORIA LÉXICA".
    
    Con el objeto 'fmt' podemos escribir una cadena formateada segun los valores de 
    los argumentos 'format' y 'args', cada vez que se llama a format() se va acumulando el 
    contenido en el objeto Formatter 'fmt' donde:    
    'args' --> son los argumentos que seran 'mostrados' a los que hacen referencia los especificadores de formato 
    en la cadena de formato, es decir, cada 'arg' tiene un especificador de formato.
    'format' --> es una cadena de formato con los siguientes simbolos:
        %15s indica que contendra 15 caracteres de espacio el 'args(1)' y es de tipo cadena con (s)
        %15s indica que contendra 15 caracteres de espacio el 'args(2)' y es de tipo cadena con (s)
        %30s\n indica que contendra 30 caracteres de espacio el 'args(3)', es de tipo cadena con (s) 
        y se coloca el cursor en la siguiente línea con '\n' al terminar de imprimir la línea actual
    nota: los caracteres de espacio empiezan a contarse de derecha a izquierda desde el caracter final de la palabra y
    posteriormente da un espacio y se repite el proceso para generar la siguiente columna a su derecha.
    
    recorremos la lista (tabla) en el ciclo 'while' mientras que temp no sea nulo, despues identificamos en el switch
    la clasificacion lexica a la que pertenece el token de la lista considerando en la parte de default cada caracter diferente 
    y las palabras reservadas en un rango determinado respecto a su codigo de token, posteriormente agrega la nueva linea 
    formateada a 'fmt' con la informacion del 'token' actual en temp.
    
    al terminar de agregar cada token, se imprime en consola el contenido acumulado en objeto 'fmt'
    gracias a el metodo toString() que se sobreescribe en la clase Formatter que se encarga
    de imprimir el contenido acumulado en el buffer del Formatter.
    */
    //- Imprime una tabla formatada de tokens que incluye lexema, código de token y categoría léxica
    public void imprimirTablas(Lista l) {
        Nodo temp = l.getInicio();                                               //- Inicia nodo temporal con el primer nodo en la lista
        String catLex = "";                                                      //- Variable para almacenar la categoría léxica del token
        Formatter fmt = new Formatter();                                         //- Utiliza Formatter para crear una cadena formatada
        fmt.format("%15s %15s %30s\n", "LEXEMA", "ATRIBUTO", "CATEGORIA LÉXICA");//- Encabezados de la tabla
        while (temp != null) {                                                   //- Itera a través de cada nodo en la lista
            switch (temp.getCódigoDeToken()) {
                case 257:
                case 258:
                    catLex = "Caracteres simples";          //- Asigna la categoría para los tokens que representan caracteres simples
                    break;
                case 259:
                    catLex = "Números enteros";             //- Asigna la categoría para los tokens que representan números enteros
                    break;
                case 260:
                    catLex = "Números de punto flotante";   //- Asigna la categoría para los tokens que representan números de punto flotante
                    break;
                case 261:
                    catLex = "Identificadores";             //- Asigna la categoría para los tokens que representan identificadores
                    break;
                default:                                    //- Se ejecuta si el código del token no coincide con ningún caso anterior
                    char tempChar = temp.getLexema().charAt(0);
                    if (temp.getLexema().equals("=") || temp.getLexema().equals("<") || isSimpleChar(tempChar)) {
                        catLex = "Caracteres simples";      //- Asigna la categoría caracteres simples bajo condiciones específicas
                    } else if (temp.getCódigoDeToken() >= 262 && temp.getCódigoDeToken() <= 271) {
                        catLex = "Palabras reservadas";     //- Categoriza como palabras reservadas si el código está en el rango de 262 a 271
                    }
                    break;
            }
            //- Formatea cada fila de datos de token para imprimir
            fmt.format("%15s %15s %30s\n", temp.getLexema(), temp.getCódigoDeToken(), catLex);
            temp = temp.getSiguiente();                     //- Mueve al siguiente nodo en la lista
        }
        System.out.println(fmt);                            //- Imprime la tabla completa
    }
    
    //- Imprimir en consola una tabla de palabras reservadas con sus atributos y categoría
    public void imprimirPalabrasRes() {
        Formatter fmt = new Formatter();                                            //- Utiliza Formatter para crear una cadena formatada
        fmt.format("%15s %15s %30s\n", "LEXEMA", "ATRIBUTO", "CATEGORIA LEXICA");   //- Agrega la cabecera de la tabla con los títulos de las columnas
        for (int i = 0; i < palabrasReservadas.length; i++) {                       //- Itera sobre el arreglo de palabras reservadas para agregar cada palabra a la tabla
            fmt.format("%15s %15s %30s\n", palabrasReservadas[i][0],                //- Añade una fila por cada palabra reservada, formateando cada columna para alinear el texto
                    palabrasReservadas[i][1], "Palabras reservadas");
        }
        System.out.println(fmt);                                                    //- Imprime el resultado final de la tabla formateada
    }

    //- Imprime una tabla de errores léxicos encontrados durante el análisis
    public void imprimirError(Lista l) {
        Nodo temp = l.getInicio();                                               //- Inicia con el primer nodo en la lista de errores.
        Formatter fmt = new Formatter();                                         //- Utiliza Formatter para crear una cadena formatada de salida.
        fmt.format("%15s %15s\n", "ERROR", "LINEA");                             //- Encabezados de la tabla.
        while (temp != null) {                                                   //- Itera sobre cada nodo en la lista.
            fmt.format("%15s %15s\n", temp.getLexema(), temp.getCódigoDeToken());//- Formatea cada fila de datos de error para imprimir.
            temp = temp.getSiguiente();                                          //- Mueve al siguiente nodo en la lista.
        }
        System.out.println(fmt);                                                 //- Imprime la tabla completa.
    }
    
    //- Devuelve la lista de tabla de símbolos del analizador léxico
    public Lista getTablaSímbolos(){
        return tablaSímbolos;  //- Corrija esta línea de código
    }

    //- Devuelve la lista de tabla general de tokens del analizador léxico
    public Lista getTablaTokens(){
        return tablaTokens;  //- Corrija esta línea de código
    }
    
    //- Devuelve la lista de errores léxicos encontrados en el análisis léxico
    public Lista getErrores(){
        return errores; //- Corrija esta línea de código
    }
}