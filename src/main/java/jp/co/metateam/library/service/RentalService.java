package jp.co.metateam.library.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.repository.Rentalrepository;

@Service
public class RentalService {

    private final Rentalrepository rentalrepository;

    public RentalService(Rentalrepository rentalrepository) {
        this.rentalrepository = rentalrepository;
    }

    public List<String> findAll() {
        return Arrays.asList(
                "000001 管理太郎",
                "468 石田");
    }

    public List<String> findStocks() {
        return List.of(
                "A00001 スッキリ分かるjava入門 第4版",
                "A00002 かんたん合格ITパスポート教科書＆必須問題 令和５年度",
                "A00003 13歳から分かる！7つの習慣",
                "A00004 チーズはどこへ消えた？");
    }

    public void insert(RentalManageDto dto) {

        RentalManage rentalManage = new RentalManage();

        rentalManage.setEmployeeId(dto.getEmployeeId());
        rentalManage.setExpectedRentalOn(dto.getExpectedRentalOn());
        rentalManage.setExpectedReturnOn(dto.getExpectedReturnOn());
        rentalManage.setStockId(dto.getStockId());
        rentalManage.setStatus(dto.getStatus());

        rentalrepository.save(rentalManage);
    }
}