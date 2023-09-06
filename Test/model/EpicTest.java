package model;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.assistants.Status;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    private TaskManager taskManager;
    private Epic epic;
    private SubTask subTask;
    private SubTask subTask1;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Большая задача.", "Описание: большая задача.");
        subTask = new SubTask("Подзадача.", "Описание: подзадача.");
        subTask.setStartTime("22.12.2022 00:30");
        subTask.setDuration(90);
        taskManager.developSubTask(subTask);
        subTask1 = new SubTask("Подзадача 1.", "Описание: подзадача 1.");
        subTask1.setStartTime("22.12.2022 03:30");
        subTask1.setDuration(90);
        taskManager.developSubTask(subTask1);
        connect();
    }

    private void connect() { // Метод для связи эпиков и сабтасок.
        epic.putSubTask(subTask.getId(), subTask);
        epic.putSubTask(subTask1.getId(), subTask1);
        epic.countTime();
        taskManager.developEpic(epic);
        subTask.setIdEpic(epic.getId());
        subTask1.setIdEpic(epic.getId());
    }

    private List<Status> getStatuses() {
        return epic.getSubTasks().stream()
                .map(SubTask::getStatus)
                .collect(Collectors.toList());
    }

    @Test
    void shouldBeEmptyListOfSubtasks() {
        taskManager.clearSubTask();
        assertEquals(0, epic.getSubTasks().size(), "Список подзадач должен быть пустым!");
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Большая задача должна иметь статус: NEW!");
    }

    @Test
    void shouldBeAllSubtasksWithNewStatus() {
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.NEW, status, "Подзадача должна иметь статус: NEW!");
        }
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Большая задача должна иметь статус: NEW!");
    }

    @Test
    void shouldBeAllSubtasksWithDoneStatus() {
        taskManager.updateSubTask(subTask.getId(), subTask, Status.DONE);
        taskManager.updateSubTask(subTask1.getId(), subTask1, Status.DONE);
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.DONE, status, "Подзадача должна иметь статус: DONE!");
        }
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Большая задача должна иметь статус: DONE!");
    }

    @Test
    void shouldBeSubtasksWithNewAndDoneStatus() {
        taskManager.updateSubTask(subTask1.getId(), subTask1, Status.DONE);
        assertEquals(Status.NEW, subTask.getStatus(), "Подзадача должна иметь статус: NEW!");
        assertEquals(Status.DONE, subTask1.getStatus(), "Подзадача должна иметь статус: DONE!");
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Большая задача должна иметь статус: IN_PROGRESS!");
    }

    @Test
    void shouldBeAllSubtasksWithInProgressStatus() {
        taskManager.updateSubTask(subTask.getId(), subTask, Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1.getId(), subTask1, Status.IN_PROGRESS);
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.IN_PROGRESS, status, "Подзадача должна иметь статус: IN_PROGRESS!");
        }
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Большая задача должна иметь статус: IN_PROGRESS!");
    }
}