package com.example.inventorymanager.controller;

import com.example.inventorymanager.dto.CategoryDto;
import com.example.inventorymanager.dto.ItemDto;
import com.example.inventorymanager.dto.RequisitionDto;
import com.example.inventorymanager.dto.WishlistDto;
import com.example.inventorymanager.entity.Category;
import com.example.inventorymanager.entity.Item;
import com.example.inventorymanager.entity.Requisition;
import com.example.inventorymanager.entity.Wishlist;
import com.example.inventorymanager.service.DealerService;
import com.example.inventorymanager.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    DealerService dealerService;

    @GetMapping("/category")
    public String getCategoryAddPage(Model model) {
        model.addAttribute("title", "Category");
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("categoryDtoList", this.getAllCategory());
        return "inventory/category-setup";
    }

    @PostMapping("/save-category")
    public String saveCategory(@ModelAttribute CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        inventoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("message", "Category Save Successfully");
        return "redirect:/inventory/category";
    }

    @GetMapping("/item")
    public String getItemAddPage(Model model) {
        model.addAttribute("title", "Product / Item");
        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("categoryDtoList", this.getAllCategory());
        return "inventory/item-setup";
    }

    @PostMapping("/save-item")
    public String saveProduct(@ModelAttribute ItemDto itemDto,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("productImage") MultipartFile multipartFile) throws IOException {

        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        item.setCategory(inventoryService.getCategory(itemDto.getCategoryId()));

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        item.setItemImage(fileName);
        String s = inventoryService.saveItem(item);

        if (s != "Product Already Exist") {
            String uploadDirectory = "./ProductPhoto/" + s;//Here s= Product id return after saving the product
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
            redirectAttributes.addFlashAttribute("message", "Item Save Successfully");
        } else {
            redirectAttributes.addFlashAttribute("message", s);
        }
        return "redirect:/inventory/item";
    }


    @GetMapping("/stock")
    public String getItemStockIn(Model model) {
        model.addAttribute("title", "Product Stock In");
        model.addAttribute("categoryDtoList", this.getAllCategory());
        return "inventory/item-stock";

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
       Item item= inventoryService.getItemDetails(itemId);
       ItemDto itemDto=new ItemDto();
       BeanUtils.copyProperties(item,itemDto);
       itemDto.setItemImagePath("/ProductPhoto/" + itemDto.getItemId() + "/" + itemDto.getItemImage());
       return itemDto;
    }

    @RequestMapping(value = "/product-stock-in", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int updateItemStockQuantity(@RequestBody ItemDto itemDto) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        int quantity = inventoryService.updateItemStockQuantity(item);
        return quantity;
    }

    @GetMapping("/requisition")
    public String getRequisitionPage(Model model) {
        model.addAttribute("title", "Dealer Requisition");
        model.addAttribute("requisitionDtoList", this.getRequisitionDtoList("Sent To Inventory Manager"));
        return "inventory/requisition-list";
    }



    @GetMapping(value = "/get-product-list/{requisitionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<WishlistDto> getOrderedProduct(@PathVariable("requisitionId") long requisitionId) {
        Requisition requisition = inventoryService.getRequisitionDetails(requisitionId);
        List<WishlistDto> wishlistDtoList=new ArrayList<>();
        for (Wishlist wishlist:requisition.getProductList()) {
        WishlistDto wishlistDto=new WishlistDto();
        wishlistDto.setItemName(wishlist.getItem().getItemName());
        wishlistDto.setOrderQuantityPrice(wishlist.getOrderQuantity());
        wishlistDtoList.add(wishlistDto);
        }
        return wishlistDtoList;
    }

    @GetMapping("/check-payment/{requisitionId}")
    public String sendRequisitionToAccountManager(@PathVariable(name = "requisitionId") long requisitionId,
                                                  RedirectAttributes redirectAttributes) {
        inventoryService.sendRequisitionToAccountManager(requisitionId);
        redirectAttributes.addFlashAttribute("message","Requisition Details Send To Account Manager For Payment Clearance");
        return "redirect:/inventory/requisition";
    }
    @GetMapping("/confirm-requisition")
    public String getConfirmRequisitions(Model model) {
        model.addAttribute("title", "Confirm Requisition");
        model.addAttribute("requisitionDtoList", this.getRequisitionDtoList("Ready To Delivery"));
        return "inventory/confirm-requisition-list";
    }


    @GetMapping("/confirm/{requisitionId}")
    public String sendRequisitionToDeliveryIncharge(@PathVariable(name = "requisitionId") long requisitionId,
                                                  RedirectAttributes redirectAttributes) {
        inventoryService.sendRequisitionToDeliveryIncharge(requisitionId);
        redirectAttributes.addFlashAttribute("message","Requisition Details Send To Delivery In-Charge For Delivery");
        return "redirect:/inventory/confirm-requisition";
    }





    /*-----------------------HELPER METHOD---------------------*/
    private List<Category> getAllCategory() {
        return inventoryService.getAllCategory();
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

    private List<RequisitionDto> getRequisitionDtoList(String status) {
        List<Requisition> requisitionList=dealerService.getDealerRequisitionList(status);
        List<RequisitionDto> requisitionDtoList=new ArrayList<>();
        for (Requisition requisition:requisitionList) {
            RequisitionDto requisitionDto=new RequisitionDto();
            BeanUtils.copyProperties(requisition,requisitionDto);
            requisitionDto.setDealer(requisition.getUser().getUsername());
            requisitionDtoList.add(requisitionDto);
        }
        return requisitionDtoList;
    }
}
