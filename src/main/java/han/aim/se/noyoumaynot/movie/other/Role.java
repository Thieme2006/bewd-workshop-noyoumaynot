package han.aim.se.noyoumaynot.movie.other;

public class Role {
    private String naam;
    private boolean beheerder;

    public Role(String naam, boolean beheerder) {
        this.naam = naam;
        this.beheerder = beheerder;
    }

    public String getNaam() {
        return naam;
    }
    public void setNaam(String naam) {
        this.naam = naam;
    }

    public boolean isBeheerder() {
        return beheerder;
    }

    public void setBeheerder(boolean beheerder) {
        this.beheerder = beheerder;
    }
}
