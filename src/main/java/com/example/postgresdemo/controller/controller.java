package com.example.postgresdemo.controller;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.postgresdemo.dao.BoDeDAO;
import com.example.postgresdemo.dao.MonHocDAO;
import com.example.postgresdemo.dao.NguoiDungDAO;
import com.example.postgresdemo.dao.NguoiDungService;
import com.example.postgresdemo.dao.SessionService;
import com.example.postgresdemo.model.BoDe;
import com.example.postgresdemo.model.MonHoc;
import com.example.postgresdemo.model.NguoiDung;

@Controller
public class controller {
    @Autowired
    SessionService session;

    private final NguoiDungService nguoiDungService;

    @Autowired
    public controller(NguoiDungService nguoiDungService) {
        this.nguoiDungService = nguoiDungService;
    }

    @Autowired
    NguoiDung nd;

    @Autowired
    MonHocDAO mhdao;

    @Autowired
    BoDeDAO bddao;

    @Autowired
    NguoiDungDAO nddao;
    @Autowired
    BoDe bd;

    @Autowired
    JavaMailSender javaMailSender;

    @RequestMapping("/quizls")
    public String quizls() {
        return "quizls";
    }

    @RequestMapping("/quiz")
    public String quiz(Model model, HttpSession session) {
        String username = (String) session.getAttribute("user");

        if (username != null) {
            addMonHocListToModel(model);
            return "quiz";
        } else {
            model.addAttribute("error", "Đăng nhập để làm bài thi");
            return "login";
        }
    }

    private void addMonHocListToModel(Model model) {
        List<MonHoc> monHocList = mhdao.findAll();
        model.addAttribute("monHocList", monHocList);
    }

    @RequestMapping("/bodethi/{idMon}")
    public String showBoDeThi(@PathVariable String idMon, Model model) {
        List<BoDe> boDeList = bddao.findByMonHoc_Id(idMon);
        model.addAttribute("boDeList", boDeList);
        addMonHocListToModel(model);
        return "dethi";
    }

    @RequestMapping("/search")
    public String searchAndPage(Model model,
            @RequestParam("keywords") Optional<String> kw) {
        String kwords = kw.orElse(session.get("keywords", ""));
        session.set("keywords", kwords);
        // Pageable pageable = PageRequest.of(p.orElse(0), 6);
        List<BoDe> items = bddao.findAllByTenDeLike("%" + kwords + "%");
        model.addAttribute("search", items);
        addMonHocListToModel(model);
        return "dethi";
    }

    @RequestMapping("/index")
    public String index(Model model) {

        addMonHocListToModel(model);
        return "index";
    }

    @RequestMapping("/xacnhan")
    public String showBatdauthi(Model model) {

        return "xacNhan";
    }

    // Search and Pagination

    @RequestMapping("/dethi")
    public String showlistdethi(Model model) {
        List<BoDe> boDeList = bddao.findAll();
        model.addAttribute("boDeList", boDeList);
        addMonHocListToModel(model);
        return "dethi";
    }

    // @RequestMapping("/bodethi")
    // public String bodethi() {
    // return "bodethi";
    // }

    @RequestMapping("/indexAd")
    public String index1() {
        return "indexAd";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/dangxuat")
    public String logout(HttpSession session) {
        session.removeAttribute("tenNguoiDung");
        return "login";
    }

    @RequestMapping("/lienhe")
    public String lienhe() {
        return "lienhe";
    }

    @RequestMapping("/gioithieu")
    public String about() {
        return "gioithieu";
    }

    @RequestMapping("/gopy")
    public String gopy() {
        return "gopy";
    }

    @PostMapping("/login")
    public String session(@RequestParam("emailOrId") String emailOrId,
            @RequestParam("matKhau") String matKhau,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Model model) {

        NguoiDung user = nddao.findByEmailOrIdND(emailOrId, emailOrId);

        if (user != null && user.getMatKhau().equals(matKhau)) {

            session.setAttribute("user", user.getHoTen());
            session.setAttribute("nguoidung", user);
            if (user.isAdmin()) {
                return "redirect:/indexAd";
            } else {
                return "redirect:/index";
            }
        } else {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng");
            return "login";
        }
    }

    @GetMapping("/dangky")
    public String dangky() {
        return "register";
    }

    @PostMapping("/dangky")
    public String processRegistration(@RequestParam String idND, @RequestParam String rematKhau,
            @RequestParam String sdt,
            @RequestParam String hoTen, @RequestParam String email, @RequestParam String matKhau, Model model) {
        if (nddao.existsByEmail(email)) {
            model.addAttribute("error", "Email này đã tồn tại");
            return "register";
        } else {
            if (nddao.existsById(idND)) {
                model.addAttribute("error", "Tên người dùng này đã tồn tại");
                return "register";
            } else {

                if (matKhau.equals(rematKhau)) {
                    NguoiDung newUser = new NguoiDung();
                    newUser.setHoTen(hoTen);
                    newUser.setIdND(idND);
                    newUser.setEmail(email);
                    newUser.setMatKhau(matKhau);
                    newUser.setSoDienThoai(sdt);
                    nddao.save(newUser);
                    return "redirect:/login"; // Chuyển hướng về trang đăng nhập sau khi đăng ký thành công
                } else {
                    model.addAttribute("error", "Mật khẩu không trùng");
                    return "register";
                }
            }
        }
    }

    // forgot pass
    @GetMapping("/quenmatkhau")
    public String change(Model model) {
        return "quenMatKhau";
    }

    private void send(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        javaMailSender.send(message);
    }

    private String generateRandomCode() {
        // Sử dụng SecureRandom để tạo số ngẫu nhiên
        SecureRandom random = new SecureRandom();

        // Tạo một số ngẫu nhiên có độ dài là 6
        int randomCode = 100000 + random.nextInt(900000);

        return String.valueOf(randomCode);
    }

    String randomCode = generateRandomCode();

    @PostMapping("/quenmatkhau")
    public String forgotPassword(@RequestParam("email") String email, HttpServletRequest request, Model model) {
        NguoiDung foundAccount = nddao.findByEmail(email);
        if (foundAccount != null) {
            try {
                // Tạo mã ngẫu nhiên

                // Đặt chủ đề và nội dung của email
                String subject = "Quên mật khẩu";
                String body = "Mã xác nhận của bạn là: " + randomCode;

                // Gửi email
                send(email, subject, body);
                request.getSession().setAttribute("userId", foundAccount.getIdND());
            } catch (Exception e) {
                e.printStackTrace(); // Xử lý lỗi gửi email
            }
            request.getSession().setAttribute("userEmail", email);
            model.addAttribute("message", "Gửi mã thành công");
            return "result";
        } else {
            model.addAttribute("message", "Không tìm thấy địa chỉ email trong hệ thống.");
            return "quenmatkhau";
        }

    }

    @PostMapping("/optcode")
    public String handleVerificationCode(@RequestParam("optcode") String verificationCode, HttpServletRequest request,
            Model model) {
        // Kiểm tra xem mã nhập từ form có trùng với mã đã gửi đi hay không
        String userEmail = (String) request.getSession().getAttribute("userEmail");
        String userId = (String) request.getSession().getAttribute("userId");

        if (verificationCode.equals(randomCode)) {

            model.addAttribute("userEmail", userEmail);
            model.addAttribute("userId", userId);
            model.addAttribute("message", "Xác nhận thành công!");

            return "redirect:/doimatkhau"; // Chuyển hướng
        } else {

            model.addAttribute("message", "Mã xác nhận không đúng. Vui lòng thử lại.");

            return "result"; // Hoặc trang nào bạn muốn quay lại để nhập lại mã
        }
    }

    @GetMapping("/doimatkhau")
    public String dmk() {
        return "doimatkhau";
    }

    @PostMapping("/doimatkhau")
    public String doiMatKhau(@RequestParam("mk") String newPassword,
            HttpServletRequest request, Model model) {
        String userId = (String) request.getSession().getAttribute("userId");
        nguoiDungService.updatePassword(userId, newPassword);

        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "login";
    }

}
