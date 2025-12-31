package com.example.shop.service;

import com.example.shop.controller.UploadController;
import com.example.shop.entity.ProductImage;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    @Autowired
    private UploadController uploadController;

    public void deleteImagesAsync(List<ProductImage> images) {
        Flowable.fromIterable(images)
                .flatMap(
                        image -> Completable.fromAction(() ->
                                        uploadController.deleteImage(image.getUrl())
                                )
                                .subscribeOn(Schedulers.io())
                                .toFlowable(),
                        5
                )
                .ignoreElements();
    }
}
