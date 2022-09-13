package com.compact.base.common.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


public class ApiResponse {
	
	@Getter
	@JsonProperty("status")
	public int status;
	@Getter
	@JsonProperty("message")
	public String message;
	@Getter
	@JsonProperty("error")
	public String error;
	@Getter
	@JsonProperty("redirect")
	public String redirect;
	@Getter
	@Setter
	@JsonProperty("data")
	public Object data;
	@Getter
	@Setter
	@JsonProperty("duration")
	public Long duration;

	public ApiResponse() {

	}

	public ApiResponse(int code, String message, String error, String redirect, Object data, Long duration) {
		this.status = code;
		this.message = message;
		this.error = error;
		this.redirect = redirect;
		this.data = data;
		this.duration = duration;
	}
	
	public static ApiResponse of(int code, String message) {
		return new ApiResponse(code, message, null, null, null, null);
	}

	public static ApiResponse error(int code, String error) {
		return new ApiResponse(code, null, error, null, null, null);
	}

	public static ApiResponse redirect(String redirect) {
		return new ApiResponse(302, null, null, redirect, null, null);
	}

	public static ApiResponse success(String message) {
		return new ApiResponse(200, message, null, null, null, null);
	}
	
	public static ApiResponse success(String message, Object data) {
		return new ApiResponse(200, message, null, null, data, null);
	}
	
	public static ApiResponse success(String message, Long duration) {
		return new ApiResponse(200, message, null, null, null, duration);
	}
	
	public static ApiResponse error(String message) {
		return new ApiResponse(500, null, message, null, null, null);
	}

}
