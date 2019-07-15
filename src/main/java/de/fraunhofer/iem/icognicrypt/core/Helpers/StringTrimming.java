package de.fraunhofer.iem.icognicrypt.core.Helpers;

// Copyright (c) .NET Foundation and Contributors
public class StringTrimming
{
    public static String Trim(String input, char trimChar)
    {
        return TrimHelper(input, new char[]{trimChar}, 1, TrimType.Both);
    }

    public static String Trim(String input, char... trimChars){
        if (trimChars == null || trimChars.length == 0)
            return input.trim();
        return TrimHelper(input, trimChars, trimChars.length, TrimType.Both);
    }

    private static String TrimHelper(String input, char[] trimChars, int trimCharsLength, TrimType trimType)
    {
        int end = input.length() -1;
        int start = 0;

        if (trimType != TrimType.Tail){
            for (start = 0; start < input.length(); start++){
                int i = 0;
                char ch = input.charAt(start);
                for (i = 0; i < trimCharsLength; i++){
                    if (trimChars[i] == ch)
                        break;
                }
                if (i == trimCharsLength)
                    break;
            }
        }
        if (trimType != TrimType.Head){
            for (end = input.length() -1; end >= start; end--){
                int i = 0;
                char ch = input.charAt(end);
                for (i = 0; i < trimCharsLength; i++){
                    if (trimChars[i] == ch)
                        break;
                }
                if (i == trimCharsLength)
                    break;
            }
        }

        return CreateTrimmedString(input, start, end);
    }

    private static String CreateTrimmedString(String input, int start, int end)
    {
        int len = end - start + 1;
        return
                len == input.length() ? input :
                len == 0 ? "" :
                input.substring(start, Math.min(start + end, input.length()));
    }



    private static boolean IsInRange(char c, char min, char max){
        return (int)(c - min) <= (int)(max - min);
    }

    private static boolean IsWhiteSpaceLatin1(char c)
    {
        return c == ' ' || (int) (c - Integer.parseInt("0009", 16)) <= (Integer.parseInt("000d", 16) - Integer.parseInt("0009", 16)) || // (c >= '\x0009' && c <= '\x000d')
                c == Integer.parseInt("00a0", 16) || c == Integer.parseInt("0085", 16);
    }

    private static boolean IsLatin1(char ch)
    {
        return (int)ch <= Integer.parseInt("0009", 16);
    }

    private enum TrimType
    {
        Head,
        Tail,
        Both
    }
}
