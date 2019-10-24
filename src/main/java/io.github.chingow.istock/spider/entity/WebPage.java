package io.github.chingow.istock.spider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.io.Serializable;

/**
 * @author chingow
 * @create 2019-10-23 9:33
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WebPage implements Serializable {

    private Long doneTime;
    private String pageUrl;
    private Document document;
    private String html;
    private Integer httpCode;

}
