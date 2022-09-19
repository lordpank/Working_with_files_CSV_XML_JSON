import java.io.*;
import java.text.DecimalFormat;
public class Basket {
    DecimalFormat dF = new DecimalFormat("0.00");
    private String[] products;
    private double[] price;
    private int[] basket;

    public Basket(String[] products, double[] price) {
        Basket basket1 = loadFromTxtFile(new File("basket.txt"));
        if (basket1 != null) {
            this.products = basket1.getProducts();
            this.price = basket1.getPrice();
            this.basket = basket1.getBasket();
        } else {
            this.products = products;
            this.price = price;
            this.basket = new int[products.length];
        }
    }
    public Basket(String[] products, double[] price, int[] basket) {
        this.products = products;
        this.price = price;
        this.basket = basket;
    }
    public void printCart() {
        System.out.println("Корзина");
        double sum = 0;
        for (int i = 0; i < basket.length; i++) {
            if (basket[i] != 0) {
                System.out.println(products[i] + " " + basket[i] + " шт " + dF.format(price[i]) + " руб/шт "
                        + dF.format(basket[i] * price[i]) + " руб");
                sum += basket[i] * price[i];
            }
        }
        System.out.println("Итог: " + dF.format(sum));
    }
    public void addToCart(int productNum, int amount) {
        basket[productNum] += amount;
    }
    public void saveTxt(File textFile) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(textFile))) {
            for (int i = 0; i < products.length; i++) {
                out.write(products[i] + ": " + basket[i] +" шт " + " " + dF.format(price[i]) + "\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Basket loadFromTxtFile(File textFile) {
        Basket result = null;
        if (textFile.exists() & textFile.length() != 0) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile));) {
                int cnt = 0;
                int size = 5;
                String[] products = new String[size];
                int[] basket = new int[size];
                double[] price = new double[size];
                String read;
                while ((read = bufferedReader.readLine()) != null) {
                    String[] strings = read.split(" ");
                    products[cnt] = strings[0];
                    basket[cnt] = Integer.parseInt(strings[0]) - 1;
                    price[cnt] = Integer.parseInt(strings[1]);
                    cnt++;
                }
                result = new Basket(products, price, basket);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    public String[] getProducts() {
        return products;
    }
    public double[] getPrice() {
        return price;
    }
    public int[] getBasket() {
        return basket;
    }
}