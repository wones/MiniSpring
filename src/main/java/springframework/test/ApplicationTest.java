package springframework.test;

import springframework.context.ApplicationContext;
import springframework.context.annotation.AnnotationConfigApplicationContext;
import springframework.test.config.ApplicationConfig;
import springframework.test.controller.TestController;
import springframework.test.service.TestService;

public class ApplicationTest {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TestController controller = (TestController) applicationContext.getBean("testController");
        controller.getHello();
        TestService service = (TestService) applicationContext.getBean("testService");
        TestService service2 = (TestService) applicationContext.getBean("testService");
        service.echo();
        service2.echo();
    }
}
