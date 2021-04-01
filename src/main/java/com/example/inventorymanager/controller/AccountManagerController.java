package com.example.inventorymanager.controller;

import com.example.inventorymanager.dto.RequisitionDto;
import com.example.inventorymanager.dto.TransactionDto;
import com.example.inventorymanager.entity.Requisition;
import com.example.inventorymanager.entity.Transaction;
import com.example.inventorymanager.service.AccountManagerService;
import com.example.inventorymanager.service.DealerService;
import com.example.inventorymanager.service.EmailService;
import com.example.inventorymanager.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ac-manager")
public class AccountManagerController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    DealerService dealerService;
    @Autowired
    AccountManagerService acManagerService;
    @Autowired
    EmailService emailService;

    @GetMapping("/requisition")
    public String getRequisitionPage(Model model) {
        model.addAttribute("title", "Requisition Transaction Check");
        model.addAttribute("requisitionDtoList", this.getRequisitionDtoList());
        return "account-manager/requisition-list";
    }


    @GetMapping(value = "/payment-details/{requisitionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TransactionDto> getRequisitionPaymentDetails(@PathVariable("requisitionId") long requisitionId) {
        Requisition requisition = inventoryService.getRequisitionDetails(requisitionId);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : requisition.getTransactionList()) {
            TransactionDto transactionDto = new TransactionDto();
            BeanUtils.copyProperties(transaction, transactionDto);
            transactionDto.setRequisitionNo(transaction.getRequisition().getRequisitionNo());
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }


    @GetMapping("/payment-clearance/{requisitionId}")
    public String sendRequisitionToInventoryManager(@PathVariable(name = "requisitionId") long requisitionId,
                                                    RedirectAttributes redirectAttributes) {
        acManagerService.sendRequisitionToInventoryManager(requisitionId);
        redirectAttributes.addFlashAttribute("message", "Requisition Payment Clearance Send To Inventory Manager");
        return "redirect:/ac-manager/requisition";
    }


    @GetMapping("/email/{requisitionNo}")
    public String sendEmailToDealer(@PathVariable(name = "requisitionNo") String requisitionNo,
                                    RedirectAttributes redirectAttributes) {
        emailService.sendEmail("bssob029@gmail.com",
                "Due Payment for Requisition",
                "Dear Client please fullfill your payment for Requisition" + requisitionNo);
        redirectAttributes.addFlashAttribute("message", "Message Sent");
        return "redirect:/ac-manager/requisition";
    }

    /*-------------------------HELPER METHOD-----------------------------*/
    private List<RequisitionDto> getRequisitionDtoList() {
        List<Requisition> requisitionList = dealerService.getDealerRequisitionList("Send To Account Manager");
        List<RequisitionDto> requisitionDtoList = new ArrayList<>();
        for (Requisition requisition : requisitionList) {
            RequisitionDto requisitionDto = new RequisitionDto();
            BeanUtils.copyProperties(requisition, requisitionDto);
            requisitionDto.setDealer(requisition.getUser().getUsername());
            requisitionDtoList.add(requisitionDto);
        }
        return requisitionDtoList;
    }
}
