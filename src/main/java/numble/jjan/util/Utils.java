package numble.jjan.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Utils {

    private static final String UNSECURED_CHAR_REGULAR_EXPRESSION =
            "!\\\"#$%&'()*+,.:;<=>?@[]^_`{]+$|.*select.*|.*create.*|.*update.*|.*alter.*|.*delete.*|.*insert.*|.*drop.*|.*--.*|.*union.*|.*join.*";

    private static Pattern unsecuredCharPattern =
            Pattern.compile(UNSECURED_CHAR_REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);

    public static String makeSecureString(String uri) {
        List<Integer> indexList = findIndex("/", uri);
        if (indexList.size() > 2) {
            uri = uri.substring(0, indexList.get(2));
        }
        Matcher matcher = unsecuredCharPattern.matcher(uri);
        return matcher.replaceAll("");
    }

    private static List<Integer> findIndex(String word, String document) {
        List<Integer> indexList = new ArrayList<>();
        int index = document.indexOf(word);
        while (index != -1) {
            indexList.add(index);
            index = document.indexOf(word, index + word.length());
        }
        return indexList;
    }
}
