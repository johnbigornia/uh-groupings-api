package edu.hawaii.its.api.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PropertyChecker {

    @Autowired
    private ResourceLoader resourceLoader;

    private final Log logger = LogFactory.getLog(getClass());

    @PostConstruct
    public void init() {
        logger.info("init; starting-----------------------------------");

        for (Resource r : propertyResources()) {
            logger.info("init; <> resource: " + r);
        }

        logger.info("init; started.-----------------------------------");
    }

    private List<Resource> propertyResources() {
        List<Resource> result = new ArrayList<>();
        try {
            Resource[] resources = loadResources("classpath:*.properties");
            if (resources != null && resources.length > 0) {
                result.addAll(Arrays.asList(resources));
            }
        } catch (Exception e) {
            logger.error("Resource loading error.", e);
        }
        return result;
    }
    
    private Resource[] loadResources(String pattern) throws Exception {
        return ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources(pattern);
    }
}
