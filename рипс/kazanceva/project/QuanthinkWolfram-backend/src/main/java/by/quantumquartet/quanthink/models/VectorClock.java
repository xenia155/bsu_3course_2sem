package by.quantumquartet.quanthink.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VectorClock implements Comparable<VectorClock> {
    private HashMap<String, Integer> activeVector;

    public VectorClock() {
        this.activeVector = new HashMap<>();
    }

    public VectorClock(HashMap<String, Integer> activeVector) {
        this.activeVector = new HashMap<>(activeVector);
    }

    public HashMap<String, Integer> getActiveVector() {
        return new HashMap<>(activeVector);
    }

    public void setActiveVector(HashMap<String, Integer> activeVector) {
        this.activeVector = new HashMap<>(activeVector);
    }

    public int getTimestamp(String userId) {
        return activeVector.getOrDefault(userId, 0);
    }

    public void setTimestamp(String userId, int timestamp) {
        this.activeVector.put(userId, timestamp);
    }

    @Override
    public int compareTo(VectorClock other) {
        Set<String> allUserIds = new HashSet<>(this.activeVector.keySet());
        allUserIds.addAll(other.activeVector.keySet());

        boolean thisGreater = false;
        boolean otherGreater = false;

        for (String userId : allUserIds) {
            int thisTimestamp = this.activeVector.getOrDefault(userId, 0);
            int otherTimestamp = other.activeVector.getOrDefault(userId, 0);

            if (thisTimestamp > otherTimestamp) {
                thisGreater = true;
            } else if (thisTimestamp < otherTimestamp) {
                otherGreater = true;
            }
        }

        if (thisGreater && !otherGreater) {
            return 1;
        } else if (!thisGreater && otherGreater) {
            return -1;
        } else {
            return 0;
        }
    }
}
