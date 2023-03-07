import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class MethodsClass {
    public int n;
    public double[][] matrix;

    public void getDataFromConsole(BufferedReader reader) throws IOException {
        System.out.println("Введите размерность квадратной матрицы:");
        try {
            n = Integer.parseInt(reader.readLine());
            if(n < 2){
                System.out.println("Размерность не может быть меньше 2!");
                return;
            }

            matrix = new double[n][n+1];
        }catch (NumberFormatException e){
            System.out.println("Размерность матрицы должна быть представлена целым числом!");
            return;
        }

        System.out.println("Количество строк и столбцов для ввода: " + n +"\nФормат ввода: а11 а12 а13 b1");
        for (int i = 0; i < n; i++) {

                if(parseString(reader.readLine(), i) == STATUS.ERROR)
                    i--;
        }
        printMatrix(matrix);
    }

    public void getDataFromFile(BufferedReader reader) throws IOException {
        System.out.println("Введите пусть к файлу:");
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(reader.readLine()));
        }catch (FileNotFoundException e){
            System.out.println("Файл не найден!");
            return;
        }


        String line;
        if((line = in.readLine()) != null){
            try {
                n = Integer.parseInt(line.trim());
                matrix = new double[n][n+1];
            }catch (NumberFormatException e){
                System.out.println("Некорректно задана размерность матрицы! В первой строке должно быть одно число.");
                e.printStackTrace();
                return;
            }
        }

        int i = 0;
        while((line = in.readLine()) != null)
        {
            if (parseString(line,i) == STATUS.OK)
                i++;
        }
        in.close();
        printMatrix(matrix);
    }

    public void generateMatrix(BufferedReader reader) throws IOException{
        System.out.println("Введите размерность квадратной матрицы:");
        try {
            n = Integer.parseInt(reader.readLine());
            if(n < 2){
                System.out.println("Размерность не может быть меньше 2!");
                return;
            }

            matrix = new double[n][n+1];
        }catch (NumberFormatException e){
            System.out.println("Размерность матрицы должна быть представлена целым числом!");
            return;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = ThreadLocalRandom.current().nextDouble(0, 10);
            }
        }
        printMatrix(matrix);
    }

    public void toTriangularForm(){
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (matrix[i][i] == 0 && i != n-1) {
                swapRows(i,i);
                swaps++;
            }
            for (int j = i+1; j < n; j++) {
                if (matrix[j][i] != 0)
                    rowsSum(i,j,i);
            }
        }

        printMatrix(matrix);
        System.out.println("Определитель матрицы: " + determinantForTriangleMatrix(matrix));
        answer(matrix);
    }

    public double determinantForTriangleMatrix(double[][] matrix){
        double det = 1.0;
        for (int i = 0; i < matrix.length; i++) {
            det = det*matrix[i][i];
        }
        return det;
    }

    public void helloGauss() throws IOException {
        System.out.println(FileUtils.readFileToString(new File("Hello.txt"), "Unicode"));
    }

    private STATUS parseString(String in, int row){
        String[] input = in.trim()
                .replaceAll(" +", " ")
                .split(" ");
        if(input.length > n+1) {
            System.out.println("Количество элементов в строке не соответствует " + n );
            return STATUS.ERROR;
        }
        else {
            int j =0;
            for (String el : input) {
                el = el.trim().replace(",", ".");
                try {
                    matrix[row][j] = Double.parseDouble(el);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    System.out.println("Данные должны быть представлены числами!");
                    return STATUS.ERROR;
                }
                j++;
            }
        }
        return STATUS.OK;
    }

    private void printMatrix(double[][] matrix){
        int size = matrix.length;

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder line ;

        for (int i = 0; i < size; i++) {
            line = new StringBuilder();
            for (int j = 0; j < size; j++) {

                if (matrix[i][j] >= 0)
                    line.append(" ").append(round(matrix[i][j],2)).append(" ");
                else
                    line.append(round(matrix[i][j],2)).append(" ");

            }
            if(matrix[i][size] >=0)
                line.append("| ").append(round(matrix[i][size],2));
            else
                line.append("|").append(round(matrix[i][size],2));

            if(i == 0)
                line = new StringBuilder("/" + line + "\\");
            else if (i == size-1)
                line = new StringBuilder("\\" + line + "/");
            else
                line = new StringBuilder("|" + line + "|");
            stringBuilder.append(line).append("\n");

        }

        System.out.println("Матрица имеет вид:\n"+stringBuilder);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void swapRows(int currentRow, int position){
        for (int i = currentRow+1; i < n; i++) {
            if (matrix[i][position] !=0 ){
                System.out.println("Поменяны местами строки " + (currentRow+1) + " и " + (i+1));
                for (int j = 0; j < n+1; j++) {
                    double x = matrix[currentRow][j];
                    matrix[currentRow][j] = matrix[i][j];
                    matrix[i][j] = x;
                }
                printMatrix(matrix);
                return;
            }
        }
        System.out.println("Коэффициенты в столбце "+(position+1) + " равны 0");
    }

    private void rowsSum(int rowA, int rowB, int position){
        double a = matrix[rowA][position];
        double b = matrix[rowB][position];

        double coefficient = -b/a;

        for (int i = 0; i <= n; i++) {
            matrix[rowB][i] = matrix[rowB][i] + matrix[rowA][i]*coefficient;
        }
        System.out.println("Из строки " + (rowB+1) + " вычтем строку " + (rowA+1) + " умноженную на " + coefficient);
        printMatrix(matrix);
    }

    private void answer(double[][] matrix){
        System.out.println("=================================");
        int rows = matrix.length;
        double[] array;
        LinkedList<Double> allX = new LinkedList<>();
        double rightSide;
        int border = matrix.length-1;
        int allXcounter;

        allX.add(0, matrix[matrix.length - 1][matrix.length] / matrix[matrix.length - 1][matrix.length - 1]);


        for (int i = rows-2; i >= 0; i--) {
            array = matrix[i];
            System.out.println("array:" + Arrays.toString(array));
            System.out.println("i:"+i);
            rightSide = 0.0;
            allXcounter = 0;
            for (int j = rows-1; j >= border; j--) {
                System.out.println("j:"+j);
                System.out.println("array[j]:" + array[j]);
                System.out.println("allX[" + allXcounter + "]:"+allX.get(allXcounter));

                rightSide -= array[j]*allX.get(allXcounter);
                allXcounter++;
            }
            allX.add(allXcounter,rightSide/array[border-1]);
            border--;
            System.out.println("=======================");

        }

        System.out.println(allX);

    }


}
