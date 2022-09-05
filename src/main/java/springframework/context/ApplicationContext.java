package springframework.context;

import springframework.beans.factory.BeanFactory;

public interface ApplicationContext extends BeanFactory {
    void refresh() throws Exception;
}
