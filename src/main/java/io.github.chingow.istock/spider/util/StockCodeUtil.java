package io.github.chingow.istock.spider.util;

/**
 * 股票基金代码工具类
 *
 * @author chingow
 * @date 2019/11/25 19:33
 */
public class StockCodeUtil {

    /**
     * 将股票代码转换成 sh 上海  sz 深圳
     *
     * @param code
     * @return
     */
    public static String formatStockCode(String code) {
        if (code.matches("^(sz|sh)\\d{6}$")) {
            return code;
        }

        // 5开头沪市基金或权证
        // 60开头上证
        // 68开头科创板
        else if (code.matches("^60.*|68.*|^5.*")) {
            return String.format("sh%s", code);
        }

        // 1开头深市基金
        // 00开头是深圳中小板
        // 300开头是创业板
        else if (code.matches("^1.*|^00.*|^300...")) {
            return String.format("sz%s", code);
        }
        return null;
    }
}
