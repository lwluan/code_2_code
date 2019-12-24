/**
 * 小明这是测试类
 */
package com.cd2cd.dom.java.demo;

import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProModuleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试类
 * 1、类顶部有注解
 * 2、类顶部注释
 * 3、类继承现父
 * 4、类实现接口
 * 5、类有可能是范型
 * 6、
 * {bbb}
 */
@Transactional // aa
@Service //fffvv
//public class DemoClass<T extends TestInterface2> extends DemoSuper implements TestInterface<TestInterface2>, TestInterface2 {
public class DemoClass<T extends TestInterface2, E extends TestInterface2> extends DemoSuper<TestInterface2> implements TestInterface<TestInterface2>, TestInterface2 {


    private final static Logger LOG = LoggerFactory.getLogger(DemoClass.class);

    @Autowired
    private ProProjectMapper proProjectMapper;

    interface TestInterface{

    }

    abstract class TestAbstract {

    }

    enum TestEnum{ /** /** /**
     测试
     */
        aaa, bbb, ccc;
    }

    /**
     * comment static
     * {
     */
    static {
        /**
         * 静态块测试类
         * {aa}, {bbb
         */
        System.out.println("is static ok now!");
    }
    /**
     * }
     */

    /**
     * init block...
     */
    {
        /**
         * test comment
         */
        System.out.println("DemoClass....");
    }

    @Value("${spring.freemarker.suffix}")
    private String staticValue;

    /**
     * child file
     */
    @Transactional
    class TestChild { // test
        private TestEnum testEnum;
        private String name;
        private String age;
        public TestChild() {
            super();
        } // test { }

        public TestChild(TestEnum testEnum) {
            this.testEnum = testEnum;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public TestEnum getTestEnum() {
            return testEnum;
        }

        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }
    }



    /**
     * 名称
     */
    private String name;

    public DemoClass() {

    }

    // /**
    /**
     * {name}
     {name} {
     * @param name

     */
    public DemoClass(String name) { // {
        this.name = name;
    }// }}

    public BaseRes<DataPageWrapper<ProModuleVo>> productList() {
        return null;
    }

    public static void main(String[] args) {
        new DemoClass();
    }
}
