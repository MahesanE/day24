package sg.edu.nus.iss.day24.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.day24.payload.TransferRequest;
import sg.edu.nus.iss.day24.service.BankAccountService;

@Controller
@RequestMapping("/accounts")
public class BankAccountController {
    @Autowired
    BankAccountService bankAcctSvc;

    @PostMapping
    public ResponseEntity<Boolean> transferMoney(@RequestBody TransferRequest trasnferRequest) {
        Boolean transferred = false;

        transferred = bankAcctSvc.transferMoney(trasnferRequest.getAccountFrom(), trasnferRequest.getAccountTo(),
                trasnferRequest.getAmount());
                if(transferred){
                    return new ResponseEntity<Boolean>(transferred, HttpStatus.OK);
                } else {
                    return new ResponseEntity<Boolean>(transferred, HttpStatus.BAD_REQUEST);
                }
    }
}
