/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;



/**
 * @author bibliasone
 * Clase que hereda de Ajuste pero que solo sobrecarga al metodo cargar(int,int) * 
 */
public class MejorAjuste extends Ajuste {

    public MejorAjuste(int tam_memoria) {
        super(tam_memoria);
    }

    @Override
    public void cargar(Proceso proceso, int tiempo_entrada) {
        int cont = -1, menor = tam_memoria;
        if (!espera) {
            aux = proceso;
            aux.tiempoEntrada(tiempo_entrada);
            //aux = new Proceso(id++, rand.nextInt(cuantoI) + 1, rand.nextInt(tamI) + 1);
        }
        for (int i = 0; i < mem_sis.size(); i++) {
            if (mem_sis.get(i).id == 0 && aux.memoria <= mem_sis.get(i).memoria && mem_sis.get(i).memoria <= menor) {
                menor = mem_sis.get(i).memoria;
                cont = i;
                espera = false;
            }
        }
        if (cont == -1) { 
            //System.out.println("En espera: " + aux.toString());
            espera = true;
        } else {          
            if (mem_sis.get(cont).memoria == aux.memoria) {
                mem_sis.get(cont).clonar(aux);
            } else {
                mem_sis.get(cont).memoria -= aux.memoria;
                mem_sis.add(cont, aux);
            }
            //System.out.println("Entra: " + aux.toString());
            listos.add(aux);
        }
    }

}
