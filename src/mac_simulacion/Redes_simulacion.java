/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_simulacion;

/**
 *
 * @author maestria
 */
public class Redes_simulacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double velMaxCanal = 20 * Math.pow(10, 6),
                lambda = 100,
                tamPakt = 10 * Math.pow(10, 3),
                miu, c;
        int n = 10000;

        int clients[] = {1,5,10,11,12,13,14,15,16,17,18,19,20};
        

        Object dataOcupacion[][] = new Object[3][4 * clients.length];
        Object dataTiempoSistema[][] = new Object[3][4 * clients.length];
        Object dataTiempoCola[][] = new Object[3][4 * clients.length];

        for (int i = 0, t, i2, t2; i < clients.length; i++) {

            c = (float) (velMaxCanal / clients[i]);
            t = i + clients.length;
            i2 = i + clients.length * 2;
            t2 = i + clients.length * 3;

            miu = (float) (c / tamPakt);

            // TAMANNO FIJO 
            simulacionTamanoFijo simulacionTamFijo = new simulacionTamanoFijo(n, lambda, miu);
            simulacionTamFijo.run();
            dataOcupacion[0][i] = simulacionTamFijo.getReal_E_n();
            dataOcupacion[1][i] = clients[i];
            dataOcupacion[2][i] = "Probabilidad real";

            dataTiempoSistema[0][i] = simulacionTamFijo.getReal_TPsistema();
            dataTiempoSistema[1][i] = clients[i];
            dataTiempoSistema[2][i] = "Probabilidad real";

            dataTiempoCola[0][i] = simulacionTamFijo.getRealTPcola();
            dataTiempoCola[1][i] = clients[i];
            dataTiempoCola[2][i] = "Probabilidad real";

            dataOcupacion[0][t] = simulacionTamFijo.getTeoric_E_n();
            dataOcupacion[1][t] = clients[i];
            dataOcupacion[2][t] = "Probabilidad teórica";

            dataTiempoCola[0][t] = simulacionTamFijo.getTeoric_TPcola();
            dataTiempoCola[1][t] = clients[i];
            dataTiempoCola[2][t] = "Probabilidad teórica";

            dataTiempoSistema[0][t] = simulacionTamFijo.getTeoric_TPsistema();
            dataTiempoSistema[1][t] = clients[i];
            dataTiempoSistema[2][t] = "Probabilidad teórica";

            System.out.println("Cantidad de clientes---" + clients[i]);
            System.out.println("Velocidad de tx---" + c);
            System.out.println("Miu-----" + miu);
            System.out.println("Cantidad promedio real de paquetes en cola---" + simulacionTamFijo.getReal_E_n());
            System.out.println("Cantidad promedio teorica de paquetes en cola---" + simulacionTamFijo.getTeoric_E_n());

            System.out.println("Tiempo promedio real en sistema---" + simulacionTamFijo.getReal_TPsistema());
            System.out.println("Tiempo promedio teorico en sistema---" + simulacionTamFijo.getTeoric_TPsistema());
            System.out.println("Tiempo promedio real en cola---" + simulacionTamFijo.getRealTPcola());
            System.out.println("Tiempo promedio teorico en cola---" + simulacionTamFijo.getTeoric_TPcola());

            //TAMANNO VARIABLE  simulacionTamanoVariable(float lambda, float miu, float data_rate, float lambda_pkt) 
            simulacionTamanoVariable simulacionTamVariable = new simulacionTamanoVariable(n, lambda, miu, c, tamPakt);
            simulacionTamVariable.run();
            dataOcupacion[0][i2] = simulacionTamVariable.getReal_E_n();
            dataOcupacion[1][i2] = clients[i];
            dataOcupacion[2][i2] = "Probabilidad real tamaño variable";

            dataTiempoSistema[0][i2] = simulacionTamVariable.getReal_TPsistema();
            dataTiempoSistema[1][i2] = clients[i];
            dataTiempoSistema[2][i2] = "Probabilidad real tamaño variable";

            dataTiempoCola[0][i2] = simulacionTamVariable.getRealTPcola();
            dataTiempoCola[1][i2] = clients[i];
            dataTiempoCola[2][i2] = "Probabilidad real tamaño variable";

            dataOcupacion[0][t2] = simulacionTamVariable.getTeoric_E_n();
            dataOcupacion[1][t2] = clients[i];
            dataOcupacion[2][t2] = "Probabilidad teórica tamaño variable";

            dataTiempoCola[0][t2] = simulacionTamVariable.getTeoric_TPcola();
            dataTiempoCola[1][t2] = clients[i];
            dataTiempoCola[2][t2] = "Probabilidad teórica tamaño variable";

            dataTiempoSistema[0][t2] = simulacionTamVariable.getTeoric_TPsistema();
            dataTiempoSistema[1][t2] = clients[i];
            dataTiempoSistema[2][t2] = "Probabilidad teórica tamaño variable";

            System.out.println("TAMANO VARIABLE");
            System.out.println("Cantidad promedio real de paquetes en cola---" + simulacionTamVariable.getReal_E_n());
            System.out.println("Cantidad promedio teorica de paquetes en cola---" + simulacionTamVariable.getTeoric_E_n());

            System.out.println("Tiempo promedio real en sistema---" + simulacionTamVariable.getReal_TPsistema());
            System.out.println("Tiempo promedio teorico en sistema---" + simulacionTamVariable.getTeoric_TPsistema());
            System.out.println("Tiempo promedio real en cola---" + simulacionTamVariable.getRealTPcola());
            System.out.println("Tiempo promedio teorico en cola---" + simulacionTamVariable.getTeoric_TPcola());

        }
        showChartOcupacion(dataOcupacion);
        showChartTiempoenCola(dataTiempoCola);
        showChartTiempoenSistema(dataTiempoSistema);

    }

    static void showChartOcupacion(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Cantidad de paquetes en cola");
        probabilidades_n.setVisible(true);
    }

    static void showChartTiempoenCola(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Tiempo en cola");
        probabilidades_n.setVisible(true);
    }

    static void showChartTiempoenSistema(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Tiempo en el sistema");
        probabilidades_n.setVisible(true);
    }
}
