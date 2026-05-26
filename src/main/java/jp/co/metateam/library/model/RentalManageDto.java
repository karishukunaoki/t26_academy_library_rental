package jp.co.metateam.library.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * 貸出管理DTO
 */
@Getter
@Setter

public class RentalManageDto {
    @NotBlank(message = "社員番号は必須です")
    private String employeeId;

    @NotBlank(message = "在庫管理番号は必須です")
    private String stockId;

    @NotNull(message = "貸出予定日は必須です")
    private LocalDate expectedRentalOn;

    @NotNull(message = "返却予定日は必須です")
    private LocalDate expectedReturnOn;

    @NotNull(message = "貸出ステータスは必須です")
    private Integer status;
}

