package jp.co.metateam.library.model;

import java.time.LocalDate;

import lombok.Data;

@Data

public class RentalManageDto {

    private String employeeId;

    private LocalDate expectedRentalOn;

    private LocalDate expectedReturnOn;

    private String stockId;

    private Integer status;

}
