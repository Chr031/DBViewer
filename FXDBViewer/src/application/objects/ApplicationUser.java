package application.objects;

public class ApplicationUser {

	private final String login;
	private final String name;

	public ApplicationUser(String login) {
		super();
		this.login = login;
		this.name = login;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public void checkPassword(String password) {
		// TODO Auto-generated method stub
		
	}

}
