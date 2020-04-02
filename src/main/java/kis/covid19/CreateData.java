package kis.covid19;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedWriter;
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
    static class Pref {
        String pref;
        int patients;
    }
    
    @Data
    static class InputPref {
        String lastupdate;
        List<Pref> prefList;
    }
    
    @Data
    static class Prefs {
        String lastUpdate;
        List<_Pref> prefs = new ArrayList<>();
    }
    @Data
    @NoArgsConstructor
    static class _Pref {
        int code;
        String pref;
        List<Integer> patients;
        List<String> dates;
        
        _Pref(String code, String pref) {
            this.code = Integer.parseInt(code);
            this.pref = pref;
            patients = new ArrayList<>();
            dates = new ArrayList<>();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Prefs prefs = new Prefs();
        prefs.prefs = prefString.lines()
                .map(p -> p.split(","))
                .map(p -> new _Pref(p[0], p[1]))
                .collect(Collectors.toUnmodifiableList());

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<List<Pref>> latest = new ArrayList<>();
        latest.add(null);
        Stream.concat(
                Stream.iterate(LocalDate.of(2020,3,8), d -> d.plusDays(1))
                      .map(d -> Path.of("data/prefs%s.json".formatted(d)))
                      .takeWhile(Files::exists),
                //Stream.of(Path.of("data/prefs2020-04-01.json")))
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
                    Map<String, Integer> patients = data.prefList.stream()
                            .collect(Collectors.toUnmodifiableMap(Pref::getPref, Pref::getPatients));
                    for (var p : prefs.prefs) {
                        p.dates.add(date.toString());
                        p.patients.add(patients.getOrDefault(p.pref, 0));
                    }
                    prefs.lastUpdate = date.toString();
                  } catch (IOException ex) {
                      throw new UncheckedIOException(ex);
                  }
              });
        
        try (var bw = Files.newBufferedWriter(Path.of("docs/prefs.js"));
             var pw = new PrintWriter(bw)) {
            pw.printf("let data = %s;%n", mapper.writeValueAsString(prefs));
            latest.get(0).sort(Comparator.comparingInt(Pref::getPatients).reversed());
            latest.get(0).remove(0);
            pw.printf("let latest = {prefs: %s, patients: %s};%n",
                    mapper.writeValueAsString(latest.get(0).stream()
                                                           .map(Pref::getPref)
                                                           .collect(Collectors.toUnmodifiableList())),
                    mapper.writeValueAsString(latest.get(0).stream()
                                                           .map(Pref::getPatients)
                                                           .collect(Collectors.toUnmodifiableList())));
        }
    }
    
        
    
    static String prefString = """
        1,北海道
        2,青森県
        3,岩手県
        4,宮城県
        5,秋田県
        6,山形県
        7,福島県
        8,茨城県
        9,栃木県
        10,群馬県
        11,埼玉県
        12,千葉県
        13,東京都
        14,神奈川県
        15,新潟県
        16,富山県
        17,石川県
        18,福井県
        19,山梨県
        20,長野県
        21,岐阜県
        22,静岡県
        23,愛知県
        24,三重県
        25,滋賀県
        26,京都府
        27,大阪府
        28,兵庫県
        29,奈良県
        30,和歌山県
        31,鳥取県
        32,島根県
        33,岡山県
        34,広島県
        35,山口県
        36,徳島県
        37,香川県
        38,愛媛県
        39,高知県
        40,福岡県
        41,佐賀県
        42,長崎県
        43,熊本県
        44,大分県
        45,宮崎県
        46,鹿児島県
        47,沖縄県
        """;
}
