package dk.example.test;

public class User {

    private String userName;
    private String playerName;

    public void user(String userName, String playerName){
        this.playerName = playerName;
        this.userName = userName;
    }

    public void changePlayername(String newname){
        playerName = newname;
    }
    public String getUsername(){
        return userName;
    }
    public String getPlayername(){
        return playerName;
    }
    public void setUsername(String newname){
        userName = newname;
    }
}
