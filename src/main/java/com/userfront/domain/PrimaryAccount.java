package com.userfront.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*************** HIBERNATE *******************
 * 
 *  Hibernate will help you to create Table
 *  But not about creating Schema
 *  
 **********************************************/
@Entity
public class PrimaryAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int accountNumber;
    private BigDecimal accountBalance;

    @OneToMany(mappedBy = "primaryAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)    // One PrimaryAccount can have multiple primaryTransactionList - Relatonship between PrimaryAccount and TransactionList is OneToMany     
    @JsonIgnore																					  // mappedBy = "primaryAccount" ovo ce biti PrimaryAcc type- koji ce da popuni listu primaryTransactionList
    private List<PrimaryTransaction> primaryTransactionList;									  // cascade = CascadeType.ALL je koja god akcija da se update-tuje ovde u ovom slucaju ce biti PrimaryAcc
    																							  // FetchType.LAZY this LAZY will boost the prefomance if you have a Large TransactionList - Every time when you instated PrimaryAccount the corensponding PrimaryTransaction will be loaded
    public Long getId() {																		  // @JsonIgnore exmp: U primaryTransactionList imamo polje PrimaryAccount - Ako bi provali da seralizujemo ovaj Json objekat dobili bi beskonacnu petlju. Ovo nece raditi ako budemo pokusali da vratimo kao JSON object
        return id;																				  // Znaci kada budemo zelili da vratimo ovo kao objekat moracemo celu listu(polje) da ignorisemo
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<PrimaryTransaction> getPrimaryTransactionList() {
        return primaryTransactionList;
    }

    public void setPrimaryTransactionList(List<PrimaryTransaction> primaryTransactionList) {
        this.primaryTransactionList = primaryTransactionList;
    }


}



