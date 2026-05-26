package jp.co.metateam.library.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.repository.AccountRepository;
import jp.co.metateam.library.repository.RentalManageRepository;
import jp.co.metateam.library.repository.StockRepository;
import jp.co.metateam.library.values.RentalStatus;

@Service
public class RentalManageService {

    private final AccountRepository accountRepository;
    private final RentalManageRepository rentalManageRepository;
    private final StockRepository stockRepository;

     @Autowired
    public RentalManageService(
        AccountRepository accountRepository,
        RentalManageRepository rentalManageRepository,
        StockRepository stockRepository
    ) {
        this.accountRepository = accountRepository;
        this.rentalManageRepository = rentalManageRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public List <RentalManage> findAll() {
        List <RentalManage> rentalManageList = this.rentalManageRepository.findAll();

        return rentalManageList;
    }

    @Transactional
    public RentalManage findById(Long id) {
        return this.rentalManageRepository.findById(id).orElse(null);
    }

    @Transactional 
    public void save(RentalManageDto rentalManageDto) throws Exception {
        try {
            Account account = this.accountRepository.findByEmployeeId(rentalManageDto.getEmployeeId()).orElse(null);
            if (account == null) {
                throw new Exception("Account not found.");
            }

            Stock stock = this.stockRepository.findById(rentalManageDto.getStockId()).orElse(null);
            if (stock == null) {
                throw new Exception("Stock not found.");
            }

            RentalManage rentalManage = new RentalManage();
            rentalManage = setRentalStatusDate(rentalManage, rentalManageDto.getStatus());

            rentalManage.setAccount(account);
            rentalManage.setExpectedRentalOn(rentalManageDto.getExpectedRentalOn());
            rentalManage.setExpectedReturnOn(rentalManageDto.getExpectedReturnOn());
            rentalManage.setStatus(rentalManageDto.getStatus());
            rentalManage.setStock(stock);

            // データベースへの保存
            this.rentalManageRepository.save(rentalManage);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, String> check(RentalManageDto rentalManageDto){

        Map<String, String> errors = new HashMap<>();

        Date rentalDate = rentalManageDto.getExpectedRentalOn();   // 貸出予定日
        Date returnDate = rentalManageDto.getExpectedReturnOn();   // 返却予定日

        // 返却予定日 > 貸出予定日
        if (!returnDate.after(rentalDate)){
            errors.put(
                "expectedReturnOn",
                "返却予定日は貸出予定日より後の日付を設定してください。"
            );
        }

        Integer status = rentalManageDto.getStatus();
        Date now = new Date();

        // ステータスチェック
        // 0:貸出待ち、1:貸出中
        if (status != 0 && status != 1) {

            errors.put(
                "status", 
                "貸出ステータスは貸出待ちか貸出中を選択してください。"
            );
        }

        // 貸出予定日は未来日付
        if (status == 0 && !rentalDate.after(now)) {

                errors.put(
                "expectedRentalOn",
                "貸出待ちの場合、貸出予定日は未来日付を設定してください。"
            );
        }

        // 貸出予定日は現在日付または過去日付
        if (status == 1 && rentalDate.after(now)) {

                errors.put(
                "expectedRentalOn",
                "貸出中の場合、貸出予定日は現在日付以前を設定してください。"
            );
        }

        return errors;
    }

    private RentalManage setRentalStatusDate(RentalManage rentalManage, Integer status) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        if (status == RentalStatus.RENTAlING.getValue()) {
            rentalManage.setRentaledAt(timestamp);
        } else if (status == RentalStatus.RETURNED.getValue()) {
            rentalManage.setReturnedAt(timestamp);
        } else if (status == RentalStatus.CANCELED.getValue()) {
            rentalManage.setCanceledAt(timestamp);
        }

        return rentalManage;
    }
}