// Copyright (c) .NET Foundation and Contributors

import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;
import org.junit.Assert;
import org.junit.Test;

public class TrimTests
{
    @Test
    public void Test1()
    {
        Trim("  Hello  ", new char[] { ' ' }, "Hello");
    }

    @Test
    public void Test2()
    {
        Trim(".  Hello  ..", new char[] { '.' }, "  Hello  ");
    }

    @Test
    public void Test3()
    {
        Trim(".  Hello  ..", new char[] { '.', ' ' }, "Hello");
    }

    @Test
    public void Test4()
    {
        Trim("123abcHello123abc", new char[] { '1', '2', '3', 'a', 'b', 'c' }, "Hello");
    }

    @Test
    public void Test5()
    {
        Trim("  Hello  ", null, "Hello");
    }

    @Test
    public void Test7()
    {
        Trim("  Hello  ", new char[0], "Hello");
    }

    @Test
    public void Test8()
    {
        Trim("      \t      ", null, "");
    }

    @Test
    public void Test9()
    {
        Trim("", null, "");
    }

    @Test
    public void Test10()
    {
        Trim("      ", new char[] { ' ' }, "");
    }

    @Test
    public void Test11()
    {
        Trim("aaaaa", new char[] { 'a' }, "");
    }

    @Test
    public void Test12()
    {
        Trim("abaabaa", new char[] { 'b', 'a' }, "");
    }

    public static void Trim(String s, char[] trimChars, String expected)
    {
        if (trimChars == null || trimChars.length == 0 || (trimChars.length == 1 && trimChars[0] == ' '))
        {
            Assert.assertEquals(expected, StringTrimming.Trim(s));
        }

        if (trimChars != null && trimChars.length == 1)
        {
            Assert.assertEquals(expected, StringTrimming.Trim(s, trimChars[0]));
        }

        if (trimChars == null)
            Assert.assertEquals(expected, StringTrimming.Trim(s, null));
        else
            Assert.assertEquals(expected, StringTrimming.Trim(s, trimChars));
    }
}
