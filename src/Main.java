import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean loading = true;
        String loadingFileName = null;
        String loadingFileFormat = null;

        boolean saving = true;
        String savingFileName = null;
        String savingFileFormat = null;

        boolean logging = true;
        String loggingFileName = null;

        try {
            File xmlFile = new File("shop.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList loadingList = doc.getElementsByTagName("load");
            for (int i = 0; i < loadingList.getLength(); i++) {
                Node node = loadingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    loading = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    loadingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    loadingFileFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList savingList = doc.getElementsByTagName("save");
            for (int i = 0; i < savingList.getLength(); i++) {
                Node node = savingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    saving = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    savingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    savingFileFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList loggingList = doc.getElementsByTagName("log");
            for (int i = 0; i < savingList.getLength(); i++) {
                Node node = loggingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    logging = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    loggingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Хлеб", "Творог", "Кефир", "Колбаса", "Пельмени"};
        int[] prices = {67, 76, 45, 356, 220};
        int[] baskets = new int[products.length];

        Basket basket = new Basket(products, prices, baskets);
        ClientLog clientLog = new ClientLog();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (loading && Objects.requireNonNull(loadingFileFormat).equals("json")) {
            assert loadingFileName != null;
            try (JsonReader reader = new JsonReader(new FileReader(loadingFileName))) {
                basket = gson.fromJson(reader, Basket.class);
                basket.printCart();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (loading && loadingFileFormat.equals("txt")) {
            assert loadingFileName != null;
            basket = Basket.loadFromTxtFile(new File(loadingFileName), products);
            basket.printCart();
        }

        System.out.println("Список товара: ");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб");
        }

        while (true) {
            System.out.println("Введите номер товара и количество через пробел.");
            System.out.println("Для завершения введите end ");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] inAmount = input.split(" ");

            if (inAmount.length != 2) {
                System.out.println("НЕ ВЕРНО Вы ввели " + input);
                continue;
            }

            try {
                int productNumb = (Integer.parseInt(inAmount[0])) - 1;

                if ((productNumb + 1) > products.length || (productNumb + 1) <= 0) {
                    System.out.println("Не верный номер продукта. Вы ввели " + (productNumb + 1));
                    System.out.println("Введите номер от 1 до" + " " + products.length);
                    continue;
                }

                int amount = (Integer.parseInt(inAmount[1]));

                if (amount <= 0) {
                    System.out.println("НЕ ВЕРНО. Количество не может быть меньше 1 шт. ");
                    System.out.println("Вы ввели " + amount);
                    continue;
                }
                clientLog.log(productNumb, amount);
                basket.addToCart(productNumb, amount);

                if (savingFileFormat != null) {
                    if (saving && savingFileFormat.equals("json")) {
                        try (FileWriter jsonWriter = new FileWriter(savingFileName)) {
                            jsonWriter.write(gson.toJson(basket));
                            jsonWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (saving && savingFileFormat.equals("txt")) {
                        basket.saveTxt(new File(savingFileName));
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("НЕ ВЕРНО. Необходимо вводить только числа.");
                System.out.println("Вы ввели " + input);
                continue;
            }
            if (logging) {
                clientLog.exportAsCSV(new File(Objects.requireNonNull(loggingFileName)));
            }
            basket.printCart();
        }
    }
}