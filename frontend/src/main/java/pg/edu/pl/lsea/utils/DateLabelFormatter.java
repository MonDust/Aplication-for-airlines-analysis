package pg.edu.pl.lsea.utils;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            return dateFormatter.format(((java.util.Calendar) value).getTime());
        }
        return "";
    }
}

