package sg.edu.nus.iss.day24.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day24.model.BankAccount;

@Repository
public class BankAccountRepo {
    @Autowired
    JdbcTemplate template;

    private final String CHECK_BALANCE_SQL = "select balance from bankaccount where id = ?";
    private final String GET_ACCOUNT_SQL = "select * from bankaccount where id = ?";
    private final String WITHDRAW_SQL = "update bankaccount set balance = balance - ? where id = ?";
    private final String DEPOSIT_SQL = "update bankaccount set balance = balance + ? where id = ?";
    private final String CREATE_SQL = "insert into bankaccount (full_name, is_active, acct_type, balance) values (?, ?, ?, ?)";

    public Boolean checkBalance(Integer accountId, Float withdrawnamount) {
        Boolean withdrawnBalanceAvailable = false;
        Float returnedBalance = template.queryForObject(CHECK_BALANCE_SQL, Float.class, accountId);

        if (withdrawnamount <= returnedBalance) {
            withdrawnBalanceAvailable = true;
        }

        return withdrawnBalanceAvailable;
    }

    public BankAccount retrieveAccountDetails(Integer accountId) {
        BankAccount bankAccount = null;

        bankAccount = template.queryForObject(GET_ACCOUNT_SQL, BeanPropertyRowMapper.newInstance(BankAccount.class), accountId);
        return bankAccount;
    }

    public Boolean withdrawAmount(Integer accountId, Float withdrawnAmount) {
        Boolean withdrawn = false;

        int updated = 0;
        updated = template.update(WITHDRAW_SQL, withdrawnAmount, accountId);

        if (updated > 0) {
            withdrawn = true;
        }

        return withdrawn;
    }

    public Boolean depositAmount(Integer accountId, Float depositedAmount) {
        Boolean deposited = false;

        int updated = 0;
        updated = template.update(DEPOSIT_SQL, depositedAmount, accountId);

        if (updated > 0) {
            deposited = true;
        }

        return deposited;
    }

    public Boolean createAccount(BankAccount bankAccount) {
        boolean created = false;

        created = template.execute(CREATE_SQL, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, bankAccount.getFullName());
                ps.setBoolean(2, bankAccount.getIsActive());
                ps.setString(3, bankAccount.getAcctType());
                ps.setFloat(4, bankAccount.getBalance());
                Boolean result = ps.execute();
                return result;
            }

        });
        return created;
    }
}
