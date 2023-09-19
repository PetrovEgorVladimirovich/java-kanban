package manager;

import KVServer.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.FileBackedTasksManager;
import service.HttpTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.assistants.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public final class Managers { // Утилитарный класс
    private Managers() {
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked(String file) {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getDefaultHttpTask(){
        return new HttpTaskManager("http://localhost:", KVServer.PORT);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}