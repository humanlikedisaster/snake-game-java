package com.company;

import java.util.*;


//ОБЪЕКТ, ОБОЗНАЧАЮЩИЙ ЯЧЕЙКУ ПОЛЯ
class Square {
    int x;
    int y;

    Square(int newX, int newY) {
        x = newX;
        y = newY;
    }
}

public class Main {
    //ИНФОРМАЦИЯ ДЛЯ ОТОБРАЖЕНИЯ СИМВОЛОВ
    private static String snakeSymbol = "o";
    private static String foodSymbol = "*";

    //ПРОВЕРКА ВЫХОДИТ ЛИ ТОЧКА ЗА ГРАНИЦЫ ПОЛЯ
    private static boolean isOutOfBounds(int x, int y) {
        return x >= 10 || x < 0 || y >= 10 || y < 0;
    }

    //ПРОВЕРКА ЯВЛЯЕТСЯ ЛИ ПОЛЕ - ЕДОЙ
    private static boolean isFoodSquare(int x, int y, Square foodSquare) {
        return foodSquare.x == x && foodSquare.y == y;
    }

    //ДВИГАЕТ ЗМЕЙКУ В НАПРАВЛЕНИИ НОВОЙ ТОЧКИ (head) ИСПОЛЬЗУЯ ДАННЫЕ О ПОЛЕ, ЕДЕ И МАССИВА САМОЙ ЗМЕИ
    //УДАЛЯЕТ ПОСЛЕДНЮЮ ТОЧКУ В ЗМЕЕ ЕСЛИ ТОЛЬКО НЕ СЪЕЛ ЕДУ. ПРОВЕРЯЕТ НА ВЫХОД ЗА ГРАНИЦЫ ПОЛЯ И НА СТОЛКНОВЕНИЕ
    //САМУ С СОБОЙ
    private static int moveToNewPosition(Square head, int[][] field, Square foodSquare, ArrayList<Square> snake) {
        if (!isOutOfBounds(head.x, head.y) && field[head.x][head.y] == 0) {
            snake.add(head);
            if (!isFoodSquare(head.x, head.y, foodSquare)) {
                snake.remove(0);
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    //СОЗДАЕТ И ВОЗВРАЩАЕТ ПОЛЕ, ИСПОЛЬЗУЯ ИНФОРМАЦИЮ О ЗМЕЕ, ЕСЛИ ЗМЕЯ В ЯЧЕЙКЕ ПОЛЯ - ТО ЗАПОЛНЯЕТ ЕДИНИЦЕЙ, ЕСЛИ НЕТ
    //ТО НУЛЕМ.
    private static int[][] updateField(ArrayList<Square> snake) {
        int[][] field = new int[10][10];
        for (int i=0; i < 10; i++) {
            java.util.Arrays.fill(field[i], 0);
        }
        for (Square square : snake) {
            field[square.x][square.y] = 1;
        }
        return field;
    }

    //ИСПОЛЬЗУЯ ДАННЫЕ О ПОЛЕ, СОЗДАЕТ ЕДУ В СВОБОДНОЙ ОТ ЗМЕЙКЕ ЯЧЕЙКЕ
    //Random - генератор случайных чисел, nextInt(10) - получает случайное число до 10 (не включая 10)
    private static Square createNewFood(int[][] field) {
        Random r = new Random(System.currentTimeMillis());
        int x;
        int y;
        Square square;
        do {
            x = r.nextInt(10);
            y = r.nextInt(10);
            square = new Square(x, y);
        } while (field[x][y] != 0);
        return square;
    }

    //ВЫВОД ЗМЕЙКИ НА ЭКРАН
    //ИСПОЛЬЗУЕТ ПОЛЕ В КОТОРОМ ЕСТЬ ИНФОРМАЦИЯ О ЗМЕЙКЕ И ИНФОРМАЦИЮ О ЕДЕ
    //ПЕРЕБИРАЕТ ВСЕ ПОЛЕ НА НАЛИЧИЕ 1 - ЧТО ОБОЗНАЧЕТ ЗМЕЙКУ, В ЛЮБОМ ДРУГОМ СЛУЧАЕ ВЫВОДИТ х
    private static void display(int[][] field, Square foodSquare) {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (foodSquare.x == x && foodSquare.y == y) {
                    System.out.print(foodSymbol);
                } else if (field[x][y] == 0)
                    System.out.print("x");
                else if (field[x][y] == 1)
                    System.out.print(snakeSymbol);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //СОЗДАЕТ ЗМЕЙКУ ПО ЦЕНТРУ ИСПОЛЬЗУЯ МАССИВ ARRAYLIST - ТК ОН ЯВЛЯЕТСЯ ДИНАМИЧЕСКИМ МАССИВОМ
        ArrayList<Square> snake = new ArrayList<>();
        Square first = new Square(5,5);
        snake.add(first);
        int[][] field = updateField(snake);

        int destination = 0;

        //СОЗДАЕТ ПЕРВУЮ ЕДУ
        Square foodSquare = createNewFood(field);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            boolean hasEated = false;
            Square square;

            //ОПРЕДЕЛЕНИЕ НАПРАВЛЕНИЯ ОТНОСИТЕЛЬНО ПЕРЕМЕННОЙ destanation. СОЗДАЕТ НОВУЮ ГОЛОВУ ПО НАПРАВЛЕНИЮ ДВИЖЕНИЯ
            switch (destination) {
                //ВВЕРХ
                case 0: {
                    Square head = snake.get(snake.size() - 1);
                    square = new Square(head.x, head.y - 1);
                }
                    break;
                //ВПРАВО
                case 1: {
                    Square head = snake.get(snake.size() - 1);
                    square = new Square(head.x + 1, head.y );
                }
                    break;
                //ВНИЗ
                case 2: {
                    Square head = snake.get(snake.size() - 1);
                    square = new Square(head.x, head.y + 1);
                }
                    break;
                //ВЛЕВО
                default: {
                    Square head = snake.get(snake.size() - 1);
                    square = new Square(head.x - 1, head.y );
                }
                    break;
            }
            //ДВИГАЕТСЯ В НАПРАВЛЕНИИ НОВОЙ ГОЛОВЫ И ВОЗВРАЩАЕТ РЕЗУЛЬТАТ (2 - ПОРАЖЕНИЕ, 1 - СЪЕЛА ЕДУ)
            int result = moveToNewPosition(square, field, foodSquare, snake);
            if (result == 2) {
                break;
            } else if (result == 1) {
                hasEated = true;
            }
            //ОБНОВЛЯЕТ ПОЛЕ С УЧЕТОМ ИНФОРМАЦИИ О ЗМЕЕ
            field = updateField(snake);

            if (hasEated) {
                //СОЗДАЕТ НОВУЮ ЕДУ ЕСЛИ ЗМЕЙКА СЪЕЛА СТАРУЮ
                foodSquare = createNewFood(field);
            }


            //ОТОБРАЖАЕТ ПОЛЕ И ЕДУ НА ЭКРАНЕ ЧЕРЕЗ КОНСОЛЬ
            display(field, foodSquare);


            //СЧИТЫВАЕТ ДАННЫЕ С КОНСОЛИ - ЕСЛИ ЭТО l ТО ТОГДА ДВИГАЕМСЯ ВЛЕВО, ЕСЛИ r - ТО ВПРАВО, В ЛЮБОМ ДРУГОМ СЛУЧАЕ
            //ПРЯМО. ВЛЕВО И ВПРАВО ДВИЖЕНИЕ ПРОИСХОДИТ ОТНОСИТЕЛЬНО СУЩЕСТВУЮЩЕГО НАПРАВЛЕНИЯ
            String scannerString = scanner.nextLine();
            if (scannerString.equals("l")) {
                destination = destination - 1 >= 0 ? destination - 1 : 3;
            } else if (scannerString.equals("r")) {
                destination = destination + 1 < 4 ? destination + 1 : 0;
            }
        }

        System.out.println("ТЫ ПРОИГРАЛ!");
    }
}
