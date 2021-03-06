package org.geektimes.projects.user.sql;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.geektimes.projects.user.domain.User;

public class DBConnectionManager {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        try {
            this.connection = DataSourceManager.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void releaseConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users("
        + "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
        + "name VARCHAR(16) NOT NULL, " + "password VARCHAR(64) NOT NULL, " + "email VARCHAR(64) NOT NULL, "
        + "phoneNumber VARCHAR(64) NOT NULL" + ")";

    public static final String CREATE_USERS_TABLE_DDL_SQL_NO_INCREMENT =
        "CREATE TABLE users(id INT NOT NULL PRIMARY KEY, name VARCHAR(16) NOT NULL, "
            + "password VARCHAR(64) NOT NULL, " + "email VARCHAR(64) NOT NULL, " + "phoneNumber VARCHAR(64) NOT NULL"
            + ")";

    public static final String INSERT_USER_DML_SQL =
        "INSERT INTO users(name,password,email,phoneNumber) VALUES " + "('A','******','a@gmail.com','1') , "
            + "('B','******','b@gmail.com','2') , " + "('C','******','c@gmail.com','3') , "
            + "('D','******','d@gmail.com','4') , " + "('E','******','e@gmail.com','5')";

    public static void testDel() throws SQLException {
        String databaseURL = "jdbc:derby:/db/user-platform;create=true";
        Connection connection = DriverManager.getConnection(databaseURL);

        Statement statement = connection.createStatement();

        System.out.println(statement.execute(DROP_USERS_TABLE_DDL_SQL));
        // System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL));
        System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL_NO_INCREMENT));

        connection.close();
    }

    public static void main(String[] args) throws Exception {
        testDel();
        // initTest();
    }

    public static void initTest() throws Exception {
        // ?????? ClassLoader ?????? java.sql.DriverManager -> static ?????? {}
        // DriverManager.setLogWriter(new PrintWriter(System.out));
        //
        // Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        // Driver driver = DriverManager.getDriver("jdbc:derby:/db/user-platform;create=true");
        // Connection connection = driver.connect("jdbc:derby:/db/user-platform;create=true", new Properties());

        String databaseURL = "jdbc:derby:/db/user-platform;create=true";
        Connection connection = DriverManager.getConnection(databaseURL);

        Statement statement = connection.createStatement();
        // ?????? users ???
        // System.out.println(statement.execute(DROP_USERS_TABLE_DDL_SQL)); // false
        // ?????? users ???
        // System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL)); // false
        // System.out.println(statement.executeUpdate(INSERT_USER_DML_SQL)); // 5

        // ?????????????????????DML???
        ResultSet resultSet = statement.executeQuery("SELECT id,name,password,email,phoneNumber FROM users");

        // BeanInfo
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);

        // ????????? Properties ??????
        for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
            System.out.println(propertyDescriptor.getName() + " , " + propertyDescriptor.getPropertyType());
        }

        // ?????????????????? ORM ??????
        while (resultSet.next()) { // ??????????????????????????????
            User user = new User();

            // ResultSetMetaData ?????????

            ResultSetMetaData metaData = resultSet.getMetaData();
            System.out.println("?????????????????????" + metaData.getTableName(1));
            System.out.println("????????????????????????" + metaData.getColumnCount());
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.println("????????????" + metaData.getColumnLabel(i) + ", ?????????" + metaData.getColumnClassName(i));
            }

            StringBuilder queryAllUsersSQLBuilder = new StringBuilder("SELECT");
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                queryAllUsersSQLBuilder.append(" ").append(metaData.getColumnLabel(i)).append(",");
            }
            // ?????????????????? ","
            queryAllUsersSQLBuilder.deleteCharAt(queryAllUsersSQLBuilder.length() - 1);
            queryAllUsersSQLBuilder.append(" FROM ").append(metaData.getTableName(1));

            System.out.println(queryAllUsersSQLBuilder);

            // ???????????????????????????????????????????????????
            // user.setId(resultSet.getLong("id"));
            // user.setName(resultSet.getString("name"));
            // user.setPassword(resultSet.getString("password"));
            // user.setEmail(resultSet.getString("email"));
            // user.setPhoneNumber(resultSet.getString("phoneNumber"));

            // ???????????? API???????????????????????????

            // User ????????????????????????????????????
            // ClassLoader.loadClass -> Class.newInstance()
            // ORM ????????????????????????????????????????????????????????????????????????
            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                Class fieldType = propertyDescriptor.getPropertyType();
                String methodName = typeMethodMappings.get(fieldType);
                // ??????????????????????????????????????????????????????
                String columnLabel = mapColumnLabel(fieldName);
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                // ?????????????????? getXXX(String) ??????
                Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                // ?????? User ??? Setter??????
                // PropertyDescriptor ReadMethod ?????? Getter ??????
                // PropertyDescriptor WriteMethod ?????? Setter ??????
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                // ??? id ????????? user.setId(resultSet.getLong("id"));
                setterMethodFromUser.invoke(user, resultValue);
            }

            System.out.println(user);
        }

        connection.close();
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * ??????????????? ResultSet ???????????????
     */
    static Map<Class, String> typeMethodMappings = new HashMap<>();

    static {
        typeMethodMappings.put(Long.class, "getLong");
        typeMethodMappings.put(String.class, "getString");
    }
}
