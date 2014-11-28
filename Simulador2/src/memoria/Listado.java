/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Listado {
    FileWriter archivo;
    PrintWriter escritor;
    Random rand;
    int pos;
    public Listado(int tam, int cuanto_proceso, int tam_proceso,int p){
        rand = new Random();
        pos = p;
        try {
            archivo = new FileWriter("procesos.txt");
            escritor = new PrintWriter(archivo);
            for (int i = 1; i <= tam; i++) {
                escritor.println(i + "," + (rand.nextInt(cuanto_proceso) + 1) + "," + (rand.nextInt(tam_proceso) + 1));
            }
            archivo.close();
        } catch (IOException ex) {
            Logger.getLogger(Listado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Proceso nuevo(){
        Proceso proceso = null;
        try {
            FileReader file = new FileReader("procesos.txt");
            BufferedReader br = new BufferedReader(file);
            String aux[] = null;
            for (int i = 0; i <= pos; i++) {
                aux = br.readLine().split(",");
            }
            proceso = new Proceso(Integer.parseInt(aux[0]), Integer.parseInt(aux[1]), Integer.parseInt(aux[2]));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Listado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Listado.class.getName()).log(Level.SEVERE, null, ex);
        }
        pos++;
        return proceso;
    }
    
}
