/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitatsemafors;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roger G. Coscojuela
 */
public class ActivitatSemafors {

    /**
     * @param args the command line arguments
     */
    private static float saldo = 10000;
    static int vecesSacar = 0;
    static int vecesMeter = 0;
    static Semaphore semaforo = new Semaphore(1, true);

    public static void main(String[] args) {
        // TODO code application logic here
        final ActivitatSemafors app = new ActivitatSemafors();

        final Runnable sacar = new Runnable() {
            @Override
            public void run() {
                while (vecesSacar < 10) {
                    try {
                        //Seccion critica a proteger

                        app.ingressar(100);
                        vecesSacar++;
                        System.out.println(llegirSaldo());
                        //Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        };
        final Runnable meter = new Runnable() {
            @Override
            public void run() {
                while (vecesMeter < 10) {
                    try {
                        //Seccion critica a proteger

                        app.treure(20);
                        vecesMeter++;
                        System.out.println(llegirSaldo());
                        //Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        };

        new Thread(sacar).start();
        new Thread(meter).start();

    }

    public void ingressar(float diners) {
        sendWait();
        System.out.println("Metiendo pasta...");
        float aux;
        aux = llegirSaldo();
        aux = aux + diners;
        saldo = aux;
        guardarSaldo(saldo);
        sendSignal();
    }

    private static float llegirSaldo() {
        return saldo;
    }

    private void guardarSaldo(float saldo) {
        this.saldo = saldo;
    }

    public void treure(float diners) {
        sendWait();
        System.out.println("Sacando pasta...");
        float aux;
        aux = llegirSaldo();
        aux = aux - diners;
        saldo = aux;
        guardarSaldo(saldo);
        sendSignal();
    }

    public void sendWait() {
        try {
            semaforo.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ActivitatSemafors.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendSignal() {
        semaforo.release();
    }

}
