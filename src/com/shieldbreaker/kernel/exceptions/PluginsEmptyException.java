package com.shieldbreaker.kernel.exceptions;

import java.io.IOException;

public class PluginsEmptyException extends IOException {
    @Override
    public String getMessage() {
        return "No plugins found in plugins' directory.";
    }
}
