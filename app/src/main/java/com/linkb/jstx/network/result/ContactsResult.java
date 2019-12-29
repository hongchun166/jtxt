
package com.linkb.jstx.network.result;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.model.Tag;
import com.linkb.jstx.model.Group;

import java.util.ArrayList;


public class ContactsResult extends BaseResult {
    public ArrayList<Group> groupList;
    public ArrayList<MicroServer> microServerList;
    public ArrayList<Tag> tagList;

    public boolean isNotEmpty(Class cls) {
        if (cls == Tag.class) {
            return tagList != null && !tagList.isEmpty();
        }
        if (cls == Group.class) {
            return groupList != null && !groupList.isEmpty();
        }
        if (cls == MicroServer.class) {
            return microServerList != null && !microServerList.isEmpty();
        }
        return true;
    }
}
