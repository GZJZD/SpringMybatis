package com.web.service.imp;

import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorConfig;
import com.qq.tars.client.CommunicatorFactory;
import com.web.common.FollowOrderEnum;
import com.web.dao.ContractInfoDao;
import com.web.dao.ContractInfoLinkDao;
import com.web.pojo.Account;
import com.web.pojo.ContractInfo;
import com.web.pojo.ContractInfoLink;
import com.web.servant.center.*;
import com.web.service.ContractInfoLinkService;
import com.web.service.ContractInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ContractInfoLinkServiceImpl implements ContractInfoLinkService {

    private static Logger log = LogManager.getLogger(ContractInfoLinkServiceImpl.class.getName());
    @Autowired
    private ContractInfoLinkDao contractInfoLinkDao;
    @Autowired
    private ContractInfoService contractInfoService;


    @Override
    public synchronized void save(ContractInfoLink contractInfoLink) {
        contractInfoLinkDao.save(contractInfoLink);
    }

    @Override
    public synchronized void updateContractInfoLink(ContractInfoLink contractInfoLink) {
        contractInfoLinkDao.updateContractInfoLink(contractInfoLink);
    }

    private void saveOrUpdate(ContractInfoLink contractInfoLink) {
    }

    /*
     * 通过contractInfoLinkId 找到对应的实体类
     * */
    @Override
    public ContractInfoLink getContractInfoLink(Long contractInfoLinkId) {
        return contractInfoLinkDao.getContractInfoLink(contractInfoLinkId);
    }

    /*
     * contractInfoId 找到对应的实体类
     * */
    @Override
    public ContractInfoLink getContractInfoLinkByInfoId(Long contractInfoId) {
        return contractInfoLinkDao.getContractInfoLinkByInfoId(contractInfoId);
    }

    /*
     * 查找合约手续费
     * */
    @Override
    public void instrumentCommissionQuery(Long contractInfoId, Account account) {
        ContractInfoLink linkByInfoId = this.getContractInfoLinkByInfoId(contractInfoId);
        if(linkByInfoId.getCloseRatioByMoney()==null) {
            ContractInfo contractInfo = contractInfoService.getContractInfoById(contractInfoId);

            final instrumentCommissionQueryRequest req = new instrumentCommissionQueryRequest();
            req.setTypeId("instrumentCommissionQueryRequest");
            req.setBrokerId(account.getPlatform().getName());
            req.setUserId(account.getAccount());
            req.setInstrumentId(contractInfo.getContractCode());
            CommunicatorConfig cfg = new CommunicatorConfig();
            Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
            TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, "TestApp.HelloServer.HelloTrade@tcp -h 192.168.3.189 -p 50506 -t 60000");


            try {
                proxy.async_instrumentCommissionQuery(new TraderServantPrxCallback() {
                    @Override
                    public void callback_expired() {
                    }

                    @Override
                    public void callback_exception(Throwable ex) {
                    }

                    @Override
                    public void callback_userLogout(int ret, userLogoutResponse rsp) {
                    }

                    @Override
                    public void callback_userLogin(int ret, userLoginResponse rsp) {

                    }

                    @Override
                    public void callback_orderOpen(int ret, orderOpenResponse rsp) {
                    }

                    @Override
                    public void callback_orderClose(int ret, orderCloseResponse rsp) {
                    }

                    @Override
                    public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {

                    }

                    @Override
                    public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {
                        if (ret == 0 && rsp.errcode == 0) {
                            ContractInfoLink link = getContractInfoLinkByInfoId((long) rsp.getRequestId());

                            link.setOpenRatioByMoney(rsp.openRatioByMoney);
                            link.setOpenRatioByVolume(rsp.openRatioByVolume);
                            link.setCloseRatioByMoney(rsp.closeRatioByMoney);
                            link.setCloseRatioByVolume(rsp.closeRatioByVolume);
                            if (link.getContractInfo() != null) {
                                updateContractInfoLink(link);
                            } else {
                                ContractInfo contractInfo = new ContractInfo();
                                link.setContractInfo(contractInfo);
                                contractInfo.setId((long) rsp.getRequestId());
                                save(link);
                            }

                        } else {
                            if (ret == 0) {
                                log.error("合约信息手续费查询异常：" + rsp.errmsg);

                            } else {
                                log.error("Tars框架异常：" + ret);
                            }
                        }

                    }

                    @Override
                    public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {

                    }
                }, req);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    /*
     *
     * 查找合约的最大成交量和最小成交量
     * */
    @Override
    public void instrumentQuery(Long contractInfoId) {
        ContractInfoLink linkByInfoId = this.getContractInfoLinkByInfoId(contractInfoId);
        if(linkByInfoId.getMaxMarketOrderVolume()==null){
            CommunicatorConfig cfg = new CommunicatorConfig();
            Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
            TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, "TestApp.HelloServer.HelloTrade@tcp -h 192.168.3.189 -p 50506 -t 60000");
            instrumentQueryRequest req = new instrumentQueryRequest();

            try {
                proxy.async_instrumentQuery(new TraderServantPrxCallback() {
                    @Override
                    public void callback_expired() {
                    }

                    @Override
                    public void callback_exception(Throwable ex) {
                    }

                    @Override
                    public void callback_userLogout(int ret, userLogoutResponse rsp) {
                    }

                    @Override
                    public void callback_userLogin(int ret, userLoginResponse rsp) {

                    }

                    @Override
                    public void callback_orderOpen(int ret, orderOpenResponse rsp) {
                    }

                    @Override
                    public void callback_orderClose(int ret, orderCloseResponse rsp) {
                    }

                    @Override
                    public void callback_instrumentQuery(int ret, instrumentQueryResponse rsp) {
                        if (ret == 0 && rsp.errcode == 0) {
                            ContractInfoLink InfoLink = getContractInfoLinkByInfoId((long) rsp.getRequestId());

                            InfoLink.setMaxMarketOrderVolume((long) rsp.maxMarketOrderVolume);
                            InfoLink.setMinMarketOrderVolume((long) rsp.minMarketOrderVolume);
                            InfoLink.setVolumeMultiple((long) rsp.volumeMultiple);
                            InfoLink.setPriceTick(rsp.priceTick);
                            if (InfoLink.getContractInfo() != null) {

                                updateContractInfoLink(InfoLink);
                            } else {
                                ContractInfo contractInfo = new ContractInfo();
                                InfoLink.setContractInfo(contractInfo);
                                contractInfo.setId((long) rsp.getRequestId());
                                save(InfoLink);
                            }

                        } else {
                            if (ret == 0) {
                                log.error("合约信息查询异常：" + rsp.errmsg);

                            } else {
                                log.error("Tars框架异常：" + ret);
                            }
                        }

                    }

                    @Override
                    public void callback_instrumentCommissionQuery(int ret, instrumentCommissionQueryResponse rsp) {

                    }

                    @Override
                    public void callback_marketDataQuery(int ret, marketDataQueryResponse rsp) {

                    }
                }, req);
            }catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}
