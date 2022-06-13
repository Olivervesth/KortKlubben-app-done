package dk.example.test;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {
    private TrafficHandler th = new TrafficHandler();
    private User user;
    private Socket socket = null;
    private String address = "192.168.1.11";
    private int port = 5004;
    private SocketData socketdata = new SocketData();
    private String message;
    private DataInputStream input = null;
    private List<Card> deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        new Thread(() -> connect()).start();
        th.setMain(this);//Here we should make a interface instead but time is null for me to do this now
    }

    public void connect() {//Connecting the socket to the server
        try {
            socket = new Socket(socketdata.Ipaddress(), socketdata.Port());
            new Thread(new ServerInput(this, socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void redirect_Register(View view){
        setContentView(R.layout.register);
    }
    //Redirect to changePlayerName screen
    public void redirect_changePlayerName(View view) {
        setContentView(R.layout.change_playername);
    }
    //Redirect to changePassword screen
    public void redirect_changePassword(View view) {
        setContentView(R.layout.change_password);
    }
    //Redirect to login screen
    public void redirect_login(View view) {
        setContentView(R.layout.login);
    }
    public void redirect_game(){//Displays the game to the user
        setContentView(R.layout.game);// Redirect to the game view
        setupDropZone();
        th.gameStartHandler(user);// Starting restrictions for game socket endpoints
    }
    public void redirect_Menu() {
        setContentView(R.layout.menu);//Redirects to the menu
        TextView playername = (TextView) findViewById(R.id.playernamelabel);//Insterting the playername into the menu so the user can see their playername
        playername.setText("Logged in as " + user.getPlayername());
    }
    //Register new user checks for empty fields
    public void registerNewUser(View view) {
        EditText username_r = (EditText) findViewById(R.id.register_username_field);
        EditText playername = (EditText) findViewById(R.id.register_playername_field);
        EditText psw_r = (EditText) findViewById(R.id.register_password_field);
        //Check for empty fields
        if (TextUtils.isEmpty(username_r.getText())) {
            username_r.setError("Username is required!");
        } else if (TextUtils.isEmpty(playername.getText())) {
            playername.setError("Playername is required!");
        } else if (TextUtils.isEmpty(psw_r.getText())) {
            psw_r.setError("Password is required!");
        }
        user = new User();//Saving the user so we can get the information when we need it
        user.setUsername(username_r.getText().toString());
        user.changePlayername(playername.getText().toString());
        th.register(user, psw_r.getText().toString());
    }
    //Change password checks for empty fields
    public void changePassword(View view) {
        EditText psw = findViewById(R.id.change_password_field);
        EditText psw2 = findViewById(R.id.change_password_field2);
        if (TextUtils.isEmpty(psw.getText())) {
            psw.setError("Password is required!");
        } else if (TextUtils.isEmpty(psw2.getText())) {
            psw2.setError("Password is required!");
        } else {
            if (psw.getText().toString().equals(psw2.getText().toString())) {

                th.changePassword(user,psw2.getText().toString());//Calling traffic handler

            } else {
                psw2.setError("Password's are not matching!");
            }

        }
    }
    //Change playername checks for empty field
    public void changePlayername(View view) {
        EditText playername = findViewById(R.id.change_playername_field);
        if (TextUtils.isEmpty(playername.getText())) {
            playername.setError("New playername is required!");
        }else{
            th.changePlayerName(playername.getText().toString(),user.getUsername());//Calling traffic handler
        }

    }
    //Login checks for empty fields
    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.login_username_field);
        EditText psw = (EditText) findViewById(R.id.login_password_field);
        //Check for empty fields
        if (TextUtils.isEmpty(username.getText())) {
            username.setError("Username is required!");
        } else if (TextUtils.isEmpty(psw.getText())) {
            psw.setError("Password is required!");
        } else {
            user = new User();
            user.setUsername(username.getText().toString());
            th.login(user,psw.getText().toString());//Calling traffic handler login method

        }
    }
//    public void loginEvent(String psw) {
//        th.login(user,psw);//Calling traffic handler
//    }
    public void sendMessage(String data) {
        new Thread(new ServerOutput(socket, data)).start();//Sends a message to the serveroutput
    }

//    public void setMessage(String msg)
//    {
//        message = msg;
//    }
//    public String getMessage(){
//        return message;
//    }

    public void setInputReader(DataInputStream in){
        th.setInputReader(in);//Calling traffic handler
    }

    public void setPlayername(String name){
        user.changePlayername(name);
    }
    public void createRoom(View view) {
        th.createRoom();
        redirect_game();
    }
     // Sets the dropzone whereto drag and drop the cards
    private void setupDropZone() {
        ImageView imageView = findViewById(R.id.dropZone);

        // Set the drag event listener for the View.
        imageView.setOnDragListener((v, e) -> {

            // Handles each of the expected events.
            switch (e.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data.
                    if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do, applies a blue color tint
                        // to the View to indicate that it can accept data.
                        ((ImageView) v).setColorFilter(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint.
                        v.invalidate();

                        // Returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false to indicate that, during the current drag and drop operation,
                    // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View.
                    ((ImageView) v).setColorFilter(Color.GREEN);

                    // Invalidates the view to force a redraw in the new tint.
                    v.invalidate();

                    // Returns true; the value is ignored.
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event.
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Resets the color tint to blue.
                    ((ImageView) v).setColorFilter(Color.BLUE);

                    // Invalidates the view to force a redraw in the new tint.
                    v.invalidate();

                    // Returns true; the value is ignored.
                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data.
                    ClipData.Item item = e.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    CharSequence dragData = item.getText();
                    String[] data;
                    data = dragData.toString().split("_");
//                    th.playCard(user,new Card(data[1],data[0]));
                    List<Card> removedcards = deck;
                    for (Card card:deck) {
                        if (card.suit().equals(data[0]) && card.value().equals(data[1])){
                            th.playCard(user,card);//Tells the traffic handler which card has been played by the user
                            removedcards.remove(card);
                            cardPlayed(card);//Shows what card has been played last
                            LinearLayout ll =findViewById(R.id.playerHandLayout);
                            final int childCount = ll.getChildCount();
                            for (int i = 0; i < childCount; i++) {//Removes the card after it have been dropped
                                View child = ll.getChildAt(i);
                                if (child != null){
                                    if (child.getTag().equals(dragData)){
                                        ((ViewGroup) child.getParent()).removeView(child);
                                    }

                                }

                            }
                            break;
                        }
                    }
                    deck = removedcards;
                    // Displays a message containing the dragged data.
                    Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    // Turns off any color tints.
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw.
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting.
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw.
                    v.invalidate();


                    // Does a getResult(), and displays what happened.
                    if (e.getResult()) {
                        Toast.makeText(this, "You played a card.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Something went wrong try again.", Toast.LENGTH_LONG).show();
                    }

                    // Returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.");
                    break;
            }

            return false;

        });
    }
    //Sets a drag event on the imageview so it can be draged and dropped
    private void setupDragEvent(ImageView imageView) {
        imageView.setLongClickable(true);

        // Sets a long click listener for the ImageView using an anonymous listener object that
        // implements the OnLongClickListener interface.
        imageView.setOnLongClickListener(v -> {

            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.

            // Create a new ClipData.Item from the ImageView object's tag.
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This creates a new ClipDescription object within the
            // ClipData and sets its MIME type to "text/plain".
            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item);

            // Instantiate the drag shadow builder.
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);

            // Start the drag.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(dragData,  // The data to be dragged
                        myShadow,  // The drag shadow builder
                        null,      // No need to use local data
                        0          // Flags (not currently used, set to 0)
                );
            }

            // Indicate that the long-click was handled.
            return true;
        });


    }
    // Fills up the user deck
    public void setHand(List<Card> cards){
        showHand(cards);
    }
    //Shows the hand to the user
    public void showHand(List<Card> cards){
        deck=cards;//Save the deck given to the user
        LinearLayout ll = (LinearLayout)findViewById(R.id.playerHandLayout);
        for (Card card:deck) {//Loops through the card deck to give it parameters and add it to the view
            ImageView imageviewcard = new ImageView(this);
            imageviewcard.setTag(card.suit()+"_"+card.value());
            Context context = imageviewcard.getContext();
            int id = this.getResources().getIdentifier(card.suit()+"_"+card.value(), "drawable", context.getPackageName());
            imageviewcard.setBackgroundResource(id);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            imageviewcard.setLayoutParams(param);
            setupDragEvent(imageviewcard);//Adds drag event so we can drag and drop det playing card
            ll.addView(imageviewcard);
        }

    }
    //Tells the user they won
    public void gameWonDialog(){
        ViewGroup gamelayout = (ViewGroup) getLayoutInflater().inflate(R.layout.game, null);
        new AlertDialog.Builder(gamelayout.getContext())
                .setTitle("Concgratulations you won").show();
    }
    public void gameLostDialog(){
        ViewGroup gamelayout = (ViewGroup) getLayoutInflater().inflate(R.layout.game, null);
        new AlertDialog.Builder(gamelayout.getContext())
                .setTitle("You lost :(").show();
    }

    public void usersTurnDialog(){
        ViewGroup gamelayout = (ViewGroup) getLayoutInflater().inflate(R.layout.game, null);
        new AlertDialog.Builder(gamelayout.getContext())
                .setTitle("Its your turn").show();
    }
    public void cardPlayed(Card card){
        ImageView dropzone = findViewById(R.id.dropZone);
        int id = this.getResources().getIdentifier(card.suit()+"_"+card.value(), "drawable", dropzone.getContext().getPackageName());
        dropzone.setBackgroundResource(id);
    }

}