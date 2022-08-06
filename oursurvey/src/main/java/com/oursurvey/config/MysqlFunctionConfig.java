package com.oursurvey.config;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.StringType;

public class MysqlFunctionConfig extends MySQL57Dialect {
    public MysqlFunctionConfig() {
        super();
        this.registerFunction("GROUP_CONCAT", new StandardSQLFunction("GROUP_CONCAT", new StringType()));
        this.registerFunction("DATE_FORMAT", new StandardSQLFunction("DATE_FORMAT", new StringType()));
        this.registerFunction("WEEKDAY", new StandardSQLFunction("WEEKDAY", new StringType()));
        this.registerFunction("DAYOFWEEK", new StandardSQLFunction("DAYOFWEEK", new StringType()));
        this.registerFunction("DATE", new StandardSQLFunction("DATE", new StringType()));
    }
}
