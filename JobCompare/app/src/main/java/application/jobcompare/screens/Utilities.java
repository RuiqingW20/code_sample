package application.jobcompare.screens;

import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Utilities {
    public static void setText(EditText inputText, int value) {
        inputText.setText(String.valueOf(value));
    }

    public static void setText(EditText inputText, String value) {
        inputText.setText(value);
    }

    public static int getTextAsInt(TextView inputText) {
        String strValue = inputText.getText().toString();
        int value;
        try {
            value = Integer.parseInt(strValue);
        } catch (Exception e) {
            try {
                Number nValue = NumberFormat.getNumberInstance(Locale.US).parse(strValue);
                value = Integer.valueOf(nValue.toString());
            } catch (ParseException pe) {
                return 0;
            }
        }

        return value;
    }

    public static String getText(TextView inputText) {
        return inputText.getText().toString();
    }

    public static void addTextWatcher(EditText editText) {
        TextWatcher tw = new NumberTextWatcher(editText, Locale.US, 0);
        editText.addTextChangedListener(tw);
    }
}
