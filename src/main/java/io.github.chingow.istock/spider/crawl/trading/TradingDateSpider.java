package io.github.chingow.istock.spider.crawl.trading;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.chingow.istock.spider.entity.WebPage;
import io.github.chingow.istock.spider.util.JsoupUitl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 交易日爬虫
 *
 * @author chingow
 * @create 2019-03-26 17:57
 * http://www.szse.cn/api/report/exchange/onepersistentday/monthList
 **/
@Slf4j
public class TradingDateSpider implements Callable<Map<String, Boolean>> {

    private final String baseUrl = "http://www.szse.cn/api/report/exchange/onepersistenthour/monthList";
    private String month;
    private String url;

    public TradingDateSpider(String month) {
        this.month = month;
        url = null == month ? baseUrl : String.format("%s?month=%s", baseUrl, month);
    }

    /**
     * 通过深交所的接口获取月日期和是否交易日信息
     * {"data":[{"zrxh":3,"jybz":"0","jyrq":"2019-10-01"},{"zrxh":5,"jybz":"1","jyrq":"2019-10-31"}],"nowdate":"2019-10-23"}
     * jyrq 交易日期
     * zrxh 周日序号 1-7 代表周日到周六
     * jybz 交易标识 0 非交易日、1 交易日
     */
    @Override
    public Map<String, Boolean> call() {
        WebPage webPage = JsoupUitl.getWebPage(url, Connection.Method.GET,
                8000, null, "http://www.szse.cn/disclosure/index.html");
        Optional.ofNullable(webPage).map(webPage1 -> webPage1.getDocument().text());

        JSONObject json = JSON.parseObject(webPage.getDocument().text());
        JSONArray jsonArray = json.getJSONArray("data");
        Map<String, Boolean> data = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            data.put(
                    jsonArray.getJSONObject(i).getString("jyrq"),
                    jsonArray.getJSONObject(i).getInteger("jybz") == 1
            );
        }
        return data;
    }
}
