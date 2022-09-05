package springframework.beans;

public class BeanWrapper {
    private Object wrappedInstance;

    public BeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }
    private Class<?> wrappedClass;
    public Object getWrappedInstance(){
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }
    public Class<?> getWrappedClass(){
        return wrappedClass;
    }
    public void setWrappedClass(Class<?> wrappedClass){
        this.wrappedClass = wrappedClass;
    }
}
