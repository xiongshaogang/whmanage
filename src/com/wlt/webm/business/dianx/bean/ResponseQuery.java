package com.wlt.webm.business.dianx.bean;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import com.commsoft.epay.util.logging.Log;

public class ResponseQuery {

	/**
	 * 返回的数据信息
	 */
	private byte[] recvMsg;
	
	/**
	 * 数据输入流
	 */
	private DataInputStream input;
	
	/**
	 * 包头
	 */
	private byte[] head = new byte[4];
	
	/**
	 * 包的标识号
	 */
	private byte[] bagFlag = new byte[10];
	
	/**
	 * 包总长度
	 */
	private byte[] bagLength = new byte[8];
	
	/**
	 * 包的类型
	 */
	private byte[] bagType = new byte[8];
	
	/**
	 * 结构标识
	 */
	private byte[] structFlag = new byte[1];
	
	/**
	 * 校验标识
	 */
	private byte[] checkFlag = new byte[1];
	
	/**
	 * 参数个数
	 */
	private byte[] paramsNum = new byte[2];
	
	/**
	 * 发送系统ID
	 */
	private byte[] sendID = new byte[2];
	
	/**
	 * 接收系统ID
	 */
	private byte[] receiveID = new byte[2];
	
	/**
	 * 预留空间
	 */
	private byte[] blankArea = new byte[26];
	
	/**
	 * 信息鉴真码
	 */
	private byte[] checkCode = new byte[8];
	
	/**
	 * 请求结果编码
	 */
	private byte[] responseCode = new byte[4];
	
	/**
	 * 请求信息说明
	 */
	private byte[] responseInfo;
	
	/**
	 * 账号余额
	 */
	private byte[] leftFee ;
	
	/**
	 * 充值/缴费类型
	 */
	private byte[] payType ;
	
	/**
	 * 交易账号
	 */
	private byte[] accountNo ;
	
	/**
	 * 支付金额
	 */
//	private byte[] payFee ;
	
	/**
	 * 账号类型
	 */
	private byte[] accountType = new byte[1];
	
	/**
	 * 号码归属地
	 */
	private byte[] numAddr ;
	
	/**
	 * 默认构造函数
	 *
	 */
	public ResponseQuery(){
		
	}
	
	public ResponseQuery(byte[] receiveMsg){
		this.recvMsg = receiveMsg;
		if( recvMsg != null){
			input = new DataInputStream(new ByteArrayInputStream(recvMsg));
			deal();
		}
	}
	
	/**
	 * 解析接收到的计费消息
	 *
	 */
	public void deal(){
		try{
			//解析包头信息
			input.read(head);
			input.read(bagFlag);
			input.read(bagLength);
			input.read(bagType);
			input.read(structFlag);
			input.read(checkFlag);
			input.read(paramsNum);
			input.read(sendID);
			input.read(receiveID);
			input.read(blankArea);
			input.read(checkCode);
			//解析包体信息
			input.read(new byte[8]);//000
			input.read(new byte[8]);
			input.read(responseCode);//请求结果编码
			input.read(new byte[4]);
			byte[] temp1 = new byte[4];
			input.read(temp1);
			responseInfo = new byte[Integer.parseInt(new String(temp1).trim())];
			input.read(responseInfo);//请求结果说明
			
			if(Integer.parseInt(new String(paramsNum)) > 1){//如果返回码为成功，则继续解析一下字段
				
				input.read(new byte[8]);//708
				
				input.read(new byte[4]);
				byte[] temp2 = new byte[4];
				input.read(temp2);
				accountNo = new byte[Integer.parseInt(new String(temp2).trim())];
				input.read(accountNo);//交易账号
				
				input.read(new byte[4]);
				byte[] temp3 = new byte[4];
				input.read(temp3);
				leftFee = new byte[Integer.parseInt(new String(temp3).trim())];
				input.read(leftFee);//账号余额
				
//				input.read(new byte[4]);
//				byte[] temp5 = new byte[4];
//				input.read(temp5);
//				payFee = new byte[Integer.parseInt(new String(temp5).trim())];
//				input.read(payFee);//支付金额
				
				input.read(new byte[4]);
				byte[] temp4 = new byte[4];
				input.read(temp4);
				payType = new byte[Integer.parseInt(new String(temp4).trim())];
				input.read(payType);//充值缴费类型
				
				input.read(new byte[4]);
				input.read(new byte[4]);
				input.read(accountType);//账号类型
				
				input.read(new byte[4]);
				byte[] temp6 = new byte[4];
				input.read(temp6);
				numAddr = new byte[Integer.parseInt(new String(temp6).trim())];
				input.read(numAddr);//号码归属地
			}
			
		}catch(Exception e){
			Log.error("解析计费返回的查询消息出错！");
			Log.error(e);
		}
	}

	/**
	 * 请求结果编码
	 * @return
	 */
	public String getResponseCode() {
		String code = new String(responseCode).trim();
		if(code.equals("6750") || code.equals("6751")){
			code = new String(responseInfo).substring(10, 14);
		}
		return code;
	}

	/**
	 * 请求结果说明
	 * @return
	 */
	public String getResponseInfo() {
		return new String(responseInfo).trim();
	}

	/**
	 * 账号余额
	 * @return
	 */
	public String getLeftFee() {
		return new String(leftFee).trim();
	}

	/**
	 * 充值/缴费类型
	 * @return
	 */
	public String getPayType() {
		return new String(payType).trim();
	}

	/**
	 * 账号类型
	 * @return
	 */
	public String getAccountType() {
		return new String(accountType).trim();
	}

	/**
	 * 充值/缴费类型
	 * @return
	 */
//	public String getPayFee() {
//		return new String(payFee).trim();
//	}

	/**
	 * 交易账号
	 * @return
	 */
	public String getAccountNo() {
		return new String(accountNo).trim();
	}
	
	/**
	 * 号码归属地
	 */
	public String getNumAddr() {
		return new String(numAddr).trim();
	}
}
