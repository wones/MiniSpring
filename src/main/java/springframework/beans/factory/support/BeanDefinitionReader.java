package springframework.beans.factory.support;

import springframework.beans.factory.config.BeanDefinition;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionReader {
    //存储扫描出来的bean的全类名
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String scanPackage) throws Exception{
        doScan(scanPackage);
    }

    public void doScan(String scanPackage) throws Exception {
        // 将包名转为文件路径
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if(url == null){
            throw new Exception("包" + scanPackage + "不存在！");
        }
        File classPath = new File(url.getFile());
//        File classPath = new File("C:\\Users\\王生辉\\Desktop\\mygit\\MiniSpring\\src\\main\\java\\springframework\\test");
        for(File file : classPath.listFiles()){
            if(file.isDirectory()){
                doScan(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }
        }
    }
    public List<BeanDefinition> loadBeanDefinitions(){
        List<BeanDefinition> result = new ArrayList<>();
        try{
            for(String className : registryBeanClasses){
                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()){
                    continue;
                }
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
                Class<?>[] interfaces = beanClass.getInterfaces();
                for(Class<?> anInterface : interfaces){
                    result.add(doCreateBeanDefinition(anInterface.getName(),beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName){
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }
    private String toLowerFirstCase(String simpleName){
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
