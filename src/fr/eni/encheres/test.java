package fr.eni.encheres;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        Matcher m = p.matcher("9 0OIU_ -!'");
        int i = 0;
        while(m.find()) {
            if(!m.matches()) {
                i++;

            }
        }
        System.out.println(i);
    }

}
