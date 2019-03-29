package core;

public class ResultAPI {
	public int errCode;
	public Object value;
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public ResultAPI(int errCode, Object value) {
		this.errCode = errCode;
		this.value = value;
	}
	
	
	
	

}
