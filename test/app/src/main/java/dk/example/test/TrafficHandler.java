package dk.example.test;

import java.io.DataInputStream;

public class TrafficHandler {
    private EventHandler eventHandler = new EventHandler();
    //Game traffic
    public void playCard(User user,Card card) {
        String data = "playcard;" + card.value() + ";" + card.suit();//Message for the server
//        new Thread(new ServerOutput(clientSocket.getSocket(), data)).start();
        sendOutput(data, "login");
    }
    //Menu traffic
    public void login(User user, String psw) {
        String data = "login;" + user.getUsername() + ";" + psw;
//        new Thread(new ServerOutput(clientSocket.getSocket(), data)).start();
        sendOutput(data, "login");
    }

    public void register(User user, String psw) {
        String data = "register;" + user.getUsername() + ";" + user.getPlayername() + ";" + psw;//Message for the server
        sendOutput(data, "register");
    }

    public void joinRoom(String room) {
        String data = "joinroom;" + room;
        sendOutput(data, "joinroom");
    }

    public void createRoom() {
        String data = "createroom";
        sendOutput(data, "createroom");
    }

    public void changePassword(User user,String psw) {
        String data = "updateuser;psw;"+user.getUsername()+";"+user.getPlayername()+";"+ psw;//Message for the server
        //String data = "updateuser;psw;" + psw; //How it is
        sendOutput(data, "updateuser");
    }

    public void changePlayerName(String clientplayername,String username) {
        String data = "updateuser;Playername;"+clientplayername+";" + username;//Message for the server
        sendOutput(data, "updateuser");
    }
    //Sends output string to the eventhandler
    public void sendOutput(String data, String action) {
        Event event = new Event(action,data);
        eventHandler.startHandler(event);//Starts handling events

    }
    //Sends main to eventhandler
    public void setMain(MainActivity ma){
        eventHandler.setMain(ma);
    }
    //Sends eventhandler input
    public void setInputReader(DataInputStream in){
        eventHandler.setInputReader(in);
    }
    //Tells the eventhandler the game started
    public void gameStartHandler(User user){
        eventHandler.gameStarted(user);
    }
}
