package com.blood.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;




public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
    	return new Class[] { MySecurityConfig.class , MyAppConfig.class };
       
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        
        return null;
    }

    @Override
    protected String[] getServletMappings() {
       
        return new String[] { "/" };
    }

   
}