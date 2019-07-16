package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.util.EnumSet;
import java.util.Set;

public class OutputFinderOptions
{
    public enum Flags
    {
        Debug(1),
        Release(2),
        AnyBuild(3),
        IncludeSigned(4),
        SignedOnly(8);

        private final int _flagValue;

        Flags(int value){
            _flagValue = value;
        }

        public int getStatusFlagValue(){
            return _flagValue;
        }
    }

    public static boolean containsAll(EnumSet<OutputFinderOptions.Flags> set, OutputFinderOptions.Flags... flags)
    {
        int maskValue = getStatusValue(set);
        for (OutputFinderOptions.Flags flag: flags)
        {
            if (!contains(maskValue, flag))
                return false;
        }
        return true;
    }

    public static boolean containsAny(EnumSet<OutputFinderOptions.Flags> set, OutputFinderOptions.Flags... flags)
    {
        int maskValue = getStatusValue(set);
        for (OutputFinderOptions.Flags flag: flags)
        {
            if (contains(maskValue, flag))
                return true;
        }
        return false;
    }

    public static boolean contains(EnumSet<OutputFinderOptions.Flags> set, OutputFinderOptions.Flags flag)
    {
        int maskValue = getStatusValue(set);
        return contains(maskValue, flag);
    }

    public static EnumSet<Flags> getStatusFlags(long statusValue) {
        EnumSet statusFlags = EnumSet.noneOf(Flags.class);
        for (Flags statusFlag : Flags.values())
        {
            long flagValue = statusFlag.getStatusFlagValue();
            if ( (flagValue&statusValue ) == flagValue )
                statusFlags.add(statusFlag);
        }
        return statusFlags;
    }

    public static int getStatusValue(Set<Flags> flags) {
        int value=0;
        for (Flags flag : flags)
            value |= flag.getStatusFlagValue();
        return value;
    }

    private static boolean contains(int mask, OutputFinderOptions.Flags flag)
    {
        int flagValue = flag.getStatusFlagValue();
        return (mask & flagValue) == flagValue;
    }
}
