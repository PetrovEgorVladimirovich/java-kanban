import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import service.*;
import service.enums.Status;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager("Save.csv");

        Task task = new Task("Обычная задача.", "Описание: обычная задача.");
        task.setStartTime("22.12.2022 14:00");
        task.setDuration(90);
        taskManager.developTask(task);

        Task task1 = new Task("Обычная задача 1.", "Описание: обычная задача 1.");
        task1.setStartTime("22.12.2022 12:30");
        task1.setDuration(90);
        taskManager.developTask(task1);


        Epic epic = new Epic("Большая задача.", "Описание: большая задача.");

        SubTask subTask = new SubTask("Подзадача.", "Описание: подзадача.");
        subTask.setStartTime("22.12.2022 09:30");
        subTask.setDuration(90);
        taskManager.developSubTask(subTask);

        SubTask subTask1 = new SubTask("Подзадача 1.", "Описание: подзадача 1.");
        subTask1.setStartTime("22.12.2022 00:30");
        subTask1.setDuration(90);
        taskManager.developSubTask(subTask1);


        epic.putSubTask(subTask.getId(), subTask);
        epic.putSubTask(subTask1.getId(), subTask1);
        epic.countTime();
        taskManager.developEpic(epic);

        subTask.setIdEpic(epic.getId());
        subTask1.setIdEpic(epic.getId());

        Epic epic1 = new Epic("Большая задача 1.", "Описание: большая задача 1.");

        SubTask subTask2 = new SubTask("Подзадача 2.", "Описание: подзадача 2.");
        subTask2.setStartTime("22.12.2022 21:30");
        subTask2.setDuration(90);
        taskManager.developSubTask(subTask2);

        epic1.putSubTask(subTask2.getId(), subTask2);
        epic1.countTime();
        taskManager.developEpic(epic1);

        subTask2.setIdEpic(epic1.getId());

        taskManager.updateTask(task.getId(), task, Status.DONE);
        taskManager.updateTask(task1.getId(), task1, Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask.getId(), subTask, Status.DONE);
        taskManager.updateSubTask(subTask1.getId(), subTask1, Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask2.getId(), subTask2, Status.DONE);

        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("1\n" + taskManager.getByIdSubTask(subTask2.getId()));
        System.out.println("2\n" + taskManager.getByIdEpic(epic1.getId()));
        System.out.println("3\n" + taskManager.getByIdTask(task1.getId()));
        System.out.println("4\n" + taskManager.getByIdSubTask(subTask2.getId()));
        System.out.println("5\n" + taskManager.getByIdEpic(epic1.getId()));
        System.out.println("6\n" + taskManager.getByIdTask(task1.getId()));
        System.out.println("7\n" + taskManager.getByIdSubTask(subTask2.getId()));
        System.out.println("8\n" + taskManager.getAllTask());
        System.out.println("9\n" + taskManager.getAllEpic());
        System.out.println("10\n" + taskManager.getAllSubTask());
        System.out.println("11\n" + taskManager.getHistory());
        System.out.println("12\n" + taskManager.getByIdTask(task.getId()));

        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("src/resources/Save.csv"));

        System.out.println("13\n" + taskManager1.getAllTask());
        System.out.println("14\n" + taskManager1.getAllEpic());
        System.out.println("15\n" + taskManager1.getAllSubTask());
        System.out.println("16\n" + taskManager1.getHistory());
    }
}