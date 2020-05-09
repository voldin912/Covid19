package kis.covid19;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.util.regex.Pattern;
import kis.covid19.CreateData.Pref;

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
        var text = Util.readPdf(url);
        System.out.println(text);
        return readData(text);
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

}
