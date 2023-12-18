package com.example.postgresdemo.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.postgresdemo.dao.TongKetDAO;
import com.example.postgresdemo.model.Tongket;

@Service
public class KetQuaService {

    @Autowired
    private TongKetDAO screenshotRepository;

    public Tongket getScreenshotById(Integer id) {
        return screenshotRepository.findById(id).orElse(null);
    }

    public Tongket saveScreenshot(String imageData, Integer tongketId) {
        Tongket screenshot = new Tongket();

        try {
            byte[] imageDataBytes = Base64.getDecoder().decode(imageData.split(",")[1]);
            screenshot.setImageData(imageDataBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid Base64 string");
        }

        return screenshotRepository.save(screenshot);
    }

    public Tongket updateScreenshot(Tongket tongket, String imageData) {
        try {
            byte[] imageDataBytes = Base64.getDecoder().decode(imageData.split(",")[1]);
            tongket.setImageData(imageDataBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid Base64 string");
        }

        return screenshotRepository.save(tongket);
    }
}
