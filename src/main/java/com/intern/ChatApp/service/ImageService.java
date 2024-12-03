package com.intern.ChatApp.service;


public interface ImageService {
    String saveImageFromBase64(String base64Image);
    String saveFileFromBase64(String base64Data);
}
