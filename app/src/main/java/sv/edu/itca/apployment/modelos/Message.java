package sv.edu.itca.apployment.modelos;

public class Message {
    private String content;
    private boolean isSentByCurrentUser;

    public Message(String content, boolean isSentByCurrentUser) {
        this.content = content;
        this.isSentByCurrentUser = isSentByCurrentUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }
}
