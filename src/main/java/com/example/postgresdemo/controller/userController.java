package com.example.postgresdemo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.postgresdemo.dao.NguoiDungDAO;
import com.example.postgresdemo.dao.TongKetDAO;
import com.example.postgresdemo.dao.BoDeDAO;
import com.example.postgresdemo.dao.MonHocDAO;
import com.example.postgresdemo.dao.NguoiDungDAO;
import com.example.postgresdemo.model.BoDe;
import com.example.postgresdemo.model.MonHoc;

import com.example.postgresdemo.model.NguoiDung;
import com.example.postgresdemo.model.Tongket;
import com.example.postgresdemo.model.count;

@Controller
public class userController {

    @Autowired
    NguoiDungDAO ndd;

    @Autowired
    MonHocDAO mhdao;

    @Autowired
    BoDeDAO bddao;

    @Autowired
    TongKetDAO tkdao;
    @Autowired
    NguoiDung nd;

    private void addMonHocListToModel(Model model) {
        List<MonHoc> monHocList = mhdao.findAll();
        model.addAttribute("monHocList", monHocList);
    }

    @GetMapping("/qlthongtin")
    public String showUserInfo(Model model, HttpSession session) {
        addMonHocListToModel(model);

        NguoiDung user = (NguoiDung) session.getAttribute("nguoidung");
        if (user == null) {
            // Lấy user id từ session hoặc từ bất kỳ nguồn dữ liệu nào khác
            String userId = (String) session.getAttribute("idND");
            if (userId != null) {
                user = ndd.findById(userId).orElse(null);
            }
        }
        if (user != null) {
            model.addAttribute("user", user);
            return "qlThongTin";
        } else {
            return "redirect:/login";
        }
    }

    // @PostMapping("/qlthongtin/update")
    // public String update(NguoiDung item, HttpSession session) {
    // String images = item.getImages();
    // // Nếu tên tệp tin mới không rỗng, cập nhật giá trị trong cơ sở dữ liệu
    // if (images != null && !images.isEmpty()) {
    // item.setImages(images);
    // }

    // ndd.save(item);
    // session.setAttribute("nguoidung", item);
    // session.setAttribute("user", item.getHoTen());
    // return "redirect:/qlthongtin";
    // }
    @PostMapping("/qlthongtin/update")
    public String update(@ModelAttribute("user") NguoiDung item,
            HttpSession session) {
        MultipartFile imagesFile = item.getImagesFile();
        // Xử lý tệp tin ảnh và lưu trữ tên vào đối tượng NguoiDung
        if (imagesFile != null && !imagesFile.isEmpty()) {
            try {
                String imageName = imagesFile.getOriginalFilename();
                // Lưu tên tệp tin vào trường images của đối tượng NguoiDung
                item.setImages(imageName);
                // Lưu tệp tin vào thư mục images (nếu cần)
                // ...
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý nếu có lỗi khi xử lý tệp tin
            }
        }

        // Lưu cập nhật vào cơ sở dữ liệu
        ndd.save(item);

        // Lưu thông tin người dùng vào session
        session.setAttribute("nguoidung", item);
        session.setAttribute("user", item.getHoTen());

        return "redirect:/qlthongtin";
    }

    @RequestMapping("/qldiemuser")
    public String qldiem1(Model model, HttpSession session) {
        List<MonHoc> subjectList = mhdao.findAll();
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("selectedSubject", "Hóa");
        List<BoDe> deList = bddao.findAll();
        model.addAttribute("deList", deList);
        model.addAttribute("selectedDe", "");
        NguoiDung user = (NguoiDung) session.getAttribute("nguoidung");
        String iduser = user.getIdND();

        // Lấy danh sách điểm của người dùng đang đăng nhập
        List<Tongket> diemuserList = tkdao.findByNguoiDungIdND(iduser);

        // Truyền danh sách điểm vào Model để hiển thị trên trang qlDiemUser
        model.addAttribute("diemuserList", diemuserList);
        addMonHocListToModel(model);
        return "qlDiemUser";
    }

    @PostMapping("/saveTongket")
    public ResponseEntity<Integer> saveTongket(@RequestBody java.util.Map<String, String> diemMap,
            HttpSession session) {
        String diemString = diemMap.get("diem");

        // Kiểm tra nếu diemString là null hoặc rỗng, thì gán giá trị mặc định
        if (diemString == null || diemString.isEmpty()) {
            diemString = "0";
        }
        String tenBoDe = diemMap.get("tenBoDe");

        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoidung");

        // Tạo đối tượng Tongket và set giá trị điểm
        Tongket tongket = new Tongket();
        tongket.setDiem(diemString);
        tongket.setNguoiDung(nguoiDung);
        tongket.setTenDe(tenBoDe);
        tkdao.save(tongket);
        Integer generatedId = tongket.getId();

        // Thực hiện lưu trữ dữ liệu vào cơ sở dữ liệu ở đây
        // Ví dụ: sử dụng Hibernate để lưu vào database
        // ...

        // Trả về một thông báo thành công hoặc thất bại
        return ResponseEntity.ok(generatedId);
    }

}
