package service;

import KVServer.KVServer;
import manager.Managers;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer server;

    private HttpTaskServer taskServer;

    @BeforeEach
    void setUp() throws IOException {
        server = new KVServer();
        server.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
        taskManager = Managers.getDefaultHttpTask();
        create();
    }

    @AfterEach
    void afterEach() {
        server.stop(0);
        taskServer.stop(0);
    }

    @Test
    void shouldLoad() {
        HttpTaskManager manager1 = Managers.getDefaultHttpTask();
        assertEquals(0, manager1.getAllEpic().size(), "Список больших задач должен быть пустой!");
        manager1.load();
        assertNotNull(manager1.getAllSubTask(),
                "Список всех подзадач должен быть не пустой!");
        assertEquals(taskManager.getByIdTask(task.getId()), manager1.getByIdTask(task.getId()),
                "Обычные задачи должны быть равны!");
        assertEquals(taskManager.getByIdSubTask(subTask.getId()), manager1.getByIdSubTask(subTask.getId()),
                "Подзадачи задачи должны быть равны!");
        assertEquals(taskManager.getByIdEpic(epic.getId()), manager1.getByIdEpic(epic.getId()),
                "Большие задачи должны быть равны!");
    }
}