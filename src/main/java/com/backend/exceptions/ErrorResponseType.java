package com.backend.exceptions;

public enum ErrorResponseType {
    BAD_REQUEST("The request sent by the client is malformed or invalid."),
    UNAUTHORIZED("The client attempted to access a protected resource without providing valid authentication credentials."),
    FORBIDDEN("The client attempted to access a resource that they are not authorized to access."),
    NOT_FOUND("The requested resource cannot be found on the server."),
    METHOD_NOT_ALLOWED("The requested HTTP method is not supported for the requested resource."),
    UNSUPPORTED_MEDIA_TYPE("The media type of the request entity is not supported by the server."),
    REQUEST_TIMEOUT("The server timed out waiting for the client to complete the request."),
    CONFLICT("The request conflicts with the current state of the server resource."),
    GONE("The requested resource is no longer available and has been permanently removed."),
    PRECONDITION_FAILED("The precondition given in the request evaluated to false by the server."),
    REQUEST_ENTITY_TOO_LARGE("The size of the request entity exceeds the server's capacity."),
    REQUEST_URI_TOO_LONG("The length of the requested URL exceeds the server's capacity."),
    INTERNAL_SERVER_ERROR("The server encountered an unexpected error that prevents it from fulfilling the request."),
    NOT_IMPLEMENTED("The server does not support the functionality required to fulfill the request."),
    SERVICE_UNAVAILABLE("The server is temporarily unable to handle the request due to maintenance or overload."),
    GATEWAY_TIMEOUT("The server acting as a gateway or proxy did not receive a timely response from an upstream server."),

    TRANSACTION_DECLINED("The transaction was declined by the payment provider."),
    TRANSACTION_FAILED("The transaction failed due to an error on the payment provider's end."),
    PROVIDER_UNAVAILABLE("The payment provider is temporarily unable to handle the request due to maintenance or overload.");






    private final String message;

    ErrorResponseType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
