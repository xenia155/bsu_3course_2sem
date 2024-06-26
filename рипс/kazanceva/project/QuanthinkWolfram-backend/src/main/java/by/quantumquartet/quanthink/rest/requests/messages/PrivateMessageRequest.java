package by.quantumquartet.quanthink.rest.requests.messages;

public class PrivateMessageRequest extends PublicMessageRequest {
    private long receiverId;

    public PrivateMessageRequest(long senderId, String content) {
        super(senderId, content);
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }
}
