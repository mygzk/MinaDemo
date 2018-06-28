package com.gzk.client;

import java.io.Serializable;

/**
 * @Description: 外部传输的简单对象
 * @date 2018年3月22日 下午9:43:14
 * @author manqingxing
 */
public class Chat implements Serializable {

	/**
	 * token:tmc_token.
	 *
	 * @since JDK 1.8.0_161 必填
	 */
	String token;

	/**
	 * 用户ID 必填
	 */
	String uId;

	/**
	 * 消息体ID，必填
	 */
	String chatId;

	/**
	 * 消息内容 非必填
	 */
	String message;

	/**
	 * 消息 必填
	 */
	int messageType;

	/**
	 * 场景 必填
	 */
	int scene;

	/**
	 * 附件对象，飞机票、火车票、酒店、订单、行程对象等 非必填
	 *
	 */
	String objects;

	/**
	 * 客户端创建时间 必填
	 */
	long timeCreate;

	public String getChatId() {
		return chatId;
	}

	public String getMessage() {
		return message;
	}

	public int getMessageType() {
		return messageType;
	}

	public String getObjects() {
		return objects;
	}

	public int getScene() {
		return scene;
	}

	public long getTimeCreate() {
		return timeCreate;
	}

	public String getToken() {
		return token;
	}

	public String getuId() {
		return uId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public void setObjects(String objects) {
		this.objects = objects;
	}

	public void setScene(int scene) {
		this.scene = scene;
	}

	public void setTimeCreate(long timeCreate) {
		this.timeCreate = timeCreate;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

}