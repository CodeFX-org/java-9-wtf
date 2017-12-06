import java.sql.Date;

public class JavaSqlUser {
    private SqlDateUser sqlDateUser;

    public JavaSqlUser() {
        sqlDateUser = new SqlDateUser();
    }

    public Date getDate() {
        return sqlDateUser.getDate();
    }

    public String toString() {
        return String.format("JavaSqlUser(%s)", sqlDateUser);
    }
}