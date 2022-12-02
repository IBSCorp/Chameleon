package ru.ibsqa.qualit.evaluate;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IDateEvaluator {

    Pattern deltaPattern = Pattern.compile("(?<sign>[\\+\\-])\\s*(?<amount>\\d+)\\s*(?<points>[ydMmsSh])");

    default Calendar applyDelta(Calendar cl, String delta) {
        if (!delta.isEmpty()) {
            Matcher match = deltaPattern.matcher(delta);
            if (match.find()) {
                String sign = match.group("sign");
                String amount = match.group("amount");
                String points = match.group("points");

                int fld = -1;
                switch (points) {
                    case "y":
                        fld = Calendar.YEAR;
                        break;
                    case "M":
                        fld = Calendar.MONTH;
                        break;
                    case "d":
                        fld = Calendar.DATE;
                        break;
                    case "h":
                        fld = Calendar.HOUR;
                        break;
                    case "m":
                        fld = Calendar.MINUTE;
                        break;
                    case "s":
                        fld = Calendar.SECOND;
                        break;
                    case "S":
                        fld = Calendar.MILLISECOND;
                        break;
                }
                cl.add(fld, (sign.equals("-") ? -1 : 1) * Integer.valueOf(amount));
            }
        }
        return cl;
    }

    DateFormatSymbols monthNominativeFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        }

    };

    DateFormatSymbols monthGenitiveFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };

    default DateFormat createFormatter(String format) {
        DateFormat formatter;
        if (format.contains("llll"))
            formatter = new SimpleDateFormat(format.replace("llll", "MMMM"), monthGenitiveFormatSymbols);
        else
            formatter = new SimpleDateFormat(format, monthNominativeFormatSymbols);
        return formatter;
    }
}
