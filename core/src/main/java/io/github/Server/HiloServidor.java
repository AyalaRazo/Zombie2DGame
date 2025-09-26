


package io.github.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class HiloServidor implements Runnable{
    //Declaramos las variables que utiliza el hilo para estar recibiendo y mandando mensajes
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    //Lista de los usuarios conectados al servidor
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();

    //Constructor que recibe el socket que atendera el hilo y la lista de los jugadores
    public HiloServidor(Socket soc,LinkedList users){
        socket = soc;
        usuarios = users;
    }


    @Override
    public void run() {
        try {
            //Inicializamos los canales de comunicacion
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            String msg = "Conectado...";
            out.writeUTF(msg);


            //Ciclo infinito que estara escuchando a cada jugador
            while(true){
                //Leer los datos que se mandan cuando se selecciona un boton
                String recibidos = in.readUTF();
                String recibido [] = recibidos.split(";");

                String resp = recibidos;

                for (Socket usuario : usuarios) {
                    out = new DataOutputStream(usuario.getOutputStream());
                    out.writeUTF(resp);
                }
            }
        } catch (Exception e) {

            //Si ocurre un excepcion lo mas seguro es que sea por que algun jugador se desconecto asi que lo quitamos de la lista de conectados
            for (int i = 0; i < usuarios.size(); i++) {
                if(usuarios.get(i) == socket){
                    usuarios.remove(i);
                    break;
                }
            }
        }
    }

}
