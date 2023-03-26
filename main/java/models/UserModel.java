package models;

import java.util.Date;

public class UserModel {
    private final String callType;
    private final Date startCall;
    private final Date endCall;
    private final String tariffType;

    public UserModel(String callType, Date startCall, Date endCall, String tariffType) {
        this.callType = callType;
        this.startCall = startCall;
        this.endCall = endCall;
        this.tariffType = tariffType;
    }

    public String getCallType() {
        return callType;
    }

    public Date getStartCall() {
        return startCall;
    }

    public Date getEndCall() {
        return endCall;
    }

    public String getTariffType() {
        return tariffType;
    }

}