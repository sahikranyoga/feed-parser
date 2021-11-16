package com.sahikran.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class RSSItem implements Comparable<RSSItem> {
    private final String title;
    private final String description;
    private final String link;
    private final List<String> creators;
    private final LocalDate publishedDate;
    private final String guid;

    private RSSItem(String title, String description, String link, 
        List<String> creators, LocalDate publishedDate, String guid){
            this.title = title;
            this.description = description;
            this.link = link;
            this.creators = creators;
            this.publishedDate = publishedDate;
            this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public List<String> getCreators() {
        return creators;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creators == null) ? 0 : creators.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((guid == null) ? 0 : guid.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((publishedDate == null) ? 0 : publishedDate.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RSSItem other = (RSSItem) obj;
        if (creators == null) {
            if (other.creators != null)
                return false;
        } else if (!creators.equals(other.creators))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (guid == null) {
            if (other.guid != null)
                return false;
        } else if (!guid.equals(other.guid))
            return false;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        if (publishedDate == null) {
            if (other.publishedDate != null)
                return false;
        } else if (!publishedDate.equals(other.publishedDate))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public static final class Builder {
        private String title;
        private String description;
        private String link;
        private List<String> creators = new ArrayList<>();
        private LocalDate publishedDate;
        private String guid;

        public Builder(){

        }

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Builder setDescription(String description){
            this.description = description;
            return this;
        }

        public Builder setLink(String link){
            this.link = link;
            return this;
        }

        public Builder addCreators(String creator){
            creators.add(creator);
            return this;
        }

        public Builder addPublishedDate(String dateFieldValue){
            if(dateFieldValue.isEmpty()){
                return this;
            }
            Objects.requireNonNull(dateFieldValue, "input date field value can not be null");
            DateTimeFormatter optionaDateTimeFormatter = DateTimeFormatter.ofPattern("[EEE,dd MMM yyyy][dd MMM yyyy][EEE, dd MMM yyyy HH:mm:ss Z]");
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                                        .parseCaseInsensitive()
                                        .appendOptional(optionaDateTimeFormatter)
                                        .toFormatter(Locale.getDefault());
            this.publishedDate = LocalDate.parse(dateFieldValue, dateTimeFormatter);
            return this;
        }

        public Builder setGuid(String guid){
            this.guid = guid;
            return this;
        }

        public Builder setRssDataItem(Item item){
            setTitle(item.getTitle());
            setDescription(item.getDescription());
            setGuid(item.getGuid());
            setLink(item.getLink());
            addPublishedDate(item.getPubDate());
            creators.addAll(item.getCreator());
            return this;
        }

        public RSSItem build(){
            Objects.requireNonNull(title, "title can not be empty");
            return new RSSItem(title, description, link, creators, publishedDate, guid);
        }
    }

    @Override
    public int compareTo(RSSItem o) {
        return publishedDate.compareTo(o.getPublishedDate());
    }
}
