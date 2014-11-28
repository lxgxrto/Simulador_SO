/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package memoria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author bibliasone
 * Superclase de la que heredan todos los Ajustes
 */
public class Ajuste {
    
    public ArrayList<Proceso> mem_sis;  //Memoria dinamica del sistema
    public Queue<Proceso> listos;       //Cola para llevar el orden de los procesos en mem_sis
    public int id,                             //Contador de id's de los procesos
        tam_memoria,                    //Guarda el tamaño total de memoria
        total_atendidos,                //Total de procesos atendidos
        media_atendidos;                //Media de atendidos
    public Proceso aux;                 //Variable que nos sirve para generar un nuevo proceso
    public boolean espera,              //Define si existe un proceso en espera para entrar a memoria
                   condensa;            //Define si se condensaron huecos en la memoria
    Random rand;                        //Gennerador de numeros aleatorios segun se necesite
    public String condensados,          //Lista de los huecos condensados
                  lista_memoria,        //Cadena que tiene los procesos de la memoria concatenados en ella
                  lista_listos;         //Cadena que tiene los procesos de listos concatenados en ella
    /**
     * Inicializa la memoria del sistema con un nodo de la lista
     * del tamaño de la memoria total.
     * @param tam_memoria tamaño de la memoria a utilizar

    */
    public Ajuste(int tam_memoria){    
        this.tam_memoria = tam_memoria;
        mem_sis = new ArrayList<>();                //Inicializa el arreglo dinamico que contendra a ls lista de procesos
        mem_sis.add(new Proceso(0,0,tam_memoria));  //Introduce un proceso inicial vacío que tendra el tamaño total de la memoria
        listos = new LinkedList<>();                //Cola de espera de procesos a asignar mem_sis
        id = 1;                                     //Inicia el contador de id's en 1
        espera = false;                             //Si espera es false entonces no hay procesos en espera
        aux = null;                                 //Se apunta a aux a nulo
        rand = new Random();                        //Instacia la variable rand como un objeto de la clase Random()
        condensa = false;                           //Si condensa es false entonces no se ha condensado nada
        condensados = "";                           //Condensados en vacio
        total_atendidos = 0;
        media_atendidos = 0;
    }
    /**
     * Realiza una búsqueda en la memoria del sistema para encontrar un espacio
     * en el cual almacenar un nuevo proceso. Si no encuentra lugar retorna false,
     * en caso contrario retorna true.
     * @param cuantos_proceso Son los cuantos del proceso.
     * @param tam_proceso Es el tamaño del proceso.
     * 
     * Funcio vacía a sobreescribir.
     * @param tiempo_entrada
     */
    public void cargar(int cuantos_proceso, int tam_proceso, int tiempo_entrada){

    }
    public void cargar(Proceso proceso, int t){

    }
    /**
     * Elimina al Proceso cuyo id corresponda al enviado como parámetro.
     * @param proceso es el proceso que se estaba ejecutando actualmente 
     * en el micro.
     * @param t_final
     */
    public void descargar(Proceso proceso ,int t_final){  
        int cont = 0;                       //cuenta la posicion donde se encuentra el proceso a descargar en la memoria
        for (Proceso actual : mem_sis) {
            if(actual.id == proceso.id)
                break;
            cont++;
        }
        total_atendidos++;
        media_atendidos +=  mem_sis.get(cont).entrada - t_final;
        mem_sis.get(cont).id = 0;           //Pone en cero el id significando que esta libre    
        mem_sis.get(cont).cuanto = 0;       //Pone el tiempo en cero 
        
    }
    /**
     * Busca por espacios en blanco en la memoria del sistema, si encuentra
     * dos espacios contiguos, los condensa en un solo espacio del tamaño de 
     * los dos primeros e imprime quienes fueron esos procesos y cuanto es lo
     * que quedo despues de condensarlos.
     */
    public void condensar(){
        condensados = "";
        condensa = false;
        for(int i = 1; mem_sis.size() > 1 && i <mem_sis.size(); i++)
            if(mem_sis.get(i).id == 0 && mem_sis.get(i-1).id == 0){
                //System.out.println("Condensando: " + mem_sis.get(i-1).toString() + mem_sis.get(i) + " = [0,0," + (mem_sis.get(i-1).memoria + mem_sis.get(i).memoria) + "]");
                condensados += "Condensando: " + mem_sis.get(i-1).toString() + mem_sis.get(i) + " = [0,0," + (mem_sis.get(i-1).memoria + mem_sis.get(i).memoria) + "]\n";
                mem_sis.get(i-1).memoria += mem_sis.get(i).memoria;
                mem_sis.remove(i);
                i--;
                condensa = true;
            }
    }
    /**
     * Retorna el siguiente proceso en la lista de listos, si no hay proceso
     * siguiente retorna un nulo.
     * @return el siguiente listo de la lista
     */
    public Proceso siguienteListo(){
        if (listos.isEmpty())
            return null;
        return listos.poll();
    }
    /**
     * Pone al final de la lista de listos a un proceso que no ha terminado
     * su ejecucion
     * @param aux El proceso con tiempo de procesamiento pendiente
     */
    public void ponerAlFinal(Proceso aux){
        listos.add(aux);
        for (int i = 0; i < mem_sis.size(); i++) {
            if(mem_sis.get(i).id == aux.id){
                mem_sis.set(i, aux);
                break;
            }
        }
    }
    /**
     * Imprime el contenido de la memoria y de listos.
     */
    public void informe(){
        lista_memoria = "";
        lista_listos = "";
        for (Proceso proceso : mem_sis) 
            lista_memoria += proceso.toString();
        lista_memoria += "\n";
        for (Proceso proceso : listos) 
            lista_listos += proceso.toString();
        lista_listos += "\n";
    }
    /**
     * Retorna toda la memoria.
     * @return 
     */
    public ArrayList<Proceso> getMemoria(){
        return mem_sis;
    }
    /**
     * Retorna toda la lista de listos.
     * @return 
     */
    public Queue<Proceso> getListos(){
        return listos;
    }
}
