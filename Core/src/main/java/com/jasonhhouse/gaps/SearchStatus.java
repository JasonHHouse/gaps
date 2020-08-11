package com.jasonhhouse.gaps;

public final class SearchStatus {
    private final Boolean isSearching;

    public SearchStatus(Boolean isSearching) {
        this.isSearching = isSearching;
    }

    public Boolean getIsSearching() {
        return isSearching;
    }
}
