package net.hehe.validate;

import java.util.List;

/**
 * @Author neo·tao
 * @Date 2017/12/20
 * @Desc
 */
public class RuleListValidator implements Validate {

    @Override
    public boolean validate(String data, Object ruleList) {
        return ruleList instanceof List && ((List) ruleList).contains(data);
    }
}
