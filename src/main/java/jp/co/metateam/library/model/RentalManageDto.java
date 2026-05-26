package jp.co.metateam.library.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RentalManageDto {

    private Integer employeeId;

    private LocalDate expectedRentalOn;

    private LocalDate expectedReturnOn;

    private Integer stockId;

    private String status;
}
