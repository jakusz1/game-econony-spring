package pl.jakusz.gameeconomyspring.model;


public class Transfer{
    private String receiverUsername;
    private int value;

    public Transfer(){

    }

    public Transfer(String receiverUsername, int value) {
        this.receiverUsername = receiverUsername;
        this.value = value;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
