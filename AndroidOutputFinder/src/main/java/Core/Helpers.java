package Core;

public class Helpers
{
    public static <T> T SafeCast(Object o, Class<T> type) {
        return type != null && type.isInstance(o) ? type.cast(o) : null;
    }
}
