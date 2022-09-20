import java.io.*;
import java.util.Arrays;

public class Basket {


    protected String[] products;
    protected int[] prices;
    protected int[] basket;

    protected double sum = 0;

    public Basket(String[] products, int[] prices, int[] basket) {
        this.products = products;
        this.prices = prices;
        this.basket = basket;
    }

    public static Basket loadFromTxtFile(File textFile, String[] products) {
        Basket toRead = null;
        if (textFile.exists() & textFile.length() != 0) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
                int cnt = 0;
                int size = products.length;
                products = new String[size];
                int[] basket = new int[size];
                int[] price = new int[size];
                String read;
                while ((read = bufferedReader.readLine()) != null) {
                    String[] strings = read.split("/");
                    products[cnt] = strings[0];
                    basket[cnt] = Integer.parseInt(strings[1]);
                    price[cnt] = Integer.parseInt(strings[2]);
                    cnt++;
                }
                toRead = new Basket(products, price, basket);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return toRead;
    }

    public void addToCart(int productNumb, int amount) {
        basket[productNumb] += amount;
        sum += prices[productNumb] * basket[productNumb];
    }

    public void printCart() {
        System.out.println("Корзина");
        double sum = 0;
        for (int i = 0; i < basket.length; i++) {
            if (basket[i] != 0) {
                System.out.println(products[i] + " " + basket[i] + " шт " + prices[i] + " руб/шт "
                        + (basket[i] * prices[i]) + " руб");
                sum += basket[i] * prices[i];
            }
        }
        System.out.println("Итог: " + sum);
    }

    public void saveTxt(File textFile) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(textFile))) {
            for (int i = 0; i < products.length; i++) {
                if (basket[i] > 0) {
                    out.write(products[i] + "/" + basket[i] + "/" + prices[i] + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Basket{" +
                "products=" + Arrays.toString(products) +
                ", prices=" + Arrays.toString(prices) +
                ", inBasket=" + Arrays.toString(basket) +
                ", sum=" + sum +
                '}';
    }
}