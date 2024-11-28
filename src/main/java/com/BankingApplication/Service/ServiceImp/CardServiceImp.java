package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Model.Card;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.Type;
import com.BankingApplication.Model.User;
import com.BankingApplication.Repository.CardRepository;
import com.BankingApplication.Service.CardService;
import com.BankingApplication.Service.TransactionService;
import com.BankingApplication.Util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional //đảm bảo rằng nếu xảy ra lỗi khi thao tác với database thì sẽ roll back lại
public class CardServiceImp implements CardService {

    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final TransactionService transactionService;

    @Override
    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow(()-> new IllegalArgumentException("Not found Card with userUid: " + user.getUid()));
    }

    //xây dựng Card từ tiền trong Account, nghĩa là trích 1 phần tiền trong Account ra để làm Card
    @Override
    public Card createCard(User user, double amount) throws Exception {
        if(amount < 2)
        {
            throw new IllegalArgumentException("Amount should be greater than $2");
        }
        if(!accountHelper.existByCodeAndOwnerUid("USD", user.getUid()))
        {
            throw new IllegalArgumentException("USD Account not found for thus user so Card can not be created");
        }
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        long cardNumber;
        do
        {
            cardNumber = generateCardNumber();
        } while(cardRepository.existsByCardNumber(cardNumber));

        Card card = Card.builder()
                .cardHolder(user.getUsername())
                .cardNumber(cardNumber)
                .exp(LocalDateTime.now().plusYears(3))
                .cvv(RandomUtil.generateRandom(3).toString())
                .balance(amount - 1)
                .owner(user)
                .build();
        cardRepository.save(card);

        //sau khi tạo Card sẽ có 3 Transaction xảy ra (Account chuyển tiền, Account mất phí tax, Card nhận được tiền từ Account)
        transactionService.createAccountTransaction(amount -1, usdAccount, Type.WITHDRAW, user, 0.00);
        transactionService.createAccountTransaction(1, usdAccount, Type.WITHDRAW, user, 0.00);
        transactionService.createCardTransaction(amount -1, card, Type.WITHDRAW, user, 0.00);
        accountHelper.save(usdAccount); //cập nhật lại tien72 trong Account sau khi trích ra làm Card
        return card;
    }

    @Override
    public Transaction creditCard(User user, double amount) {
       var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
       usdAccount.setBalance(usdAccount.getBalance() - amount);
       transactionService.createAccountTransaction(amount, usdAccount, Type.WITHDRAW, user, 0.00);
       Card card = user.getCard();
       card.setBalance(card.getBalance() + amount);
       cardRepository.save(card);
       return transactionService.createCardTransaction(amount,card,Type.CREDIT, user,0.00 );
    }

    @Override
    public Transaction debitCard(User user, double amount) {
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        transactionService.createAccountTransaction(amount, usdAccount, Type.DEPOSIT, user, 0.00);
        Card card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount,card,Type.DEBIT, user,0.00);
    }

    private long generateCardNumber()
    {
        return RandomUtil.generateRandom(16);
    }




}
