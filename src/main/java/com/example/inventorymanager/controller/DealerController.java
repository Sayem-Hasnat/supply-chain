package com.example.inventorymanager.controller;

import com.example.inventorymanager.dto.*;
import com.example.inventorymanager.entity.*;
import com.example.inventorymanager.service.DealerService;
import com.example.inventorymanager.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dealer")
public class DealerController {

    @Autowired
    InventoryService inventoryService;
    @Autowired
    DealerService dealerService;

    @GetMapping("/requisition")
    public String getRequisitionPage(Model model) {
        model.addAttribute("title", "Product Requisition");
        model.addAttribute("categoryDtoList", this.getAllCategory());
        return "dealer/requisition";
    }

    @GetMapping(value = "/get-item-list/{categoryId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ItemDto> getItemsByCategory(@PathVariable("categoryId") long categoryId) {
        Category category = inventoryService.getCategory(categoryId);
        return getCategoryDto(category).getItemDtoList();
    }

    @GetMapping(value = "/get-item-details/{itemId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ItemDto getItemDetails(@PathVariable("itemId") long itemId) {
        Item item = inventoryService.getItemDetails(itemId);
        ItemDto itemDto = new ItemDto();
        BeanUtils.copyProperties(item, itemDto);
        itemDto.setItemImagePath("/ProductPhoto/" + itemDto.getItemId() + "/" + itemDto.getItemImage());
        return itemDto;
    }

    @PostMapping(value = "/product-order",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String itemOrder(@RequestBody ItemDto itemDto) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        dealerService.itemOrder(item);
        return "redirect:/dealer/requisition";
    }

    @GetMapping("/wishlist")
    public String getWishlistPage(Model model) {
        model.addAttribute("title", "Product Requisition Wishlist");
        model.addAttribute("wishlistDtoList", this.getAllWishlistProduct());
        model.addAttribute("totalPrice", this.countTotalPayablePrice());
        return "dealer/wishlist";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, RedirectAttributes redirectAttributes) {
        if (dealerService.getNonPaidRequisition() != null) {
            redirectAttributes.addFlashAttribute("message", "Previous Requisition Payment Not Clear");
            return "redirect:/dealer/wishlist";
        } else {
            model.addAttribute("title", "Checkout");
            TransactionDto transactionDto = new TransactionDto();
            model.addAttribute("payableAmount", this.countTotalPayablePrice());
            model.addAttribute("transactionDto", transactionDto);
            return "dealer/checkout";
        }
    }

    @GetMapping("/due-payment/{requisitionId}")
    public String getDueCheckoutPage(@PathVariable(name = "requisitionId") long requisitionId, Model model) {
        model.addAttribute("title", "Checkout");
        Requisition requisition = dealerService.getRequisitionDetails(requisitionId);
        model.addAttribute("payableAmount", requisition.getPayable() - requisition.getPaid());
        model.addAttribute("transactionDto", new TransactionDto());
        return "dealer/checkout";
    }


    @GetMapping("/requisition-list")
    public String getRequisitionListPage(Model model) {
        model.addAttribute("title", "Requisition List");
        model.addAttribute("requisitionDtoList", this.getAlRequisitionDtoList());

        return "dealer/requisition-list";
    }


    @PostMapping("/payment")
    public String saveTransaction(@ModelAttribute TransactionDto transactionDto, RedirectAttributes redirectAttributes) {
        String message;
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDto, transaction);
        if (dealerService.getNonPaidRequisition() != null) {
            message = dealerService.saveDueTransaction(transaction);
        } else {
             message = dealerService.saveTransaction(transaction, this.countTotalPayablePrice());
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/dealer/requisition-list";
    }

    @GetMapping(value = "/payment-details/{requisitionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TransactionDto> getRequisitionPaymentDetails(@PathVariable("requisitionId") long requisitionId) {
        Requisition requisition = inventoryService.getRequisitionDetails(requisitionId);
        List<TransactionDto> transactionDtoList=new ArrayList<>();
        for (Transaction transaction:requisition.getTransactionList()) {
            TransactionDto transactionDto=new TransactionDto();
            BeanUtils.copyProperties(transaction,transactionDto);
            transactionDto.setRequisitionNo(transaction.getRequisition().getRequisitionNo());
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    /*-----------------------HELPER METHOD---------------------*/
    private List<Category> getAllCategory() {
        return inventoryService.getAllCategory();
    }

    private List<WishlistDto> getAllWishlistProduct() {
        List<Wishlist> wishlists = dealerService.getAllWishlistProduct();
        List<WishlistDto> wishlistDtos = new ArrayList<>();
        for (Wishlist w : wishlists) {
            WishlistDto wishlistDto = new WishlistDto();
            BeanUtils.copyProperties(w, wishlistDto);
            wishlistDto.setItem(w.getItem());
            wishlistDto.setOrderQuantityPrice(w.getOrderQuantity() * w.getItem().getPricePerCarton());

            wishlistDtos.add(wishlistDto);
        }
        return wishlistDtos;
    }

    private CategoryDto getCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);

        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : category.getItemList()) {
            ItemDto itemDto = new ItemDto();
            BeanUtils.copyProperties(item, itemDto);
            itemDtoList.add(itemDto);
        }
        categoryDto.setItemDtoList(itemDtoList);

        return categoryDto;
    }

    private double countTotalPayablePrice() {
        List<Wishlist> wishlists = dealerService.getAllWishlistProduct();
        double totalPrice = 0;
        for (Wishlist w : wishlists) {
            totalPrice += w.getOrderQuantity() * w.getItem().getPricePerCarton();
        }
        return totalPrice;
    }

    private List<RequisitionDto> getAlRequisitionDtoList() {
        List<Requisition> requisitionList = dealerService.allRequisitionList();
        List<RequisitionDto> requisitionDtoList = new ArrayList<>();
        for (Requisition requisition : requisitionList) {
            RequisitionDto requisitionDto = new RequisitionDto();
            BeanUtils.copyProperties(requisition, requisitionDto);
            requisitionDtoList.add(requisitionDto);
        }
        return requisitionDtoList;
    }
}
