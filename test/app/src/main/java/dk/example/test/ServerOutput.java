package dk.example.test;


import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerOutput extends Thread{//Writes out to the server
    private Socket socket = null;
    private DataOutputStream output = null;
    private String msg = null;

    public ServerOutput(Socket socket, String msg)
    {
        this.socket = socket;
        this.msg = msg;
        try {
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try {
            output.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}