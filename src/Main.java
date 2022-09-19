import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Творог", "Кефир", "Колбаса", "Пельмени"};
        double[] prices = {67.90, 76.60, 45.50, 356.20, 220.80};
        Basket basket = new Basket(products, prices);
        System.out.println("Список товара: ");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт.");
        }

        while (true) {
            System.out.println("Введите номер товара и количество через пробел.");
            System.out.println("Для завершения введите end ");

            String input = scanner.nextLine();
            if (input.equals("end")) {
                basket.saveTxt(new File("basket.txt"));
                System.out.println("Программа завершена!");
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
                basket.addToCart(productNumb, amount);

            } catch (NumberFormatException e) {
                System.out.println("НЕ ВЕРНО. Необходимо вводить только числа.");
                System.out.println("Вы ввели " + input);
                continue;
            }
        }
        basket.printCart();
    }
}