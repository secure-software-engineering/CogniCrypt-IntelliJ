package de.fraunhofer.iem.icognicrypt.ui.ToolWindow;

import java.util.HashMap;
import java.util.Map;

public enum ToolWindowModelType{
    Results(0);

    private int _value;
    private static Map _map = new HashMap<>();

    ToolWindowModelType(int value) {
        _value = value;
    }

    static {
        for (ToolWindowModelType models : ToolWindowModelType.values()) {
            _map.put(models._value, models);
        }
    }

    public static ToolWindowModelType valueOf(int pageType) {
        return (ToolWindowModelType) _map.get(pageType);
    }

    public int getValue() {
        return _value;
    }
}
