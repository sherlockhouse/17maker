package com.droi.app.maker.common.log;

import java.util.Objects;

import android.content.Context;

/**
 * Single entry point for all logging/analytics-related work for all user interactions.
 */
public class Logger {
    private static LoggingBindings loggingBindings;

    public static LoggingBindings get(Context context) {
        Objects.requireNonNull(context);
        if (loggingBindings != null) {
            return loggingBindings;
        }

        loggingBindings = new LoggingBindingsStub();

        return loggingBindings;
    }

    public static void setForTesting(LoggingBindings loggingBindings) {
        Logger.loggingBindings = loggingBindings;
    }

    private Logger() {
    }
}
