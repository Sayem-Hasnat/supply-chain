package com.example.inventorymanager.service;

import com.example.inventorymanager.entity.*;
import com.example.inventorymanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DealerService {
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    WishlistRepo wishlistRepo;
    @Autowired
    ApplyRepo applyRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    RequisitionRepo requisitionRepo;



    public void itemOrder(Item item) {
        Item item1 = itemRepo.getOne(item.getItemId());
        Wishlist wishlist = new Wishlist();
        wishlist.setItem(item1);
        wishlist.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        wishlist.setOrderQuantity(item.getItemQuantity());
        wishlistRepo.save(wishlist);
    }

    public List<Wishlist> getAllWishlistProduct() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wishlistRepo.findAllByEnableTrueAndUser_UserId(user.getUserId());
    }

    public String saveApply(Apply apply) {

        if (applyRepo.findByUsername(apply.getUsername()) != null) {
            return "This Email Already Exist";
        } else {
            applyRepo.save(apply);
            return String.valueOf(applyRepo.findByUsername(apply.getUsername()).getApplyId());
        }
    }

    public String saveTransaction(Transaction transaction,double payable) {
        transactionRepo.save(transaction);
        Transaction transaction1 = transactionRepo.findByTransactionNo(transaction.getTransactionNo());
        List<Transaction> transactionList=new ArrayList<>();
        transactionList.add(transaction1);
        Requisition requisition = new Requisition();
        requisition.setTransactionList(transactionList);
        requisition.setPayable(payable);
        requisition.setPaid(transaction.getAmount());
        requisition.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        requisition.setProductList(this.getAllOrderedProductList());
        if ( payable- transaction.getAmount() <= 0) {
            requisition.setPaymentStatus("Payment Clear");
        } else {
            requisition.setPaymentStatus("Payment Not Clear");
        }
        requisitionRepo.save(requisition);
        return "Payment Successful";
    }

    public String saveDueTransaction(Transaction transaction) {
        Requisition requisition= this.getNonPaidRequisition();
        requisition.setPaid(requisition.getPaid()+ transaction.getAmount());
        if (requisition.getPayable()- requisition.getPaid() <= 0){
            requisition.setPaymentStatus("payment Clear");
        }
        transaction.setRequisition(requisition);

        transactionRepo.save(transaction);

        return "Due Payment Successfully Paid";
    }

    public Requisition getNonPaidRequisition() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return requisitionRepo.findByPaymentStatusAndUser_UserId("Payment Not Clear",user.getUserId());
    }

    public List<Requisition> allRequisitionList() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return requisitionRepo.findByUser_UserId(user.getUserId());
    }

    public List<Requisition> getDealerRequisitionList(String status) {
        return requisitionRepo.findAllByStatus(status);
    }

    public Transaction getTransactionDetails(long transactionId) {
        return transactionRepo.getOne(transactionId);
    }

    public Requisition getRequisitionDetails(long requisitionId) {
        return requisitionRepo.getOne(requisitionId);
    }

    /*-------------------------HELPER METHOD-----------------------------*/
    List<Wishlist> getAllOrderedProductList() {
        List<Wishlist> productList = this.getAllWishlistProduct();
        for (Wishlist w : productList) {
            w.setEnable(false);
        }
        return productList;
    }



}

