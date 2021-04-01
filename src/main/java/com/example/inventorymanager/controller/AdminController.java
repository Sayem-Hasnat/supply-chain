package com.example.inventorymanager.controller;

import com.example.inventorymanager.dto.ApplyDto;
import com.example.inventorymanager.dto.ItemDto;
import com.example.inventorymanager.entity.Apply;
import com.example.inventorymanager.entity.Item;
import com.example.inventorymanager.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;


    @GetMapping("/approval")
    public String getDealerApprovalPage(Model model) {
        model.addAttribute("title", "Dealer Approval");
        model.addAttribute("applyDtoList", this.getAllApplyList());
        return "admin/approval";
    }


    @GetMapping(value = "/get-apply-details/{applyId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApplyDto getApplyDetails(@PathVariable("applyId") long applyId) {
        Apply apply = adminService.getApplyDetails(applyId);
        ApplyDto applyDto = new ApplyDto();

        applyDto.setApplyImagePath("/Apply Photo/" + apply.getApplyId()+ "/" + apply.getNidTinImage());
        return applyDto;
    }

    @PostMapping(value = "/search-nid",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getNidDetails(@RequestBody String nidNo) {
       return new ResponseEntity<String>("/Apply Photo/" + nidNo+ "/" + nidNo+".jpg", HttpStatus.OK);
    }















    private List<ApplyDto> getAllApplyList() {
        List<Apply> applyList = adminService.getAllApplyList();
        List<ApplyDto> applyDtoList = new ArrayList<>();
        for (Apply apply : applyList) {
            ApplyDto applyDto = new ApplyDto();
            BeanUtils.copyProperties(apply, applyDto);
            applyDtoList.add(applyDto);
        }
        return applyDtoList;
    }
}
