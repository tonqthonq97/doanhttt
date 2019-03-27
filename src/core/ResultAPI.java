package core;

import java.util.ArrayList;

public class ResultAPI {
	public int ErrCode;
	public Object value;
	
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getErrCode() {
		return ErrCode;
	}
	public void setErrCode(int errCode) {
		this.ErrCode = errCode;
	}
	
	public ResultAPI(int errCode, Object value) {
		this.ErrCode = errCode;
		this.value  = value;
	}
	
	

}
