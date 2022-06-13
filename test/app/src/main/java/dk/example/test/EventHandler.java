package dk.example.test;

import android.os.Handler;
import android.os.Looper;
import android.text.BoringLayout;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventHandler {
    private MainActivity main;
    private DataInputStream input = null;
    private boolean connected;
    private boolean gamestarted = false;

    public void setMain(MainActivity ma) {//Sets the private main
        main = ma;
    }

    public void setInputReader(DataInputStream in) {//Sets the private input
        input = in;
    }

    public void startHandler(Event event) {//Starts the handler thread
        new Thread(() -> eventHandling(event)).start();
    }

    public void gameStarted(User user) {//Starts the game thread
        new Thread(() -> inGame(user)).start();
    }

    //Waits data about the game from the server
    public void inGame(User user) {
//        final String playername = user.getPlayername().toString();
        while (true) {
            String line = "";
            try {
                line = input.readUTF();
                System.out.println("Im in the ingame");
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] data;
            if (line.contains(";")) {
                int counter = 1;
                data = line.split(";");
                if (data.length > 13) {
                    List<Card> cardlist = new ArrayList();
                    for (int i = 0; i < data.length; i += 2) {
                        cardlist.add(new Card(data[i].toLowerCase(), data[i + 1].toLowerCase()));
                    }
                    setCardsInHand(cardlist);
                } else if (data.length == 2) {
                    Card playedcard = new Card(data[0], data[1].toLowerCase());
                    opponentCardPlayed(playedcard);

                }
            }
            if (line.equals("YourTurn")) {
                yourTurn();
            } else if (line.equals("You Lost")) {
                yourLost();
            } else if (line.equals("You Won")) {
                yourWon();
            }

        }
    }

    //Handles the user events
    public void eventHandling(Event event) {
        if (event.data().equals("createroom")) {
            gamestarted = true;
        }
        main.sendMessage(event.data());
        if (!gamestarted) {

            String line = "";
            try {
                line = input.readUTF();
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Answer HERE - " + line);
            if (stringsEqual(line, "true")) {
                System.out.println("Im in redirect");
            }
            String[] data;
            if (line.contains(";")) {//Change this
                data = line.split(";");
                if (stringsEqual(data[0], "true"))
                    switch (event.action()) {
                        case "login":
                            main.setPlayername(data[1]);
                            redirecter("menu");
                            break;
                    }
//                    eventlist.remove(event);
            }
            if (stringsEqual(line, "true")) {
                switch (event.action()) {
                    case "register":
                    case "updateuser":
                        redirecter("menu");
                        break;
                }
            }
        }
    }

    //Just a possible way to be extra sure that the strings are equals. Java is very precise with bytes and ids
    public boolean stringsEqual(String one, String two) {
        if (one.equals(two) || one.trim() == two || one.trim().equals(two)) {
            return true;
        } else {
            return false;
        }
    }

    private void redirecter(String view) {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {//Get the main thread so i can call main functions
            switch (view) {
                case "menu":
                    main.redirect_Menu();
                    break;
            }
        }, 1000);
    }

    public void setCardsInHand(List<Card> cardlist) {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {//Get the main thread so i can call main functions
            main.setHand(cardlist);
        }, 1000);
    }

    public void yourTurn() {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            main.usersTurnDialog();//Tells main its users turn
        }, 1000);
    }

    public void yourLost() {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            main.gameLostDialog();//Tells main that user lost the game
        }, 1000);
    }

    public void yourWon() {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            main.gameWonDialog();//Tells main that user won the game
        }, 1000);
    }

    public void opponentCardPlayed(Card card) {// I think this is a really bad idea and i dont know what else i could have done
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            main.cardPlayed(card);//Tells main that user won the game
        }, 1000);
    }
}
