import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Pattern pattern = Pattern.compile("[0-9]{2}\\.[0-9]{2}");


    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        return Jsoup.parse(new URL(url), 3000);
    }

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string!");
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(0);
            //boolean isMorning = valueLn.text().contains("Утро");
            if (valueLn.text().contains("Утро")) {
                iterationCount = 4;
            } else if (valueLn.text().contains("День")) {
                iterationCount = 3;
            } else if (valueLn.text().contains("Вечер")) {
                iterationCount = 2;
            } else if (valueLn.text().contains("Ночь")) {
                iterationCount = 1;
            }
        }

        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "          ");
            }
            System.out.println();
        }
        return iterationCount;
    }


    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String day = name.select("th[id=dt]").text();
            String date = getDateFromString(day);
            System.out.println(date + "           Явления                        Температура     Давление    Отн.Влажность     Ветер   ");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount - 1;
        }
    }
}
