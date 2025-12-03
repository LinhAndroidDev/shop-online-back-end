package com.example.shop.controller;

import com.example.shop.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    /**
     * Trả về response thành công với dữ liệu
     * @param data Dữ liệu trả về
     * @param message Thông báo thành công
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> successWithMessageResponse(T data, String message) {
        BaseResponse<T> response = new BaseResponse<T>() {};
        response.setData(data);
        response.setMessage(message != null ? message : "Thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Trả về response thành công với dữ liệu (message mặc định)
     * @param data Dữ liệu trả về
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data, String message) {
        String msg;
        if (message == null || message.isEmpty()) {
            msg = "Thành công";
        } else {
            msg = message;
        }
        return successWithMessageResponse(data, msg);
    }

    /**
     * Trả về response thành công chỉ với message (không có dữ liệu)
     * @param message Thông báo thành công
     * @return ResponseEntity với status 200
     */
    protected ResponseEntity<BaseResponse<Void>> successResponse(String message) {
        BaseResponse<Void> response = new BaseResponse<Void>() {};
        response.setMessage(message != null ? message : "Thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    /**
     * Trả về response thất bại với message và status code
     * @param message Thông báo lỗi
     * @param statusCode Mã trạng thái HTTP
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status code tương ứng
     */
    protected <T> ResponseEntity<BaseResponse<T>> errorResponse(String message, HttpStatus statusCode) {
        BaseResponse<T> response = new BaseResponse<T>() {};
        response.setMessage(message != null ? message : "Đã xảy ra lỗi");
        response.setStatus(statusCode.value());
        return ResponseEntity.status(statusCode).body(response);
    }

    /**
     * Trả về response thất bại với message (status code mặc định 400)
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 400
     *//**/
    protected <T> ResponseEntity<BaseResponse<T>> errorResponse(String message) {
        return errorResponse(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trả về response thất bại 404 - Not Found
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 404
     */
    protected <T> ResponseEntity<BaseResponse<T>> notFoundResponse(String message) {
        return errorResponse(message != null ? message : "Không tìm thấy dữ liệu", HttpStatus.NOT_FOUND);
    }

    /**
     * Trả về response thất bại 500 - Internal Server Error
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 500
     */
    protected <T> ResponseEntity<BaseResponse<T>> internalErrorResponse(String message) {
        return errorResponse(message != null ? message : "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Trả về response thất bại 401 - Unauthorized
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 401
     */
    protected <T> ResponseEntity<BaseResponse<T>> unauthorizedResponse(String message) {
        return errorResponse(message != null ? message : "Không có quyền truy cập", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Trả về response thất bại 403 - Forbidden
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return ResponseEntity với status 403
     */
    protected <T> ResponseEntity<BaseResponse<T>> forbiddenResponse(String message) {
        return errorResponse(message != null ? message : "Bị cấm truy cập", HttpStatus.FORBIDDEN);
    }
}

