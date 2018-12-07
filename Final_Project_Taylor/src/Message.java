
public class Message {
	
	String message = "no_message";
	String who = "no_player";
	
	public Message(String m, String w) {
		this.message = m;
		this.who = w;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getWho() {
		return this.who;
	}
	
}