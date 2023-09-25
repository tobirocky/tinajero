import java.util.concurrent.locks.*;

public class Segundo_programa {

    public static final int numero_procesos = 3;//número de procesos asignados
    public static final int NUM_LECTURAS_POR_PROCESO = 1;//número de iteraciones a realizar

    //variable es compartida entre todas las instancias.
    private static CustomSemaphore[] semaforos = new CustomSemaphore[numero_procesos];//arreglo de tipo objeto donde se asigan el número de procesos

    public static void main(String[] args) {//Este es un bucle for que se ejecuta numero_procesos. las veces
                                            //donde numero_procesos es una constante establecida en 3. 
                                            //En cada iteración del bucle, se inicializará un semáforo para cada proceso.
        for (int i = 0; i < numero_procesos; i++) {
            semaforos[i] = new CustomSemaphore(0); // Inicialmente, todos los semáforos están en rojo y no hay permiso para acceder a otro proceso.(0).
        }

        // Inicialmente, el primer semáforo se pone en verde (1) para que comience el proceso 1.
        semaforos[0].release();//con release se liberan los procesos

        for (int i = 1; i <= numero_procesos; i++) {
            Thread proceso = new Thread(new Proceso(i));//aqui se asigna  un hilo por proceso 
            proceso.start();//comienza el hilo a ejecutarse por medio del for que ira de uno proceso a otro.
        }
    }

    static class CustomSemaphore {//clase que indica las propiedades a funcionar del arreglo [] semaforos
        private final Lock bloqueo_de_proceso = new ReentrantLock(); //variable de tipo objeto que bloquea o desbloquea 
        private final Condition condicion = bloqueo_de_proceso.newCondition(); //variable  para usar en condiciones junto con los bloqueos si se cumple una condicón libera el bloqueo
        public int permisos;//variable donde alamcenamos los permisos que se ocuparan.

        public CustomSemaphore(int permisos) {
            this.permisos = permisos;//constructor para inicializar la variable y que no retorne nada.
        }


       public void bloqueo() throws InterruptedException {
            bloqueo_de_proceso.lock();// utilizado para asegurarse de que la sección de código  sea ejecutada por un solo hilo a la vez
            try {
                while (permisos <= 0) {// significa que no hay permisos disponibles en este momento, por lo que el hilo actual se bloquea. 
                    condicion.await();
                }
                permisos--;//decrementa la cantidad de permisos cuando ya salen del blucle while, representando que un permiso a sido utilizado por un proceso
            } 
            finally {//asegura que se libere el bloqueo del proceso, incluso si ocurre una excepción. 
                bloqueo_de_proceso.unlock();
            }
       }

        public void release() { //liberar un permiso, lo que permite que otro hilo lo adquiera.(controlar el acceso a recursos)
            bloqueo_de_proceso.lock();
            try {
                permisos++;//se incrementa el contador de permisos osea liberando un permiso, lo que permitirá que otro hilo pueda adquirirlo y avanzar
                condicion.signal();//Despierta a un hilo que está esperando en el método await()
            } finally {
                bloqueo_de_proceso.unlock();//el bloqueo del proceso se libera
            }
        }
    }

//Empieza  método de selleción de procesos

    static class Proceso implements Runnable {
        private final int numeroProceso;//variable de tipo objeto

        public Proceso(int numeroProceso) {
            this.numeroProceso = numeroProceso;//inicializando variable con el constructor
        }

        @Override//hereda elementos de las otras clases y si nos equivocamos en los parámetros el método no redefine nada.
        public void run() {
            for (int i = 0; i < NUM_LECTURAS_POR_PROCESO; i++) {//ciclo para dar la asignación de tareas de lo procesos
                try {
                    // Esperar a que el semáforo del proceso actual esté en verde.
                    semaforos[numeroProceso - 1].bloqueo();//La resta de 1 se hace porque los índices en Java comienzan en 0, mientras que los números de proceso comienzan en 1
                                                            //llama al método bloqueo creado anteriormente
                    // Realizar la acción del proceso.
                    if (numeroProceso == 1) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para ensamblar");
                    } else if (numeroProceso == 2) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para soldar");
                    } else if (numeroProceso == 3) {
                        System.out.println("Robot " + numeroProceso + " consigue las herramientas para embalar");
                    }

                    // Liberar el semáforo del siguiente proceso.
                    semaforos[numeroProceso % numero_procesos].release();// el operador % (módulo), que calcula el residuo de la división de numeroProceso entre numero_procesos
                                                                        //si tenemos residuo sigue asignando bloqueos y permisos hasta que ya no tengamos ningun residuo 
                    // Mensaje indicando que el proceso ha sido liberado.
                    System.out.println("Robot " + numeroProceso + " libre para hacer su tarea");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();//Permite que se interrumpa un hilo(excepciones de interrupción, asegurándose de que el hilo ha sido interrumpido)
            }
            }
        }
    }
}
