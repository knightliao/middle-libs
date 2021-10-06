package com.github.knightliao.middle.utils.test.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.knightliao.middle.utils.lang.StringUtil;

/**
 * @author liaoqiqi
 * @version 2014-8-28
 */
public class StringUtilTestCase {

    // ==========================================================================
    // 默认值函数。
    //
    // 当字符串为empty或blank时，将字符串转换成指定的默认字符串。
    // 判断字符串为null时，可用更通用的ObjectUtil.defaultIfNull。
    // ==========================================================================

    @Test
    public void defaultIfEmpty() {
        assertEquals("default", StringUtil.defaultIfEmpty(null, "default"));
        assertEquals("default", StringUtil.defaultIfEmpty("", "default"));
        assertEquals("  ", StringUtil.defaultIfEmpty("  ", "default"));
        assertEquals("bat", StringUtil.defaultIfEmpty("bat", "default"));
    }

    @Test
    public void defaultIfBlank() {
        assertEquals("default", StringUtil.defaultIfBlank(null, "default"));
        assertEquals("default", StringUtil.defaultIfBlank("", "default"));
        assertEquals("default", StringUtil.defaultIfBlank("  ", "default"));
        assertEquals("bat", StringUtil.defaultIfBlank("bat", "default"));
    }

    @Test
    public void toCamelCase() {
        assertNull(StringUtil.toCamelCase(null));
        assertEquals("", StringUtil.toCamelCase(""));

        assertEquals("a", StringUtil.toCamelCase("A"));
        assertEquals("a", StringUtil.toCamelCase("a"));

        assertEquals("ab", StringUtil.toCamelCase("AB"));
        assertEquals("ab", StringUtil.toCamelCase("Ab"));
        assertEquals("aB", StringUtil.toCamelCase("aB"));
        assertEquals("ab", StringUtil.toCamelCase("ab"));

        assertEquals("aB", StringUtil.toCamelCase("A_B"));
        assertEquals("aB", StringUtil.toCamelCase("A_b"));
        assertEquals("aB", StringUtil.toCamelCase("a_B"));
        assertEquals("aB", StringUtil.toCamelCase("a_b"));

        assertEquals("aBc", StringUtil.toCamelCase("aBc"));
        assertEquals("aBcDef", StringUtil.toCamelCase("  aBc def "));
        assertEquals("aBcDef", StringUtil.toCamelCase("aBcDef"));
        assertEquals("aBcDefGhi", StringUtil.toCamelCase("aBc def_ghi"));
        assertEquals("aBcDefGhi", StringUtil.toCamelCase("aBcDefGhi"));
        assertEquals("aBcDefGhi123", StringUtil.toCamelCase("aBc def_ghi 123"));
        assertEquals("aBcDefGhi123", StringUtil.toCamelCase("aBcDefGhi123"));
        assertEquals("aBcDefGhi123", StringUtil.toCamelCase("aBcDEFGhi123"));

        assertEquals("123ABcDefGhi", StringUtil.toCamelCase("123aBcDEFGhi")); // 数字开始

        // 不保留下划线
        assertEquals("ab", StringUtil.toCamelCase("__AB__"));
        assertEquals("ab", StringUtil.toCamelCase("__Ab__"));
        assertEquals("aB", StringUtil.toCamelCase("__aB__"));
        assertEquals("ab", StringUtil.toCamelCase("__ab__"));

        assertEquals("aB", StringUtil.toCamelCase("__A__B__"));
        assertEquals("aB", StringUtil.toCamelCase("__A__b__"));
        assertEquals("aB", StringUtil.toCamelCase("__a__B__"));
        assertEquals("aB", StringUtil.toCamelCase("__a__b__"));

        // 保留除下划线以外的其它分隔符
        assertEquals("..ab..", StringUtil.toCamelCase("..AB.."));
        assertEquals("..ab..", StringUtil.toCamelCase("..Ab.."));
        assertEquals("..aB..", StringUtil.toCamelCase("..aB.."));
        assertEquals("..ab..", StringUtil.toCamelCase("..ab.."));

        assertEquals("..a..b..", StringUtil.toCamelCase("..A..B.."));
        assertEquals("..a..b..", StringUtil.toCamelCase("..A..b.."));
        assertEquals("..a..b..", StringUtil.toCamelCase("..a..B.."));
        assertEquals("..a..b..", StringUtil.toCamelCase("..a..b.."));

        assertEquals("..a..123B..", StringUtil.toCamelCase("..A..123B.."));
        assertEquals("..a..123B..", StringUtil.toCamelCase("..A..123b.."));
        assertEquals("..a..123B..", StringUtil.toCamelCase("..a..123B.."));
        assertEquals("..a..123B..", StringUtil.toCamelCase("..a..123b.."));

        assertEquals("fmh.m.0.n", StringUtil.toCamelCase("_fmh.m._0.n"));
        assertEquals("aaa-bbb-ccc", StringUtil.toCamelCase("aaa-bbb-ccc"));
    }

    @Test
    public void toPascalCase() {
        assertNull(StringUtil.toPascalCase(null));
        assertEquals("", StringUtil.toPascalCase(""));

        assertEquals("A", StringUtil.toPascalCase("A"));
        assertEquals("A", StringUtil.toPascalCase("a"));

        assertEquals("Ab", StringUtil.toPascalCase("AB"));
        assertEquals("Ab", StringUtil.toPascalCase("Ab"));
        assertEquals("AB", StringUtil.toPascalCase("aB"));
        assertEquals("Ab", StringUtil.toPascalCase("ab"));

        assertEquals("AB", StringUtil.toPascalCase("A_B"));
        assertEquals("AB", StringUtil.toPascalCase("A_b"));
        assertEquals("AB", StringUtil.toPascalCase("a_B"));
        assertEquals("AB", StringUtil.toPascalCase("a_b"));

        assertEquals("ABc", StringUtil.toPascalCase("aBc"));
        assertEquals("ABcDef", StringUtil.toPascalCase("  aBc def "));
        assertEquals("ABcDef", StringUtil.toPascalCase("aBcDef"));
        assertEquals("ABcDefGhi", StringUtil.toPascalCase("aBc def_ghi"));
        assertEquals("ABcDefGhi", StringUtil.toPascalCase("aBcDefGhi"));
        assertEquals("ABcDefGhi123", StringUtil.toPascalCase("aBc def_ghi 123"));
        assertEquals("ABcDefGhi123", StringUtil.toPascalCase("aBcDefGhi123"));
        assertEquals("ABcDefGhi123", StringUtil.toPascalCase("aBcDEFGhi123"));

        assertEquals("123ABcDefGhi", StringUtil.toPascalCase("123aBcDEFGhi")); // 数字开始

        // 不保留下划线
        assertEquals("Ab", StringUtil.toPascalCase("__AB__"));
        assertEquals("Ab", StringUtil.toPascalCase("__Ab__"));
        assertEquals("AB", StringUtil.toPascalCase("__aB__"));
        assertEquals("Ab", StringUtil.toPascalCase("__ab__"));

        assertEquals("AB", StringUtil.toPascalCase("__A__B__"));
        assertEquals("AB", StringUtil.toPascalCase("__A__b__"));
        assertEquals("AB", StringUtil.toPascalCase("__a__B__"));
        assertEquals("AB", StringUtil.toPascalCase("__a__b__"));

        // 保留除下划线以外的其它分隔符
        assertEquals("..Ab..", StringUtil.toPascalCase("..AB.."));
        assertEquals("..Ab..", StringUtil.toPascalCase("..Ab.."));
        assertEquals("..AB..", StringUtil.toPascalCase("..aB.."));
        assertEquals("..Ab..", StringUtil.toPascalCase("..ab.."));

        assertEquals("..A..B..", StringUtil.toPascalCase("..A..B.."));
        assertEquals("..A..B..", StringUtil.toPascalCase("..A..b.."));
        assertEquals("..A..B..", StringUtil.toPascalCase("..a..B.."));
        assertEquals("..A..B..", StringUtil.toPascalCase("..a..b.."));

        assertEquals("..A..123B..", StringUtil.toPascalCase("..A..123B.."));
        assertEquals("..A..123B..", StringUtil.toPascalCase("..A..123b.."));
        assertEquals("..A..123B..", StringUtil.toPascalCase("..a..123B.."));
        assertEquals("..A..123B..", StringUtil.toPascalCase("..a..123b.."));

        assertEquals("Fmh.M.0.N", StringUtil.toPascalCase("_fmh.m._0.n"));
        assertEquals("Aaa-Bbb-Ccc", StringUtil.toPascalCase("aaa-bbb-ccc"));
    }

    @Test
    public void toUpperCaseWithUnderscores() {
        assertNull(StringUtil.toUpperCaseWithUnderscores(null));
        assertEquals("", StringUtil.toUpperCaseWithUnderscores(""));

        assertEquals("A", StringUtil.toUpperCaseWithUnderscores("A"));
        assertEquals("A", StringUtil.toUpperCaseWithUnderscores("a"));

        assertEquals("AB", StringUtil.toUpperCaseWithUnderscores("AB"));
        assertEquals("AB", StringUtil.toUpperCaseWithUnderscores("Ab"));
        assertEquals("A_B", StringUtil.toUpperCaseWithUnderscores("aB"));
        assertEquals("AB", StringUtil.toUpperCaseWithUnderscores("ab"));

        assertEquals("A_B", StringUtil.toUpperCaseWithUnderscores("A_B"));
        assertEquals("A_B", StringUtil.toUpperCaseWithUnderscores("A_b"));
        assertEquals("A_B", StringUtil.toUpperCaseWithUnderscores("a_B"));
        assertEquals("A_B", StringUtil.toUpperCaseWithUnderscores("a_b"));

        assertEquals("A_BC", StringUtil.toUpperCaseWithUnderscores("aBc"));
        assertEquals("A_BC_DEF",
                StringUtil.toUpperCaseWithUnderscores("  aBc def "));
        assertEquals("A_BC_DEF",
                StringUtil.toUpperCaseWithUnderscores("aBcDef"));
        assertEquals("A_BC_DEF_GHI",
                StringUtil.toUpperCaseWithUnderscores("aBc def_ghi"));
        assertEquals("A_BC_DEF_GHI",
                StringUtil.toUpperCaseWithUnderscores("aBcDefGhi"));
        assertEquals("A_BC_DEF_GHI_123",
                StringUtil.toUpperCaseWithUnderscores("aBc def_ghi 123"));
        assertEquals("A_BC_DEF_GHI_123",
                StringUtil.toUpperCaseWithUnderscores("aBcDefGhi123"));
        assertEquals("A_BC_DEF_GHI_123",
                StringUtil.toUpperCaseWithUnderscores("aBcDEFGhi123"));

        assertEquals("123_A_BC_DEF_GHI",
                StringUtil.toUpperCaseWithUnderscores("123aBcDEFGhi")); // 数字开始

        // 保留下划线
        assertEquals("__AB__", StringUtil.toUpperCaseWithUnderscores("__AB__"));
        assertEquals("__AB__", StringUtil.toUpperCaseWithUnderscores("__Ab__"));
        assertEquals("__A_B__", StringUtil.toUpperCaseWithUnderscores("__aB__"));
        assertEquals("__AB__", StringUtil.toUpperCaseWithUnderscores("__ab__"));

        assertEquals("__A__B__",
                StringUtil.toUpperCaseWithUnderscores("__A__B__"));
        assertEquals("__A__B__",
                StringUtil.toUpperCaseWithUnderscores("__A__b__"));
        assertEquals("__A__B__",
                StringUtil.toUpperCaseWithUnderscores("__a__B__"));
        assertEquals("__A__B__",
                StringUtil.toUpperCaseWithUnderscores("__a__b__"));

        // 保留所有的分隔符
        assertEquals("..AB..", StringUtil.toUpperCaseWithUnderscores("..AB.."));
        assertEquals("..AB..", StringUtil.toUpperCaseWithUnderscores("..Ab.."));
        assertEquals("..A_B..", StringUtil.toUpperCaseWithUnderscores("..aB.."));
        assertEquals("..AB..", StringUtil.toUpperCaseWithUnderscores("..ab.."));

        assertEquals("..A..B..",
                StringUtil.toUpperCaseWithUnderscores("..A..B.."));
        assertEquals("..A..B..",
                StringUtil.toUpperCaseWithUnderscores("..A..b.."));
        assertEquals("..A..B..",
                StringUtil.toUpperCaseWithUnderscores("..a..B.."));
        assertEquals("..A..B..",
                StringUtil.toUpperCaseWithUnderscores("..a..b.."));

        assertEquals("..A..123_B..",
                StringUtil.toUpperCaseWithUnderscores("..A..123B.."));
        assertEquals("..A..123_B..",
                StringUtil.toUpperCaseWithUnderscores("..A..123b.."));
        assertEquals("..A..123_B..",
                StringUtil.toUpperCaseWithUnderscores("..a..123B.."));
        assertEquals("..A..123_B..",
                StringUtil.toUpperCaseWithUnderscores("..a..123b.."));

        assertEquals("_FMH.M._0.N",
                StringUtil.toUpperCaseWithUnderscores("_fmh.m._0.n"));
        assertEquals("AAA-BBB-CCC",
                StringUtil.toUpperCaseWithUnderscores("aaa-bbb-ccc"));
    }

    @Test
    public void toLowerCaseWithUnderscores() {
        assertNull(StringUtil.toLowerCaseWithUnderscores(null));
        assertEquals("", StringUtil.toLowerCaseWithUnderscores(""));

        assertEquals("a", StringUtil.toLowerCaseWithUnderscores("A"));
        assertEquals("a", StringUtil.toLowerCaseWithUnderscores("a"));

        assertEquals("ab", StringUtil.toLowerCaseWithUnderscores("AB"));
        assertEquals("ab", StringUtil.toLowerCaseWithUnderscores("Ab"));
        assertEquals("a_b", StringUtil.toLowerCaseWithUnderscores("aB"));
        assertEquals("ab", StringUtil.toLowerCaseWithUnderscores("ab"));

        assertEquals("a_b", StringUtil.toLowerCaseWithUnderscores("A_B"));
        assertEquals("a_b", StringUtil.toLowerCaseWithUnderscores("A_b"));
        assertEquals("a_b", StringUtil.toLowerCaseWithUnderscores("a_B"));
        assertEquals("a_b", StringUtil.toLowerCaseWithUnderscores("a_b"));

        assertEquals("a_bc", StringUtil.toLowerCaseWithUnderscores("aBc"));
        assertEquals("a_bc_def",
                StringUtil.toLowerCaseWithUnderscores("  aBc def "));
        assertEquals("a_bc_def",
                StringUtil.toLowerCaseWithUnderscores("aBcDef"));
        assertEquals("a_bc_def_ghi",
                StringUtil.toLowerCaseWithUnderscores("aBc def_ghi"));
        assertEquals("a_bc_def_ghi",
                StringUtil.toLowerCaseWithUnderscores("aBcDefGhi"));
        assertEquals("a_bc_def_ghi_123",
                StringUtil.toLowerCaseWithUnderscores("aBc def_ghi 123"));
        assertEquals("a_bc_def_ghi_123",
                StringUtil.toLowerCaseWithUnderscores("aBcDefGhi123"));
        assertEquals("a_bc_def_ghi_123",
                StringUtil.toLowerCaseWithUnderscores("aBcDEFGhi123"));

        assertEquals("123_a_bc_def_ghi",
                StringUtil.toLowerCaseWithUnderscores("123aBcDEFGhi")); // 数字开始

        // 保留下划线
        assertEquals("__ab__", StringUtil.toLowerCaseWithUnderscores("__AB__"));
        assertEquals("__ab__", StringUtil.toLowerCaseWithUnderscores("__Ab__"));
        assertEquals("__a_b__", StringUtil.toLowerCaseWithUnderscores("__aB__"));
        assertEquals("__ab__", StringUtil.toLowerCaseWithUnderscores("__ab__"));

        assertEquals("__a__b__",
                StringUtil.toLowerCaseWithUnderscores("__A__B__"));
        assertEquals("__a__b__",
                StringUtil.toLowerCaseWithUnderscores("__A__b__"));
        assertEquals("__a__b__",
                StringUtil.toLowerCaseWithUnderscores("__a__B__"));
        assertEquals("__a__b__",
                StringUtil.toLowerCaseWithUnderscores("__a__b__"));

        // 保留所有的分隔符
        assertEquals("..ab..", StringUtil.toLowerCaseWithUnderscores("..AB.."));
        assertEquals("..ab..", StringUtil.toLowerCaseWithUnderscores("..Ab.."));
        assertEquals("..a_b..", StringUtil.toLowerCaseWithUnderscores("..aB.."));
        assertEquals("..ab..", StringUtil.toLowerCaseWithUnderscores("..ab.."));

        assertEquals("..a..b..",
                StringUtil.toLowerCaseWithUnderscores("..A..B.."));
        assertEquals("..a..b..",
                StringUtil.toLowerCaseWithUnderscores("..A..b.."));
        assertEquals("..a..b..",
                StringUtil.toLowerCaseWithUnderscores("..a..B.."));
        assertEquals("..a..b..",
                StringUtil.toLowerCaseWithUnderscores("..a..b.."));

        assertEquals("..a..123_b..",
                StringUtil.toLowerCaseWithUnderscores("..A..123B.."));
        assertEquals("..a..123_b..",
                StringUtil.toLowerCaseWithUnderscores("..A..123b.."));
        assertEquals("..a..123_b..",
                StringUtil.toLowerCaseWithUnderscores("..a..123B.."));
        assertEquals("..a..123_b..",
                StringUtil.toLowerCaseWithUnderscores("..a..123b.."));

        assertEquals("_fmh.m._0.n",
                StringUtil.toLowerCaseWithUnderscores("_fmh.m._0.n"));
        assertEquals("aaa-bbb-ccc",
                StringUtil.toLowerCaseWithUnderscores("aaa-bbb-ccc"));
    }

    // ==========================================================================
    // 将数字或字节转换成ASCII字符串的函数。
    // ==========================================================================

    @Test
    public void longToString() {
        longToString("", 0);
        longToString("1", 62);
        longToString("2", 62 + 62);
    }

    private void longToString(String prefix, long base) {
        char ch = '0';

        for (long i = base; i < base + 10; i++, ch++) {
            assertEquals(ch + prefix, StringUtil.longToString(i));
            assertEquals(ch + prefix, StringUtil.longToString(-i));
        }

        ch = 'A';

        for (long i = base + 10; i < base + 36; i++, ch++) {
            assertEquals(ch + prefix, StringUtil.longToString(i));
            assertEquals(ch + prefix, StringUtil.longToString(-i));
        }

        ch = 'a';

        for (long i = base + 36; i < base + 62; i++, ch++) {
            assertEquals(ch + prefix, StringUtil.longToString(i));
            assertEquals(ch + prefix, StringUtil.longToString(-i));
        }

        long l = 0 //
                + 62L //
                + 10 * 256L //
                + 2 * 256 * 256L //
                + 1 * 256 * 256 * 256L //
                + 0 * 256 * 256 * 256 * 256L;

        assertEquals("cIx81", StringUtil.longToString(l));
        assertEquals("IJG2A", StringUtil.longToString(l, true));
    }

    @Test
    public void bytesToString() {
        assertEquals("0", StringUtil.bytesToString(null));
        assertEquals("0", StringUtil.bytesToString(new byte[] {}));
        assertEquals("0",
                StringUtil.bytesToString(new byte[] {0, 0, 0, 0, 0, 0}));
        assertEquals("1",
                StringUtil.bytesToString(new byte[] {0, 0, 0, 0, 0, 1}));
        assertEquals("GWO823H",
                StringUtil.bytesToString(new byte[] {1, 0, 0, 0, 0, 0}));
        assertEquals("cIx81",
                StringUtil.bytesToString(new byte[] {0, 1, 2, 10, 62}));
        assertEquals(
                "cIx8QaO8KjH",
                StringUtil.bytesToString(new byte[] {0, 1, 2, 10, 62, 0, 1, 2,
                        10, 62}));

        assertEquals(
                "IJG2Y0YVRQ5V2",
                StringUtil.bytesToString(new byte[] {0, 1, 2, 10, 62, 0, 1, 2,
                        10, 62}, true));
    }

    @Test
    public void endsWithChar() {
        assertFalse(StringUtil.endsWithChar(null, ' '));
        assertFalse(StringUtil.endsWithChar("xxxxx", ' '));
        assertTrue(StringUtil.endsWithChar("xxx     ", ' '));

        assertFalse(StringUtil.endsWithChar("xxxxb", 'x'));
        assertTrue(StringUtil.endsWithChar("xxxxx", 'x'));
    }

    @Test
    public void startsWithChar() {
        assertFalse(StringUtil.startsWithChar(null, ' '));
        assertFalse(StringUtil.startsWithChar("xxxxx", ' '));
        assertTrue(StringUtil.startsWithChar(" xxx", ' '));

        assertTrue(StringUtil.startsWithChar("xxxxb", 'x'));
        assertFalse(StringUtil.startsWithChar("xxxxx", 'b'));
    }

}
