package service;

public final class Managers { // Утилитарный класс
    private Managers() {
    }

    private final static TaskManager taskManager = new InMemoryTaskManager();
    private final static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}