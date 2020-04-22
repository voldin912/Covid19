package kis.covid19;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

        public Pref(String pref, String patients, String hospitalizations, String discharges, String mortality) {
            this.pref = pref;
            this.patients = Integer.parseInt(patients);
            this.hospitalizations = Integer.parseInt(hospitalizations);
            this.discharges = Integer.parseInt(discharges);
            this.mortality = Integer.parseInt(mortality);
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
        List<Integer> patients;
        List<Integer> motarity;
        List<String> dates;
        
        ChartPref(String code, String pref) {
            this.code = Integer.parseInt(code);
            this.pref = pref;
            patients = new ArrayList<>();
            dates = new ArrayList<>();
            motarity = new ArrayList<>();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Prefs prefs = new Prefs();
        prefs.prefs = prefString.lines()
                .map(p -> p.split(","))
                .map(p -> new ChartPref(p[0], p[1]))
                .collect(Collectors.toUnmodifiableList());

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<List<Pref>> latest = new ArrayList<>();
        latest.add(null);
        Stream.concat(
                Stream.iterate(LocalDate.of(2020,3,8), d -> d.plusDays(1))
                      .map(d -> Path.of(String.format("data/prefs%s.json", d)))
                      .takeWhile(Files::exists),
                // Stream.of(Path.of("data/nhk/input-pref2020-04-03.json")))
                Stream.empty())
              .forEach(path -> {
                  try(var is = Files.newInputStream(path)) {
                    InputPref data = mapper.readValue(is, 
                            InputPref.class);
                    latest.set(0, data.prefList);

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
                        p.patients.add(patients.getOrDefault(p.pref, zero).patients);
                        p.motarity.add(patients.getOrDefault(p.pref, zero).mortality);
                    }
                    prefs.lastUpdate = date.toString();
                  } catch (IOException ex) {
                      throw new UncheckedIOException(ex);
                  }
              });
        
        try (var bw = Files.newBufferedWriter(Path.of("docs/prefs.js"));
             var pw = new PrintWriter(bw)) {
            pw.printf("let data = %s;%n", mapper.writeValueAsString(prefs));
            var d = latest.get(0);
            d.sort(Comparator.comparingInt(Pref::getPatients).reversed());
            if (List.of("総計", "全国").contains(d.get(0).getPref())) {
                d.remove(0);
            }
            pw.printf("let latest = {prefs: %s, patients: %s, motarity: %s};%n",
                    mapper.writeValueAsString(d.stream()
                                               .map(Pref::getPref)
                                               .collect(Collectors.toUnmodifiableList())),
                    mapper.writeValueAsString(d.stream()
                                               .map(Pref::getPatients)
                                               .collect(Collectors.toUnmodifiableList())),
                    mapper.writeValueAsString(d.stream()
                                               .map(Pref::getMortality)
                                               .collect(Collectors.toUnmodifiableList())));
        }
    }
    
        
    
    static String prefString = "" +
        "1,北海道\n" +
        "2,青森県\n" +
        "3,岩手県\n" +
        "4,宮城県\n" +
        "5,秋田県\n" +
        "6,山形県\n" +
        "7,福島県\n" +
        "8,茨城県\n" +
        "9,栃木県\n" +
        "10,群馬県\n" +
        "11,埼玉県\n" +
        "12,千葉県\n" +
        "13,東京都\n" +
        "14,神奈川県\n" +
        "15,新潟県\n" +
        "16,富山県\n" +
        "17,石川県\n" +
        "18,福井県\n" +
        "19,山梨県\n" +
        "20,長野県\n" +
        "21,岐阜県\n" +
        "22,静岡県\n" +
        "23,愛知県\n" +
        "24,三重県\n" +
        "25,滋賀県\n" +
        "26,京都府\n" +
        "27,大阪府\n" +
        "28,兵庫県\n" +
        "29,奈良県\n" +
        "30,和歌山県\n" +
        "31,鳥取県\n" +
        "32,島根県\n" +
        "33,岡山県\n" +
        "34,広島県\n" +
        "35,山口県\n" +
        "36,徳島県\n" +
        "37,香川県\n" +
        "38,愛媛県\n" +
        "39,高知県\n" +
        "40,福岡県\n" +
        "41,佐賀県\n" +
        "42,長崎県\n" +
        "43,熊本県\n" +
        "44,大分県\n" +
        "45,宮崎県\n" +
        "46,鹿児島県\n" +
        "47,沖縄県\n" +
        "";
}
