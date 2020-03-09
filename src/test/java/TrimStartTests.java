// Copyright (c) .NET Foundation and Contributors

import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;
import org.junit.Assert;
import org.junit.Test;

public class TrimStartTests
{
    @Test
    public void Test1()
    {
        TrimStart("  Hello  ", new char[] { ' ' }, "Hello  ");
    }

    @Test
    public void Test2()
    {
        TrimStart(".  Hello  ..", new char[] { '.' }, "  Hello  ..");
    }

    @Test
    public void Test3()
    {
        TrimStart(".  Hello  ..", new char[] { '.', ' ' }, "Hello  ..");
    }

    @Test
    public void Test4()
    {
        TrimStart("123abcHello123abc", new char[] { '1', '2', '3', 'a', 'b', 'c' }, "Hello123abc");
    }

    @Test
    public void Test5()
    {
        TrimStart("  Hello  ", null, "Hello  ");
    }

    @Test
    public void Test7()
    {
        TrimStart("  Hello  ", new char[0], "Hello  ");
    }

    @Test
    public void Test8()
    {
        TrimStart("      \t      ", null, "");
    }

    @Test
    public void Test9()
    {
        TrimStart("", null, "");
    }

    @Test
    public void Test10()
    {
        TrimStart("      ", new char[] { ' ' }, "");
    }

    @Test
    public void Test11()
    {
        TrimStart("aaaaa", new char[] { 'a' }, "");
    }

    @Test
    public void Test12()
    {
        TrimStart("abaabaa", new char[] { 'b', 'a' }, "");
    }

    public static void TrimStart(String s, char[] trimChars, String expected)
    {
        if (trimChars == null || trimChars.length == 0 || (trimChars.length == 1 && trimChars[0] == ' '))
        {
            Assert.assertEquals(expected, StringTrimming.TrimStart(s));
        }

        if (trimChars != null && trimChars.length == 1)
        {
            Assert.assertEquals(expected, StringTrimming.TrimStart(s, trimChars[0]));
        }

        if (trimChars == null)
            Assert.assertEquals(expected, StringTrimming.TrimStart(s, null));
        else
            Assert.assertEquals(expected, StringTrimming.TrimStart(s, new String(trimChars)));
    }
}
