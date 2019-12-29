
package com.linkb.jstx.network.result;

import com.linkb.jstx.network.model.Page;
import com.farsunset.cim.sdk.android.constant.CIMConstant;


public class BaseResult {

    public String code;
    public String message;
    public Page page;

    public boolean isSuccess() {
        return CIMConstant.ReturnCode.CODE_200.equals(code);
    }
}
