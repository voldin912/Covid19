package kis.covid19;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author naoki
 */
public class CreateData {
    @Data
    @NoArgsConstructor
    static class Pref {
        String pref;
        int patients;
        int hospitalizations;
        int discharges;
        int mortality;
        int pcr;
        int severe;

        public Pref(String pref, int patients, int hospitalizations, int discharges, int mortality, int severe, int pcr) {
            this.pref = pref;
            this.patients = patients;
            this.hospitalizations = hospitalizations;
            this.discharges = discharges;
            this.mortality = mortality;
            this.severe = severe;
            this.pcr = pcr;
        }
        
        public Pref(String pref, int patients, int hospitalizations, int discharges, int mortality) {
            this(pref, patients, hospitalizations, discharges, mortality, 0, 0);
        }
        public Pref(String pref, String patients, String hospitalizations, String discharges, String mortality) {
            this(pref, Util.parseInt(patients), Util.parseInt(hospitalizations), Util.parseInt(discharges), Util.parseInt(mortality));
        }
    }
    
    @Data
    static class InputPref {
        String lastupdate;
        List<Pref> prefList;
    }
    
    @Data
    static class Prefs {
        String lastUpdate;
        List<ChartPref> prefs = new ArrayList<>();
    }
    @Data
    @NoArgsConstructor
    static class ChartPref {
        int code;
        String pref;
        int population;
        List<Integer> patients;
        List<Integer> motarity;
        List<Integer> hospitalizations;
        List<Integer> severes;
        List<String> dates;
        
        ChartPref(String code, String pref, String population) {
            this.code = Integer.parseInt(code.trim());
            this.pref = pref.trim();
            this.population = Integer.parseInt(population.trim());
            patients = new ArrayList<>();
            dates = new ArrayList<>();
            motarity = new ArrayList<>();
            hospitalizations = new ArrayList<>();
            severes = new ArrayList<>();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Prefs prefs = new Prefs();
        prefs.prefs = prefString.lines()
                .map(p -> p.split(","))
                .map(p -> new ChartPref(p[0], p[1], p[2]))
                .collect(Collectors.toUnmodifiableList());

        var start = LocalDate.of(2021,6,25);
        //var start = LocalDate.of(2020,3,8);
        Stream.concat(
                Stream.iterate(start, d -> d.plusDays(1))
                      .map(d -> Path.of(PrefJsonProc.jsonName(d)))
                      .takeWhile(Files::exists),
                // Stream.of(Path.of("data/nhk/input-pref2020-04-03.json")))
                Stream.empty())
              .forEach(path -> {
                    InputPref data = PrefJsonProc.readJson(path);

                    var pat = Pattern.compile("(\\d+-)?(\\d{1,2})[/-](\\d{1,2})");
                    var mat = pat.matcher(data.lastupdate);
                    if (!mat.find()) {
                        throw new IllegalArgumentException("wrong date for " + data.lastupdate);
                    }
                    LocalDate date;
                    try {
                        date = LocalDate.now()
                                .withMonth(Integer.parseInt(mat.group(2)))
                                .withDayOfMonth(Integer.parseInt(mat.group(3)));
                    } catch (DateTimeException ex) {
                        throw new RuntimeException("wrong date for " + data.lastupdate);
                    }
                    Map<String, Pref> patients = data.prefList.stream()
                            .collect(Collectors.toUnmodifiableMap(Pref::getPref, Function.identity()));
                    Pref zero = new Pref();
                    for (var p : prefs.prefs) {
                        p.dates.add(date.toString());
                        var pref = patients.getOrDefault(p.pref, zero);
                        p.patients.add(pref.patients);
                        p.motarity.add(pref.mortality);
                        p.hospitalizations.add(Math.max(pref.hospitalizations, 0));
                        p.severes.add(pref.severe);
                    }
                    prefs.lastUpdate = date.toString();
              });
        
        var mapper = PrefJsonProc.createMapper();
        try (var bw = Files.newBufferedWriter(Path.of("docs/prefs.js"));
             var pw = new PrintWriter(bw)) {
            pw.printf("let data = %s;%n", mapper.writeValueAsString(prefs));
        }
    }
    
        
    
    static String prefString = "" +
        " 1,北海道  , 5320\n" +
        " 2,青森県  , 1278\n" +
        " 3,岩手県  , 1255\n" +
        " 4,宮城県  , 2323\n" +
        " 5,秋田県  ,  996\n" +
        " 6,山形県  , 1102\n" +
        " 7,福島県  , 1882\n" +
        " 8,茨城県  , 2892\n" +
        " 9,栃木県  , 1957\n" +
        "10,群馬県  , 1960\n" +
        "11,埼玉県  , 7310\n" +
        "12,千葉県  , 6246\n" +
        "13,東京都  ,13724\n" +
        "14,神奈川県, 9159\n" +
        "15,新潟県  , 2267\n" +
        "16,富山県  , 1056\n" +
        "17,石川県  , 1147\n" +
        "18,福井県  ,  779\n" +
        "19,山梨県  ,  823\n" +
        "20,長野県  , 2076\n" +
        "21,岐阜県  , 2008\n" +
        "22,静岡県  , 3675\n" +
        "23,愛知県  , 7525\n" +
        "24,三重県  , 1800\n" +
        "25,滋賀県  , 1413\n" +
        "26,京都府  , 2599\n" +
        "27,大阪府  , 8823\n" +
        "28,兵庫県  , 5503\n" +
        "29,奈良県  , 1348\n" +
        "30,和歌山県,  945\n" +
        "31,鳥取県  ,  565\n" +
        "32,島根県  ,  685\n" +
        "33,岡山県  , 1907\n" +
        "34,広島県  , 2829\n" +
        "35,山口県  , 1383\n" +
        "36,徳島県  ,  743\n" +
        "37,香川県  ,  967\n" +
        "38,愛媛県  , 1364\n" +
        "39,高知県  ,  714\n" +
        "40,福岡県  , 5107\n" +
        "41,佐賀県  ,  824\n" +
        "42,長崎県  , 1354\n" +
        "43,熊本県  , 1765\n" +
        "44,大分県  , 1152\n" +
        "45,宮崎県  , 1089\n" +
        "46,鹿児島県, 1626\n" +
        "47,沖縄県  , 1443\n" +
        "";
}
