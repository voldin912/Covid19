package kis.covid19;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author naoki
 */
public class ScrapeDetailFromMhlwPDF {
    public static void main(String[] args) throws IOException, InterruptedException {
        // var url = "https://www.mhlw.go.jp/content/10906000/000610652.pdf"; // 3/20 with detail
        // var url = "https://www.mhlw.go.jp/content/10906000/000610713.pdf"; // 3/21
        // var url = "https://www.mhlw.go.jp/content/10906000/000610761.pdf"; // 3/22
        // var url = "https://www.mhlw.go.jp/content/10906000/000614789.pdf"; // 3/28
        // var url = "https://www.mhlw.go.jp/content/10906000/000615354.pdf"; // 3/29
        // var url = "https://www.mhlw.go.jp/content/10906000/000618979.pdf"; // 4/4
        // var url = "https://www.mhlw.go.jp/content/10906000/000619075.pdf"; // 4/5
        // var url = "https://www.mhlw.go.jp/content/10906000/000619390.pdf"; // 4/6 with index
        // var url = "https://www.mhlw.go.jp/content/10906000/000619752.pdf"; // 4/7 no index
        // var url = "https://www.mhlw.go.jp/content/10906000/000620185.pdf"; // 4/8 with index
        // var url = "https://www.mhlw.go.jp/content/10906000/000620471.pdf"; // 4/9
        // var url = "https://www.mhlw.go.jp/content/10900000/000620956.pdf"; // 4/10
        // var url = "https://www.mhlw.go.jp/content/10906000/000621070.pdf"; // 4/11
        // var url = "https://www.mhlw.go.jp/content/10906000/000621111.pdf"; // 4/12
        // var url = "https://www.mhlw.go.jp/content/10906000/000621407.pdf"; // 4/13
        // var url = "https://www.mhlw.go.jp/content/10906000/000621708.pdf"; // 4/14
        var url = "https://www.mhlw.go.jp/content/10906000/000622034.pdf"; // 4/15
        
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();
            var res = client.send(req, BodyHandlers.ofInputStream());
        try (var is = res.body(); 
        // try (var is = Files.newInputStream(Path.of("data/nhk/pref4-4.pdf"));
             var doc = PDDocument.load(is)) {
            var stripper = new PDFTextStripper();
            var text = stripper.getText(doc);
            System.out.println(text);
            var daypat = Pattern.compile("([０-９0-9]+)月([０-９0-9]+)日");
            var daymat = daypat.matcher(text);
            if (!daymat.find()) {
                throw new IllegalArgumentException("no date on " + url);
            }
            LocalDate date = LocalDate.now()
                    .withMonth(Integer.parseInt(daymat.group(1)))
                    .withDayOfMonth(Integer.parseInt(daymat.group(2)))
                    .plusDays(1);
            System.out.println(date);
            var pat = Pattern.compile("(\\S+)\\s+(\\d+)\\s*?");
            var data = text.lines()
                    .filter(line -> pat.matcher(line).find())
                    .map(line -> line.split("\\s+"))
                    .map(ar -> new CreateData.Pref(ar[0], ar[ar.length - 9], 
                            ar[ar.length - 6], ar[ar.length - 4], ar[ar.length - 2]))
                    .collect(Collectors.toUnmodifiableList());
            PrefJsonProc.writeJson(date, data);
        }
    }
}
