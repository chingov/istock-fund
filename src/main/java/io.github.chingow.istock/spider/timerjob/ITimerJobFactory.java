package io.github.chingow.istock.spider.timerjob;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.chingow.istock.spider.timerjob.impl.ClearTimerJobImpl;
import io.github.chingow.istock.spider.timerjob.impl.CoreScheduleTimerJobImpl;
import io.github.chingow.istock.spider.timerjob.impl.IndexTimerJobImpl;

import java.util.HashMap;

/**
 * 命令定时器简单工厂
 *
 * @author chingow
 * @create 2019-10-25 15:25
 **/
public class ITimerJobFactory {

    public enum TimerJob {
        CORE_SCHEDULE, INDEX, CLEAR
    }

    private static HashMap<TimerJob, ITimerJob> map;

    static {
        map = new HashMap<>();
        map.put(TimerJob.CORE_SCHEDULE, new CoreScheduleTimerJobImpl());
        map.put(TimerJob.INDEX, new IndexTimerJobImpl());
        map.put(TimerJob.CLEAR, new ClearTimerJobImpl());
    }

    /**
     * 得到指定的定时器
     *
     * @param timerJob
     * @return
     */
    public static ITimerJob getJob(TimerJob timerJob) {
        return map.get(timerJob);
    }

    public static JSONArray getTasks() {
        JSONArray jsonArray = new JSONArray();
        JSONObject rows;
        for (TimerJob key : map.keySet()) {
            rows = new JSONObject();
            AbstractTimerJob job = (AbstractTimerJob) map.get(key);
            rows.put("id", key);
            rows.put("name", job.name);
            rows.put("status", job.status);
            jsonArray.add(rows);
        }
        return jsonArray;
    }

}
