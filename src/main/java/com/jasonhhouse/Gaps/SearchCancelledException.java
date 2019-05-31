package com.jasonhhouse.Gaps;

public class SearchCancelledException extends Exception {
    public SearchCancelledException() {
    }

    public SearchCancelledException(String message) {
        super(message);
    }

    public SearchCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchCancelledException(Throwable cause) {
        super(cause);
    }

    public SearchCancelledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
