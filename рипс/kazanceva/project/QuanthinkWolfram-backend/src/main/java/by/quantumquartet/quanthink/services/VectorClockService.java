package by.quantumquartet.quanthink.services;

import by.quantumquartet.quanthink.models.VectorClock;
import by.quantumquartet.quanthink.models.Message;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class VectorClockService {
    private final HashMap<String, Integer> activeVector = new HashMap<>();
    private final List<Message> history = new ArrayList<>();
    private final HashMap<String, Message> lastUserEntry = new HashMap<>();

    public VectorClock getLogicalTimestamp(String email) {
        return new VectorClock(activeVector);
    }

    public void incrementClock(String email) {
        activeVector.put(email, activeVector.getOrDefault(email, 0) + 1);
        Message entry = lastUserEntry.get(email);
        if (entry != null) {
            entry.setTimestamp(new VectorClock(new HashMap<>(activeVector))); // Обновляем timestamp у существующего сообщения
            lastUserEntry.put(email, entry); // Обновляем lastUserEntry
        }
    }

    public void addHistoryEntry(String email, Message message) {
        VectorClock newTimestamp = new VectorClock(new HashMap<>(activeVector));
        message.setTimestamp(newTimestamp);

        history.add(message);
        lastUserEntry.put(email, message);
    }

    public List<Message> getHistory() {
        return new ArrayList<>(history);
    }

    public void removeUser(String email) {
        activeVector.remove(email);
    }

    public boolean isUserExists(String email) {
        return activeVector.containsKey(email);
    }

    public void addUser(String email) {
        activeVector.put(email, 0);
    }

    public int getLastTimestampFromHistory(String email) {
        Message entry = lastUserEntry.get(email);
        if (entry != null) {
            return entry.getTimestamp().getTimestamp(email);
        }
        return 0;
    }

    public VectorClock getInitialTimestamp() {
        return new VectorClock(new HashMap<>());
    }

    public void removeLastUserEntry(String email) {
        lastUserEntry.remove(email);
    }
}
