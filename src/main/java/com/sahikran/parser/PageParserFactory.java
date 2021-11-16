package com.sahikran.parser;

import com.sahikran.parser.PageParserFactoryImpl.ParserType;

public interface PageParserFactory {
    PageParser get(String url, ParserType parserType);
}
