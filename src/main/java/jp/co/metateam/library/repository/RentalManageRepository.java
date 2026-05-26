package jp.co.metateam.library.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.metateam.library.model.RentalManage;

@Repository
public interface RentalManageRepository 
    extends JpaRepository<RentalManage,Integer>{

        List<RentalManage>findByStockIdAndStatusIn(
             String stockId,
            List<Integer>statuList);
    
}