/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: matieli[ma_tl@suixingpay.com] 
 * @date: 2018年1月8日 下午6:10:11   
 * @Copyright ©2018 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.data.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Domain
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2018年1月8日 下午6:10:11
 * @version: V1.0
 * @review: matieli[ma_tl@suixingpay.com]/2018年1月8日 下午6:10:11
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseDomain<Pk extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    Pk id;
    @ApiModelProperty(value = "创建人", readOnly = true)
    String creater;
    @ApiModelProperty(value = "修改人", readOnly = true)
    String updater;
    @ApiModelProperty(value = "创建时间", readOnly = true)
    Date createdDate;
    @ApiModelProperty(value = "修改时间", readOnly = true)
    Date lastUpdated;
    @ApiModelProperty(value = "版本号", readOnly = true)
    Integer version;
}
