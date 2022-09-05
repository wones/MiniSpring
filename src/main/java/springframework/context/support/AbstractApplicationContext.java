package springframework.context.support;

import springframework.annotation.*;
import springframework.beans.BeanWrapper;
import springframework.beans.factory.config.BeanDefinition;
import springframework.beans.factory.support.BeanDefinitionReader;
import springframework.beans.factory.support.DefaultListableBeanFactory;
import springframework.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractApplicationContext extends DefaultListableBeanFactory implements ApplicationContext {
    protected BeanDefinitionReader reader;
    //保存单例对象
    private Map<String,Object> factoryBeanObjectCache = new HashMap<>();

    private Map<String,BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    @Override
    public void refresh() throws Exception {
        // 扫描需要扫描的包，并把相关的类转化为beanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 注册，将beanDefinition放入IOC容器存储
        doRegisterBeanDefinition(beanDefinitions);
        // 将非懒加载的类初始化
        doAutowired();
    }

    //将beanDefinition放入IOC容器存储
    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws  Exception{
        for(BeanDefinition beanDefinition : beanDefinitions){
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception(beanDefinition.getFactoryBeanName() + "已经存在");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    //将非懒加载的类初始化
    private void doAutowired(){
        for(Map.Entry<String,BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                getBean(beanName);
            }
        }
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        try{
            Object instance = instantiateBean(beanDefinition);
            if(instance == null){
                return null;
            }
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanDefinition.getBeanClassName(),beanWrapper);

            populateBean(instance);
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //通过bd，实例化bean
    private Object instantiateBean(BeanDefinition beanDefinition){
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        try{
            if(this.factoryBeanObjectCache.containsKey(factoryBeanName)){
               instance = this.factoryBeanObjectCache.get(factoryBeanName);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    //开始注入操作
    public void populateBean(Object instance){
        Class clazz = instance.getClass();
        if(!(clazz.isAnnotationPresent(Component.class) ||
                clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class))){
            return ;
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(Autowired.class)){
                continue;
            }
            String autowiredBeanName = field.getType().getName();
            field.setAccessible(true);
            try{
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }
}
