package io.github.chingow.istock.spider.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 基金信息
 *
 * @author chingow
 * @date 2019/11/26 17:05
 */
@Data
@Document(collection = "fund")
public class Fund {

    @Id
    private String code;

    /**
     * 深市 or 沪市
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 当前价格
     */
    private Double price;

    /**
     * 昨日收盘价格
     */
    private Double yesterdayPrice;

    /**
     * 波动（涨跌幅）
     */
    private Double fluctuate;

    /**
     * 当日最高
     */
    private Double todayMax;

    /**
     * 当日最低
     */
    private Double todayMin;

    /**
     * 价格日期
     */
    private Long priceDate;
}
