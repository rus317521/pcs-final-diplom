import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Users\\scherbakova.alsu\\IdeaProjects\\Diplom\\pcs-final-diplom\\pdfs"));
        //  System.out.println(engine.search("бизнес"));

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
        int port = 8989;
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port);) { // порт можете выбрать любой в доступном диапазоне 0-65536. Но чтобы не нарваться на уже занятый - рекомендуем использовать около 8080
                try (Socket clientSocket = serverSocket.accept(); // ждем подключения
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    // ваш код

                    final String word = in.readLine();

                    //*********************
                    //Внутри JSONArray будут храниться JSON - объекты
                    JSONArray pageEntries = new JSONArray();

                    //Наполняем JSONArray
                    for (PageEntry pageEntry : engine.search(word)) {   //Формируем страницу
                        JSONObject pageEntryJson = new JSONObject();
                        pageEntryJson.put("pdfName", pageEntry.getPdfName());
                        pageEntryJson.put("page", pageEntry.getPage());
                        pageEntryJson.put("count", pageEntry.getCount());

                        //Добавлякем страницу
                        pageEntries.add(pageEntryJson);
                    }
                    //Кладем JSON - массив в наш JSON - объект
                    //pageEntryJson.put("",contacts);
                    //Получаем(создаем) файл формата json

                    out.println(pageEntries.toJSONString());


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
