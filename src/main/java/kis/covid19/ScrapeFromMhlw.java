package kis.covid19;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;

/**
 *
 * @author naoki
 */
public class ScrapeFromMhlw {
    public static void main(String[] args) throws IOException {
        // var url = "https://www.mhlw.go.jp/stf/newpage_10062.html"; // 3/9
        // var url = "https://www.mhlw.go.jp/stf/newpage_10094.html"; // 3/10
        // var url = "https://www.mhlw.go.jp/stf/newpage_10130.html"; // 3/11
        // var url = "https://www.mhlw.go.jp/stf/newpage_10157.html"; // 3/12
        // var url = "https://www.mhlw.go.jp/stf/newpage_10187.html"; //3/13
        // var url = "https://www.mhlw.go.jp/stf/newpage_10226.html"; // 3/16
        // var url = "https://www.mhlw.go.jp/stf/newpage_10251.html"; // 3/17
        // var url = "https://www.mhlw.go.jp/stf/newpage_10309.html"; // 3/18
        // var url = "https://www.mhlw.go.jp/stf/newpage_10335.html"; // 3/19
        // var url = "https://www.mhlw.go.jp/stf/newpage_10385.html"; // 3/23 with detail
        // var url = "https://www.mhlw.go.jp/stf/newpage_10521.html"; // 3/27
        // var url = "https://www.mhlw.go.jp/stf/newpage_10465.html"; // 3/26
        // var url = "https://www.mhlw.go.jp/stf/newpage_10442.html"; // 3/25
        // var url = "https://www.mhlw.go.jp/stf/newpage_10426.html"; // 3/24
        // var url = "https://www.mhlw.go.jp/stf/newpage_10595.html"; // 3/30
        // var url = "https://www.mhlw.go.jp/stf/newpage_10636.html"; // 3/31
        // var url = "https://www.mhlw.go.jp/stf/newpage_10651.html"; // 4/1
        // var url = "https://www.mhlw.go.jp/stf/newpage_10668.html"; // 4/2
        var url = "https://www.mhlw.go.jp/stf/newpage_10688.html"; // 4/3
        var conn = Jsoup.connect(url);
        var doc = conn.get();

        var timeElm = doc.select("time");
        String datetime = timeElm.attr("datetime");
        var date = LocalDate.parse(datetime);

        var tables = doc.select("table");
        Map<String, Integer> data = tables.stream()
                .map(tbl -> tbl.select("tr"))
                .filter(trs -> trs.select("td").get(0).text().contains("都道府県"))
                .flatMap(trs -> trs.stream().skip(1))
                .map(tr -> tr.select("td"))
                .collect(Collectors.toUnmodifiableMap(tds -> tds.get(0).text(),
                        tds -> ScrapePrefFromFK.findInt(tds.get(1).text())));
        PrefJsonProc.writeJson(date, data);
    }
}
