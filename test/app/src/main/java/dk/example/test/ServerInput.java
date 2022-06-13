package dk.example.test;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerInput  extends Thread{//Gets input from the server

    private MainActivity ma = null;
    private Socket socket = null;
    private DataInputStream input = null;
    public ServerInput(MainActivity ma, Socket socket)
    {
        this.socket = socket;
        this.ma = ma;
        try {
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        while(!line.equals("true")){
            try {
                line = input.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            ma.setInputReader(input);
    }

    public void run()
    {


        String line = "";
        while(!(line.equals("Done")))//Most important code in the project right here
        {

        }
    }
}