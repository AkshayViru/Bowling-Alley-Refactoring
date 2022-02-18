
/**
 *  Class that holds all bowler info
 *
 */

public class Bowler {

    private String fullName;
    private String nickName;
    private String email;

    public Bowler( String nick, String full, String mail ) {
		nickName = nick;
		fullName = full;
		email = mail;
    }


    public String getNickName() {
        return nickName;  
    }

	public String getFullName ( ) {
		return fullName;
	}
	
	public String getNick ( ) {
		return nickName;
	}

	public String getEmail ( ) {
		return email;	
	}
	
	public boolean equals ( Bowler b) {
		return nickName.equals(b.getNickName()) && fullName.equals(b.getFullName()) && email.equals(b.getEmail());
	}
}