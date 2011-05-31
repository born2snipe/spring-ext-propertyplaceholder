/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
