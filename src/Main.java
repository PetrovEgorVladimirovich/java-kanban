import model.Epic;
import model.SubTask;
import model.Task;
import service.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task = taskManager.developTask(new Task("Обычная задача.", "Описание: обычная задача."));
        Task task1 = taskManager.developTask(new Task("Обычная задача 1.", "Описание: обычная задача 1."));

        Epic epic = taskManager.developEpic(new Epic("Большая задача.", "Описание: большая задача."));
        SubTask subTask = taskManager.developSubTask(new SubTask("Подзадача.", "Описание: подзадача."));
        SubTask subTask1 = taskManager.developSubTask(new SubTask("Подзадача 1.", "Описание: подзадача 1."));

        epic.putSubTask(subTask.getId(), subTask);
        epic.putSubTask(subTask1.getId(), subTask1);

        subTask.setIdEpic(epic.getId());
        subTask1.setIdEpic(epic.getId());

        Epic epic1 = taskManager.developEpic(new Epic("Большая задача 1.", "Описание: большая задача 1."));
        SubTask subTask2 = taskManager.developSubTask(new SubTask("Подзадача 2.", "Описание: подзадача 2."));

        epic1.putSubTask(subTask2.getId(), subTask2);

        subTask2.setIdEpic(epic1.getId());

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("\n");

        taskManager.updateTask(task.getId(), task, Status.DONE);
        taskManager.updateTask(task1.getId(), task1, Status.IN_PROGRESS);

        taskManager.updateSubTask(subTask.getId(), subTask, Status.DONE);
        taskManager.updateSubTask(subTask1.getId(), subTask1, Status.IN_PROGRESS);

        taskManager.updateSubTask(subTask2.getId(), subTask2, Status.DONE);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("\n");

        taskManager.deleteByIdTask(task.getId());

        taskManager.deleteByIdEpic(epic.getId());

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("\n" + taskManager.getByIdSubTask(7));
        System.out.println(taskManager.getByIdEpic(6));
        System.out.println(taskManager.getByIdTask(2));
        System.out.println(taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + taskManager.getByIdEpic(6));
        System.out.println(taskManager.getByIdTask(2));
        System.out.println(taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + taskManager.getByIdSubTask(7));
        System.out.println(taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + taskManager.getByIdSubTask(7));
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + "\n" + "\n");

        Task task2 = taskManager.developTask(new Task("Обычная задача 2.", "Описание: обычная задача 2."));
        Task task3 = taskManager.developTask(new Task("Обычная задача 3.", "Описание: обычная задача 3."));

        Epic epic2 = taskManager.developEpic(new Epic("Большая задача 2.", "Описание: большая задача 2."));
        SubTask subTask3 = taskManager.developSubTask(new SubTask("Подзадача 3.", "Описание: подзадача 3."));
        SubTask subTask4 = taskManager.developSubTask(new SubTask("Подзадача 4.", "Описание: подзадача 4."));
        SubTask subTask5 = taskManager.developSubTask(new SubTask("Подзадача 5.", "Описание: подзадача 5."));
        epic2.putSubTask(subTask3.getId(), subTask3);
        epic2.putSubTask(subTask4.getId(), subTask4);
        epic2.putSubTask(subTask5.getId(), subTask5);
        subTask3.setIdEpic(epic2.getId());
        subTask4.setIdEpic(epic2.getId());
        subTask5.setIdEpic(epic2.getId());


        Epic epic3 = taskManager.developEpic(new Epic("Большая задача 3.", "Описание: большая задача 3."));


        taskManager.getByIdEpic(epic2.getId());
        taskManager.getByIdEpic(epic2.getId());
        taskManager.getByIdEpic(epic2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdTask(task3.getId());
        taskManager.getByIdTask(task3.getId());
        taskManager.getByIdTask(task3.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdEpic(epic3.getId());
        taskManager.getByIdEpic(epic3.getId());
        taskManager.getByIdEpic(epic3.getId());
        System.out.println("\n" + taskManager.getHistory());

        taskManager.getByIdSubTask(subTask3.getId());
        taskManager.getByIdSubTask(subTask3.getId());
        taskManager.getByIdSubTask(subTask3.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdTask(task2.getId());
        taskManager.getByIdTask(task2.getId());
        taskManager.getByIdTask(task2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdSubTask(subTask3.getId());
        System.out.println("\n" + taskManager.getHistory());

        taskManager.getByIdSubTask(subTask5.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdTask(task2.getId());
        System.out.println("\n" + taskManager.getHistory());

        taskManager.getByIdEpic(epic2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdEpic(epic3.getId());
        System.out.println("\n" + taskManager.getHistory());


        taskManager.getByIdSubTask(subTask4.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.getByIdTask(task3.getId());
        System.out.println("\n" + taskManager.getHistory());

        System.out.println("\n" + "\n" + "\n");

        taskManager.deleteByIdTask(task1.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdTask(task2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdTask(task3.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdSubTask(subTask5.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdSubTask(subTask2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdSubTask(subTask.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdEpic(epic.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdEpic(epic1.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdEpic(epic2.getId());
        System.out.println("\n" + taskManager.getHistory());
        taskManager.deleteByIdEpic(epic3.getId());
        System.out.println("\n" + taskManager.getHistory());
    }
}