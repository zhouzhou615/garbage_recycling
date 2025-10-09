package com.stu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "企业用户信息（由认证校验后得到）")
public class EnterpriseUser {
    @Schema(description = "企业用户ID（即系统用户ID）")
    private Long id;
    @Schema(description = "公司名称")
    private String companyName;
    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;
    @Schema(description = "默认发票抬头")
    private String invoiceTitle;
    @Schema(description = "默认税号")
    private String taxNumber;
}

