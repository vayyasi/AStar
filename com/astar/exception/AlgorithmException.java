package com.astar.exception;

import java.io.Serializable;

public class AlgorithmException extends Exception implements Serializable {

    private static final long serialVersionUID = -77915248602899008L;

    public AlgorithmException() {
        super();
    }

    public AlgorithmException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AlgorithmException(final String message) {
        super(message);
    }

    public AlgorithmException(final Throwable cause) {
        super(cause);
    }
}
