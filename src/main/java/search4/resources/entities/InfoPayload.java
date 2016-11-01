package search4.resources.entities;

public class InfoPayload {

	private String user_Message;
	private String internal_Message;
	private String errorCode;
	private String more_Info;
	private boolean resultOK;
	

	public boolean isResultOK() {
		return resultOK;
	}
	public void setResultOK(boolean resultOK) {
		this.resultOK = resultOK;
	}
	
	public String getUser_Message() {
		return user_Message;
	}
	public void setUser_Message(String user_Message) {
		this.user_Message = user_Message;
	}

	public String getInternal_Message() {
		return internal_Message;
	}
	public void setInternal_Message(String internal_Message) {
		this.internal_Message = internal_Message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMore_Info() {
		return more_Info;
	}
	public void setMore_Info(String more_Info) {
		this.more_Info = more_Info;
	}

	
	
	
}
