package jp.co.metateam.library.service;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;



import org.springframework.stereotype.Service;

import ch.qos.logback.core.rolling.helper.RenameUtil;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.common.util.StringUtils;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.Stock;

import jp.co.metateam.library.repository.RentalManageRepository;
import jp.co.metateam.library.repository.StockRepository;
import jp.co.metateam.library.repository.AccountRepository;

@Service
public class RentalManageService {
    private final RentalManageRepository rentalManageRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RentalManageService(RentalManageRepository rentalManageRepository, StockRepository stockRepository, AccountRepository accountRepository) {
        this.rentalManageRepository = rentalManageRepository;
        this.stockRepository = stockRepository;
        this.accountRepository = accountRepository;

    }
    @Transactional//貸出テーブルから全件取得するメソッド
    public List<RentalManage> findAll() {
        List<RentalManage> rentalManages = this.rentalManageRepository.findAll();

        return rentalManages;
    }
    @Transactional//アカウントテーブルから全件取得するメソッド
    public List<Account> findAllAccounts() {
        List<Account> accounts = this.accountRepository.findAll();

        return accounts;
    }
    @Transactional//在庫テーブルから全件取得するメソッド
    public List<Stock> findAllStocks() {
        List<Stock> stocks = this.stockRepository.findAll();

        return stocks;
    }
    @Transactional
    public List<RentalManage> findByExpectedRentalOn(Date getExpectedRentalOn) {
        List<RentalManage> rentalManages =  this.rentalManageRepository.findByExpectedRentalOn(getExpectedRentalOn);
        return rentalManages;
    }
    @Transactional
    public List<RentalManage> findByExpectedReturnOn(Date getExpectedReturnOn) {
        List<RentalManage> rentalManages = this.rentalManageRepository.findByExpectedReturnOn(getExpectedReturnOn);

        return rentalManages;
    }
    @Transactional
    public List<RentalManage> findByStockIdAndRentalStatusIn(String stockId,List<Integer> statusList) {
        Stock stock = stockRepository.findById(stockId).orElse(null);
        return rentalManageRepository.findByStockIdAndRentalStatusIn(stock, statusList);
    }
    //貸出テーブルに保存するメソッド
    @Transactional
    public void save (RentalManageDto rentalManageDto) {

        RentalManage rentalManage = new RentalManage();//貸出テーブルのEntityクラスのオブジェクトを作成
        //RentalManageDtoからRentalManageへの変換
        //在庫管理番号は外部キーのため、StockRepositoryを使用してStockエンティティを取得し、RentalManageエンティティのstockIdフィールドにセット
        rentalManage.setId(rentalManageDto.getId());
        Stock stock = this.stockRepository.findById(rentalManageDto.getStockId()).orElse(null);
        //社員番号も同様
        rentalManage.setStockId(stock);
        Account account = this.accountRepository.findByEmployeeId(rentalManageDto.getEmployeeId()).orElse(null);

        rentalManage.setEmployeeId(account);
        rentalManage.setRentalStatus(rentalManageDto.getStatus());
        rentalManage.setExpectedRentalOn(rentalManageDto.getExpectedRentalOn());
        rentalManage.setExpectedReturnOn(rentalManageDto.getExpectedReturnOn());

       //貸出テーブルに保存する際に、createdAtとupdatedAtに現在の日時をセット
        LocalDateTime now = LocalDateTime.now();
        rentalManage.setCreatedAt(now);
        rentalManage.setUpdatedAt(now);
        //EntityクラスのオブジェクトをDBに保存
        this.rentalManageRepository.save(rentalManage);
    }
    

}
    



