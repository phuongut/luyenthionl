package com.example.postgresdemo.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.postgresdemo.dao.TongKetDAO;
import com.example.postgresdemo.model.KetQua;
import com.example.postgresdemo.model.Tongket;
import com.example.postgresdemo.service.KetQuaService;

@Controller
@RequestMapping("/api/screenshots")
public class ketQuaController {

    @Autowired
    private KetQuaService screenshotService;

    @Autowired
    TongKetDAO tkdao;

    @PostMapping("/")
    public ResponseEntity<String> updateScreenshot(
            @RequestBody Map<String, Object> screenshotData) {
        try {
            String imageData = (String) screenshotData.get("imageData");
            Integer tongketId = Integer.valueOf(screenshotData.get("tongketId").toString());

            // Lấy đối tượng Tongket từ cơ sở dữ liệu dựa trên tongketId
            Optional<Tongket> optionalTongket = tkdao.findById(tongketId);

            if (optionalTongket.isPresent()) {
                Tongket tongket = optionalTongket.get();

                // Thực hiện cập nhật ảnh và sử dụng tongketId khi cần
                screenshotService.updateScreenshot(tongket, imageData);

                // Không cần save lại, vì đối tượng đã được cập nhật trong updateScreenshot
                String imagePath = "/api/screenshots/" + tongket.getId();
                return ResponseEntity.ok("Screenshot updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tongket not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating screenshot");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getScreenshot(@PathVariable Integer id) {
        Tongket screenshot = screenshotService.getScreenshotById(id);
        if (screenshot != null) {
            ByteArrayResource resource = new ByteArrayResource(screenshot.getImageData());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + id + ".png")
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(screenshot.getImageData().length)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // @GetMapping("/{id}")
    // public String showScreenshot(@PathVariable Integer id, Model model) {
    // Tongket screenshot = screenshotService.getScreenshotById(id);
    // if (screenshot != null) {
    // model.addAttribute("screenshot", screenshot);
    // return "screenshot_detail";
    // } else {
    // return "error"; // Thay thế bằng trang lỗi hoặc trả về một mã lỗi khác
    // }
    // }

}
