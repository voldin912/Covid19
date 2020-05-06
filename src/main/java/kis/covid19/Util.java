package kis.covid19;

/**
 * @author naoki
 */
public class Util {
    public static int parseInt(String text) {
        return Integer.parseInt(text.replaceAll(",", ""));
    }
    
    public static String zenDigitToHan(String text) {
        var zen = "０１２３４５６７８９";
        var han = "0123456789";
        String result = text;
        for (int i = 0; i < zen.length(); ++i) {
            result = result.replaceAll(zen.substring(i, i + 1), han.substring(i, i + 1));
        }
        return result;
    }
}
