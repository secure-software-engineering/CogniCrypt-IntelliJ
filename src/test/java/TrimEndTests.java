// Copyright (c) .NET Foundation and Contributors

import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;
import org.junit.Assert;
import org.junit.Test;


public class TrimEndTests
{
    @Test
    public void Test1()
    {
        TrimEnd("  Hello  ", new char[] { ' ' }, "  Hello");
    }

    @Test
    public void Test2()
    {
        TrimEnd(".  Hello  ..", new char[] { '.' }, ".  Hello  ");
    }

    @Test
    public void Test3()
    {
        TrimEnd(".  Hello  ..", new char[] { '.', ' ' }, ".  Hello");
    }

    @Test
    public void Test4()
    {
        TrimEnd("123abcHello123abc", new char[] { '1', '2', '3', 'a', 'b', 'c' }, "123abcHello");
    }

    @Test
    public void Test5()
    {
        TrimEnd("  Hello  ", null, "  Hello");
    }

    @Test
    public void Test7()
    {
        TrimEnd("  Hello  ", new char[0], "  Hello");
    }

    @Test
    public void Test8()
    {
        TrimEnd("      \t      ", null, "");
    }

    @Test
    public void Test9()
    {
        TrimEnd("", null, "");
    }

    @Test
    public void Test10()
    {
        TrimEnd("      ", new char[] { ' ' }, "");
    }

    @Test
    public void Test11()
    {
        TrimEnd("aaaaa", new char[] { 'a' }, "");
    }

    @Test
    public void Test12()
    {
        TrimEnd("abaabaa", new char[] { 'b', 'a' }, "");
    }


    public static void TrimEnd(String s, char[] trimChars, String expected)
    {
        if (trimChars == null || trimChars.length == 0 || (trimChars.length == 1 && trimChars[0] == ' '))
            Assert.assertEquals(expected, StringTrimming.TrimEnd(s));

        if (trimChars != null && trimChars.length == 1)
            Assert.assertEquals(expected, StringTrimming.TrimEnd(s, trimChars[0]));

        if (trimChars == null)
            Assert.assertEquals(expected, StringTrimming.TrimEnd(s, null));
        else
            Assert.assertEquals(expected, StringTrimming.TrimEnd(s, new String(trimChars)));
    }
}
