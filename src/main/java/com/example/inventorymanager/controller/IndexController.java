package com.example.inventorymanager.controller;

import com.example.inventorymanager.dto.ApplyDto;
import com.example.inventorymanager.entity.Apply;
import com.example.inventorymanager.service.DealerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class IndexController {
    @Autowired
    DealerService dealerService;

    @GetMapping("/")
    public String getIndexPage() {
        return "/index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login-page";
    }

    @GetMapping("/get-dealer-reg")
    public String getRegPage(Model model) {
        model.addAttribute("applyDto", new ApplyDto());
        return "/reg";
    }


    @PostMapping("/apply")
    public String apply(@ModelAttribute ApplyDto applyDto,
                        RedirectAttributes redirectAttributes,
                        @RequestParam("nidTinImage") MultipartFile multipartFile) throws IOException {
        Apply apply = new Apply();
        BeanUtils.copyProperties(applyDto, apply);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        apply.setNidTinImage(fileName);
        String s = dealerService.saveApply(apply);

        if (s != "This Email Already Exist") {
            String uploadDirectory = "./Apply Photo/" + s;//Here s= Product id return after saving the product
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Fail to Save the image");
            }
            redirectAttributes.addFlashAttribute("message", "Apply Successfully");
        } else {
            redirectAttributes.addFlashAttribute("message", s);
        }

        return "redirect:/get-dealer-reg";
    }


}
