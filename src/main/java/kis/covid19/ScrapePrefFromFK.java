package kis.covid19;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 福岡県のサイトから都道府県ごとの報告数を得る
 * @author naoki
 */
public class ScrapePrefFromFK {
    
    public static void main_(String[] args) throws IOException {
        var temp = "https://www3.nhk.or.jp/news/special/coronavirus/data/input-pref.json";
        Connection conn = Jsoup.connect(
                "https://www.pref.fukuoka.lg.jp/contents/corona-kokunai.html");
        Document doc = conn.get();
        
        // page update
        Elements spans = doc.select("span.page_update");
        Pattern pat = Pattern.compile("(\\d+)年(\\d+)月(\\d+)日");
        
        LocalDate date = spans.stream()
                .findFirst()
                .map(Element::text)
                .map(pat::matcher)
                .filter(Matcher::find)
                .map(mat -> LocalDate.of(Integer.parseInt(mat.group(1)),
                        Integer.parseInt(mat.group(2)),
                        Integer.parseInt(mat.group(3))))
                .orElseThrow(() -> new RuntimeException("Data not found"));
        
        // prefs
        try (var pw = new PrintWriter(String.format("prefs%s.json", date), "UTF-8")) {
            pw.printf("{\n" +
                       "  \"lastupdate\":\"%s\",\n" +
                       "  \"prefList\": [\n", date);
            Elements tables = doc.select("table");
            tables.stream()
                  .filter(table -> table.select("caption").stream()
                                        .map(Element::text)
                                        .anyMatch(c -> c.contains("国内事例")))
                  .flatMap(table -> table.select("tbody tr").stream())
                  .map(tr -> tr.select("td"))
                  .filter(tds -> tds.size() >= 2)
                  .forEach(tds -> pw.printf(
                       "    {\n" +
                       "      \"pref\": \"%s\",\n" +
                       "      \"patients\": %s\n" +
                       "    },\n", tds.get(0).text(), findInt(tds.get(1).text())));
            pw.println("    {\n" +
                       "      \"pref\": \"_\",\n" +
                       "      \"patients\": 0\n" +
                       "    }\n" +
                       "  ]\n" +
                       "}\n");
        }
    }
    static Pattern ipat = Pattern.compile("\\d+");
    static int findInt(String s) {
        Matcher mat = ipat.matcher(s);
        if (!mat.find()) {
            throw new IllegalArgumentException(String.format("%s is not int", s));
        }
        return Integer.parseInt(mat.group());
    }
}
