package br.ufal.ic.p2.wepayu;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public final class DataUtil {

    private DataUtil() {
    }

    public static final DateTimeFormatter FORMATO =
        new DateTimeFormatterBuilder()
            .appendPattern("d/M/uuuu")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
}
