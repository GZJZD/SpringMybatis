package com.web.pojo;
/**
 * 品种
 * @author riseSun
 *
 * 2017年12月14日下午10:10:41
 */
public class Variety {
    private Long id;
    private String varietyName;
    private String varietyCode;
    private String tradePlaceName;

    public Variety() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getVarietyName() {
        return varietyName;
    }
    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }
    public String getVarietyCode() {
        return varietyCode;
    }
    public void setVarietyCode(String varietyCode) {
        this.varietyCode = varietyCode;
    }

    public String getTradePlaceName() {
        return tradePlaceName;
    }

    public void setTradePlaceName(String tradePlaceName) {
        this.tradePlaceName = tradePlaceName;
    }

    public Variety(Long id, String varietyName, String varietyCode, String tradePlaceName) {
        this.id = id;
        this.varietyName = varietyName;
        this.varietyCode = varietyCode;
        this.tradePlaceName=tradePlaceName;
    }
}