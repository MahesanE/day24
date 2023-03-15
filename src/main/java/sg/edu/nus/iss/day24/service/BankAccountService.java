package sg.edu.nus.iss.day24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.day24.model.BankAccount;
import sg.edu.nus.iss.day24.repository.BankAccountRepo;

@Service
public class BankAccountService {
    @Autowired
    BankAccountRepo bankAcctRepo;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Boolean transferMoney(Integer accountFrom, Integer accountTo, Float amount) {
        Boolean transferred = false;
        Boolean withdrawn = false;
        Boolean deposited = false;
        
        Boolean depositedAccountValid = false;
        Boolean withdrawAccountValid = false;
        Boolean proceed = false;

        BankAccount depositAccount = null;
        BankAccount withdrawalAccount = null;

        // check if accounts(withdrawn and deposited) are valid (account is active)
        withdrawalAccount = bankAcctRepo.retrieveAccountDetails(accountFrom);
        depositAccount = bankAcctRepo.retrieveAccountDetails(accountTo);
        withdrawAccountValid = withdrawalAccount.getIsActive();
        depositedAccountValid = depositAccount.getIsActive();
        if (withdrawAccountValid && depositedAccountValid)
            proceed = true;

        if (proceed) {
            // check if withdrawal account has more money than withdrawal amount
            if (withdrawalAccount.getBalance() < amount) {
                proceed = false;
            } 
        }

        if (proceed) {
            // perform the withdrawal (requires Transaction)
            withdrawn = bankAcctRepo.withdrawAmount(accountFrom, amount);

            if(!withdrawn){
                throw new IllegalArgumentException("Simulate error before Withdrawal");
            }

            // perform the deposit (requires Transaction)
            deposited = bankAcctRepo.depositAmount(accountTo, amount);
            
            if(!deposited){
                throw new IllegalArgumentException("Simulate error before Deposit");
            }
        }
        // check transaction successful
        if (withdrawn && deposited)
            transferred = true;


        return transferred;
    }
}
