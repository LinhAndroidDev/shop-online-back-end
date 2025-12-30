package com.example.shop.controller;

import com.example.shop.response.BaseResponse;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/upload")
public class UploadController extends BaseController {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @PostMapping
    public ResponseEntity<BaseResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        // Kiểm tra file trống
        if (file.isEmpty()) {
            return errorResponse( "File rỗng");
        }

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody fileBody = RequestBody.create(file.getBytes(),
                    MediaType.parse(Objects.requireNonNull(file.getContentType())));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Request request = new Request.Builder()
                    .url(supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName)
                    .post(fileBody)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("apikey", supabaseKey)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String publicUrl = supabaseUrl + "/storage/v1/object/public/" + supabaseBucket + "/" + fileName;
                return successResponse(publicUrl, "Upload thành công");
            } else {
                assert response.body() != null;
                return errorResponse(response.body().string());
            }
        } catch (HttpClientErrorException | IOException e) {
            return errorResponse(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(@RequestParam("fileName") String fileName) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Gửi DELETE request đến Supabase Storage
            Request request = new Request.Builder()
                    .url(supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName)
                    .delete()
                    .addHeader("Authorization", "Bearer " + supabaseKey) // Dùng service key
                    .addHeader("apikey", supabaseKey)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                return successResponse("Xóa thành công");
            } else {
                assert response.body() != null;
                return successResponse(response.body().string(), response.message());
            }
        } catch (IOException e) {
            return errorResponse(e.getMessage());
        }
    }
}
