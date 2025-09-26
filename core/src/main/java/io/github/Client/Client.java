package io.github.Client;


import io.github.ayala.Main;
import io.github.ayala.RoundManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
    
    private int puerto = 2027;
    
    private String host = "localhost";

    
    private String mensaje;
    long id;

    RoundManager main;

    
    public Client(RoundManager main) {
        try {
            
            cliente = new Socket(host, puerto);
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            id = System.currentTimeMillis();
            this.main = main;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            mensaje = in.readUTF();
            String split[] = mensaje.split(";");
            System.out.println("Desde cliente: " + id);

            
            while (true) {
                
                mensaje = in.readUTF();
                String[] mensajes = mensaje.split(";");
                System.out.println("Desde cliente: " + mensaje);

                if (id == Long.parseLong(mensajes[0])) {
                    System.out.println("Son iguales");
                    switch (mensajes[1]) {
                        case "Player_Speed":
                            System.out.println("Son iguales");
                            break;
                    }
                    continue;
                }
                switch (mensajes[1]) {
                    case "Wild_Zombie_Faster":
                        main.addFasterWildZombie();
                        break;
                }
                System.out.println("Desde cliente " + mensajes[0]);
                System.out.println(mensajes[1]);

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void enviar(String mensaje_out) {
        try {
            out.writeUTF(id + ";" + mensaje_out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
