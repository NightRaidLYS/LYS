package com.lys.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinyinUtils {

    private static final HanyuPinyinOutputFormat FORMAT;

    static {
        FORMAT = new HanyuPinyinOutputFormat();
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 将中文转换为不带声调的拼音
     *
     * @param chinese 中文字符串
     * @return 不带声调的拼音字符串
     */
    public static String convertToPinyinWithoutTone(String chinese) {
        StringBuilder pinyinBuilder = new StringBuilder();

        char[] chars = chinese.toCharArray();
        for (char c : chars) {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                // 如果是中文字符，则转换为不带声调的拼音
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, FORMAT);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        // 取第一个拼音，你也可以根据需求进行处理
                        pinyinBuilder.append(pinyinArray[0]);
                    }
                } catch (Exception e) {
                    // 异常处理
                    e.printStackTrace();
                }
            } else {
                // 如果不是中文字符，直接追加
                pinyinBuilder.append(c);
            }
        }

        return pinyinBuilder.toString();
    }
}
