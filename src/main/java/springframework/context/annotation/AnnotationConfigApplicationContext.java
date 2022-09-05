package springframework.context.annotation;

import springframework.annotation.ComponentScan;
import springframework.beans.factory.support.BeanDefinitionReader;
import springframework.context.support.AbstractApplicationContext;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    public  AnnotationConfigApplicationContext(Class annotatedClass) throws Exception {
        //初始化父类
        super.reader = new BeanDefinitionReader(getScanPackage(annotatedClass));
        refresh();
    }

    @Override
    public void refresh() throws Exception {
        super.refresh();
    }

    public String getScanPackage(Class annotatedClass) throws Exception {
        if(!annotatedClass.isAnnotationPresent(ComponentScan.class)){
            throw new Exception("请为注解配置类加上@ComponentScan注解！");
        }
        ComponentScan componentScan = (ComponentScan) annotatedClass.getAnnotation(ComponentScan.class);
        return componentScan.value().trim();
    }
}
