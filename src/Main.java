import KVServer.KVServer;
import KVServer.KVTaskClient;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import server.HttpTaskServer;
import service.HttpTaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();
        server.start();
//        TaskManager manager = Managers.getDefaultHttpTask();
//        Task task = new Task("Обычная задача.", "Описание: обычная задача.");
//        Task task1 = new Task("Обычная задача 1.", "Описание: обычная задача 1.");
//
//        Epic epic = new Epic("Большая задача.", "Описание: большая задача.");
//
//        SubTask subTask = new SubTask("Подзадача.", "Описание: подзадача.");
//        SubTask subTask1 = new SubTask("Подзадача 1.", "Описание: подзадача 1.");
//
//        manager.developTask(task);
//        manager.developTask(task1);
//        manager.developEpic(epic);
//        manager.developSubTask(subTask);
//        manager.developSubTask(subTask1);
//
//        System.out.println(manager.getAllTask());
//
//        HttpTaskManager manager1 = Managers.getDefaultHttpTask();
//        manager1.load();
//
//        System.out.println(manager1.getAllTask());
//        server.stop(3);
        HttpTaskServer server1 = new  HttpTaskServer();
        server1.start();
    }
}