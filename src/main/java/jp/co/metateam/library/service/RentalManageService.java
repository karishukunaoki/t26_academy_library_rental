package jp.co.metateam.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.repository.RentalManageRepository;

@Service
public class RentalManageService {

    private final RentalManageRepository rentalManageRepository;

    public RentalManageService(RentalManageRepository rentalManageRepository) {
        this.rentalManageRepository = rentalManageRepository;
    }

    public RentalManage convertToEntity(RentalManageDto rentalManageDto) {
        RentalManage rentalManage = new RentalManage();
        rentalManage.setEmployeeId(rentalManageDto.getEmployeeId());
        rentalManage.setStockId(rentalManageDto.getStockId());
        rentalManage.setExpectedRentalOn(rentalManageDto.getExpectedRentalOn());
        rentalManage.setExpectedReturnOn(rentalManageDto.getExpectedReturnOn());
        rentalManage.setStatus(rentalManageDto.getStatus());

        return rentalManage;
    }

    public void validateRentalManage(
            RentalManageDto rentalManageDto,
            BindingResult bindingResult) {

        RentalManage rentalManage = convertToEntity(rentalManageDto);

        // ステータス妥当性チェック
        if (rentalManage.getStatus() != null
                && (rentalManage.getStatus() == 2
                        || rentalManage.getStatus() == 3)) {

            bindingResult.rejectValue(
                    "status",
                    "status.invalid",
                    "貸出ステータスは「貸出待ち」もしくは「貸出中」を選択してください");
        }

        // 日付妥当性チェック
        if (rentalManage.getExpectedRentalOn() != null
                && rentalManage.getExpectedReturnOn() != null) {

            if (rentalManage.getExpectedReturnOn()
                    .isBefore(rentalManage.getExpectedRentalOn())
                    || rentalManage.getExpectedReturnOn()
                            .isEqual(rentalManage.getExpectedRentalOn())) {

                bindingResult.rejectValue(
                        "expectedReturnOn",
                        "date.invalid",
                        "返却予定日は貸出予定日以降の日付を入力してください");
            }
        }

        // 貸出予定日過去日付チェック
        if (rentalManage.getExpectedRentalOn() != null
                && rentalManage.getStatus() != null
                && rentalManage.getExpectedRentalOn().isBefore(LocalDate.now())
                && rentalManage.getStatus() != 1) {

            bindingResult.rejectValue(
                    "status",
                    "status.invalid",
                    "過去日付では「貸出中」を選択してください");
        }

        // 貸出予定日未来日付チェック
        if (rentalManage.getExpectedRentalOn() != null
                && rentalManage.getStatus() != null
                && rentalManage.getExpectedRentalOn().isAfter(LocalDate.now())
                && rentalManage.getStatus() != 0) {

            bindingResult.rejectValue(
                    "status",
                    "status.invalid",
                    "未来日付では「貸出待ち」を選択してください");
        }

        // 貸出予定日・返却予定日が過去日付の場合チェック
if (rentalManage.getExpectedRentalOn() != null
        && rentalManage.getExpectedReturnOn() != null
        && rentalManage.getStatus() != null
        && rentalManage.getExpectedRentalOn().isBefore(LocalDate.now())
        && rentalManage.getExpectedReturnOn().isBefore(LocalDate.now())
        && rentalManage.getStatus() == 1) {

    bindingResult.rejectValue(
            "status",
            "status.invalid",
            "過去日付では「貸出中」以外を選択してください");
    }

        // 重複チェック
        if (rentalManage.getStockId() != null
                && rentalManage.getExpectedRentalOn() != null
                && rentalManage.getExpectedReturnOn() != null) {

            List<RentalManage> rentalManageList = rentalManageRepository.findByStockIdAndStatusIn(
                    rentalManage.getStockId(),
                    List.of(0, 1));

            for (RentalManage registeredRentalManage : rentalManageList) {

                if (!(rentalManage.getExpectedReturnOn()
                        .isBefore(registeredRentalManage.getExpectedRentalOn())
                        || rentalManage.getExpectedRentalOn()
                                .isAfter(registeredRentalManage.getExpectedReturnOn()))) {

                    bindingResult.rejectValue(
                            "stockId",
                            "rental.duplicate",
                            "指定した在庫管理番号は、入力した期間ですでに貸出予定があります");
                    break;
                }
            }
        }
    }

    public void saveRentalManage(RentalManageDto rentalManageDto) {
        RentalManage rentalManage = convertToEntity(rentalManageDto);
        rentalManageRepository.save(rentalManage);
    }
}
