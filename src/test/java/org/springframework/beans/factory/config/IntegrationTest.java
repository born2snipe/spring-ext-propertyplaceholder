package org.springframework.beans.factory.config;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static junit.framework.Assert.assertEquals;

public class IntegrationTest {
    @Test
    public void setterInjection() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:setter.xml");
        TestBean testBean = (TestBean) context.getBean("test");

        assertEquals(System.getProperty("user.home"), testBean.value);
    }

    @Test
    public void propertyFileStore() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:just-property-file.xml");
        TestBean testBean = (TestBean) context.getBean("test");

        assertEquals("value", testBean.value);
    }

    public static class TestBean {
        public String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
