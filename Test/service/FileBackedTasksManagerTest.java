package service;

import manager.Managers;
import manager.TaskManager;
import manager.TaskManagerTest;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.assistants.ManagerSaveException;
import service.assistants.Type;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {


    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTasksManager("SaveTest.csv");
        create();

    }

    @Test
    void save() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/SaveTest.csv"))) {
            while (reader.read() != -1) {
                String line = reader.readLine();
                String[] str = line.split(",");
                if (!line.startsWith("id")) {
                    if (str[1].equals(Type.EPIC.toString())) {
                        Epic epic = new Epic(str[2], str[4]);
                        assertEquals(0, epic.getSubTasks().size(),
                                "Большая задача должна быть без подзадач!");
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения!");
        }
        taskManager.clearTask();
        taskManager.clearEpic();
        Managers.getDefaultHistory().clear();
        taskManager.save();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/SaveTest.csv"))) {
            while (reader.read() != -1) {
                String line = reader.readLine();
                assertEquals("id,type,name,status,description,startTime,duration,endTime,epic", line,
                        "Файл должен быть пустым (только с заголовком)!");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения!");
        }
    }

    @Test
    void loadFromFile() {
        taskManager.clearSubTask();
        taskManager.save();
        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("src/resources/SaveTest.csv"));
        Epic epic1 = taskManager1.getAllEpic().get(0);
        assertEquals(0, epic1.getSubTasks().size(), "Большая задача должна быть без подзадач!");
        taskManager.clearEpic();
        taskManager.clearTask();
        Managers.getDefaultHistory().clear();
        taskManager.save();
        taskManager1 = FileBackedTasksManager.loadFromFile(new File("src/resources/SaveTest.csv"));
        assertEquals(0, taskManager1.getAllTask().size(), "Список обычных задач должен быть пустым!");
        assertEquals(0, taskManager1.getAllEpic().size(), "Список больших задач должен быть пустым!");
        assertEquals(0, taskManager1.getAllSubTask().size(), "Список подзадач должен быть пустым!");
        assertEquals(0, taskManager1.getHistory().size(), "Список истории должен быть пустым!");
    }
}