import model.Epic;
import model.SubTask;
import model.Task;
import service.Manager;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task = manager.developTask(new Task("Обычная задача.", "Описание: обычная задача."));
        Task task1 = manager.developTask(new Task("Обычная задача 1.", "Описание: обычная задача 1."));

        Epic epic = manager.developEpic(new Epic("Большая задача.", "Описание: большая задача."));
        SubTask subTask = manager.developSubTask(new SubTask("Подзадача.", "Описание: подзадача."));
        SubTask subTask1 = manager.developSubTask(new SubTask("Подзадача 1.", "Описание: подзадача 1."));

        epic.putSubTask(subTask.getId(), subTask);
        epic.putSubTask(subTask1.getId(), subTask1);

        subTask.setIdEpic(epic.getId());
        subTask1.setIdEpic(epic.getId());

        Epic epic1 = manager.developEpic(new Epic("Большая задача 1.", "Описание: большая задача 1."));
        SubTask subTask2 = manager.developSubTask(new SubTask("Подзадача 2.", "Описание: подзадача 2."));

        epic1.putSubTask(subTask2.getId(), subTask2);

        subTask2.setIdEpic(epic1.getId());

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println("/n");

        manager.updateTask(task.getId(), task, "Done");
        manager.updateTask(task1.getId(), task1, "In_Progress");

        manager.updateSubTask(subTask.getId(), subTask, "Done");
        manager.updateSubTask(subTask1.getId(), subTask1, "In_Progress");

        manager.updateSubTask(subTask2.getId(), subTask2, "Done");

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println("/n");

        manager.deleteByIdTask(task.getId());

        manager.deleteByIdEpic(epic.getId());

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
    }
}
