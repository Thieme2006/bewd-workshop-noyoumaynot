package han.aim.se.noyoumaynot.movie.other;

public class HashObject {
    private String hid;
    private String salt;

    public HashObject(String hid, String salt) {
        this.hid = hid;
        this.salt = salt;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
