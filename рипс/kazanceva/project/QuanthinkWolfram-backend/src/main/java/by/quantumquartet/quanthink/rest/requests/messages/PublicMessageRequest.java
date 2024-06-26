package by.quantumquartet.quanthink.rest.requests.messages;

public class PublicMessageRequest {
    private long senderId;
    private String content;

    public PublicMessageRequest(long senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
