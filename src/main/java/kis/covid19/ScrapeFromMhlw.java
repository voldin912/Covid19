package kis.covid19;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import org.jsoup.Jsoup;

/**
 *
 * @author naoki
 */
public class ScrapeFromMhlw {
    public static void main_(String[] args) throws IOException {
        // var url = "https://www.mhlw.go.jp/stf/newpage_10465.html"; // 3/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_10442.html"; // 3/25
        // var url = "https://www.mhlw.go.jp/stf/newpage_10426.html"; // 3/24
        var url = "https://www.mhlw.go.jp/stf/newpage_10385.html"; // 3/23
        var conn = Jsoup.connect(url);
        var doc = conn.get();

        var timeElm = doc.select("time");
        String datetime = timeElm.attr("datetime");
        var date = LocalDate.parse(datetime);
        
        try (var pw = new PrintWriter("prefs%s.json".formatted(date), "UTF-8")) {
            pw.print("""
                       {
                         "lastupdate":"%s",
                         "prefList": [
                       """.formatted(date));
            
            var tables = doc.select("table");
            tables.stream()
                    .map(tbl -> tbl.select("tr"))
                    .filter(trs -> trs.select("td").get(0).text().contains("都道府県"))
                    .flatMap(trs -> trs.stream().skip(1))
                    .map(tr -> tr.select("td"))
                    .forEach(tds -> pw.print(
                       """
                           {
                             "pref": "%s",
                             "patients": %s
                           },
                       """.formatted(tds.get(0).text(),
                               ScrapePrefFromFK.findInt(tds.get(1).text()))));
            pw.print("""
                           {
                             "pref": "_",
                             "patients": 0
                           }
                         ]
                       }
                       """);
            
        }
    }
}
