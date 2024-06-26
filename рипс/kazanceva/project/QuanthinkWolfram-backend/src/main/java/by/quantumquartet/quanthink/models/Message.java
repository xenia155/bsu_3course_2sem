package by.quantumquartet.quanthink.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.IOException;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EMessage type;

    @NotBlank
    @Column(nullable = false)
    private String content;

    @NotBlank
    @Column(nullable = false)
    private Timestamp date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @NotBlank
    @Transient
    @Convert(converter = LogicalTimestampConverter.class)
    private VectorClock timestamp;

    public Message() {
    }

    public Message(EMessage type, String content, Timestamp date,
                   User sender, User receiver, VectorClock timestamp) {
        this.type = type;
        this.content = content;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


    public VectorClock getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(VectorClock timestamp) {
        this.timestamp = timestamp;
    }

    @Converter
    public static class LogicalTimestampConverter implements AttributeConverter<VectorClock, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(VectorClock attribute) {
            try {
                return objectMapper.writeValueAsString(attribute.getActiveVector());
            } catch (IOException e) {
                throw new RuntimeException("Error converting LogicalTimestamp to JSON", e);
            }
        }

        @Override
        public VectorClock convertToEntityAttribute(String dbData) {
            try {
                return new VectorClock(objectMapper.readValue(dbData, new TypeReference<>() {
                }));
            } catch (IOException e) {
                throw new RuntimeException("Error converting JSON to LogicalTimestamp", e);
            }
        }
    }
}
