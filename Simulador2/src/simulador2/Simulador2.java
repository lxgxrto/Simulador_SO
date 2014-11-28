/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador2;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import memoria.*;

/**
 *
 * @author Biblias
 */
public class Simulador2 extends JFrame implements Runnable {

    JPanel principal, ajustes, micro, procesos, botones, superior, central, inferior, graficas,desfragmentado;
    JRadioButton a1, a2, a3;
    JButton run, reset, comparativa;
    JLabel l1, l2, l3, l4, l5, tiempo;
    JTextField memoria_total, tiempo_total, cuantos_sistema, memoria_proceso, cuanto_proceso;
    ButtonGroup grupo;
    JTextArea estado;
    JScrollPane scroll;
    JSlider velocidad;
    Thread hilo;
    Listado lista;
    JLabel memoria_desf,memoria_uso,l_memoria_desf;
    public Simulador2() {
        hilo = new Thread(this, "Mi Hilo");
        Componentes();
        Eventos();
        
    }

    private void Componentes() {
        superior = new JPanel(new FlowLayout());
        ajustes = new JPanel();
        //ajustes es un panel que contendra a los radios 
        ajustes.setLayout(new GridLayout(3, 1, 10, 15));
        ajustes.setBorder(BorderFactory.createTitledBorder("Ajustes"));
        a1 = new JRadioButton("Primer Ajuste");
        a1.setSelected(true);
        a2 = new JRadioButton("Mejor Ajuste");
        a3 = new JRadioButton("Peor Ajuste");
        grupo = new ButtonGroup();
        grupo.add(a1);
        grupo.add(a2);
        grupo.add(a3);
        ajustes.add(a1);
        ajustes.add(a2);
        ajustes.add(a3);
        superior.add(ajustes);
        //
        micro = new JPanel(new GridLayout(3, 2,10,20));
        micro.setBorder(BorderFactory.createTitledBorder("Micro"));
        l1 = new JLabel("Memoria Total");
        memoria_total = new JTextField();
        l2 = new JLabel("Tiempo Total");
        tiempo_total = new JTextField();
        l3 = new JLabel("Cuantos del sistema");
        cuantos_sistema = new JTextField();
        micro.add(l1);
        micro.add(memoria_total);
        micro.add(l2);
        micro.add(tiempo_total);
        micro.add(l3);
        micro.add(cuantos_sistema);
        superior.add(micro);
        //
        procesos = new JPanel(new GridLayout(2, 2,20,62));
        procesos.setBorder(BorderFactory.createTitledBorder("Procesos"));
        l4 = new JLabel("Tamaño del proceso");
        memoria_proceso = new JTextField();
        l5 = new JLabel("Cuanto del proceso");
        cuanto_proceso = new JTextField();
        procesos.add(l4);
        procesos.add(memoria_proceso);
        procesos.add(l5);
        procesos.add(cuanto_proceso);
        superior.add(procesos);
        //
        botones = new JPanel(new GridLayout(4, 1,5,2));
        run = new JButton("Iniciar");
        reset = new JButton("Restablecer");
        comparativa = new JButton("Comparativa");
        tiempo = new JLabel();
        botones.add(run);
        botones.add(reset);
        botones.add(comparativa);
        botones.add(tiempo);
        superior.add(botones);
        //
        central = new JPanel(new FlowLayout());
        estado = new JTextArea(15, 75);
        scroll = new JScrollPane(estado);
        velocidad = new JSlider(JSlider.VERTICAL, 0, 20, 20);
        central.add(scroll);
        central.add(velocidad);
        //
        inferior = new JPanel(new FlowLayout());
        memoria_desf = new JLabel();
        memoria_desf.setPreferredSize(new Dimension(850, 25));
        memoria_desf.setBorder(BorderFactory.createLineBorder(Color.gray, 2, true));
        inferior.add(memoria_desf);
        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.add(superior);
        principal.add(central);
        principal.add(inferior);
        this.getContentPane().add(principal);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        setDefaultLookAndFeelDecorated(true);
    }
    
    @Override
    public void run() {
        int tiempo_ejecucion = Integer.parseInt(tiempo_total.getText());
        int cuanto_sistema = Integer.parseInt(cuantos_sistema.getText());
        int tam_proceso = Integer.parseInt(memoria_proceso.getText());
        int c_proceso = Integer.parseInt(cuanto_proceso.getText());
        lista = new Listado(tiempo_ejecucion, c_proceso, tam_proceso,0);
        Proceso actual = new Proceso(0, 0, 0);
        Ajuste ajuste = new Ajuste(0);
        int mem = Integer.parseInt(memoria_total.getText());
        if (a1.isSelected()) {
            ajuste = new PrimerAjuste(mem);
        } else if (a2.isSelected()) {
            ajuste = new MejorAjuste(mem);
        } else if (a3.isSelected()) {
            ajuste = new PeorAjuste(mem);
        }
        while (tiempo_ejecucion > 0) {
            calculaPuntos(ajuste, memoria_desf.getGraphics());
            try {
                Proceso nuevo = ajuste.aux;
                if(!ajuste.espera)
                    nuevo = lista.nuevo();
                ajuste.cargar(nuevo, tiempo_ejecucion);
                if (!ajuste.espera) {
                    estado.append("Entra: " + ajuste.aux.toString() + "\n");
                } else {
                    estado.append("En espera: " + ajuste.aux.toString() + "\n");
                }
                hilo.sleep(velocidad.getValue());
                if (--tiempo_ejecucion == 0) break;
                tiempo.setText(String.valueOf(tiempo_ejecucion));
                actual = ajuste.siguienteListo();
                if (actual != null) {
                    hilo.sleep(velocidad.getValue());
                    if (--tiempo_ejecucion == 0) break;
                    tiempo.setText(String.valueOf(tiempo_ejecucion));
                    estado.append("Ejecutando: " + actual.toString() + "\n");
                    actual.cuanto -= cuanto_sistema;
                    hilo.sleep(velocidad.getValue()*cuanto_sistema);
                    if ((tiempo_ejecucion -= cuanto_sistema) <= 0) break;
                    tiempo.setText(String.valueOf(tiempo_ejecucion));
                    hilo.sleep(velocidad.getValue());
                    if (--tiempo_ejecucion == 0) break;
                    tiempo.setText(String.valueOf(tiempo_ejecucion));
                    if (actual.cuanto <= 0) {
                        estado.append("Descargando: " + actual.toString() + "\n");
                        ajuste.descargar(actual,tiempo_ejecucion);
                        hilo.sleep(velocidad.getValue());
                        if (--tiempo_ejecucion == 0) break;
                        tiempo.setText(String.valueOf(tiempo_ejecucion));
                        ajuste.condensar();
                        estado.append(ajuste.condensados);
                        if (ajuste.condensa) {
                            hilo.sleep(velocidad.getValue());
                            if (--tiempo_ejecucion == 0) break;   
                            tiempo.setText(String.valueOf(tiempo_ejecucion));
                        }
                    } else {
                        ajuste.ponerAlFinal(actual);
                    }
                }
                ajuste.informe();
                estado.append("Memoria: " + ajuste.lista_memoria);
                estado.append("Listos: " + ajuste.lista_listos + "\n");
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulador2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        estado.append("\nTotal de procesos atendidos: " + ajuste.total_atendidos + "\n");
        estado.append("Tiempo medio de atencion a procesos: " + (ajuste.media_atendidos/ajuste.total_atendidos));
        estado.append("\nTiempo medio: " + ajuste.media_atendidos);
        
    }
    
    private double porcentajeFragmentado(Ajuste ajuste){
        double porcentaje = 0;
        int cont=0;
        for(int i = 0; i < ajuste.mem_sis.size()-1; i++){
            if(ajuste.mem_sis.get(i).id == 0)
                porcentaje+=ajuste.mem_sis.get(i).memoria;
                cont++;
        }
        if(cont != 0)
            return porcentaje/cont;
        return 0.0;
    }
    
    public void comparativaAlgoritmos(){
        int tiempo_ejecucion = Integer.parseInt(tiempo_total.getText());
        int cuanto_sistema = Integer.parseInt(cuantos_sistema.getText());
        int tam_proceso = Integer.parseInt(memoria_proceso.getText());
        int c_proceso = Integer.parseInt(cuanto_proceso.getText());
        lista = new Listado(tiempo_ejecucion, c_proceso, tam_proceso,0);
        int puntoX = tiempo_ejecucion/100;
        Proceso actual = new Proceso(0, 0, 0);
        Ajuste ajuste = new Ajuste(0);
        int mem = Integer.parseInt(memoria_total.getText());
        for (int i = 0; i < 3; i++) {
            String nombre;
            Map<Double,Double> puntos = null;
            if (i == 0) {
                ajuste = new PrimerAjuste(mem);
                nombre ="Primer Ajuste"; 
                puntos = new HashMap<>();
            } else if (i == 1) {
                ajuste = new MejorAjuste(mem);
                nombre ="Mejor Ajuste";
                puntos = new HashMap<>();
            } else if (i == 2) {
                ajuste = new PeorAjuste(mem);
                nombre ="Peor Ajuste";
                puntos = new HashMap<>();
            }
            while (tiempo_ejecucion > 0) {
                Proceso nuevo = ajuste.aux;
                if(!ajuste.espera)
                    nuevo = lista.nuevo();
                ajuste.cargar(nuevo, tiempo_ejecucion);
                if(tiempo_ejecucion%puntoX == 0)
                    puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                if (--tiempo_ejecucion == 0) break;
                actual = ajuste.siguienteListo();
                if (actual != null) {
                    if(tiempo_ejecucion%puntoX == 0)
                    puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                    if (--tiempo_ejecucion == 0) break;
                    actual.cuanto -= cuanto_sistema;
                    if(tiempo_ejecucion%puntoX == 0)
                    puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                    if ((tiempo_ejecucion -= cuanto_sistema) <= 0) break;
                    if(tiempo_ejecucion%puntoX == 0)
                    puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                    if (--tiempo_ejecucion == 0) break;
                    if (actual.cuanto <= 0) {
                        ajuste.descargar(actual,tiempo_ejecucion);
                        if(tiempo_ejecucion%puntoX == 0)
                        puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                        if (--tiempo_ejecucion == 0) break;
                        ajuste.condensar();
                        if (ajuste.condensa) {
                            if(tiempo_ejecucion%puntoX == 0)
                            puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
                            if (--tiempo_ejecucion == 0) break;
                        }
                    } else {
                        ajuste.ponerAlFinal(actual);
                    }
                }  
                
            }
        estado.append("\nTotal de procesos atendidos: " + ajuste.total_atendidos + "\n");
        estado.append("Tiempo medio de atencion a procesos: " + (ajuste.media_atendidos/ajuste.total_atendidos));
        estado.append("\nTiempo medio: " + ajuste.media_atendidos);
        if(tiempo_ejecucion%puntoX == 0)
            puntos.put((double)tiempo_ejecucion, porcentajeFragmentado(ajuste));
        
        //final LineChart grafica = new LineChart("Fragmentación");
        }
        
    }
    
    public void calculaPuntos(Ajuste aux, Graphics a1){
        //ArrayList<Integer> puntos = new ArrayList<>();
        int cont = 0;
        for (Proceso actual : aux.mem_sis) {
            if(actual.id == 0)
                a1.setColor(Color.white);
            else
                a1.setColor(Color.black);
            a1.fillRect(cont, 0, (actual.memoria*850)/aux.tam_memoria, 25);
            cont += (actual.memoria*850)/aux.tam_memoria;
        }
    }

    private void limpiar() {
        memoria_total.setText("");
        tiempo_total.setText("");
        cuantos_sistema.setText("");
        cuanto_proceso.setText("");
        memoria_proceso.setText("");
        a1.setSelected(true);
        estado.setText("");
    }
    
    
    private boolean validar(){
        if(memoria_total.getText().equals(""))
            return false;
        if(tiempo_total.getText().equals(""))
            return false;
        if(cuantos_sistema.getText().equals(""))
            return false;
        if(memoria_proceso.getText().equals(""))
            return false;
        if(cuanto_proceso.getText().equals(""))
            return false;
        return true;
    }
    
    public static void main(String[] args) {
        new Simulador2();

    }
    
    

    

    private void Eventos() {
        run.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!validar())
                    JOptionPane.showMessageDialog(Simulador2.this, "Hay almenos un campo vacio");
                else    
                    hilo.start();
            }
        });
        comparativa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comparativaAlgoritmos();
            }
        });
        reset.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                limpiar();
            }
        });
    }

}
