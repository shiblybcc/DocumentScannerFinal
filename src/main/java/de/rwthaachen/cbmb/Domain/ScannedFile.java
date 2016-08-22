package de.rwthaachen.cbmb.Domain;

import javax.persistence.*;

@Entity
//@Getter
//@Setter
public class ScannedFile {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable = true)
    private String fallnummer;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(nullable = true)
    private byte[] key;

    @Column(nullable = true)
    private String name;


    @Lob
    @Basic(fetch=FetchType.LAZY)
    protected  byte[]  scannedImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFallnummer() {
        return fallnummer;
    }

    public void setFallnummer(String fallnummer) {
        this.fallnummer = fallnummer;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getScannedImage() {
        return scannedImage;
    }

    public void setScannedImage(byte[] scannedImage) {
        this.scannedImage = scannedImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
