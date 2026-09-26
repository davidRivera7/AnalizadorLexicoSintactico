/** LecturaArchivo.java
 *  Noviembre de 2024
 *  Tecnológico Nacional de México
 *  Instituto Tecnológico de León
 *  Ingeniería en Sistemas Computacionales
 *  Lenguajes y Automatas I
 *  --------------------------------------------------------------
 *  Autor:
 *  Rivera Ponce David Eduardo
 *  --------------------------------------------------------------
 *  Esta clase facilitará la lectura de un archivo de texto, el
 *  cual deberá contener información relacionada con el lenguaje a 
 *  procesar.
 * 
 */
package analizadorLexico;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LecturaArchivo {
    private Scanner lector;

    public LecturaArchivo(String archivo) throws IOException {
        lector = new Scanner(new File(archivo));
    }

    public String leerLinea() throws IOException {
        return lector.hasNext()?lector.nextLine():null; 
    }

    public void cerrarArchivo() throws IOException {
        if (lector != null)
            lector.close();        
    }        
}