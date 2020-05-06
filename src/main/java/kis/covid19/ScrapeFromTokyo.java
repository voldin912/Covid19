/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kis.covid19;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.util.regex.Pattern;
import kis.covid19.CreateData.Pref;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author naoki
 */
public class ScrapeFromTokyo {
    public static void main(String[] args) throws IOException, InterruptedException {
        var url = "https://www.bousai.metro.tokyo.lg.jp/_res/projects/default_project/_page_/001/007/825/20200505.pdf"; // 5/5
        read(url);
        
    }
    static void amendTokyo(String tokyoUrl, LocalDate date) throws IOException, InterruptedException {
        Path p = Path.of(PrefJsonProc.jsonName(date));
        CreateData.InputPref json = PrefJsonProc.readJson(p);
        Pref tokyo = ScrapeFromTokyo.read(tokyoUrl);
        amend(json, tokyo);
        PrefJsonProc.writeJson(date, json.getPrefList());
    }
    
    static void amend(CreateData.InputPref data, Pref pref) {
        for (Pref p : data.getPrefList()) {
            if (!p.pref.equals(pref.pref)) {
                continue;
            }
            p.patients = pref.patients;
            p.discharges = pref.discharges;
            p.hospitalizations = pref.hospitalizations;
            p.mortality = pref.mortality;
        }
    }

    static Pref read(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        var res = client.send(req, BodyHandlers.ofInputStream());
        try (var input = res.body();
             var doc = PDDocument.load(input)) {
            var stripper = new PDFTextStripper();
            var text = stripper.getText(doc);
            System.out.println(text);
            return readData(text);
        }
    }
    
    static Pref readData(String text) {
        var ar = readMortality(text);
        var dischargeText = readDischarge(text);
        int patients = Util.parseInt(ar[0]);
        int mortality = Util.parseInt(ar[2]);
        int discharge = Util.parseInt(dischargeText);
        return new Pref("東京都", patients, patients - discharge - mortality, discharge, mortality);
    }
    
    static String[] readMortality(String text) {
        return text.lines()
                .dropWhile(str -> !str.contains("死亡"))
                .skip(1)
                .map(line -> line.split("\\s+"))
                .findFirst().get();
    }
    static String readDischarge(String text) {
        return text.lines()
                .dropWhile(str -> !str.contains("退院（累計）"))
                .skip(1)
                .findFirst().get();
    }
    static LocalDate readDate(String text) {
        var pt = Pattern.compile("(\\d+)年(\\d+)月(\\d+)日");
        var mat = pt.matcher(Util.zenDigitToHan(text));
        if (!mat.find()) {
            throw new IllegalArgumentException("Can't find a date");
        }
        var dt = JapaneseDate.of(JapaneseEra.REIWA, 
                Integer.parseInt(mat.group(1)),
                Integer.parseInt(mat.group(2)),
                Integer.parseInt(mat.group(3)));
        return LocalDate.from(dt);
    }
    

}
