package manager;

import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

public final class Managers { // Утилитарный класс
    private Managers() {
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}