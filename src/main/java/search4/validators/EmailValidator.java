package search4.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public boolean validateEmail(Object value) {
        String email = (String) value;
        //Regex for email verification
        String regexPatternString = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@+[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern regexPattern = Pattern.compile(regexPatternString);

        Matcher matcher = regexPattern.matcher(email);

        if (!email.equals("")) {
            if(!matcher.matches()) {
                return false;
            }
        }
        return true;
    }
}
