package kis.covid19;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;

/**
 *
 * @author naoki
 */
public class ScrapeDetailFromMhlw {
    public static void main(String... args) throws IOException {

        // var url = "https://www.mhlw.go.jp/stf/newpage_10385.html"; // 3/23 with detail
        // var url = "https://www.mhlw.go.jp/stf/newpage_10426.html"; // 3/24
        // var url = "https://www.mhlw.go.jp/stf/newpage_10442.html"; // 3/25
        // var url = "https://www.mhlw.go.jp/stf/newpage_10465.html"; // 3/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_10521.html"; // 3/27
        // var url = "https://www.mhlw.go.jp/stf/newpage_10595.html"; // 3/30
        // var url = "https://www.mhlw.go.jp/stf/newpage_10636.html"; // 3/31
        // var url = "https://www.mhlw.go.jp/stf/newpage_10651.html"; // 4/1
        // var url = "https://www.mhlw.go.jp/stf/newpage_10668.html"; // 4/2
        // var url = "https://www.mhlw.go.jp/stf/newpage_10688.html"; // 4/3
        var url = args[0];
        scrape(url);
    }
    
    static LocalDate scrape(String url) throws IOException {
        var conn = Jsoup.connect(url);
        var doc = conn.get();

        var timeElm = doc.select("time");
        String datetime = timeElm.attr("datetime");
        var date = LocalDate.parse(datetime);
        System.out.println(date);
        var tables = doc.select("table");
        var data = tables.stream()
                .map(tbl -> tbl.select("tr"))
                .filter(trs -> trs.select("td").get(0).text().contains("都道府県"))
                .flatMap(trs -> trs.stream().skip(1))
                .map(tr -> tr.select("td"))
                .map(tds -> new CreateData.Pref(tds.get(0).text(),
                                tds.get(1).text(),
                                tds.get(2).text(),
                                tds.get(3).text(),
                                tds.get(4).text()))
                .collect(Collectors.toUnmodifiableList());
                        
        PrefJsonProc.writeJson(date, data);
        return date;
    }
}
