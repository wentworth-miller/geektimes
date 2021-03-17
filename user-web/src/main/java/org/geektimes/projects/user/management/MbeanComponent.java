package org.geektimes.projects.user.management;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.geektimes.projects.user.domain.BookStore;
import org.geektimes.projects.user.domain.User;

/**
 * @author xubin
 * @date 2021/3/17 16:35
 */
public class MbeanComponent {

    public MbeanComponent() throws MalformedObjectNameException, NotCompliantMBeanException,
        InstanceAlreadyExistsException, MBeanRegistrationException, NoSuchMethodException {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = new ObjectName("org.geektimes.projects.user.management:type=User");
        // 创建 UserMBean 实例
        User user = new User();
        mBeanServer.registerMBean(new UserManager(user), objectName);

        BookStore bookStore = new BookStore();
        bookStore.setStoreName("test");
        ObjectName objectName2 = new ObjectName("org.geektimes.projects.user.management:type=BookStore");
        mBeanServer.registerMBean(new BookStoreManagement(bookStore), objectName2);

        UserDynamicMXBean userDynamicMXBean = new UserDynamicMXBean();
        ObjectName objectName3 = new ObjectName("org.geektimes.projects.user.management:type=UserDynamicMXBean");
        mBeanServer.registerMBean(userDynamicMXBean, objectName3);
    }
}
