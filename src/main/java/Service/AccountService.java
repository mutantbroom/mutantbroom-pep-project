package Service;


import DAO.accountDAO;
import Model.Account;
import java.util.List;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }
    public Account registerAccount(Account account) {
        List<Account> accounts = accountDAO.getAllAccounts();
        if(!account.getUsername().isEmpty() && account.getPassword().length() >=4){
            for(Account ac : accounts){
                if(ac.getUsername().equals(account.getUsername())){
                    return null;
                }
            }
            return accountDAO.addAccount(account);
        }
        return null;
    }
    public Account login(Account account) {
        List<Account> accounts = accountDAO.getAllAccounts();
        for(Account ac : accounts){
            if(ac.getUsername().equals(account.getUsername()) && ac.getPassword().equals(account.getPassword())){
                return accountDAO.login(ac);
            }
        }
        return null;
    }
}