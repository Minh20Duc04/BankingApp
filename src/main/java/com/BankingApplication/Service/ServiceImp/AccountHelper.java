package com.BankingApplication.Service.ServiceImp;
import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Dto.ConvertDto;
import com.BankingApplication.Exception.AccountNonExistsForUserException;
import com.BankingApplication.Exception.InsufficientFundsException;
import com.BankingApplication.Exception.SameCurrencyConversionException;
import com.BankingApplication.Model.*;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Service.TransactionService;
import com.BankingApplication.Util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee",
            "VND", "VietNam Dong"
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

        Account account = createNewAccount(accountDto, user, accountNumber);
        return accountRepository.save(account);
    }

    private Account createNewAccount(AccountDto accountDto, User user, long accountNumber) {
        return Account.builder()
                .accountName(user.getFirstName() + " " + user.getLastName())
                .accountNumber(accountNumber)
                .owner(user)
                .ownerEmail(user.getEmail())
                .label(CURRENCIES.get(accountDto.getCode()))
                .code(accountDto.getCode())
                .balance(1000)
                .symbol(accountDto.getSymbol())
                .build();
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) throws Exception {
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        var senderTransaction = transactionService.createAccountTransaction(amount, senderAccount, Type.WITHDRAW, user, amount * 0.01);
        var receiverTransaction = transactionService.createAccountTransaction(amount, receiverAccount, Type.DEPOSIT, receiverAccount.getOwner(), 0.00);

        return senderTransaction;
    }

    public void validateAccountNonExistsForUser(String code, String uId) throws Exception
    {
        if(accountRepository.existsByCodeAndOwnerUid(code, uId))
        {
            throw new AccountNonExistsForUserException("Account of this type already exist for this user");
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
    public void validateSufficientFunds(Account account, double amount) throws InsufficientFundsException {
        if(account.getBalance() < amount)
        {
            throw new InsufficientFundsException("Insufficient funds in the account");
        }
    }

    public void validateAmount(double amount) throws InsufficientFundsException
    {
        if(amount <= 0)
        {
            throw new InsufficientFundsException("Invalid amount");
        }
    }

    public void validateDifferentCurrencyType(ConvertDto convertDto) throws SameCurrencyConversionException
    {
        if(convertDto.getFromCurrency().equals(convertDto.getToCurrency()))
        {
            throw new SameCurrencyConversionException("Conversion between the same currency type is not allowed");
        }
    }

    //đảm bảo rằng việc đổi tiền phải từ cùng 1 User, ví dụ 1 người có 2 tk muốn chuyển tk chứa tiền Việt sang tk chứa tiền Đô thì
    public void validateAccountOwnership(ConvertDto convertDto, String uid) throws Exception
    {
        accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(convertDto.getFromCurrency(), uid).orElseThrow(()-> new AccountNonExistsForUserException("No account found with currency" + convertDto.getFromCurrency()));
        accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(convertDto.getToCurrency(), uid).orElseThrow(()-> new AccountNonExistsForUserException("No account found with currency" + convertDto.getToCurrency()));
    }

    public void validateConversion(ConvertDto convertDto, String uid) throws Exception
    {
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto, uid);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(convertDto.getFromCurrency(), uid).get(), convertDto.getAmount());
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        validateConversion(convertDto, user.getUid());
        var rates = exchangeRateService.getRates();
        var sendingRates = rates.get(convertDto.getFromCurrency());
        var receivingRates = rates.get(convertDto.getToCurrency());
        var computedAmount = (receivingRates / sendingRates) * convertDto.getAmount();

        Account fromAccount = accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(convertDto.getFromCurrency(), user.getUid()).orElseThrow();
        Account toAccount = accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(convertDto.getToCurrency(), user.getUid()).orElseThrow();

        fromAccount.setBalance(fromAccount.getBalance() - (convertDto.getAmount() * 1.01));
        toAccount.setBalance(toAccount.getBalance() + computedAmount);
        accountRepository.saveAll(List.of(fromAccount, toAccount));

        Transaction fromAccountTransaction = transactionService.createAccountTransaction(convertDto.getAmount(), fromAccount, Type.CONVERSION, user, convertDto.getAmount() * 0.01);
        Transaction toAccountTransaction = transactionService.createAccountTransaction(computedAmount, toAccount, Type.DEPOSIT, user, convertDto.getAmount() * 0.00);

        return fromAccountTransaction;
    }

    public boolean existByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
