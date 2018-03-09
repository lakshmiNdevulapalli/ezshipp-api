package com.ezshipp.api.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by srinivasseri on 2/22/18.
 */
public class QueryUtil {

    public static Query getTodayQuery() {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.HOUR_OF_DAY, 00);
        todayMidnight.set(Calendar.MINUTE, 01);
        todayMidnight.set(Calendar.SECOND, 01);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String midnight = formatter.format(todayMidnight.getTime());

        Calendar current = Calendar.getInstance();
        String now = formatter.format(current.getTime());

        Query query = new Query();
        query.addCriteria(where("Date").ne(null).andOperator(
                where("Date").gte(DateUtil.getDate(midnight, DateUtil.DB_FORMAT_DATETIME)),
                where("Date").lte(DateUtil.getDate(now, DateUtil.DB_FORMAT_DATETIME))
        ));
        query.addCriteria(where("Whether_Deleted").ne(Boolean.TRUE));
        return query;
    }

    public static Query getPastDayQuery() {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.MONTH, Calendar.MARCH);
        todayMidnight.set(Calendar.DAY_OF_MONTH, 8);
        todayMidnight.set(Calendar.HOUR_OF_DAY, 00);
        todayMidnight.set(Calendar.MINUTE, 01);
        todayMidnight.set(Calendar.SECOND, 01);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String midnight = formatter.format(todayMidnight.getTime());

        Calendar current = Calendar.getInstance();
        String now = formatter.format(current.getTime());

        Query query = new Query();
        query.addCriteria(where("Date").ne(null).andOperator(
                where("Date").gte(DateUtil.getDate(midnight, DateUtil.DB_FORMAT_DATETIME)),
                where("Date").lte(DateUtil.getDate(now, DateUtil.DB_FORMAT_DATETIME))
        ));
        query.addCriteria(where("Whether_Deleted").ne(Boolean.TRUE));
        return query;
    }

    public static Query getCentralPublicationsQuery() {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.MONTH, Calendar.FEBRUARY);
        todayMidnight.set(Calendar.YEAR, 2018);
        todayMidnight.set(Calendar.DAY_OF_MONTH, 25);
        todayMidnight.set(Calendar.HOUR_OF_DAY, 00);
        todayMidnight.set(Calendar.MINUTE, 00);
        todayMidnight.set(Calendar.SECOND, 01);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String midnight = formatter.format(todayMidnight.getTime());

        //Calendar current = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        toDate.set(Calendar.MONTH, Calendar.MARCH);
        toDate.set(Calendar.YEAR, 2018);
        toDate.set(Calendar.DAY_OF_MONTH, 02);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);
        String now = formatter.format(toDate.getTime());
        System.out.println(now);

        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(where("Date").ne(null).andOperator(
                where("Date").gte(DateUtil.getDate(midnight, DateUtil.DB_FORMAT_DATETIME)),
                where("Date").lte(DateUtil.getDate(now, DateUtil.DB_FORMAT_DATETIME))
        ));
        query.addCriteria(where("Whether_Deleted").ne(Boolean.TRUE));
        criteria.orOperator(where("customerPhone").is("9949771012"),
                where("customerName").is("Central Book Shop"));
        query.addCriteria(criteria);
        return query;
    }

    public static Query getCentralPublicationsItemQuery() {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.MONTH, Calendar.FEBRUARY);
        todayMidnight.set(Calendar.YEAR, 2018);
        todayMidnight.set(Calendar.DAY_OF_MONTH, 24);
        todayMidnight.set(Calendar.HOUR_OF_DAY, 00);
        todayMidnight.set(Calendar.MINUTE, 00);
        todayMidnight.set(Calendar.SECOND, 01);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String midnight = formatter.format(todayMidnight.getTime());

        //Calendar current = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        toDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        toDate.set(Calendar.YEAR, 2018);
        toDate.set(Calendar.DAY_OF_MONTH, 27);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);
        String now = formatter.format(toDate.getTime());
        System.out.println(now);

        Query query = new Query();
        query.addCriteria(where("Whether_Deleted").ne(Boolean.TRUE));
        query.addCriteria(where("itemName").in("200001461", "200001462",
                "200001614", "200001624",
                "200001640", "200001641",
                "200001642", "200001658",
                "200001683", "200001793"));

        return query;
    }

    public static Query getCentralPublicationsRecieverPhoneQuery() {
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        todayMidnight.set(Calendar.MONTH, Calendar.FEBRUARY);
        todayMidnight.set(Calendar.YEAR, 2018);
        todayMidnight.set(Calendar.DAY_OF_MONTH, 26);
        todayMidnight.set(Calendar.HOUR_OF_DAY, 00);
        todayMidnight.set(Calendar.MINUTE, 00);
        todayMidnight.set(Calendar.SECOND, 01);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String midnight = formatter.format(todayMidnight.getTime());

        //Calendar current = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        toDate.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        toDate.set(Calendar.MONTH, Calendar.MARCH);
        toDate.set(Calendar.YEAR, 2018);
        toDate.set(Calendar.DAY_OF_MONTH, 01);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);
        String now = formatter.format(toDate.getTime());
        System.out.println(now);

        Query query = new Query();
        query.addCriteria(where("Whether_Deleted").ne(Boolean.TRUE));
        //query.addCriteria(where("receiverPhone").is("9849810750"));
        //query.addCriteria(where("receiverPhone").in("9246509381", "7702590190", "7702094797"));
        //query.addCriteria(where("receiverPhone").in("8861200672", "9246509381", "8801898555"));
        query.addCriteria(where("receiverPhone").in("996610880","7331178970","9849727637","8008355337",
                "9052401400","9989244613","9395164422","986676788","9949430666","9885922082",
                "9160002575","9849396288","9000847979","9000847979","9618762618","9052033233","9848677000",
                "9848677000","9676575252","8978304842","9441227181","9390057429","9502501232","990870667",
                "9849810750","9908226248","8978931877","8978888345","9972020212","9886086390",
                "9231779954","9618883294","8106011144","800886448","8106011144","8106415532",
                "7893877226","8374749200","8374749200","9849329080","8885494275","98489750511",
                "9963629053","9618772582","98499284087","9666676234","9000552754","8096101991",
                "8096101991","9989488645","9949990348","9849405709","9966024351","9295042441",
                "8332062137","9177599130","9866598350","9731425000","9666635791","9885467950",
                "9948039332","9966403556","988590451","9581928990","9704123190","8008279999",
                "9949172405","9177244452"));
        //query.addCriteria(where("customerPhone").is("9949771012"));
        return query;
    }

}
