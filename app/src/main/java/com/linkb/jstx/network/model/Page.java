
package com.linkb.jstx.network.model;

import java.io.Serializable;

public class Page implements Serializable {

    private static final long serialVersionUID = 1L;
    public int count;
    public int size;
    private int currentPage;
    private int countPage;


    public boolean hasMore() {

        return currentPage < countPage;
    }
}
