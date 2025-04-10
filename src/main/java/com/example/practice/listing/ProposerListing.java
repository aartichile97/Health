package com.example.practice.listing;

import java.util.List;

public class ProposerListing {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortOrder;
    private List<SearchFilter> searchFilters;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(List<SearchFilter> searchFilters) {
        this.searchFilters = searchFilters;
    }
}
