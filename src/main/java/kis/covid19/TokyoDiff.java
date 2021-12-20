/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kis.covid19;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author naoki
 */
public class TokyoDiff {
    static String url = "https://www.metro.tokyo.lg.jp/tosei/hodohappyo/press/2021/10/29/documents/20211029_27_3.pdf";
    static String file = "20211029_27_3.pdf";
    /*
    7月12日（月） 502 182,140 1 0 503 182,116 756.7 756.9
    */
    static Pattern pat = Pattern.compile("(\\d+)月(\\d+)日（.） ([0-9,]+) ([0-9,]+) ([0-9,]+) (▲ )?([0-9,]+) ([0-9,]+) ([0-9,]+) ([0-9\\.,]+) ([0-9\\.,]+)");
    
    public record TokyoDif(LocalDate date, int daily, int total, int addition, int deleted, int dailyFixed, int totalFixed, double ma7, double ma7Fixed){}
    static NumberFormat format = NumberFormat.getInstance();
    public static void main(String[] args) throws IOException, InterruptedException {
        /*
                var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();
        var res = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        try (var is = res.body(); 
                */
         diffs().forEach(System.out::println);
                 
    }
    
    public static Map<LocalDate, TokyoDif> dateDiff() throws IOException {
        var df = DateTimeFormatter.ISO_DATE;
        return diffs().stream()
                .collect(Collectors.toMap(TokyoDif::date, Function.identity()));
    }
    
    public static List<TokyoDif> diffs() throws IOException {
        return diffs(Files.newInputStream(Path.of("data", file)));
    }
    
    public static List<TokyoDif> diffs(InputStream is) throws IOException {
        try (is;
             var doc = PDDocument.load(is)) {
            var stripper = new PDFTextStripper();
            var text = stripper.getText(doc);
            return text.lines()
                    .map(Util::zenDigitToHan)
                    .map(pat::matcher)
                    .filter(Matcher::find)
                    .map(mat -> new TokyoDif(LocalDate.of(2021, Integer.parseInt(mat.group(1)), Integer.parseInt(mat.group(2))),
                            parse(mat.group(3)), parse(mat.group(4)), parse(mat.group(5)), parse(mat.group(7)), parse(mat.group(8)), parse(mat.group(9)), Double.parseDouble(mat.group(10).replaceAll(",", "")), Double.parseDouble(mat.group(11).replaceAll(",", ""))))
                    .toList();
            
        }        
    }
    
    static int parse(String str) {
        try {
            return format.parse(str).intValue();
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
            
}
