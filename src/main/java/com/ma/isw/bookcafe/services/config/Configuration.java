package com.ma.isw.bookcafe.services.config;

import java.util.Calendar;
import java.util.logging.Level;

import com.ma.isw.bookcafe.model.dao.DAOFactory;

public class Configuration {

    /* Database Configuration */
    public static final String DAO_IMPL=DAOFactory.PSQLJDBCIMPL;
    public static final String DATABASE_DRIVER="org.postgresql.Driver";
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();
    public static final String
            DATABASE_URL="jdbc:postgresql://localhost:5432/bookCafeDB?user=postgres&password=Cretino021234ì&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;

    /* Session Configuration */
    public static final String COOKIE_IMPL=DAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="bookCafe";
    public static final String GLOBAL_LOGGER_FILE="/home/marco/Scrivania/Mio_Lx/Uni/Inge 3_Lx/Ingegneria dei Sistemi Web/BookCafe/debug/bookCafe_log.%g.%u.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;

}
