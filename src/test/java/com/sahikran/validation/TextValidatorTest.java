package com.sahikran.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class TextValidatorTest {
    
    private Validator<String> pageElementValidator;

    @Test
    @DisplayName("find if a string exists in a large text string")
    public void whenStringExistsInALargeText_returnABoolean(){
        pageElementValidator = new TextValidator("of yoga", "yoga", null);
        boolean actual = pageElementValidator.validate();
        assertTrue(actual);
    }

    @Test
    @DisplayName("Returns false if string does not exists in a large text")
    public void whenStringDontExistsInALargeText_returnFalse(){
        pageElementValidator = new TextValidator("tring exists in a large text string", "yoga", null);
        boolean actual = pageElementValidator.validate();
        assertFalse(actual);
    }

    @Test
    @DisplayName("Returns true if string exists but as part of an another word in a large text")
    public void whenStringExistsInALargeText_combineWithAWord_returnTrue(){
        pageElementValidator = new TextValidator("Effect of selectedyoga practices or physical exercises on cardio pulmonary function and motor ability of the adolescent boys and girls with bronchial asthma", 
                                "yoga", null);
        boolean actual = pageElementValidator.validate();
        assertTrue(actual);
    }

}
