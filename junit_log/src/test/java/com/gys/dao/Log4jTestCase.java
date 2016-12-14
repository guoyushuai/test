package com.gys.dao;

/*import org.apache.log4j.Logger;*/

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTestCase {

    @Test
    public void logTest() {

        //Logger logger = Logger.getLogger(Log4jTestCase.class);
        Logger logger = LoggerFactory.getLogger(Log4jTestCase.class);

        //logger.trace("trace message");//log4j
        logger.trace("{}-{} message","foo","bar");//slf4j

        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        /*logger.fatal("fatal message");//log4j*/

    }

}
