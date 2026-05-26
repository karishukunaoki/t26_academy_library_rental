package jp.co.metateam.library.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Setter
@Getter
//貸出管理Dto→画面から来た値を受け取る
public class RentalManageDto {

    private String id;

    @NotBlank(message = "社員番号は必須です")
    private String employeeId;

    @NotNull(message = "貸出予定日は必須です")
    @DateTimeFormat(pattern = "yyyy-mm-dd")//htmlに渡る値のフォーマットを指定"yyyy-MM-dd"
    private LocalDate expectedRentalOn;

    @NotNull(message = "返却予定日は必須です")
    @DateTimeFormat(pattern = "yyyy-mm-dd")//htmlに渡る値のフォーマットを指定"yyyy-mm-dd"
    private LocalDate expectedReturnOn;

    @NotBlank(message = "在庫管理番号は必須です")
    private String stockId;

    @NotNull(message = "貸出ステータスは必須です")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rentalAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date canceledAt;

}

