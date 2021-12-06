# NewsFeed Parser

A parser that parses websites, search engines and RSS feeds to generate feeds
This service reads the messages (url, url type) from SQS, parses the web URLs (HTML, RSS FEED XMLs) for news feed items in each message
  * RSS feed XMLs are read by an inbuilt RSS parser and
  * Web scrapping of known html files that has table of items to be consumed readily are read by IndentifiedPageParser implementation

The output of these parsers is stored in a database for further processing.

      Messages(input)   --> Parser    --> Items created   --> staged in a database
