package by.quantumquartet.quanthink.rest.responses.messages;

import by.quantumquartet.quanthink.models.EMessage;

public class MessageDto {
    private long id;
    private EMessage type;
    private String content;
    private String date;
    private long senderId;
    private long receiverId;

    public MessageDto() {
    }

    public MessageDto(long id, EMessage type, String content, String date, long senderId, long receiverId) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.date = date;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EMessage getType() {
        return type;
    }

    public void setType(EMessage type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }
}
