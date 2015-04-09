package kr.ac.yonsei.mobilesw.assertonintent;

public class MalformedIntentException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String msg;
	private int num;
	
	public MalformedIntentException(String msg, int num)
	{
		this.msg = msg;
		this.num = num;
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public int getNumber()
	{
		return num;
	}
}