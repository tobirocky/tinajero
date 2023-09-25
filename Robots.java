import java.util.concurrent.*;

public class Robots {

    public static final int numero_procesos = 3;
    public static final int NUM_LECTURAS_POR_PROCESO = 1;

    private static Semaphore[] semaforos = new Semaphore[numero_procesos];

    public static void main(String[] args) {
        for (int i = 0; i < numero_procesos; i++) {
            semaforos[i] = new Semaphore(0); // Inicialmente, todos los semáforos están en rojo (0).
        }

        // Inicialmente, el primer semáforo se pone en verde (1) para que comience el proceso 1.
        semaforos[0].release();

        for (int i = 1; i <= numero_procesos; i++) {
            Thread proceso = new Thread(new Proceso(i));
            proceso.start();
        }
    }

    static class Proceso implements Runnable {
        private final int numeroProceso;

        public Proceso(int numeroProceso) {
            this.numeroProceso = numeroProceso;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_LECTURAS_POR_PROCESO; i++) {
                try {
                    // Esperar a que el semáforo del proceso actual esté en verde.
                    semaforos[numeroProceso - 1].acquire();

                    // Realizar la acción del proceso.
                    if (numeroProceso == 1) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para ensamblar" );
                        
                    } else if (numeroProceso == 2) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para soldar");
                    } else if (numeroProceso == 3) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para embalar");
                    }

                    // Liberar el semáforo del siguiente proceso.
                    semaforos[numeroProceso % numero_procesos].release();
                    
                    // Mensaje indicando que el proceso ha sido liberado.
                    System.out.println("Robot " + numeroProceso + " libre para hacer su tarea"); 

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
