package org.nb.bbbook.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("TeamPerGame")
public class TeamPerGame implements Serializable {
    private final int rank;
    @Id private final String name;
    private final int gp;
    private final float mp;
    private final float fg;
    private final float fga;
    private final float fgp;
    private final float three;
    private final float threea;
    private final float threep;
    private final float two;
    private final float twoa;
    private final float twop;
    private final float ft;
    private final float fta;
    private final float ftp;
    private final float oreb;
    private final float dreb;
    private final float treb;
    private final float ast;
    private final float stl;
    private final float blk;
    private final float tov;
    private final float pf;
    private final float pts;

    public TeamPerGame(
        String rank,
        String name,
        String gp,
        String mp,
        String fg,
        String fga,
        String fgp,
        String three,
        String threea,
        String threep,
        String two,
        String twoa,
        String twop,
        String ft,
        String fta,
        String ftp,
        String oreb,
        String dreb,
        String treb,
        String ast,
        String stl,
        String blk,
        String tov,
        String pf,
        String pts
    ) {
        this.rank = Integer.parseInt(rank);
        this.name = name;
        this.gp = Integer.parseInt(gp);
        this.mp = Float.parseFloat(mp);
        this.fg = Float.parseFloat(fg);
        this.fga = Float.parseFloat(fga);
        this.fgp = Float.parseFloat(fgp);
        this.three = Float.parseFloat(three);
        this.threea = Float.parseFloat(threea);
        this.threep = Float.parseFloat(threep);
        this.two = Float.parseFloat(two);
        this.twoa = Float.parseFloat(twoa);
        this.twop = Float.parseFloat(twop);
        this.ft = Float.parseFloat(ft);
        this.fta = Float.parseFloat(fta);
        this.ftp = Float.parseFloat(ftp);
        this.oreb = Float.parseFloat(oreb);
        this.dreb = Float.parseFloat(dreb);
        this.treb = Float.parseFloat(treb);
        this.ast = Float.parseFloat(ast);
        this.stl = Float.parseFloat(stl);
        this.blk = Float.parseFloat(blk);
        this.tov = Float.parseFloat(tov);
        this.pf = Float.parseFloat(pf);
        this.pts = Float.parseFloat(pts);
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getGp() {
        return gp;
    }

    public float getMp() {
        return mp;
    }

    public float getFg() {
        return fg;
    }

    public float getFga() {
        return fga;
    }

    public float getFgp() {
        return fgp;
    }

    public float getThree() {
        return three;
    }

    public float getThreea() {
        return threea;
    }

    public float getThreep() {
        return threep;
    }

    public float getTwo() {
        return two;
    }

    public float getTwoa() {
        return twoa;
    }

    public float getTwop() {
        return twop;
    }

    public float getFt() {
        return ft;
    }

    public float getFta() {
        return fta;
    }

    public float getFtp() {
        return ftp;
    }

    public float getOreb() {
        return oreb;
    }

    public float getDreb() {
        return dreb;
    }

    public float getTreb() {
        return treb;
    }

    public float getAst() {
        return ast;
    }

    public float getStl() {
        return stl;
    }

    public float getBlk() {
        return blk;
    }

    public float getTov() {
        return tov;
    }

    public float getPf() {
        return pf;
    }

    public float getPts() {
        return pts;
    }
}
