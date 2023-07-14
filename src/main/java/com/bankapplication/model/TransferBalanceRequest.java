package com.bankapplication.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferBalanceRequest {
    @JsonProperty("fromAccountNumber")
    @NotNull(message = "{transfer.from.account.number.absent}")
    @NotEmpty
    private Integer fromAccountNumber;
    @JsonProperty("toAccountNumber")
    @NotNull(message = "{transfer.to.account.number.absent}")
    @NotEmpty
    private Integer toAccountNumber;
    @JsonProperty("amount")
    @NotNull(message = "{transfer.amount.absent}")
    @NotEmpty
    @Min(value = 1,message = "{transfer.min.balance}")
    private BigDecimal amount;
    @JsonProperty("currency")
    @NotNull(message = "{transfer.currency.absent}")
    @NotEmpty
    private String currency;
}
