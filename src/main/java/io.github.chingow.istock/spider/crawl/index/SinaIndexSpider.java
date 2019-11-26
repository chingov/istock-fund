package io.github.chingow.istock.spider.crawl.index;

import io.github.chingow.istock.common.util.MathFormat;
import io.github.chingow.istock.spider.AbstractHtmlSpider;
import io.github.chingow.istock.spider.entity.WebPage;
import io.github.chingow.istock.spider.po.Fund;
import io.github.chingow.istock.spider.util.StockCodeUtil;
import io.github.chingow.istock.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 新浪财经爬虫
 * http://hq.sinajs.cn/list=%s
 *
 * @author chingow
 * @create 2019-11-17 19:31
 **/
@Slf4j
public class SinaIndexSpider extends AbstractHtmlSpider<Fund> {

    public SinaIndexSpider(String[] stockCode, ConcurrentLinkedQueue<Fund> queue) {
        StringBuilder queryStr = new StringBuilder();
        for (String code : stockCode) {
            String resultCode = StockCodeUtil.formatStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String pageUrl = String.format("http://hq.sinajs.cn/list=%s", queryCode);
        this.queue = queue;
        this.pageUrl = pageUrl;
        this.timeOut = 8000;
        this.ignoreContentType = true;
        this.method = Connection.Method.GET;
    }

    @Override
    public void parsePage(WebPage webPage) throws Exception {
        String[] line = webPage.getDocument().text().split(";");
        for (String s : line) {
            String row = s.trim().replaceAll("^var\\D+|\"", "").replace("=", ",");
            String[] item = row.split(",");
            if (item.length < 30) {
                throw new Exception("代码不存在!");
            }
            double xj = MathFormat.doubleFormat(item[4]);
            double zs = MathFormat.doubleFormat(item[3]);
            double zf = (xj - zs) / zs * 100;
            double todayMax = MathFormat.doubleFormat(item[5]);
            double todayMin = MathFormat.doubleFormat(item[6]);
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

            Fund fund = new Fund();
            //一般这种是停牌的
            if (xj == 0) {
                //波动
                fund.setFluctuate(0d);
            } else {
                NumberFormat nf = NumberFormat.getNumberInstance();
                // 保留两位小数
                nf.setMaximumFractionDigits(2);
                // 如果不需要四舍五入，可以使用RoundingMode.DOWN
                nf.setRoundingMode(RoundingMode.UP);
                // 波动
                fund.setFluctuate(MathFormat.doubleFormat(nf.format(zf)));
            }
            fund.setCode(item[0]);
            fund.setType(StockCodeUtil.formatStockCode(item[0]).replaceAll("\\d", ""));
            fund.setName(item[1].replaceAll("\\s", ""));
            fund.setPrice(xj);
            fund.setTodayMax(todayMax);
            fund.setTodayMin(todayMin);
            fund.setYesterdayPrice(zs);
            fund.setPriceDate(Long.valueOf(TradingDateUtil.getDateTime()));
            queue.offer(fund);
        }
    }
}
