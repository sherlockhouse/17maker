package com.droi.app.maker.common.log;


import android.app.Activity;

import com.droi.app.maker.common.utils.proto.InteractionEvent;
import com.droi.app.maker.common.utils.proto.ScreenEvent;

/**
 * Default implementation for logging bindings.
 */
public class LoggingBindingsStub implements LoggingBindings {

    @Override
    public void logInteraction(InteractionEvent.Type interaction) {
    }

    @Override
    public void logScreenView(ScreenEvent.Type screenEvent, Activity activity) {
    }

    @Override
    public void sendHitEventAnalytics(String category, String action, String label, long value) {
    }
}
