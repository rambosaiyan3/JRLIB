We have two ways to use the LIB, we can extend JRSQLConnection
or, just use JRGenericSQLConnection

# Using the JRSQLConnection class

public class MyDAO extends JRSQLConnection<MyEntity> {

# The JRSQLConnection is an abstract class, and there is a abstract method
protected abstract Class<MyEntity> getClazz();


# NOTE: There are simple methods done, to you
find, findAll, insert, delete, update


}

# OR, we can use the simple generic class
that is the same as JRSQLConnection, but we need to pass the target class
as an argument

- Something like : find(MyEntity.class, ... )

