package kis.covid19;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
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

        Stream.of(
                "prefs2020-03-23",
                "prefs2020-03-24",
                "prefs2020-03-25",
                "prefs2020-03-26",
                "prefs2020-03-27",
                "input-pref2020-03-28",
                "input-pref2020-03-29",
                "input-pref2020-03-30",
                "input-pref2020-03-31")
              .map(p -> Path.of("data/%s.json".formatted(p)))
              .forEach(path -> {
                  try(var is = Files.newInputStream(path)) {
                    InputPref data = mapper.readValue(is, 
                            InputPref.class);

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
        mapper.writeValue(Files.newBufferedWriter(Path.of("data/prefs.json")), prefs);
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
