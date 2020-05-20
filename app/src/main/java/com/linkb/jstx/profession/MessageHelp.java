package com.linkb.jstx.profession;

import com.linkb.jstx.app.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHelp {

    public static boolean hasGrpActionByAction(String action){
        return Constant.MessageAction.ACTION_3.equals(action)
                || Constant.MessageAction.ACTION_GrpRedPack.equals(action)
                || Constant.MessageAction.ACTION_1.equals(action);
    }
    public static String[] getGrpAction(){
        List<String> actionArr=getGrpActionList();
        return actionArr.toArray(new String[actionArr.size()]);
    }
    public static  List<String> getGrpActionList(){
        List<String> actionArr=new ArrayList<>();
        actionArr.add(Constant.MessageAction.ACTION_1);
        actionArr.add(Constant.MessageAction.ACTION_3);
        actionArr.add(Constant.MessageAction.ACTION_GrpRedPack);
        return actionArr;
    }
}
