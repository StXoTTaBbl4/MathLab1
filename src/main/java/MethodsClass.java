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

    public status getDataFromConsole(BufferedReader reader) throws IOException {
        System.out.println("Введите размерность квадратной матрицы:");
        try {
            n = Integer.parseInt(reader.readLine());
            if(n < 2){
                System.out.println("Размерность не может быть меньше 2!");
                return status.ERROR;
            }
            matrix = new double[n][n+1];
        }catch (NumberFormatException e){
            System.out.println("Размерность матрицы должна быть представлена целым числом!");
            return status.ERROR;
        }

        System.out.println("Количество строк и столбцов для ввода: " + n +"\nФормат ввода: а11 а12 а13 b1");
        for (int i = 0; i < n; i++) {

                if(parseString(reader.readLine(), i) == status.ERROR)
                    return status.ERROR;
        }
        printMatrix(matrix);
        return status.OK;
    }

    public status getDataFromFile(BufferedReader reader) throws IOException {
        System.out.println("Введите пусть к файлу:");
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(reader.readLine()));
        }catch (FileNotFoundException e){
            System.out.println("Файл не найден!");
            return status.ERROR;
        }


        String line;
        if((line = in.readLine()) != null){
            try {
                n = Integer.parseInt(line.trim());
                matrix = new double[n][n+1];
            }catch (NumberFormatException e){
                System.out.println("Некорректно задана размерность матрицы! В первой строке должно быть одно число.");
                e.printStackTrace();
                return status.ERROR;
            }
        }

        int i = 0;
        while((line = in.readLine()) != null)
        {
            if (parseString(line,i) == status.OK)
                i++;
        }
        in.close();
        printMatrix(matrix);
        return status.OK;
    }

    public status generateMatrix(BufferedReader reader) throws IOException{
        System.out.println("Введите размерность квадратной матрицы:");
        try {
            n = Integer.parseInt(reader.readLine());
            if(n < 2){
                System.out.println("Размерность не может быть меньше 2!");
                return status.ERROR;
            }

            matrix = new double[n][n+1];
        }catch (NumberFormatException e){
            System.out.println("Размерность матрицы должна быть представлена целым числом!");
            return status.ERROR;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = ThreadLocalRandom.current().nextDouble(0, 10);
            }
        }
        printMatrix(matrix);
        return status.OK;
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
        System.out.println("Определитель матрицы: " + determinantForTriangleMatrix(matrix,swaps));
        answer(matrix);
    }

    public double determinantForTriangleMatrix(double[][] matrix, int swaps){
        double det = 1.0;
        for (int i = 0; i < matrix.length; i++) {
            det = det*matrix[i][i];
        }
        if(swaps%2 == 0)
            return det;
        else
            return -det;
    }

    public void helloGauss() throws IOException {
        System.out.println(FileUtils.readFileToString(new File("Hello.txt"), "Unicode"));
    }

    public void help() throws IOException {
        System.out.println(FileUtils.readFileToString(new File("help.txt"), "UTF-8"));
    }

    private status parseString(String in, int row){
        String[] input = in.trim()
                .replaceAll(" +", " ")
                .split(" ");
        System.out.println(input.length);
        if(input.length > n+1 || input.length < n+1) {
            System.out.println("Количество элементов в строке не соответствует " + n + ",\n" +
                    " повторите ввод строки/исправьте данные!" );
            return status.ERROR;
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
                    return status.ERROR;
                }
                j++;
            }
        }
        return status.OK;
    }

    private void printMatrix(double[][] matrix){
        int size = matrix.length;

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder line ;

        for (int i = 0; i < size; i++) {
            line = new StringBuilder();
            for (int j = 0; j < size; j++) {

                if (matrix[i][j] >= 0)
                    line.append(" ").append(round(matrix[i][j])).append(" ");
                else
                    line.append(round(matrix[i][j])).append(" ");

            }
            if(matrix[i][size] >=0)
                line.append("| ").append(round(matrix[i][size]));
            else
                line.append("|").append(round(matrix[i][size]));

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

    private double round(double value) {

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
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
        int allXCounter;

        allX.add(0, matrix[matrix.length - 1][matrix.length] / matrix[matrix.length - 1][matrix.length - 1]);


        for (int i = rows-2; i >= 0; i--) {
            array = matrix[i];
            System.out.println("array:" + Arrays.toString(array));
            System.out.println("i:"+i);
            rightSide = 0.0;
            allXCounter = 0;
            for (int j = rows-1; j >= border; j--) {
                System.out.println("j:"+j);
                System.out.println("array[j]:" + array[j]);
                System.out.println("allX[" + allXCounter + "]:"+allX.get(allXCounter));

                rightSide -= array[j]*allX.get(allXCounter);
                allXCounter++;
            }
            System.out.println("det" + array[border-1]);
            allX.add(allXCounter,rightSide/array[border-1]);
            border--;
            System.out.println("=======================");

        }

        StringBuilder sbX = new StringBuilder();
        int c = 1;
        for (int i = allX.size()-1; i >=0 ; i--) {
            sbX.append("x").append(c).append("=").append(round(allX.get(i))).append(" \n");
            c++;
        }
        System.out.println(sbX);

        StringBuilder sbR = new StringBuilder();
        double[] xRow;
        for (int i = rows-1; i >= 0; i--) {
            xRow = matrix[i];
//            System.out.println(Arrays.toString(xRow));
//            double sum = xRow[xRow.length-1];
            double sum = 0;
            for (int j = xRow.length-2; j >=0 ; j--) {
//                System.out.println("x "+xRow[j]);
//                System.out.println("c "+allX.get(xRow.length-2-j));
                  sum = sum + xRow[j]*allX.get(xRow.length-2-j);
//                sum = sum - xRow[j]*allX.get(xRow.length-2-j);
            }
            System.out.println("sum " + sum);
//            System.out.println("+++++");
            sbR.append("r").append(i+1).append("=").append(xRow[xRow.length-1]).append("\n");
        }

        System.out.println(sbR);

        printMatrix(matrix);

    }


}
