package dk.example.test;

public class Card {//Card object
    private String value;
    private String suit;

    public Card(String value, String suit){
        this.value = value;
        this.suit = suit;
    }
    public String value(){
        return value;
    }
    public String suit(){
        return suit;
    }
}
