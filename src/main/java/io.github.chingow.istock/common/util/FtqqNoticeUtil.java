package io.github.chingow.istock.common.util;

import com.alibaba.fastjson.JSONObject;
import io.github.chingow.istock.common.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 方糖通知
 *
 * @author yangpeng
 * @date 2020/3/11 21:21
 */
@Slf4j
@Component
public class FtqqNoticeUtil {

    @Value("${ftqq.sckey}")
    protected String ftqqSckey;

    private static final String urlFormat = "https://sc.ftqq.com/%s.send";

    /**
     * 发送通知
     *
     * @param sckey
     * @param text
     * @param desp
     * @return
     */
    public Boolean sendNotice(String sckey, String text, String desp) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("text", text);
        paramMap.put("desp", desp);

        // 将信息发送给管理员用户
        JSONObject resObject = JSONObject.parseObject(HttpClientUtils.sendPostForm(String.format(urlFormat, ftqqSckey),
                null,
                headerMap,
                paramMap));

        // 如果有指定发送人，则将信息也发送给指定用户
        if (!StringUtils.isEmpty(sckey)) {
            HttpClientUtils.sendPostForm(String.format(urlFormat, sckey),
                    null,
                    headerMap,
                    paramMap);
        }

        return ((String) resObject.get("errmsg")).contains("success");
    }
}
