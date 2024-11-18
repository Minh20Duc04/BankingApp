package com.BankingApplication.Service.ServiceImp;
import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Model.*;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Repository.TransactionRepository;
import com.BankingApplication.Util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee",
            "DONG", "VietNam Dong"
    );

    public Account createAccount(AccountDto accountDto, User user)
    {
        try
        {
            validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        }catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        long accountNumber;
        do {
            accountNumber = RandomUtil.generateRandom(10);
        }while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountName(user.getFirstName() + " " + user.getLastName())
                .accountNumber(accountNumber)
                .owner(user)
                .label(CURRENCIES.get(accountDto.getCode()))
                .code(accountDto.getCode())
                .balance(1000)
                .symbol(accountDto.getSymbol())
                .build();
        return accountRepository.save(account);
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) throws Exception {
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        var senderTransaction = Transaction.builder()
                .account(senderAccount)
                .txFee(amount * 0.01)
                .amount(amount)
                .status(Status.COMPLETED)
                .type(Type.WITHDRAW)
                .owner(senderAccount.getOwner())
                .build();

        var receiverTransaction = Transaction.builder()
                .account(receiverAccount)
                .amount(amount)
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .owner(receiverAccount.getOwner())
                .build();

        return transactionRepository.saveAll(List.of(senderTransaction, receiverTransaction)).get(0);
    }

    public void validateAccountNonExistsForUser(String code, String uId) throws Exception
    {
        if(accountRepository.existsByCodeAndOwnerUid(code, uId))
        {
            throw new Exception("Account of this type already exist for this user");
        }
    }

    //kiểm tra xem account người gửi có chính là user đăng ký account đó hay không
    public void validateAccountOwner(Account account, User user) throws OperationNotSupportedException
    {
        if(!account.getOwner().getUid().equals(user.getUid()))
        {
            throw new OperationNotSupportedException("Invalid account owner");
        }
    }

    //kiểm tra logic tiền muốn gửi và tiền thực tế
    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if(account.getBalance() < amount)
        {
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

























}
