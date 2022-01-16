package ru.jcups.testbotii.utils;

public class PP {

    public static void print(Object object) {
        String obj = object.toString();
        StringBuilder result = new StringBuilder();

        int tabs = 0;
        for (String ch : obj.replaceAll(", ",",").split("")) {
            switch (ch) {
                case "{":
                    tabs++;
                    result.append("{\n");
                    result.append("\t".repeat(Math.max(0, tabs)));
                    break;
                case "}":
                    tabs--;
                    result.append("\n");
                    result.append("\t".repeat(Math.max(0, tabs)));
                    result.append("}");
                    break;
                case "[":
                    tabs++;
                    result.append("[\n");
                    result.append("\t".repeat(Math.max(0, tabs)));
                    break;
                case "]":
                    tabs--;
                    result.append("\n");
                    result.append("\t".repeat(Math.max(0, tabs)));
                    result.append("]");
                    break;
                case ",":
                    result.append(",\n");
                    result.append("\t".repeat(Math.max(0, tabs)));
                    break;
                default:
                    result.append(ch);
                    break;
            }
        }
        System.out.println(result);
    }
}
