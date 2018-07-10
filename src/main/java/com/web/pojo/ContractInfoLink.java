package com.web.pojo;

import java.io.Serializable;

/**
 * 合约信息
 *@Author: May
 *@param
 *@Date: 14:11 2018/5/21
 */
public class ContractInfoLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4211332563353291158L;
	private Long id;
	//合约信息
    private ContractInfo contractInfo;
    //市价最大下单量
    private Long maxMarketOrderVolume;
    //市价最小下单量
    private Long minMarketOrderVolume;
    //合约数量乘积
    private Long volumeMultiple;
    //最小变动价位，即点位
    private Double priceTick;
    //开仓手续费率
    private Double openRatioByMoney;
    //开仓手续费
    private Double openRatioByVolume;
    //平仓手续费率
    private Double closeRatioByMoney;
    //平仓手续费
    private Double closeRatioByVolume;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public ContractInfo getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(ContractInfo contractInfo) {
		this.contractInfo = contractInfo;
	}

	public Long getMaxMarketOrderVolume() {
		return maxMarketOrderVolume;
	}

	public void setMaxMarketOrderVolume(Long maxMarketOrderVolume) {
		this.maxMarketOrderVolume = maxMarketOrderVolume;
	}

	public Long getMinMarketOrderVolume() {
		return minMarketOrderVolume;
	}

	public void setMinMarketOrderVolume(Long minMarketOrderVolume) {
		this.minMarketOrderVolume = minMarketOrderVolume;
	}

	public Long getVolumeMultiple() {
		return volumeMultiple;
	}

	public void setVolumeMultiple(Long volumeMultiple) {
		this.volumeMultiple = volumeMultiple;
	}

	public Double getPriceTick() {
		return priceTick;
	}

	public void setPriceTick(Double priceTick) {
		this.priceTick = priceTick;
	}

	public Double getOpenRatioByMoney() {
		return openRatioByMoney;
	}

	public void setOpenRatioByMoney(Double openRatioByMoney) {
		this.openRatioByMoney = openRatioByMoney;
	}

	public Double getOpenRatioByVolume() {
		return openRatioByVolume;
	}

	public void setOpenRatioByVolume(Double openRatioByVolume) {
		this.openRatioByVolume = openRatioByVolume;
	}

	public Double getCloseRatioByMoney() {
		return closeRatioByMoney;
	}

	public void setCloseRatioByMoney(Double closeRatioByMoney) {
		this.closeRatioByMoney = closeRatioByMoney;
	}

	public Double getCloseRatioByVolume() {
		return closeRatioByVolume;
	}

	public void setCloseRatioByVolume(Double closeRatioByVolume) {
		this.closeRatioByVolume = closeRatioByVolume;
	}
    
}