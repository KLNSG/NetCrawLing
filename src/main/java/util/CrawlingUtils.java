package util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.NetCalendarDay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：Lxin
 * @date ：Created in 2020/9/23 15:38
 */
public class CrawlingUtils {

    private static final Log logger = LogFactory.getLog(CrawlingUtils.class);
    
    public static List<NetCalendarDay> getCurrentDateInfo(int month) {
        WebClient webClient = null;
        List<NetCalendarDay> dateList = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateList = new ArrayList<NetCalendarDay>();
            webClient = new WebClient();
            HtmlPage page = webClient.getPage("http://hao.360.cn/rili/");
            for (int k = 0; k < 60; k++) {
                if (!page.getElementById("M-dates").asText().equals("")) break;
                Thread.sleep(1000);
            }
            DomNodeList<HtmlElement> htmlElements = page.getElementById("M-dates").getElementsByTagName("li");
            for (HtmlElement element : htmlElements) {
                NetCalendarDay NetCalendarDay = new NetCalendarDay();
                NetCalendarDay.setSolarDate(dateFormat.parse(element.getAttribute("date")));
                if (element.getAttribute("class").contains("vacation")) {
                    NetCalendarDay.setVacation(true);
                    NetCalendarDay.setVacationName(getVocationName(htmlElements, element.getAttribute("date")));
                }
                if (element.getAttribute("class").contains("weekend") &&
                        !element.getAttribute("class").contains("last")) {
                    NetCalendarDay.setSaturday(true);
                }
                if (element.getAttribute("class").contains("last weekend")) {
                    NetCalendarDay.setSunday(true);
                }
                if (element.getAttribute("class").contains("work")) {
                    NetCalendarDay.setWorkFlag(true);
                } else if (!NetCalendarDay.isSaturday() &&
                        !NetCalendarDay.isSunday() &&
                        !NetCalendarDay.isVacation()) {
                    NetCalendarDay.setWorkFlag(true);
                } else {
                    NetCalendarDay.setWorkFlag(false);
                }
                dateList.add(NetCalendarDay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("爬取360万年历失败");
        } finally {
            webClient.closeAllWindows();
        }
        return dateList.stream().filter(x->(x.getSolarDate().getMonth()+1)==month).collect(Collectors.toList());
    }

    public static NetCalendarDay getTodayInfo() {
        List<NetCalendarDay> dateList = getCurrentDateInfo(new Date().getMonth()+1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (NetCalendarDay date : dateList) {
            if (dateFormat.format(date.getSolarDate()).equals(dateFormat.format(new Date()))) {
                return date;
            }
        }
        return new NetCalendarDay();
    }

    private static String getVocationName(DomNodeList<HtmlElement> htmlElements, String date) throws ParseException {
        String rst = "";
        for (int i = 0; i < htmlElements.size(); i++) {
            HtmlElement element = htmlElements.get(i);
            if (element.getAttribute("class").contains("vacation")) {
                boolean hitFlag = false;
                String voationName = "";
                for (; i < htmlElements.size(); i++) {
                    HtmlElement elementTmp = htmlElements.get(i);
                    String liDate = elementTmp.getAttribute("date");
                    List<HtmlElement> lunar = elementTmp.getElementsByAttribute("span", "class", "lunar");
                    voationName=lunar.get(0).asText();
                    if (liDate.equals(date)) {
                        hitFlag = true;
                    }
                    if (!elementTmp.getAttribute("class").contains("vacation")) {
                        break;
                    }
                }
                if (hitFlag && !voationName.equals("")) {
                    rst = voationName;
                    break;
                }
            } else {
                continue;
            }
        }
        return rst;
    }

}
