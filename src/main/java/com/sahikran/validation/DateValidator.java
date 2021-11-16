package com.sahikran.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

public final class DateValidator implements Validator<String> {

    private final Validator<String> pageElementValidator;

    private final String elementValue;

    private final String digitRegEx = "[0-9]+";

    private DateTimeFormatter dateTimeFormatter;

    public DateValidator(String elementValue, Validator<String> pageElementValidator){
        this.elementValue = elementValue;
        this.pageElementValidator = pageElementValidator;
    }

    @Override
    public boolean validate() {
        // check if an element has date value
        boolean isDate = false;
        /**
         * *** Date value criteria ***
         *  1. text should start and end with a number
         *  2. contains - or / or \
         *  3. if 2 satisfies, check for contains month name, ex: apr, may, jun, jul...
         *  4. is 3 satisfies, then return true
         */
        if(elementValue == null || elementValue.isEmpty()){
            return false;
        }

        if(elementValue.length() < 2){
            return false;
        }

        String lowerCaseElementValue = elementValue.toLowerCase();
        isDate = startsWithNumber(lowerCaseElementValue) 
            && containsHiphen(lowerCaseElementValue) 
            && isADate(lowerCaseElementValue);
        
        if(isDate && pageElementValidator != null){
            return pageElementValidator.validate();
        }
        return isDate;
    }

    private boolean startsWithNumber(String elementValue){
        Pattern numberPattern = Pattern.compile(digitRegEx);
        return numberPattern.matcher(elementValue.substring(0, 2)).matches();
    }

    private boolean containsHiphen(String elementValue){
        return elementValue.contains("-") || elementValue.contains("/");
    }

    private boolean isADate(String elementValue){
        dateTimeFormatter = new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("dd-MMM-yyyy")
                            .toFormatter(Locale.getDefault());
        try{
            LocalDate.parse(elementValue, dateTimeFormatter);
        } catch (DateTimeParseException ex){
            return false;
        }
        return true;       
    }

    
}
