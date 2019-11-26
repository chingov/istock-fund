package io.github.chingow.istock.spider.crawl.index;

import io.github.chingow.istock.spider.AbstractHtmlSpider;
import io.github.chingow.istock.spider.entity.WebPage;
import io.github.chingow.istock.spider.po.Fund;
import io.github.chingow.istock.spider.util.StockCodeUtil;
import io.github.chingow.istock.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 腾讯证券爬虫
 * http://qt.gtimg.cn/q=%s
 *
 * @author chingow
 * @create 2019-11-17 19:58
 **/
@Slf4j
public class TencentIndexSpider extends AbstractHtmlSpider<Fund> {

    public TencentIndexSpider(String[] stockCode, ConcurrentLinkedQueue<Fund> queue) {
        StringBuilder queryStr = new StringBuilder();
        for (String code : stockCode) {
            String resultCode = StockCodeUtil.formatStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String pageUrl = String.format("http://qt.gtimg.cn/q=%s", queryCode);
        this.queue = queue;
        this.pageUrl = pageUrl;
        this.timeOut = 8000;
        this.ignoreContentType = true;
        this.method = Connection.Method.GET;
    }

    @Override
    public void parsePage(WebPage webPage) throws Exception {

        String content = webPage.getDocument().text();
        List<String> rows = Arrays.asList(content.split(";"));
        for (int i = 0; i < rows.size(); i++) {
            String[] item = rows.get(i).replaceAll("v_.*=|\"", "").split("~");
            Fund fund = new Fund();
            fund.setCode(item[2]);
            fund.setFluctuate(Double.parseDouble(item[32]));
            fund.setType(StockCodeUtil.formatStockCode(item[2]).replaceAll("\\d", ""));
            fund.setName(item[1].replaceAll("\\s", ""));
            fund.setPrice(Double.parseDouble(item[3]));
            fund.setTodayMax(Double.parseDouble(item[41]));
            fund.setTodayMin(Double.parseDouble(item[42]));
            fund.setYesterdayPrice(Double.parseDouble(item[4]));
            fund.setPriceDate(Long.valueOf(TradingDateUtil.getDateTime()));
            queue.offer(fund);
        }
    }
}
