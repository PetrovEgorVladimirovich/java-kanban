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
    }
}