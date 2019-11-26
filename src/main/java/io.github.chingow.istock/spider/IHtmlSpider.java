package io.github.chingow.istock.spider;

import io.github.chingow.istock.spider.entity.WebPage;

/**
 * @author chingow
 * @create 2019-11-25 19:26
 **/
public interface IHtmlSpider {
    /**
     * 爬取页面
     *
     * @return
     */
    WebPage crawlPage();

    /**
     * 解析页面
     *
     * @param webPage
     */
    void parsePage(WebPage webPage) throws Exception;


}
