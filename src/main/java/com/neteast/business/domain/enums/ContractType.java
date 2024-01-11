package com.neteast.business.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 项目类型
 * @author lzp
 * @date 2024年01月10 17:29
 */

@Getter
@AllArgsConstructor
public enum ContractType {

    /** 违约处置单 */
    BreakDo(1,"违约处置单"),
    /** 劳动竞赛 */
    LaborCompetition(2,"劳动竞赛"),
    /** 考评考核 */
    Evaluation(3,"考评考核");

    private final Integer Id;

    private final String type;

    public static String getType(int id){
        ContractType[] types = values();
        for (ContractType type:types) {
            if (type.getId()==id){
                return type.getType();
            }
        }
        return null;
    }
}
