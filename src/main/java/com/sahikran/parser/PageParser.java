package com.sahikran.parser;

import com.sahikran.exception.PageParserException;

public interface PageParser {
    Result parse() throws PageParserException;
}
