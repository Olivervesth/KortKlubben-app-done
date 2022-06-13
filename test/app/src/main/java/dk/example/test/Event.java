package dk.example.test;
public class Event {//Event to send data as an object

    private String action;
    private String data;
    public Event(String newaction, String data){
        this.action = newaction;
        this.data = data;
    }
    public String action(){
        return action;
    }
    public String data(){
        return data;
    }
}
