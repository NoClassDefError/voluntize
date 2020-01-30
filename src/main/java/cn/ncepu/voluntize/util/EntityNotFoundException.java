package cn.ncepu.voluntize.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message){
        super();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error(message);
    }
}
