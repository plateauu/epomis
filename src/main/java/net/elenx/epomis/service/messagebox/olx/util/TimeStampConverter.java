package net.elenx.epomis.service.messagebox.olx.util;

import org.jsoup.helper.StringUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import static java.time.temporal.ChronoField.YEAR;

public class TimeStampConverter {

    private static final String DUMB_DATE = "2017-01-01 00:01";

    public static String convert(String timestamp) {
        if (StringUtil.isBlank(timestamp)) {
            return DUMB_DATE;
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MMM, kk:mm")
                .parseDefaulting(YEAR, LocalDateTime.now().getYear())
                .toFormatter()
                .withLocale(Locale.forLanguageTag("pl"));

        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(dateTime);
    }
}
