package de.fraunhofer.iem.icognicrypt.core.Helpers;

// Copyright (c) .NET Foundation and Contributors
public class StringTrimming
{
    public static String Trim(String input, char trimChar)
    {
        return TrimHelper(input, new char[]{trimChar}, 1, TrimType.Both);
    }

    public static String TrimStart(String input)
    {
        return TrimWhiteSpaceHelper(input, TrimType.Head);
    }

    public static String TrimStart(String input, char trimChar)
    {
        return TrimHelper(input, new char[]{trimChar}, 1, TrimType.Head);
    };

    public static String TrimEnd(String input){
        return TrimWhiteSpaceHelper(input, TrimType.Tail);
    };

    public static String TrimEnd(String input, char trimChar)
    {
        return TrimHelper(input, new char[]{trimChar}, 1, TrimType.Tail);
    }

    public static String Trim(String input, char... trimChars){
        if (trimChars == null || trimChars.length == 0)
            return input.trim();
        return TrimHelper(input, trimChars, trimChars.length, TrimType.Both);
    }

    public static String TrimStart(String input, String trimChars)
    {
        if (trimChars == null || trimChars.length() == 0)
            return TrimWhiteSpaceHelper(input, TrimType.Head);
        return TrimHelper(input, trimChars.toCharArray(), trimChars.length(), TrimType.Head);
    }

    public static String TrimEnd(String input, String trimChars)
    {
        if (trimChars == null || trimChars.length() == 0)
            return TrimWhiteSpaceHelper(input, TrimType.Tail);
        return TrimHelper(input, trimChars.toCharArray(), trimChars.length(), TrimType.Tail);
    }

    private static String TrimHelper(String input, char[] trimChars, int trimCharsLength, TrimType trimType)
    {
        int end = input.length() -1;
        int start = 0;

        if (trimType != TrimType.Tail){
            for (start = 0; start < input.length(); start++){
                int i;
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

    private static String TrimWhiteSpaceHelper(String input, TrimType trimType)
    {
        int end = input.length() - 1;
        int start = 0;

        if (trimType != TrimType.Tail)
            for (start = 0; start < input.length(); start++)
                if (!Character.isWhitespace(input.charAt(start)))
                    break;

        if (trimType != TrimType.Head)
            for (end = input.length() - 1; end >= start; end--)
                if (!Character.isWhitespace(input.charAt(end)))
                    break;

        return CreateTrimmedString(input, start, end);
    }

    private static String CreateTrimmedString(String input, int start, int end)
    {
        int len = end - start + 1;
        return InternalSubString(input, start, len);
    }

    private static String InternalSubString(String input, int startIndex, int length)
    {
        if (!(startIndex >= 0 && startIndex <= input.length()))
            throw new IndexOutOfBoundsException();
        if (!(length >= 0 && startIndex <= input.length() - length))
            throw new IndexOutOfBoundsException();

        int endIndex = startIndex + length;
        if (endIndex < startIndex || endIndex > input.length())
            throw new IndexOutOfBoundsException();

        return input.substring(startIndex, endIndex);
    }


    private static boolean IsInRange(char c, char min, char max){
        return (c - min) <= (max - min);
    }

    private static boolean IsWhiteSpaceLatin1(char c)
    {
        return c == ' ' || (c - Integer.parseInt("0009", 16)) <= (Integer.parseInt("000d", 16) - Integer.parseInt("0009", 16)) || // (c >= '\x0009' && c <= '\x000d')
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
