package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import service.HttpTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final HttpTaskManager manager;
    private final Gson gson;
    private final Gson gsonNoAdapter;

    public HttpTaskServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handle);
        this.manager = Managers.getDefaultHttpTask();
        this.gson = Managers.getGson();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls();
        this.gsonNoAdapter = gsonBuilder.create();
    }

    private void handle(HttpExchange httpExchange) throws IOException {
        try {
            String uri = httpExchange.getRequestURI().toString();
            String path = httpExchange.getRequestURI().getPath().replace("/tasks", "");
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " запроса: " + uri);

            switch (method) {
                case "GET": {
                    switch (path) {
                        case "": {
                            String response;
                            try {
                                response = gson.toJson(manager.getPrioritizedTasks());
                            } catch (NullPointerException e) {
                                response = gsonNoAdapter.toJson(manager.getPrioritizedTasks());
                            }
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/task": {
                            String response;
                            try {
                                response = gson.toJson(manager.getAllTask());
                            } catch (NullPointerException e) {
                                response = gsonNoAdapter.toJson(manager.getAllTask());
                            }
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/task/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                                Task task = manager.getByIdTask(id);
                                String response;
                                if (task != null) {
                                    try {
                                        response = gson.toJson(task);
                                    } catch (NullPointerException e) {
                                        response = gsonNoAdapter.toJson(task);
                                    }
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Обычной задачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос!";
                                writeResponse400(httpExchange, response);

                            }
                            break;
                        }
                        case "/subtask": {
                            String response;
                            try {
                                response = gson.toJson(manager.getAllSubTask());
                            } catch (NullPointerException e) {
                                response = gsonNoAdapter.toJson(manager.getAllSubTask());
                            }
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/subtask/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                                SubTask subTask = manager.getByIdSubTask(id);
                                String response;
                                if (subTask != null) {
                                    try {
                                        response = gson.toJson(subTask);
                                    } catch (NullPointerException e) {
                                        response = gsonNoAdapter.toJson(subTask);
                                    }
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Подзадачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/epic": {
                            String response;
                            try {
                                response = gson.toJson(manager.getAllEpic());
                            } catch (NullPointerException e) {
                                response = gsonNoAdapter.toJson(manager.getAllEpic());
                            }
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                                Epic epic = manager.getByIdEpic(id);
                                String response;
                                if (epic != null) {
                                    try {
                                        response = gson.toJson(epic);
                                    } catch (NullPointerException e) {
                                        response = gsonNoAdapter.toJson(epic);
                                    }
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Большой задачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/subtask/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/epic/?id=", ""));
                                String response;
                                try {
                                    response = gson.toJson(manager.getSubTaskByEpic(id));
                                } catch (NullPointerException e) {
                                    response = gsonNoAdapter.toJson(manager.getSubTaskByEpic(id));
                                }
                                if (!response.equals("null")) {
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Большой задачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/history": {
                            String response;
                            try {
                                response = gson.toJson(manager.getHistory());
                            } catch (NullPointerException e) {
                                response = gsonNoAdapter.toJson(manager.getHistory());
                            }
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        default:
                            String response = "Такого эндпойнта не существует!";
                            writeResponse404(httpExchange, response);
                            break;
                    }
                    break;
                }
                case "POST": {
                    switch (path) {
                        case "/task": {
                            try {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                Task task = gson.fromJson(body, Task.class);
                                if (task.getName() == null) {
                                    task.setName("");
                                }
                                if (task.getDescription() == null) {
                                    task.setDescription("");
                                }
                                manager.developTask(task);
                                String response = "Обычная задача успешно добавлена!";
                                writeResponse200(httpExchange, response);
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/task/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                                Task task = manager.getByIdTask(id);
                                String response;
                                if (task != null) {
                                    InputStream inputStream = httpExchange.getRequestBody();
                                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                    Task task1 = gson.fromJson(body, Task.class);
                                    task1.setId(id);
                                    if (task1.getStatus() == null) {
                                        task1.setStatus(task.getStatus());
                                    }
                                    if (task1.getName() == null) {
                                        task1.setName(task.getName());
                                    }
                                    if (task1.getDescription() == null) {
                                        task1.setDescription(task.getDescription());
                                    }
                                    manager.updateTask(id, task1, task1.getStatus());
                                    response = "Обычная задача успешно успешно обновлена!";
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Обычной задачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/subtask": {
                            try {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                SubTask subTask = gson.fromJson(body, SubTask.class);
                                Epic epic = manager.getByIdEpic(subTask.getIdEpic());
                                if (subTask.getIdEpic() != 0 && epic != null) {
                                    if (subTask.getName() == null) {
                                        subTask.setName("");
                                    }
                                    if (subTask.getDescription() == null) {
                                        subTask.setDescription("");
                                    }
                                    manager.developSubTask(subTask);
                                    epic.putSubTask(subTask.getId(), subTask);
                                    epic.countTime();
                                    String response = "Подзадача успешно добавлена!";
                                    writeResponse200(httpExchange, response);
                                } else {
                                    String response = "В теле неверный индефикатор большой задачи!";
                                    writeResponse400(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/subtask/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                                SubTask subTask = manager.getByIdSubTask(id);
                                String response;
                                if (subTask != null) {
                                    InputStream inputStream = httpExchange.getRequestBody();
                                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                    SubTask subTask1 = gson.fromJson(body, SubTask.class);
                                    subTask1.setId(id);
                                    subTask1.setIdEpic(subTask.getIdEpic());
                                    if (subTask1.getStatus() == null) {
                                        subTask1.setStatus(subTask.getStatus());
                                    }
                                    if (subTask1.getName() == null) {
                                        subTask1.setName(subTask.getName());
                                    }
                                    if (subTask1.getDescription() == null) {
                                        subTask1.setDescription(subTask.getDescription());
                                    }
                                    manager.updateSubTask(id, subTask1, subTask1.getStatus());
                                    response = "Подзадача успешно успешно обновлена!";
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Подзадачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/epic": {
                            try {
                                InputStream inputStream = httpExchange.getRequestBody();
                                String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                Epic epic = gson.fromJson(body, Epic.class);
                                if (epic.getStartTime() == null && epic.getDuration() == 0) {
                                    if (epic.getHashMapSubTask() == null) {
                                        HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
                                        epic.setSubTasks(subTaskMap);
                                    }
                                    if (epic.getName() == null) {
                                        epic.setName("");
                                    }
                                    if (epic.getDescription() == null) {
                                        epic.setDescription("");
                                    }
                                    manager.developEpic(epic);
                                    String response = "Большая задача успешно добавлена!";
                                    writeResponse200(httpExchange, response);
                                } else {
                                    String response = "Время большой задачи расчитывается по подзадачам!";
                                    writeResponse400(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                                Epic epic = manager.getByIdEpic(id);
                                String response;
                                if (epic != null) {
                                    InputStream inputStream = httpExchange.getRequestBody();
                                    String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                                    Epic epic1 = gson.fromJson(body, Epic.class);
                                    epic1.setId(id);
                                    epic1.setStatus(epic.getStatus());
                                    if (epic1.getName() == null) {
                                        epic1.setName(epic.getName());
                                    }
                                    if (epic1.getDescription() == null) {
                                        epic1.setDescription(epic.getDescription());
                                    }
                                    manager.updateEpic(id, epic1);
                                    response = "Большая задача успешно успешно обновлена!";
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Большая задачи с таким id не существует!";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Неверно сформирован запрос или тело!";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    switch (path) {
                        case "/task": {
                            try {
                                manager.clearTask();
                                writeResponse200(httpExchange, "Все обычные задачи удалены!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/task/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                                manager.deleteByIdTask(id);
                                writeResponse200(httpExchange, "Обычная задача удалена!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/subtask": {
                            try {
                                manager.clearSubTask();
                                writeResponse200(httpExchange, "Все подзадачи удалены!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/subtask/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                                manager.deleteByIdSubTask(id);
                                writeResponse200(httpExchange, "Подзадача удалена!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/epic": {
                            try {
                                manager.clearEpic();
                                writeResponse200(httpExchange, "Все большие задачи удалены!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                                manager.deleteByIdEpic(id);
                                writeResponse200(httpExchange, "Больщая задача удалена!");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                    }
                    break;
                }
                default:
                    String response = "Такой метод здесь не принимается!";
                    writeResponse400(httpExchange, response);
            }
        } catch (Throwable throwable) {
            writeResponse400(httpExchange, throwable.getMessage());
        } finally {
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("Сервер запущен в работу. Порт - " + PORT);
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("Сервер остановлен.");
    }

    private void writeResponse200(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponse404(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponse400(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }
}