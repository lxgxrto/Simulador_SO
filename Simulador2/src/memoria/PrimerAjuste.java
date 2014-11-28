/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;


/**
 *
 * @author bibliasone
 */
public class PrimerAjuste extends Ajuste {

    public PrimerAjuste(int tam_memoria) {
        super(tam_memoria);
    }

    @Override
    public void cargar(Proceso proceso, int t) {
        int cont = 0;
        if (!espera){
            aux = proceso;
            aux.tiempoEntrada(t);
            //aux = new Proceso(id++, rand.nextInt(cuantos_proceso) + 1, rand.nextInt(tam_proceso) + 1);
        }
        for (Proceso actual : mem_sis) {
            if (actual.id == 0 && aux.memoria <= actual.memoria) {
                if (aux.memoria == actual.memoria) {
                    actual.clonar(aux);
                } else {
                    actual.memoria -= aux.memoria;
                    mem_sis.add(cont, aux);
                }
                //System.out.println("Entra: " + aux.toString());
                listos.add(aux);
                espera = false;
                return;
            }
            cont++;
        }
        //System.out.println("En espera: " + aux.toString());
        espera = true;
    }

}
