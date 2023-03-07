import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatrixCalc {
    public static void main(String[] args) throws IOException {
        MethodsClass methods = new MethodsClass();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        methods.helloGauss();
        while (true) {
            System.out.println("Для ввода данных из консоли введите c, из файла - f, для авто-генерации - auto.\nДля выхода введите exit");
            input = reader.readLine();
            switch (input) {
                case "c" -> {
                    System.out.println("Выбран ввод из консоли.");
                    methods.getDataFromConsole(reader);
                    methods.toTriangularForm();
                }
                case "f" -> {
                    System.out.println("Выбран ввод из файла.");
                    methods.getDataFromFile(reader);
                    methods.toTriangularForm();
                }
                case "auto" ->{
                    System.out.println("Выбрано автозаполнение");
                    methods.generateMatrix(reader);
                    methods.toTriangularForm();
                }
                case "exit" -> {
                    System.out.println("Завершение.");
                    System.exit(0);
                }
                default -> System.out.println("Данный вариант не предусмотрен!");
            }
        }

    }


}
