import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Блок ввода выражения
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите простое арифметическое выражение. Числа от 1 до 10 римские или арабские.");
        String input = scan.nextLine();
        // обрезаем по бокам пробелы
        input = input.trim();

        String result = calc(input);
        if (result == "NO") {throw new IOException();} else {System.out.println(result);}
    }
    //enum для римских
    enum DigitalR {
        I(1), II(2), III(3), IV(4), V(5), VI(6), VII(7), VIII(8), IX(9), X(10);
        private int digitalR;
        DigitalR(int digitalR){
            this.digitalR = digitalR;
        }
        private int getDigitalR(){
            return digitalR;
        }
    }

     public static String calc(String input) throws IOException {

        // блок парсинга
        // начало -----------------------------------------------------------
        //обращаемся к методу для выявления наличия операторов и их количества
        String operator =  checkOperators(input);
        if (operator == "N"){errors();}
        // проверяем, что инпут не заканчивается на оператор. Вернёт false если заканчивается.
        if (!(startEndOperator(input, operator))) {errors();}
        //Разбиваем инпут на два операнда, разделитель оператор.
         String [] twoOperands = new String[2];
         switch (operator){
             case "+": twoOperands = input.split("\\+"); break;
             case "*": twoOperands = input.split("\\*"); break;
             case "-": twoOperands = input.split("-"); break;
             case "/": twoOperands = input.split("/"); break;
         }
       //  String [] twoOperands = input.split(operator);
        String operand1 = twoOperands[0].trim();
        String operand2 = twoOperands[1].trim();
        //Выясням какой системе записи принадлежат операторы Rome, Decimal or Another. При этом в нужном нам диапазоне.
         String systemOperand1 = whatSystems(operand1);
         String systemOperand2 = whatSystems(operand2);
         // конец ---------------------------------------------------------------

         // обработка в зависимости от системы записи операндов
         // первая проверка на арабские
         if (systemOperand1 == "Decimal" && systemOperand2 == "Decimal"){
             int a = Integer.parseInt(operand1);
             int b = Integer.parseInt(operand2);
             int result = calculate(a,b,operator);
             return String.valueOf(result);}
         // вторая проверка на риские
         else if (systemOperand1 == "Rome" && systemOperand2 == "Rome"){
             int a = DigitalR.valueOf(operand1).digitalR;
             int b = DigitalR.valueOf(operand2).digitalR;
             int result = calculate(a,b,operator);
             if (result < 1){errors();} else {return conventArabToRome(result);}
         }
         return "NO";

     }

    // Метод непосрдественно производящий рассчёт. Принимает два числа и операнд.
    static  int calculate(int a, int b, String operand){
        int res;
        switch (operand){
            case "+": res = a+b; break;
            case "-": res = a-b; break;
            case "*": res = a*b; break;
            case "/": res = a/b; break;
            default: res = 0;
        }
        return res;
    }
    //Метод для выкидывания исключения в случае некоректного ввода
    static void errors() throws IOException {
        throw new IOException();
    }

    // Метод переводящий из арабских цифр в римские в диапазоне 1-100.
    static String conventArabToRome(int x){
        String romanNumber = "";
        int decade = x / 10;
        switch (decade){
            case 0: break;
            case 1: romanNumber += "X"; break;
            case 2: romanNumber += "XX"; break;
            case 3: romanNumber += "XXX"; break;
            case 4: romanNumber += "XL"; break;
            case 5: romanNumber += "L"; break;
            case 6: romanNumber += "LX"; break;
            case 7: romanNumber += "LXX"; break;
            case 8: romanNumber += "LXXX"; break;
            case 9: romanNumber += "XC"; break;
            case 10: romanNumber += "C"; break;
        }
        int units = x % 10;
        switch (units){
            case 0: break;
            case 1: romanNumber += "I"; break;
            case 2: romanNumber += "II"; break;
            case 3: romanNumber += "III"; break;
            case 4: romanNumber += "IV"; break;
            case 5: romanNumber += "V"; break;
            case 6: romanNumber += "VI"; break;
            case 7: romanNumber += "VII"; break;
            case 8: romanNumber += "VIII"; break;
            case 9: romanNumber += "IX"; break;
        }
        return romanNumber;

    }
    //Метод выясняющий наличие и количество операторов. Возвращает оператор, если он один и "N" если нет или больше одного.
    static String checkOperators (String input){
        //Инициируем оператор к возврату
        String returningOperator = "N";
        // инициируем счётчик операторов
        int countOperators = 0;
        String [] operators = {Character.toString('+'), Character.toString('-'), Character.toString('*'), Character.toString('/')};
        for (String element: operators){
            //проверяем наличие оператора
            int check = input.indexOf(element);
            if (check != -1){
                //считаем количество вхождения оператора
                int check_count = input.length() - input.replace(element, "").length();
                if (check_count > 1){return "N";}  else {countOperators++;
                    }
                // оператор к возврату
                returningOperator = element;
            }
        }
        // если число операторов =1, то возвращаем найденный единственный оператор, иначе возвращаем "N".
        if (countOperators == 1){return returningOperator;} else {return "N";}
    }
    // Проверка что выражение не ничанается и не заканчивается на оператор.
    static boolean startEndOperator(String input, String operator){
        return !input.startsWith(operator) && !input.endsWith(operator);
    }
    // Метод выявляющий принадлежит ли операнд десятичной или римской системе в нужном диапазоне.
    static String whatSystems(String operand){
        String resultSystems;
        // проверка через трай принадлежность к интежеру и если принадлежит, то срзу проверяем диапазон
        try {
            int flag = Integer.parseInt(operand);
            if (1<=flag && flag<=10){
                resultSystems = "Decimal";
                return resultSystems;
            } else {resultSystems = "Another"; return resultSystems;}
        } catch (NumberFormatException e){
            resultSystems = "Another";
        }
        // проверяем с помощью enum римские или нет в нужном диапазоне
        for (DigitalR element:DigitalR.values()){
            if (element.name().equals(operand)){
                resultSystems = "Rome";
                return resultSystems;
            }
        }
        return resultSystems;

    }

}