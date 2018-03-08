package com.ezshipp.api.convertor;

import com.ezshipp.api.document.Order;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by srinivasseri on 2/6/18.
 */
public class OrderWriterConverter implements Converter<Order, DBObject> {

    @Override
    public DBObject convert(final Order order) {
        final DBObject dbObject = new BasicDBObject();
//        dbObject.put("name", order.getName());
//        dbObject.put("age", order.getAge());
//        if (user.getEmailAddress() != null) {
//            final DBObject emailDbObject = new BasicDBObject();
//            emailDbObject.put("value", user.getEmailAddress().getValue());
//            dbObject.put("email", emailDbObject);
//        }
//        dbObject.removeField("_class");
        return dbObject;
    }

}
