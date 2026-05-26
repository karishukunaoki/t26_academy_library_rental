package jp.co.metateam.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.log4j.Log4j2;
import jp.co.metateam.library.model.RentalManageDto;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.service.StockService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {
    private final AccountService accountService;
    private final StockService stockService;
    private final RentalManageService rentalManageService;

    @Autowired
    public RentalManageController(AccountService accountService,
            StockService stockService,
            RentalManageService rentalManageService) {

        this.accountService = accountService;
        this.stockService = stockService;
        this.rentalManageService = rentalManageService;
    }

    /**
     * 貸出一覧画面初期表示
     * @param model
     * @return
     */
    @GetMapping("/rental/index")
    public String index(Model model) {
        // 貸出管理テーブルから全件取得

        // 貸出一覧画面に渡すデータをmodelに追加

        // 貸出一覧画面に遷移
        return "rental/index";
    }

    @GetMapping("/rental/add") // ブラウザで/rental/addにアクセスしたときの処理
    public String add(Model model) {// 以下登録画面を作る処理を開始
        List<Account> account = accountService.findAll();
        List<Stock> stock = stockService.findAll();

        model.addAttribute("title", "貸出登録");
        model.addAttribute("rentalManageDto", new RentalManageDto());
        model.addAttribute("accounts", account);
        model.addAttribute("stockList", stock);

        Map<Integer, String> rentalStatus = new LinkedHashMap<>();
        rentalStatus.put(0, "貸出待ち");
        rentalStatus.put(1, "貸出中");
        rentalStatus.put(2, "返却済み");
        rentalStatus.put(3, "キャンセル");
        model.addAttribute("rentalStatus", rentalStatus);
        return "rental/add";
    }

    @PostMapping("/rental/add")
    public String create(
            @Valid @ModelAttribute RentalManageDto rentalManageDto,
            BindingResult result,
            Model model) {
        // 入力された貸出予定日が空かの確認
        if (rentalManageDto.getExpectedRentalOn() == null
                || rentalManageDto.getExpectedRentalOn().isEmpty()) {

            result.rejectValue(
                    "expectedRentalOn",
                    "error.expectedRentalOn",
                    "「貸出予定日」は必須項目です。");
        }
        // 入力された返却予定日が空かの確認
        if (rentalManageDto.getExpectedReturnOn() == null
                || rentalManageDto.getExpectedReturnOn().isEmpty()) {

            result.rejectValue(
                    "expectedReturnOn",
                    "error.expectedReturnOn",
                    "「返却予定日」は必須項目です。");
        }
        // 必須チェックでエラーがあれば、貸出登録画面に戻る
        if (result.hasErrors()) {

            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());

            Map<Integer, String> rentalStatus = new LinkedHashMap<>();
            rentalStatus.put(0, "貸出待ち");
            rentalStatus.put(1, "貸出中");
            rentalStatus.put(2, "返却済み");
            rentalStatus.put(3, "キャンセル");

            model.addAttribute("rentalStatus", rentalStatus);

            return "rental/add";
        }
        // 入力された貸出予定日と返却予定日がyyyy/MM/dd形式かの確認
        if (!rentalManageDto.getExpectedRentalOn().matches("\\d{4}/\\d{2}/\\d{2}")) {

            result.rejectValue(
                    "expectedRentalOn",
                    "error.expectedRentalOn",
                    "貸出予定日はyyyy/MM/dd形式で入力してください。");
        }

        if (!rentalManageDto.getExpectedReturnOn().matches("\\d{4}/\\d{2}/\\d{2}")) {

            result.rejectValue(
                    "expectedReturnOn",
                    "error.expectedReturnOn",
                    "返却予定日はyyyy/MM/dd形式で入力してください。");
        }
        // エラーがあれば、貸出登録画面に戻る
        if (result.hasErrors()) {

            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());

            Map<Integer, String> rentalStatus = new LinkedHashMap<>();
            rentalStatus.put(0, "貸出待ち");
            rentalStatus.put(1, "貸出中");
            rentalStatus.put(2, "返却済み");
            rentalStatus.put(3, "キャンセル");

            model.addAttribute("rentalStatus", rentalStatus);

            return "rental/add";
        }
        // 文字列の日付をLocalDate型に変換
        LocalDate rentalDate = LocalDate.parse(
                rentalManageDto.getExpectedRentalOn().replace("/", "-"));
        LocalDate returnDate = LocalDate.parse(
                rentalManageDto.getExpectedReturnOn().replace("/", "-"));
        // 入力された貸出予定日が過去日付かの確認
        if (rentalDate.isBefore(LocalDate.now())) {
            result.rejectValue(
                    "expectedRentalOn",
                    "error.expectedRentalOn",
                    "過去の日付は選択できません。");
        }
        // 入力された返却予定日が貸出予定日より前ではないか確認
        if (returnDate.isBefore(rentalDate)) {
            result.rejectValue(
                    "expectedReturnOn",
                    "error.expectedReturnOn",
                    "返却予定日は貸出予定日より後の日付を選択してください。");
        }
        // 同じ日ではないかの確認
        if (returnDate.isEqual(rentalDate)) {
            result.rejectValue(
                    "expectedReturnOn",
                    "error.expectedReturnOn",
                    "貸出予定日と返却予定日は同じ日にできません。");
            if (result.hasErrors()) {
                model.addAttribute("accounts", accountService.findAll());
                model.addAttribute("stockList", stockService.findAll());

                Map<Integer, String> rentalStatus = new LinkedHashMap<>();
                rentalStatus.put(0, "貸出待ち");
                rentalStatus.put(1, "貸出中");
                rentalStatus.put(2, "返却済み");
                rentalStatus.put(3, "キャンセル");

                model.addAttribute("rentalStatus", rentalStatus);

                return "rental/add";
            }
        }
        // 貸出予定日が未来日付で、貸出ステータスが貸出中の場合はエラー
        if (rentalDate.isAfter(LocalDate.now())
                && rentalManageDto.getStatus() != null
                && rentalManageDto.getStatus() == 1) {

            result.rejectValue(
                    "status",
                    "error.status",
                    "未来の貸出予定日の場合、貸出中は選択できません。");
        }
        // 貸出予定日が当日で、貸出ステータスが貸出待ちの場合はエラー
        if (rentalDate.isEqual(LocalDate.now())
                && rentalManageDto.getStatus() != null
                && rentalManageDto.getStatus() == 0) {

            result.rejectValue(
                    "status",
                    "error.status",
                    "本日貸出する場合、貸出待ちは選択できません。");
        }
        // 日付チェックでエラーがあれば、貸出登録画面に戻る
        if (result.hasErrors()) {

            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());

            Map<Integer, String> rentalStatus = new LinkedHashMap<>();
            rentalStatus.put(0, "貸出待ち");
            rentalStatus.put(1, "貸出中");
            rentalStatus.put(2, "返却済み");
            rentalStatus.put(3, "キャンセル");

            model.addAttribute("rentalStatus", rentalStatus);

            return "rental/add";
        }
        // 同じ在庫管理番号で貸出期間が重複していないかの確認

        if (rentalManageService.existsRentalPeriod(
                rentalManageDto.getStockId(),
                rentalManageDto.getExpectedRentalOn().replace("/", "-"),
                rentalManageDto.getExpectedReturnOn().replace("/", "-"))) {

            result.rejectValue(
                    "expectedRentalOn",
                    "error.expectedRentalOn",
                    "選択された貸出期間は重複しています。");
        }
        // 期間重複チェックでエラーがあれば、貸出登録画面に戻る
        if (result.hasErrors()) {

            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());

            Map<Integer, String> rentalStatus = new LinkedHashMap<>();
            rentalStatus.put(0, "貸出待ち");
            rentalStatus.put(1, "貸出中");
            rentalStatus.put(2, "返却済み");
            rentalStatus.put(3, "キャンセル");

            model.addAttribute("rentalStatus", rentalStatus);

            return "rental/add";
        }
        // すべてのチェックを通過したら、貸出管理テーブルに保存して、貸出一覧画面にリダイレクト
        rentalManageService.save(rentalManageDto);

        return "redirect:/rental/index";
    }
}
