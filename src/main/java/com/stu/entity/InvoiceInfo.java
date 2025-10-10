package com.stu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发票信息")
public class InvoiceInfo {
    @Schema(description = "发票抬头")
    private String invoiceTitle;
    @Schema(description = "税号/纳税人识别号")
    private String taxNumber;
}

