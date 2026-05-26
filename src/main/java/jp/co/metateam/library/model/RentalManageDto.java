package jp.co.metateam.library.model;

import java.security.Timestamp;
import java.util.Date;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * 貸出管理DTO
 */
@Getter
@Setter
public class RentalManageDto {

    private Long id;

    @NotEmpty(message = "社員番号は必須です")
    private String employeeId;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "貸出予定日は必須です")
    private Date expectedRentalOn;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "返却予定日は必須です")
    private Date expectedReturnOn;

    @NotNull(message = "在庫管理番号は必須です")
    private String stockId;

    @NotNull(message = "貸出ステータスは必須です")
    private Integer status;

    private Timestamp rentaledAt;

    private Timestamp returnedAt;

    private Timestamp canceledAt;

    private Stock stock;

    private Account account;

}
