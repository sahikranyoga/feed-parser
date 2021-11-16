package com.sahikran.validation;

import com.sahikran.hasher.StringHashImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concreate implementation of {@link Validator} to specifically search
 * for existence of a given string passed by the client 
 */
public final class TextValidator implements Validator<String> {

    private static final Logger log = LoggerFactory.getLogger(TextValidator.class);

    private final String searchString;

    private final String inputString;

    private final Validator<String> pageElementValidator;

    public TextValidator(String inputString, String searchString, Validator<String> pageElementValidator){
        this.inputString = inputString;
        this.searchString = searchString;
        this.pageElementValidator = pageElementValidator;
    }

    /**
     * validates if the search string is present in the given Element's own text
     * if exists return true else false
     * @param t jsoup nodes Element object
     */
    @Override
    public boolean validate() {
        boolean isStringExists = false;
        // for example, search for string "yoga" in the input
        if(searchForInputString(inputString, searchString)){
            isStringExists = true;
        }

        if(isStringExists && pageElementValidator != null){
            return pageElementValidator.validate();
        }
        return isStringExists;
    }
    
    private boolean searchForInputString(String input, String target){
        if(input == null || target == null){
            log.error("Either input or target strings are null");
            return false;
        }
        // target cant be empty
        if(target.isEmpty()){
            log.error("target string is empty");
            return false;
        }
        // target cant be greater than input
        if(target.length() > input.length()){
            log.error("target is greater than input");
            return false;
        }

        input = input.toLowerCase();
        target = target.toLowerCase();

        String inputByTargetLength = input.substring(0, target.length());
        long targetHash = (new StringHashImpl()).generateHash(target);
        long inpHashByTargetLen = (new StringHashImpl()).generateHash(inputByTargetLength);
        // check if target is found at the first substring
        if(targetHash == inpHashByTargetLen && target.equalsIgnoreCase(inputByTargetLength)){
            return true;
        }

        int xPow = 1;
        for(int i = 0; i < target.length() - 1; i++){
            xPow *= StringHashImpl.POLYNOMIAL; 
        }

        for(int i = target.length(); i < input.length(); i++){
            int toRemove = input.charAt(i - target.length());
            inpHashByTargetLen = (inpHashByTargetLen - (toRemove * xPow)) * StringHashImpl.POLYNOMIAL + input.charAt(i);
            if(isEqualsToTarget(targetHash, inpHashByTargetLen) 
                    && isEqualsToTarget(target, input.substring(i - target.length() + 1, i + 1)))
                    return true;
        }
        return false;
    }

    private static <T> boolean isEqualsToTarget(T target, T input){
        if(target instanceof String && input instanceof String){
            return ((String) target).equalsIgnoreCase((String) input);
        }
        if(target instanceof Long && input instanceof Long){
            return ((Long)target).equals((Long)input);
        }
        return target == input;
    }
}
