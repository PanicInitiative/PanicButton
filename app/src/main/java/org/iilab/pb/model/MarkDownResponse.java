package org.iilab.pb.model;

/**
 * Created by aoe on 12/14/13.
 */
public class MarkDownResponse {
    private String mdData;
    private int status;

    public MarkDownResponse() {
    }

    public MarkDownResponse(String mdData, int status) {
        this.mdData = mdData;
        this.status = status;
    }

    public String getMdData() {
        return mdData;
    }

    public void setMdData(String mdData) {
        this.mdData = mdData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
