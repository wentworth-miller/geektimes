package org.geektimes.projects.user.management;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

/**
 * 动态结构，无固定接口类型（运行时确定）
 */
public class UserDynamicMXBean implements DynamicMBean {

    private String nameKey = "Name";

    private String emailKey = "Email";

    // 五个属性
    // id、name、password、email、phoneNumber
    private Map<String, Object> attributes = new HashMap<>();

    private MBeanInfo mBeanInfo = null;

    public UserDynamicMXBean() throws NoSuchMethodException {
        init();
        fillVal();
    }

    private void fillVal() {
        attributes.put(nameKey, "test");
        // Config config = ConfigProvider.getConfig();
        // String email = config.getValue("email", String.class);
        // attributes.put(emailKey, email);
    }

    private void init() throws NoSuchMethodException {
        String className = this.getClass().getName();
        // 没有显示书写其他的构造方法，只有一个默认的构造方法
        // Constructor<? extends UserDynamicMXBean> constructor = this.getClass().getConstructor(null);
        Constructor<?>[] constructors = this.getClass().getConstructors();
        MBeanConstructorInfo mBeanConstructorInfo =
            new MBeanConstructorInfo("UserDynamic(): Constructs a UserDynamic object", constructors[0]);
        // 属性首字母大写，set get 拼接需要，(可以不需要)方法不要命名为set get 开头，会认为是属性，其他的是方法
        MBeanAttributeInfo[] mBeanAttributeInfos = new MBeanAttributeInfo[] {
            new MBeanAttributeInfo("Id", "java.lang.Long", "Id: name long", true, true, false),
            new MBeanAttributeInfo("Name", "java.lang.String", "Name: name string", true, true, false),
            new MBeanAttributeInfo("Password", "java.lang.String", "Password: name string", true, true, false),
            new MBeanAttributeInfo("Email", "java.lang.String", "Email: name string", true, true, false),
            new MBeanAttributeInfo("PhoneNumber", "java.lang.String", "PhoneNumber: name string", true, true, false)};

        // 方法，无参，无返回值
        MBeanParameterInfo[] mBeanParameterInfos =
            new MBeanParameterInfo[] {new MBeanParameterInfo("param", "java.lang.String", "query param")};
        MBeanOperationInfo mBeanOperationInfo = new MBeanOperationInfo("query", "query(): query the name",
            mBeanParameterInfos, "java.lang.String", MBeanOperationInfo.INFO);
        mBeanInfo = new MBeanInfo(className, "UserDynamicMXBean object", mBeanAttributeInfos,
            new MBeanConstructorInfo[] {mBeanConstructorInfo}, new MBeanOperationInfo[] {mBeanOperationInfo},
            new MBeanNotificationInfo[0]);
    }

    @Override
    public Object getAttribute(String attribute)
        throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (!attributes.containsKey(attribute)) {
            throw new AttributeNotFoundException("...");
        }
        return attributes.get(attribute);
    }

    @Override
    public void setAttribute(Attribute attribute)
        throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        attributes.put(attribute.getName(), attribute.getValue());
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        AttributeList attributeList = new AttributeList();
        for (String attribute : attributes) {
            try {
                Object attributeValue = getAttribute(attribute);
                attributeList.add(new Attribute(attribute, attributeValue));
            } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
            }
        }
        return attributeList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        if (Objects.isNull(attributes)) {
            return null;
        }
        AttributeList resultList = new AttributeList();
        attributes.forEach(item -> {
            Attribute attr = (Attribute)item;
            resultList.add(new Attribute(attr.getName(), attr.getValue()));
        });
        return resultList;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
        throws MBeanException, ReflectionException {
        if ("query".equals(actionName)) {
            // 打印值且修改name的值
            String name = String.class.cast(attributes.get(nameKey));
            System.out.println("UserDynamic name: " + name);
            name = name + "abc";
            attributes.put(nameKey, name);
            return name;
        }
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }
}
