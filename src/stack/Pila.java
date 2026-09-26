/** Pila.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México 
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 * -------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 * -------------------------------------------------
 *  Esta clase contiene la implementación de una
 *  estructura de datos dinámica tipo LIFO (Last In
 *  First Out) útil para realizar tareas de análisis
 *  sintáctico.
 * 
 *  Contenido: Los métodos peek(), push() y pop() así
 *             como isEmpty() y el constructor de la 
 *             clase.
 */
package stack;

public class Pila {
    private NodoPila tope;                      
    
    public Pila() {
        this.tope = null;//al instanciar o crear una pila inicializamos el tope en null teniendo una pila vacia.
    }

    public boolean estaVacía() {
        return tope == null;//retorna boolean indicandonos si el tope es igual a null, lo que significa que la pila esta vacia
    }
   
    public void push(String info) {
        if (estaVacía()) {
            //- código por implementar
            tope = new NodoPila(info);//instanciamos el primer valor NodoPila que contendra la Pila con la info proporcionada por el parametro de este metodo.
        } else {
            //- código por implementar
            NodoPila nuevoTope = new NodoPila(info);//existe un nuevo valor 'info' que pasara a ser el nuevo tope de la pila.
            nuevoTope.setSiguiente(tope);//el nuevo tope no debera de perder la conexion con los demas Nodos Pila por lo que el siguiente del 'nuevo' apunta al anterior tope de la pila.
            tope = nuevoTope;//apuntamos con variable 'tope' hacia el nuevo tope de la pila.
        }
    }

    public NodoPila pop() {
        if (estaVacía()) {
            // - código por implementar
            return null;//no existe ningun elemento en la pila y retornamos nulo
        } else {
            // - código por implementar
            NodoPila eliminado = tope;//apuntamos hacia el elemento que se encuentra en el tope de la pila.
            tope = tope.getSiguiente();//actualizamos el tope de la pila.
            return eliminado;//retornamos el elemento 'eliminado' que se encontraba en el 'tope' de la pila.
        }        
    }
    
    public String peek(){
        if(estaVacía()){
            return null;
        }else{
            //- código por implementar
            return tope.getDato();//accedemos al dato del elemento que se encuentra en el tope de la pila.
        }
    }
        
    public NodoPila getTop(){
        return tope; //- modificar esta línea
    }
}