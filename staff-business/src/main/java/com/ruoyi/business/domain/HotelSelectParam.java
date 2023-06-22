package com.ruoyi.business.domain;

import lombok.Data;

@Data
public class HotelSelectParam {
    private BaseHotel baseHotel;

    /**
     * 1. 当distance不为空，lat,lng一定不能为空，查询范围内
     * 2. 当level不为空，lat,lng一定不能为空，查询同城内
     * 3. 当lat,lng都不为空时，查询结果会计算距离
     * */

    /** 可选参数：纬度 */
    private Double lat;
    /** 可选参数：经度 */
    private Double lng;
    /**
     * 可选参数
     * null： 不限制城市
     * 1： 限制到区
     * 2： 限制到市
     * 3： 限制到省
     * */
    private Integer level;
    /**
     *  可选参数 单位：km
     *  null： 不限制距离
     *  not null: 查询范围内
     *  */
    private Double distance;
}
