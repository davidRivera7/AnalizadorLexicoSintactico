
package main;

import java.io.IOException;

import analizadorSintactico.AnalizadorSintactico;

public class Main {
    public static void main(String[] args) throws IOException {        
        new AnalizadorSintactico("src\\archivos\\gramatica.txt", "src\\archivos\\programa.txt");                                        
    }
}