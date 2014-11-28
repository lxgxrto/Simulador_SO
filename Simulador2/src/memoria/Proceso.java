
package memoria;

/**
 * Clase que nos servira de modelo para generar las estructuras de datos
 * necesarias para la simulacion de los procesos, siendo esta la unidad
 * atomica de nuestra aplicacion
 */
public class Proceso {
    
    public int id,      //Identificador unico del proceso 
               cuanto,  //Tiempo de procesamiento del proceso
               memoria, //Memoria del proceso
               entrada; //indica en que momento entro el proceso a la memoria 
    /**
     * Id = 0 es memoria disponible.
     * @param id Identificador unico del proceso, si es cero significa que es
     * espacio vacio.
     * @param cuanto Tiempo de procesamiento del proceso.
     * @param memoria Memoria del proceso.
     */
    public Proceso(int id, int cuanto, int memoria){
        this.entrada = 0;
        this.id = id;
        this.cuanto = cuanto;
        this.memoria = memoria;
        
    }
    
    /**
     * Clona un proceso completo (el estado de sus variables).
     * @param aux Proceso a clonar
     */
    public void clonar(Proceso aux){
        id = aux.id;
        cuanto = aux.cuanto;
        memoria = aux.memoria;
    }
    /**
     * Sobreescritura del metodo toString para devolver una cadena que contenga
     * con la informacion que queremos del proceso en formato [id,cuanto,memoria]
     * @return la cadena con formato
     */
    @Override
    public String toString(){
        return "["+id+","+cuanto+","+memoria+"]";
    }
    
    public void tiempoEntrada(int t){
        entrada = t;
    }
}
