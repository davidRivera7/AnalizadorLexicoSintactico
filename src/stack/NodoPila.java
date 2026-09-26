/** NodoPila.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 * ----------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo 
 * ----------------------------------------------------
 *  La clase NodoPila representa la unidad más pequeña
 *  de información a guardar en la estructura dinámica
 *  de tipo LIFO.
 * 
 *  En esta versión esta clase solo puede guardar datos
 *  de tipo String, útiles para el procesamiento de la
 *  gramática.
 */
package stack;

public class NodoPila {
    private String dato;                
    private NodoPila siguiente;         
    
    
    public NodoPila(String dato){
        //- código por implementar
        this.dato = dato;//inicializamos el dato que contendra el NodoPila
        siguiente = null;//no tenemos un siguiente hasta este momento e inicializamos siguiente a nulo.
    }
    
    
    public String getDato(){
        return dato; //- código por corregir
    }
    
    
    public NodoPila getSiguiente(){
        return siguiente; // - código por corregir
    }
    
    
    public void setSiguiente(NodoPila n){
        //- código por implementar
        siguiente = n;//siguiente apunta al objeto NodoPila n
    }
    
}