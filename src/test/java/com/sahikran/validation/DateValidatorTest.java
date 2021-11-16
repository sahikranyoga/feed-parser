package com.sahikran.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DateValidatorTest {
    
    private Validator<String> pageElementValidator;

    @Test
    @DisplayName("test if given page element has date value in it")
    public void whenInputElementHasDate_returnTrue(){
        pageElementValidator = new DateValidator("12-Apr-2020", null);
        assertTrue(pageElementValidator.validate());
    }

    @Test
    @DisplayName("test if given page element has invalid date value in it")
    public void whenInputElementHasInvalidDate_returnFalse(){
        pageElementValidator = new DateValidator("12-nffad", null);
        assertFalse(pageElementValidator.validate());
    }
}
