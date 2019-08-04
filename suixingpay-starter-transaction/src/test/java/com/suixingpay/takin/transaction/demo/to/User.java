package com.suixingpay.takin.transaction.demo.to;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月31日 下午1:35:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月31日 下午1:35:34
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    Long id;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;

    @Min(value = 18, message = "年龄不能小于{value}")
    @Max(value = 100, message = "年龄不能大于{value}")
    @ApiModelProperty("年龄")
    private Integer age;

    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty(value = "创建人", readOnly = true)
    private String creater;

    @ApiModelProperty(value = "修改人", readOnly = true)
    private String updater;

    @ApiModelProperty(value = "创建时间", readOnly = true)
    private Date createdDate;

    @ApiModelProperty(value = "修改时间", readOnly = true)
    private Date lastUpdated;

    @ApiModelProperty(value = "版本号", readOnly = true)
    private Integer version;

}
