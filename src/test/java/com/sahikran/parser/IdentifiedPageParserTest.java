package com.sahikran.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.sahikran.exception.PageParserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IdentifiedPageParserTest {

    private PageParser pageParser;

    public static File getInputFile(String inputFileName){
        return new File(IdentifiedPageParserTest.class.getClassLoader().getResource(inputFileName).getFile());
    }

    private static List<Arguments> provideMutlipleFileInputs(){
        return Arrays.asList(
          Arguments.of(getInputFile("shodhganga-sample.html").getAbsolutePath(), "yoga", 119),
          Arguments.of(getInputFile("shodhganga-complex.html").getAbsolutePath(), "ayurveda", 13)  
        );
    }
    
    @ParameterizedTest
    @DisplayName("To search for a content related to a subject such as yoga in a local html page")
    @MethodSource("provideMutlipleFileInputs")
    public void whenGivenAValidLocalHtmlPage_returnBooleanForAGivenString(String inputFilePath, String searchString, int expected) 
        throws PageParserException{
        PageParserFactory pageParserFactory = new PageParserFactoryImpl(searchString, Duration.ofSeconds(10));
        pageParser = pageParserFactory.get(inputFilePath, PageParserFactoryImpl.ParserType.IDENTIFIED);
        Result result = pageParser.parse();
        assertTrue(result != null);
        assertTrue(result.getFeedItems().size() == expected);
    }
}
