package jp.co.metateam.library.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.Stock;

import org.springframework.data.jpa.repository.JpaRepository;


//貸出管理テーブルのリポジトリクラス作成//
@Repository
public interface RentalManageRepository extends JpaRepository<RentalManage, Integer> {
    List<RentalManage> findAll();
    List<RentalManage> findByExpectedRentalOn(Date getExpectedRentalOn);
    List<RentalManage> findByExpectedReturnOn(Date getExpectedReturnOn);
    List<RentalManage> findByStockIdAndRentalStatusIn(Stock stockId, List<Integer> statusList);
    
    
    


    
}

