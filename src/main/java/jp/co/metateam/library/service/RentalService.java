package jp.co.metateam.library.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.repository.RentalRepository;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public List<String> validate(RentalManageDto dto) {

        List<String> errors = new ArrayList<>();

        if (dto.getEmployeeId() == null || dto.getEmployeeId().isBlank()) {
            errors.add("社員番号は必須です。");
        }

        if (dto.getExpectedRentalOn() == null) {
            errors.add("貸出予定日は必須です。");
        }

        if (dto.getExpectedReturnOn() == null) {
            errors.add("返却予定日は必須です。");
        }

        if (dto.getStockId() == null || dto.getStockId().isBlank()) {
            errors.add("在庫管理番号は必須です。");
        }

        if (dto.getStatus() == null) {
            errors.add("貸出ステータスは必須です。");
        }

        // ステータスチェック
        if (dto.getStatus() != null) {

            // 「返却済み」「キャンセル」は登録不可
            if (dto.getStatus() == 2 || dto.getStatus() == 3) {

                errors.add("貸出ステータスは「貸出待ち」「貸出中」のどちらかに設定してください。");
            }
        }

        // ステータス妥当性チェック
        if (dto.getExpectedRentalOn() != null && dto.getStatus() != null) {

            // 未来日なのに「貸出待ち」以外
            if (dto.getExpectedRentalOn().isAfter(LocalDate.now())
                    && dto.getStatus() != 0) {

                errors.add("未来日付では「貸出待ち」を選択してください");
            }

            // 過去日なのに「貸出中」以外
            if (dto.getExpectedRentalOn().isBefore(LocalDate.now())
                    && dto.getStatus() != 1) {

                errors.add("過去日付では「貸出中」を選択してください");
            }
        }

        if (dto.getExpectedRentalOn() != null
                && dto.getExpectedReturnOn() != null) {

            if (dto.getExpectedReturnOn()
                    .isBefore(dto.getExpectedRentalOn())) {

                errors.add("返却予定日は貸出予定日以降を入力してください。");
            }
        }

        return errors;
    }

    public void insert(RentalManageDto rentalManageDto) {

        rentalRepository.insert(rentalManageDto);

    }
}