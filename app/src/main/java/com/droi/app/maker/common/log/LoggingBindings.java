package com.droi.app.maker.common.log;


import android.app.Activity;

import com.droi.app.maker.common.utils.proto.InteractionEvent;
import com.droi.app.maker.common.utils.proto.ScreenEvent;

/**
 * Allows the container application to gather analytics.
 */
public interface LoggingBindings {

    /**
     * Logs an interaction that occurred.
     *
     * @param interaction an integer representing what interaction occurred.
     * @see InteractionEvent
     */
    void logInteraction(InteractionEvent.Type interaction);

    /**
     * Logs an event indicating that a screen was displayed.
     *
     * @param screenEvent an integer representing the displayed screen.
     * @param activity    Parent activity of the displayed screen.
     * @see ScreenEvent
     */
    void logScreenView(ScreenEvent.Type screenEvent, Activity activity);

    /**
     * Logs a hit event to the analytics server.
     */
    void sendHitEventAnalytics(String category, String action, String label, long value);
}
