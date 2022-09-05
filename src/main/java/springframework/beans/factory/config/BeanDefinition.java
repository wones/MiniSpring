package springframework.beans.factory.config;

public class BeanDefinition {
    private String beanClassName; //全类名
    private boolean lazyInit = false; //是否懒加载
    private String factoryBeanName; //保存在IOC容器是的key值

    public String getBeanClassName(){
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName){
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit(){
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit){
        this.lazyInit = lazyInit;
    }
    public String getFactoryBeanName(){
        return factoryBeanName;
    }
    public void setFactoryBeanName(String factoryBeanName){
        this.factoryBeanName = factoryBeanName;
    }
}
