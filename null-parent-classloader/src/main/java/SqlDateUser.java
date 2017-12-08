import java.sql.Date;

public class SqlDateUser {

	private Date date;

	public SqlDateUser() {
		date = new Date(System.currentTimeMillis());
	}

	public Date getDate() {
		return date;
	}

}
