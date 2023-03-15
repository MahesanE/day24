package sg.edu.nus.iss.day24.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    private Integer id;

    private String fullName;

    private Boolean isActive;

    private String acctType;
    
    private Float balance;
    
}
