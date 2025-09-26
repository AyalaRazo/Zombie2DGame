/*
 * Click nbfs:
 */

package io.github.Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    
    private final int puerto = 2027;
    
    private final int noConexiones = 2;
    
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();

   
    public void escuchar(){
        try {

            
            ServerSocket servidor = new ServerSocket(puerto,noConexiones);
            
            System.out.println("Esperando jugadores....");
            while(true){
                    
                    Socket cliente = servidor.accept();
                    
                    usuarios.add(cliente);

                    
                    Runnable  run = new HiloServidor(cliente,usuarios);
                    Thread hilo = new Thread(run);
                    hilo.start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        Server servidor= new Server();
        servidor.escuchar();
    }
}
