package client;
/*
 * Author: Carl Frisenstam. carl.frisenstam@gmail.com
 * */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private boolean shouldRun = true;

    public ClientConnection(Socket socket, Client client){
        this.socket = socket;

    }

    public void sendToServer(String data){
        try{
            dout.writeUTF(data);
            dout.flush();
        }catch (IOException exception){
            exception.printStackTrace();
            close();
        }
    }

    public void run(){
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            while (shouldRun){
                try {
                    while (din.available()==0){
                        try{
                            Thread.sleep(1);
                        }catch (InterruptedException exception){
                            exception.printStackTrace();
                        }
                    }
                    String reply = din.readUTF();
                    System.out.println(reply);

                }catch (IOException exception){
                    exception.printStackTrace();
                    close();
                }
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

    public void close(){
        try {
            din.close();
            dout.close();
            socket.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

}
