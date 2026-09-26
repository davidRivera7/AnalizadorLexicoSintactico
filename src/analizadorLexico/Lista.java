/** Lista.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 *  -----------------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 *  -----------------------------------------------------------
 *  Esta clase sirve para gestionar estructuras tipo lista para
 *  las parejas de lexema, atributo necesarias en el proceso de
 *  tokens.
 */
package analizadorLexico;

public class Lista {
    private Nodo inicio, fin;                            
    
    public Lista(){                                      
        //- código por implementar
        inicio = fin = null;
    }
    
    public boolean estaVacia(){
        return inicio == null; //- código para corregir
    }
    
    public  void addNodo(String lexema, int códigoDeToken){             
        if(this.inicio == null){                   
            //- código por implementar
            inicio = fin = new Nodo(códigoDeToken, lexema);
        }else{
            //- código por impementar
            Nodo nuevoNodo = new Nodo(códigoDeToken, lexema);
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
        }
    }
    
    public boolean buscar(String lexema){
        Nodo temp = this.inicio;                            
        while(temp != null){                                
//            if(temp.getLexema().contains(lexema))
            if(temp.getLexema().equals(lexema))//cambiamos contains por equals, para comparar que exactamente sea un string 'igual' y no que solo forme 'parte de'
                return true;
            else
                temp = temp.getSiguiente();                 
        }
        return false;                                       
    }        
    
    public void setInicio(Nodo n){
        //- código por implementar
        inicio = n;
    }
    
    public Nodo getInicio(){
        return inicio; //- código por corregir
    }
        
    public void setFin(Nodo n){
        //- código por implementar
        fin = n;
    }
}