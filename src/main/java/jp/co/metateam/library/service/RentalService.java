package jp.co.metateam.library.service;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.metateam.library.model.RentalManageDto;

import jp.co.metateam.library.repository.RentalRepository;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor

public class RentalService {

    private final RentalRepository rentalRepository;

    public List<String> validate(RentalManageDto dto) {

        List<String> errors = new ArrayList<>();

        LocalDate today = LocalDate.now();

        if (dto.getEmployeeId() == null) {

            errors.add("社員番号は必須です。");

        }

        if (dto.getExpectedRentalOn() == null) {

            errors.add("貸出予定日は必須です。");

        }

        if (dto.getExpectedReturnOn() == null) {

            errors.add("返却予定日は必須です。");

        }

        if (dto.getStockId() == null) {

            errors.add("在庫管理番号は必須です。");

        }

        if (dto.getStatus() == null) {

            errors.add("貸出ステータスは必須です。");

        }

        if (dto.getExpectedRentalOn() != null && dto.getExpectedReturnOn() != null) {

            if (dto.getExpectedReturnOn().isBefore(dto.getExpectedRentalOn())) {

                errors.add("返却予定日は貸出予定日以降を入力してください。");

            }

        }

        return errors;

    }

    public void insert(RentalManageDto rentalManageDto) {

        rentalRepository.insert(rentalManageDto);

    }

}
 