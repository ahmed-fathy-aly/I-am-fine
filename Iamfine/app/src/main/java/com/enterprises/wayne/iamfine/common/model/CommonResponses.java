package com.enterprises.wayne.iamfine.common.model;


public class CommonResponses {

	public static abstract class DataResponse {

	}

	public static abstract class SuccessResponse extends DataResponse {

	}

	public static abstract class FailResponse extends DataResponse {

	}

	public final static class NetworkErrorResponse extends FailResponse {
	}

	public final static class AuthenticationErrorResponse extends FailResponse {
	}

	public final static class ServerErrorResponse extends FailResponse {
	}

}
