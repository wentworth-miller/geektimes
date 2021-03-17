package org.geektimes.projects.user.management;

import javax.management.MBeanInfo;
import javax.management.StandardMBean;

import org.geektimes.projects.user.domain.User;

public class StandardMBeanDemo {

    public static void main(String[] args) throws Exception {
        // 将静态的 MBean 接口转化成 DynamicMBean
        StandardMBean standardMBean = new StandardMBean(new UserManager(new User()), UserManagerMBean.class);

        MBeanInfo mBeanInfo = standardMBean.getMBeanInfo();

        System.out.println(mBeanInfo);
    }
}
