package proj.demo.campushaat;

import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by stpl on 5/23/2017.
 */

public class CustomInputFilter implements InputFilter {
    private TextInputLayout layout;
    private String lengthError,contentError;
    int length;

    public CustomInputFilter(TextInputLayout layout, int length) {
        this.layout = layout;
        if(length==6){
            lengthError="Max 6 Characters Allowed";
        }
        else {
            lengthError="Max 20 Characters Allowed";
        }
        contentError="Only Alphabets and Numeric Allowed";
        this.length=length;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        String regEx = "^[a-zA-Z0-9]+$";
        if (source != null && !source.toString().matches(regEx) && !source.toString().equals("")) {
            layout.setErrorEnabled(true);
            layout.setError(contentError);
            return "";
        }
        layout.setErrorEnabled(false);
        int keep = length - (dest.length() - (dend - dstart));
        if (6 - (dest.length() - (dend - dstart)) == 0) {
            layout.setErrorEnabled(true);
            layout.setError(lengthError);
            return "";
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            layout.setErrorEnabled(false);
            return source.subSequence(start, keep);
        }

    }
}

