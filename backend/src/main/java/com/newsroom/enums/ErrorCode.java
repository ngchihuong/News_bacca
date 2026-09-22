package com.newsroom.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED(1000, "Vui lòng đăng nhập để tiếp tục", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1001, "Email/Số điện thoại hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1002, "Tài khoản không tồn tại trong hệ thống", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1003, "Email hoặc số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(1004, "Tài khoản tạm thời bị khóa do nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(1005, "Tài khoản của bạn đã bị vô hiệu hóa hoặc khóa bởi quản trị viên", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(1006, "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1007, "Mã xác thực không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS(1008, "Bạn không có quyền thực hiện thao tác này", HttpStatus.FORBIDDEN),

    // 1100 - 1199: Request & Validation
    INVALID_REQUEST_BODY(1101, "Dữ liệu yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_FORMAT(1102, "Địa chỉ email không đúng định dạng", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(1103, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    PHONE_INVALID_FORMAT(1104, "Số điện thoại không đúng định dạng", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED(1105, "Vui lòng điền đầy đủ các thông tin bắt buộc", HttpStatus.BAD_REQUEST),

    // 1200 - 1299: Profile & File Upload
    FILE_TOO_LARGE(1201, "Kích thước tệp vượt quá giới hạn cho phép (tối đa 2MB)", HttpStatus.BAD_REQUEST),
    FILE_INVALID_TYPE(1202, "Định dạng tệp không hợp lệ. Chỉ chấp nhận JPG, PNG hoặc WebP", HttpStatus.BAD_REQUEST),
    BIO_TOO_LONG(1203, "Tiểu sử không được vượt quá 500 ký tự", HttpStatus.BAD_REQUEST),

    // 2000 - 2099: Content & Post (Dành cho các Story tiếp theo)
    POST_NOT_FOUND(2001, "Bài viết không tồn tại", HttpStatus.NOT_FOUND),
    POST_FORBIDDEN(2002, "Bạn không có quyền chỉnh sửa hoặc xóa bài viết này", HttpStatus.FORBIDDEN),
    POST_RATE_LIMITED(2003, "Bạn đăng bài quá nhanh. Vui lòng thử lại sau ít phút.", HttpStatus.TOO_MANY_REQUESTS),

    // 9000 - 9999: System Errors
    INTERNAL_SERVER_ERROR(9999, "Hệ thống gặp sự cố. Vui lòng thử lại sau.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
