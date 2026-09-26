/** Nodo.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 *  ----------------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 *  ----------------------------------------------------------
 *  Esta clase es útil para almacenar información de un token
 *  para el análisis léxico; la información a almacenar tendrá
 *  relación con el lexema y el atributo correspondiente al 
 *  token.
 *
 */
package analizadorLexico;

public class Nodo {
    private int códigoDeToken;
    private String lexema;              
    private Nodo siguiente;             
    
    public Nodo(int códigoDeToken, String lexema){
        //- código a implementar
        this.códigoDeToken = códigoDeToken;
        this.lexema = lexema;
        this.siguiente = null;
    }

    public Nodo getSiguiente(){
        return siguiente; //- código a modificar
    }
         
    public void setSiguiente(Nodo nodoSiguiente){
        //- código a implementar
        siguiente = nodoSiguiente;
    }
    
    public void setLexema(String lexema){
        //- código a implementar
        this.lexema = lexema;
    }
    
    public String getLexema (){
        return lexema; //- código a modificar
    }
    
    public void setCódigoDeToken(int códigoDeToken){
        //- código a implementar
        this.códigoDeToken = códigoDeToken;
    }
    
    public int getCódigoDeToken(){
        return códigoDeToken; //- código a modificar
    }
}