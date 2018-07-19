package com.web.service.imp;

import com.web.common.OrderUserEnum;
import com.web.dao.OrderUserDao;
import com.web.database.OrderHongKongService;
import com.web.database.entity.Agent;
import com.web.database.entity.Prices;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.database.entity.PlatFromUsers;
import com.web.pojo.vo.orderuser.HoldOrderUserVo;
import com.web.pojo.vo.orderuser.OrderUserDetailsVo;
import com.web.pojo.vo.orderuser.OrderUserListVo;
import com.web.pojo.vo.orderuser.OrderUserVo;
import com.web.service.OrderUserService;
import com.web.util.common.DateUtil;
import com.web.util.common.DoubleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("orderUserService")
@Transactional
public class OrderUserServiceImpl implements OrderUserService {
    public static final String[] platFromList = {"orders75", "orders76"};
    private static Logger log = LogManager.getLogger(OrderUserServiceImpl.class.getName());
    @Autowired(required = false)
    private OrderUserDao orderUserDao;
    @Autowired
    private OrderHongKongService orderHongKongService;



    private static final  SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
    @Override
    public List<OrderUser> findAll() {
        List<OrderUser> orderUserList = orderUserDao.findAll();
        return orderUserList;
    }


    public String addOrderUser(DataSource dataSource) {
        String message = null;
        if (dataSource == null) {
            message = "数据为空";
        } else {
            //开仓
            if (dataSource.getOpenClose() == OrderUserEnum.openOrClose.open.getCode()) {
                message = save(dataSource);
            }
            //平仓
            if (dataSource.getOpenClose() == OrderUserEnum.openOrClose.close.getCode()) {
                message = updateOrAdd(dataSource);
            }
        }
        log.info(message);
        return message;
    }

    public String updateOrAdd(DataSource dataSource) {
        String message = null;
        OrderUser orderUser = findByTicket(dataSource.getTicket());
        Integer longShor; //历史多空
        Double openPrice = 0.0;// 历史开仓价格
        Double difference;
        if (orderUser == null) {
            message = saveClose(dataSource);
            return message;
        } else {
            longShor = orderUser.getLongShort(); //历史多空
            openPrice = orderUser.getOpenPrice();// 历史开仓价格
        }
        //全平
        if (dataSource.getTicket().equals(dataSource.getNewTicket())) {

            orderUser.setCloseTime(dataSource.getCreateTime()); //平仓时间
            orderUser.setCommission(dataSource.getCommission());//手续费
            orderUser.setStopLoss(dataSource.getStopLoss());//止损
            orderUser.setStopProfit(dataSource.getStopProfit());//止盈
            //          orderUser.setHandNumber(dataSource.getHandNumber());
            orderUser.setCommission(dataSource.getCommission());//手续费
            orderUser.setStopLoss(dataSource.getStopLoss());//止损
            orderUser.setStopProfit(dataSource.getStopProfit());//止盈
            orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
            orderUser.setClosePrice(dataSource.getPrice()); //平仓价格
            orderUser.setLongShort(dataSource.getCmd());//开多  ，平 空
            message = update(orderUser);

        }
        //平半
        if (!dataSource.getTicket().equals(dataSource.getNewTicket())) {
            //平开
            if (orderUser.getHandNumber() > dataSource.getHandNumber()) {
                double handNumber = orderUser.getHandNumber();

                double everyHandPrice = DoubleUtil.div(orderUser.getCommission(), orderUser.getHandNumber());//每手价格
                //先平仓
                orderUser.setHandNumber(dataSource.getHandNumber()); // 手数
                orderUser.setCloseTime(dataSource.getCreateTime()); //平仓时间
                orderUser.setLongShort(dataSource.getCmd());//多空
                orderUser.setProfit(dataSource.getProfit()); //平仓盈亏
                orderUser.setClosePrice(dataSource.getPrice());//平仓价格
                orderUser.setUpdateDate(DateUtil.getStringDate());//修改时间
                orderUser.setStopLoss(dataSource.getStopLoss());//止损
                orderUser.setStopProfit(dataSource.getStopProfit());//止盈
                orderUser.setCommission(DoubleUtil.add(DoubleUtil.mul(everyHandPrice, dataSource.getHandNumber()), dataSource.getCommission()));//手续费
                update(orderUser);
                //后开仓
                DataSource newDataSource = new DataSource();
                newDataSource = dataSource;
                difference = DoubleUtil.sub(handNumber, dataSource.getHandNumber()); //计算差值
                newDataSource.setHandNumber(difference);
                newDataSource.setCmd(longShor);
                newDataSource.setPrice(openPrice);
                newDataSource.setCommission(DoubleUtil.mul(difference, everyHandPrice));//手续费

                save(newDataSource);
                message = "平仓成功";
            }
        }
        return message;
    }


    /**
     * 新增
     *
     * @param dataSource tcp 数据包
     * @return String  状态
     */
    public String save(DataSource dataSource) {
        OrderUser orderUser = new OrderUser();
//            orderUser.setCloseTime(dataSource.getCreateTime());//平仓时间&
        orderUser.setTicket(dataSource.getNewTicket());//新开仓单号
        orderUser.setOpenTime(dataSource.getCreateTime());//开仓时间
        orderUser.setUserCode(dataSource.getLogin()); //账号
        orderUser.setAgencyName(dataSource.getAgencyName());//代理人
        orderUser.setPlatFormCode(dataSource.getPlatformName());//平台名称
        orderUser.setProductCode(dataSource.getVarietyCode());//商品
        orderUser.setHandNumber(dataSource.getHandNumber());//手数);
        orderUser.setLongShort(dataSource.getCmd());//多空
        orderUser.setPrice(dataSource.getPrice());//价位
        orderUser.setProfit(dataSource.getProfit());//平仓盈亏
        orderUser.setOpenPrice(dataSource.getPrice());//开仓价
        orderUser.setCommission(dataSource.getCommission());//手续费
        orderUser.setStopLoss(dataSource.getStopLoss());//止损
        orderUser.setStopProfit(dataSource.getStopProfit());//止盈
        orderUser.setCreateDate(DateUtil.getStringDate());//创建时间
        orderUserDao.addOrderUser(orderUser);
        log.info("新增orderUser数据成功");
        return "添加成功";
    }

    /**
     * @param dataSource tcp 数据包
     * @return
     */
    public String saveClose(DataSource dataSource) {
        OrderUser orderUser = new OrderUser();
        orderUser.setCloseTime(dataSource.getCreateTime());//平仓时间&
        orderUser.setTicket(dataSource.getNewTicket());//新开仓单号
        orderUser.setUserCode(dataSource.getLogin()); //账号
        orderUser.setAgencyName(dataSource.getAgencyName());//代理人
        orderUser.setPlatFormCode(dataSource.getPlatformName());//平台名称
        orderUser.setProductCode(dataSource.getVarietyCode());//商品
        orderUser.setHandNumber(dataSource.getHandNumber());//手数);
        orderUser.setLongShort(dataSource.getCmd());//多空
        orderUser.setPrice(dataSource.getPrice());//价位
        orderUser.setProfit(dataSource.getProfit());//平仓盈亏
        orderUser.setCommission(dataSource.getCommission());//手续费
        orderUser.setStopLoss(dataSource.getStopLoss());//止损
        orderUser.setStopProfit(dataSource.getStopProfit());//止盈
        orderUser.setClosePrice(dataSource.getPrice());//平仓价
        orderUser.setCreateDate(DateUtil.getStringDate());//创建时间
        orderUserDao.addOrderUser(orderUser);
        log.info("平仓成功 新开仓单号为：" + orderUser.getTicket());
        return "添加成功";
    }


    /**
     * //通过开仓单号 查询交易信息
     *
     * @param ticket 开仓单号
     * @return
     */
    public OrderUser findByTicket(String ticket) {
        OrderUser orderUser = new OrderUser();
        if (ticket != null && !StringUtils.isEmpty(ticket)) {
            orderUser = orderUserDao.findByTicket(ticket);
        }
        return orderUser;
    }

    /**
     * 平仓
     *
     * @param orderUser 平仓数据对象
     * @return
     */
    public String update(OrderUser orderUser) {
        String message = null;
        if (orderUser != null) {
            orderUser.setUpdateDate(DateUtil.getStringDate());//修改时间
            orderUserDao.update(orderUser);
            message = "平仓成功";
        } else {
            message = "平仓修改失败";
        }

        return message;
    }


    /**
     * 用户列表显示 & 数据统计查询
     *
     * @param orderUserVo
     * @return
     */
    public OrderUserListVo countOrderUser(OrderUserVo orderUserVo) {
                List<OrderUserVo> list = new ArrayList<>();
                List<OrderUser> orderUserlist = orderUserDao.countOrderUser(orderUserVo);
                OrderUserListVo orderUserListVo = new OrderUserListVo();
                double total_Position_gain_and_loss = DoubleUtil.Double_val; //总持仓盈亏
                double total_gain_and_loss = DoubleUtil.Double_val;//客户总盈亏
                double total_commission = DoubleUtil.Double_val; //总手续费
                double total_profit_loss_than = DoubleUtil.Double_val; //总盈亏效率
        for (int i = 0; i < orderUserlist.size(); i++) {
                OrderUser orderUser = orderUserlist.get(i);
                OrderUserVo orderUserVo1 = new OrderUserVo();
                Prices prices = orderHongKongService.getMarketPrice(orderUser.getProductCode()); //价格
                PlatFromUsers platFromUsers = getPlatFromUser(orderUser);
                Agent agent = new Agent();
            if (platFromUsers == null) {
                     agent = null;
             } else {
                     agent = orderHongKongService.getAgent(platFromUsers.getAGENT());//代理人
                 }
                    //平仓盈亏
                    double profit = DoubleUtil.Double_val;
                    //累计盈亏
                    double totalGainAndLoss = DoubleUtil.Double_val;
                    //盈利值（计算胜率）
                    double winRate = DoubleUtil.Double_val; //盈利值
                    //持仓盈亏
                    double position_gain_and_loss = DoubleUtil.Double_val;
                    //盈亏效率
                    double profit_loss_than = DoubleUtil.Double_val;
                    int doOrderNumber = 0; //做单数
                    double handNumber = DoubleUtil.Double_val; //手数

                    Date recentlyTime = null;//最近下单时间
                    boolean  countOrderUser = false;
                for (int o = 1; o < orderUserlist.size(); o++) {
                        OrderUser orderUser1 = orderUserlist.get(o);
                    if (orderUser.getUserCode().equals(orderUser1.getUserCode()) && orderUser.getPlatFormCode().equals(orderUser1.getPlatFormCode())) {
                                if (countOrderUser == false){countOrderUser=true;}
                                total_commission = DoubleUtil.add(total_commission, (orderUser1.getCommission() == null ? DoubleUtil.Double_val : orderUser1.getCommission()));
                                if (orderUser1.getCloseTime() == null && StringUtils.isEmpty(orderUser1.getCloseTime())) {
                                    //統計持倉盈虧
                                    position_gain_and_loss =  countPosition_gain_and_loss(orderUser1 , position_gain_and_loss,prices);
                                }
                                //平仓盈亏
                                if (orderUser1.getCloseTime() != null && !StringUtils.isEmpty(orderUser1.getCloseTime()) && orderUser1.getProfit() != null) {
                                        profit = DoubleUtil.add(profit, (orderUser1.getProfit() == null ? DoubleUtil.Double_val : orderUser1.getProfit()));
                                        doOrderNumber++; //做单数
                                        handNumber = DoubleUtil.add(handNumber, (orderUser1.getHandNumber() == null ? DoubleUtil.Double_val : orderUser1.getHandNumber())); //手数计算
                                    if (orderUser1.getProfit() > 0) {
                                        winRate = DoubleUtil.add(winRate, (orderUser1.getProfit() == null ? DoubleUtil.Double_val : orderUser1.getProfit())); //盈利
                                    }
                                }
                        //计算最近下单时间
                        recentlyTime = countRecentlyTime(recentlyTime ,orderUser1);

                    }
                    //当只有一条数据是 以当前数据为准
                    if (countOrderUser == false){
                          countOrderUser=true;
                            try {
                                recentlyTime = sdf.parse(orderUser1.getCreateDate());
                                } catch (ParseException e) {
                                       e.printStackTrace();
                                    }
                       }

                }

                if (doOrderNumber != DoubleUtil.Double_val) {
                    orderUserVo1.setWinRate(DoubleUtil.div(winRate, doOrderNumber, 1));//胜率
                } else {
                    orderUserVo1.setWinRate(DoubleUtil.Double_val);//胜率
                }

                if (agent == null) {
                    orderUserVo1.setAgencyName("-");//代理人
                } else {
                    orderUserVo1.setAgencyName(agent.getAgentname());// 代理人
                }
                orderUserVo1.setPlatFormCode(orderUser.getPlatFormCode()); //平台名称
                if (platFromUsers == null) {
                    orderUserVo1.setUserName("-");//用户名称
                } else {
                    orderUserVo1.setUserName(platFromUsers.getNAME());//用户名称
                }


                orderUserVo1.setRecentlyTime(DateUtil.longToStrDate(recentlyTime.getTime()));//最近下单时间

                orderUserVo1.setUserCode(orderUser.getUserCode()); //客户
                orderUserVo1.setHandNumber(handNumber);//持仓手数
                orderUserVo1.setPosition_gain_and_loss(position_gain_and_loss);//持仓盈亏
                orderUserVo1.setOffset_gain_and_loss(profit);//平仓盈亏

                if (profit != DoubleUtil.Double_val) {
                    profit_loss_than = DoubleUtil.div(profit, handNumber, 1);
                } else {
                    profit_loss_than = DoubleUtil.Double_val;
                }

                if (Double.isNaN(profit_loss_than)) {
                    profit_loss_than = 0.0;
                }
                orderUserVo1.setProfit_loss_than(profit_loss_than);  //盈亏效率
                orderUserVo1.setDoOrderNumber(doOrderNumber);//做单数
                orderUserVo1.setTotalGainAndLoss(DoubleUtil.add(position_gain_and_loss, profit));//累计盈亏
                total_Position_gain_and_loss = DoubleUtil.add(total_Position_gain_and_loss, position_gain_and_loss); //总持仓盈亏
                total_gain_and_loss = DoubleUtil.add(DoubleUtil.add(profit, position_gain_and_loss), total_gain_and_loss); //客户总盈亏
                total_profit_loss_than = DoubleUtil.add(total_profit_loss_than, profit_loss_than); //总盈亏率
                list.add(orderUserVo1);
        }

                orderUserListVo.setTotal_gain_and_loss(total_gain_and_loss);
                orderUserListVo.setTotal_Position_gain_and_loss(total_Position_gain_and_loss);
                orderUserListVo.setTotal_commission(total_commission);
                orderUserListVo.setTotal_profit_loss_than(total_profit_loss_than);
                removeDuplicateWithOrder(list);
                orderUserListVo.setListVo(list);
                return orderUserListVo;
    }

    /**
     * 条件查询
     *
     * @param list              用户id集合
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param productCode       产品代码
     * @param openOrCloseStatus
     * @return
     */
    @Override
    public List<OrderUser> findByUserIdList( Map<String, String> map, String startTime, String endTime, String productCode, Integer openOrCloseStatus) {

        List<OrderUser> orderUserList = new ArrayList<>();
        if (map.size() > 0 && startTime != null && !StringUtils.isEmpty(startTime) && productCode != null && !StringUtils.isEmpty(productCode)) {
            orderUserList = orderUserDao.findByUserIdList(map, startTime, endTime, productCode, openOrCloseStatus);
        }
        return orderUserList;
    }

    /**
     * 用户明细
     *
     * @param userCode    用户代码
     * @param productCode 产品代码
     * @return
     */
    @Override
    public OrderUserDetailsVo getUserDetails(String userCode, String productCode,String platFormCode) {
        OrderUserDetailsVo detailsVo = new OrderUserDetailsVo();
        if (userCode != null && !StringUtils.isEmpty(userCode) && productCode != null && !StringUtils.isEmpty(productCode)) {
            int index = 0;
            int endIndex = 10;
            List<OrderUser> orderUserList = orderUserDao.getUserDetails(userCode, productCode,platFormCode);
            Prices prices = orderHongKongService.getMarketPrice(productCode); //价格
            LinkedHashSet<String> set = new LinkedHashSet<String>(orderUserList.size());
            List<HoldOrderUserVo> holdList = new ArrayList<>(); //持仓数据
            List<OrderUser> profitList = new ArrayList<>();//平仓数据
            double totalHandNumber = DoubleUtil.Double_val;//持仓数
            double profit = DoubleUtil.Double_val; //平仓盈亏
            double commission = DoubleUtil.Double_val; //手续费
            long countNumber = orderUserList.size(); //做单数
            double handNumber = DoubleUtil.Double_val; // 平仓手数
            long countLoss = 0; //亏损次数
            long countProfit = 0; //盈利次数
            double profitVal = DoubleUtil.Double_val; //盈利值
            double lossVal = DoubleUtil.Double_val;//亏损值
            double position_gain_and_loss = DoubleUtil.Double_val;//持仓盈亏
            for (OrderUser orderUser : orderUserList) {
                commission = DoubleUtil.add(commission, orderUser.getCommission());//手续费
                totalHandNumber = DoubleUtil.add(totalHandNumber, orderUser.getHandNumber());//累计持仓数
                if (orderUser.getCloseTime() != null && !StringUtils.isEmpty(orderUser.getCloseTime())) {
                    profit = DoubleUtil.add(profit, (orderUser.getProfit() == null ? DoubleUtil.Double_val : orderUser.getProfit())); //平仓盈亏
                    profitList.add(orderUser); //构造平仓List
                    //亏损
                    if( orderUser.getProfit()<0 && orderUser.getProfit() !=0 ){
                        countLoss++;
                        lossVal = DoubleUtil.add(orderUser.getProfit(),lossVal );
                    }
                    //盈利
                    if ( orderUser.getProfit() > 0 && orderUser.getProfit() !=0 ){
                        countProfit++;
                        profitVal = DoubleUtil.add(profitVal,orderUser.getProfit());
                    }
                }

                if (orderUser.getCloseTime() == null || StringUtils.isEmpty(orderUser.getCloseTime())) {
                    if (prices != null) {
                        position_gain_and_loss = countPosition_gain_and_loss( orderUser ,position_gain_and_loss,prices );
                    }
                    holdList.add(addHandListDetail(orderUser));//构造持仓list
                    handNumber = DoubleUtil.add(handNumber, (orderUser.getHandNumber() == null ? DoubleUtil.Double_val : orderUser.getHandNumber())); //手数计算
                }
                if (orderUser.getCreateDate() != null && !StringUtils.isEmpty(orderUser.getCreateDate())) {
                    set.add(orderUser.getCreateDate().substring(index, endIndex).trim());//计算做单天数
                }
            }
            OrderUser orderUser = new OrderUser();
            orderUser.setUserCode(userCode);
            orderUser.setPlatFormCode(platFormCode);
            PlatFromUsers platFromUsers = getPlatFromUser(orderUser);

            Agent agent = new Agent();
            if (platFromUsers == null) {
                detailsVo.setLoginTime("-");//注册时间
                detailsVo.setInMoney(DoubleUtil.Double_val);//入金
                detailsVo.setOutMoney(DoubleUtil.Double_val);//出金
                detailsVo.setRemainMoney(DoubleUtil.Double_val);//BALANCE 余额
                agent = null;

            } else {

                if(platFromUsers.getWITHDRAWAL() == null ){
                    platFromUsers.setWITHDRAWAL(DoubleUtil.Double_val);
                }
                if(platFromUsers.getDEPOSIT() == null ){
                    platFromUsers.setDEPOSIT(DoubleUtil.Double_val);

                }
                BigDecimal DEPOSIT =   new BigDecimal(platFromUsers.getDEPOSIT());;//入金
                BigDecimal WITHDRAWAL =   new BigDecimal(platFromUsers.getWITHDRAWAL());//出金
                detailsVo.setLoginTime(platFromUsers.getREGDATE());//注册时间
                detailsVo.setInMoney(platFromUsers.getDEPOSIT() == null ? DoubleUtil.Double_val : DEPOSIT.setScale(1, BigDecimal.ROUND_DOWN).doubleValue());//入金platFromUsers.getDEPOSIT()
                detailsVo.setOutMoney((platFromUsers.getWITHDRAWAL() == null ? DoubleUtil.Double_val : WITHDRAWAL.setScale(1, BigDecimal.ROUND_DOWN).doubleValue()));//出金 platFromUsers.getWITHDRAWAL()
                detailsVo.setRemainMoney((platFromUsers.getBALANCE() == null ? DoubleUtil.Double_val : platFromUsers.getBALANCE()));//BALANCE 余额
                agent = orderHongKongService.getAgent(platFromUsers.getAGENT());//代理人
            }
            //构造vo类
            detailsVo.setDoOrderDays(set.size()); //做单天数
            detailsVo.setCountNumber(countNumber);//做单数
            detailsVo.setHoldList(holdList);//持仓数据
            detailsVo.setProfitList(profitList);//平仓数据
            detailsVo.setPosition_gain_and_loss(position_gain_and_loss);//持仓盈亏
            detailsVo.setPlatFromCode(platFormCode);//平台
            detailsVo.setCmmission(commission);//手续费
            detailsVo.setLossNumber(countLoss);//亏损次数
            detailsVo.setProfitNumber(countProfit);//盈利次数
            if(countNumber != DoubleUtil.Double_val){
                detailsVo.setWinRate(Double.valueOf(countProfit/countNumber));//胜率
            }else {
                detailsVo.setWinRate(DoubleUtil.Double_val);//胜率
            }

            detailsVo.setHandNumber(handNumber);
            detailsVo.setLossVal(lossVal); //亏损值
            detailsVo.setProfitVal(profitVal);//盈利值
            //胜率
            detailsVo.setOffset_gain_and_loss(profit);//平仓盈亏
            if(lossVal != DoubleUtil.Double_val){
                detailsVo.setProfitAndLossRatio(DoubleUtil.div(profitVal ,lossVal,1)); // 盈亏比
            }else {
                detailsVo.setProfitAndLossRatio(DoubleUtil.Double_val);
            }
            if( handNumber != DoubleUtil.Double_val){
                detailsVo.setProfitAndLossEfficiency(DoubleUtil.div(profit,handNumber,1)); //盈亏效率
            }else {
                detailsVo.setProfitAndLossEfficiency(DoubleUtil.Double_val); //盈亏效率
            }

            if (agent == null) {
                detailsVo.setAgencyName("-");//代理人
            } else {
                detailsVo.setAgencyName(agent.getAgentname());//代理人
            }


        }

        return detailsVo;
    }



    /**
     * 获取不同用户信息
     *
     * @param orderUser
     * @return
     */

    public PlatFromUsers getPlatFromUser(OrderUser orderUser) {
            PlatFromUsers platFromUsers = new PlatFromUsers();
        if (orderUser.getPlatFormCode().equals(platFromList[0])) {
            platFromUsers = orderHongKongService.getUser75(orderUser.getUserCode());
        }
        if (orderUser.getPlatFormCode().equals(platFromList[1])) {
            platFromUsers = orderHongKongService.getUser76(orderUser.getUserCode());
        }
        return platFromUsers;
    }


    /**
     * 获取用户在仓盈亏以及盈亏效率
     */
    public OrderUserDetailsVo getOrderUserCount(String userCode, String productCode,String platFormCode) {
            OrderUserDetailsVo detailsVo = new OrderUserDetailsVo();
            Prices prices = orderHongKongService.getMarketPrice(productCode); //价格
            double handNumber = DoubleUtil.Double_val; //手数
            int doOrderNumber = 0; //做单数
        if (userCode != null && !StringUtils.isEmpty(userCode) && productCode != null && !StringUtils.isEmpty(productCode)) {
            List<OrderUser> orderUserList = orderUserDao.getUserDetails(userCode, productCode,platFormCode);
            double position_gain_and_loss = DoubleUtil.Double_val;//持仓盈亏
            double profit = DoubleUtil.Double_val; //平仓盈亏
            for (OrderUser orderUser : orderUserList) {
                //平仓
                if (orderUser.getCloseTime() != null && !StringUtils.isEmpty(orderUser.getCloseTime()) && orderUser.getProfit() != null) {
                    profit = DoubleUtil.add(profit, (orderUser.getProfit() == null ? DoubleUtil.Double_val : orderUser.getProfit()));//平仓盈亏
                    doOrderNumber++; //做单数
                    handNumber = DoubleUtil.add(handNumber, (orderUser.getHandNumber() == null ? DoubleUtil.Double_val : orderUser.getHandNumber())); //手数计算
                }
                //持仓
                if (orderUser.getCloseTime() == null || StringUtils.isEmpty(orderUser.getCloseTime())) {
                    position_gain_and_loss= countPosition_gain_and_loss(orderUser,position_gain_and_loss,prices);

                }
            }
            if (doOrderNumber != DoubleUtil.Double_val) {
                detailsVo.setEveryHandNumber(DoubleUtil.div(handNumber, doOrderNumber, 1));//每单手数学
            } else {
                detailsVo.setEveryHandNumber(DoubleUtil.Double_val);
            }

            detailsVo.setPosition_gain_and_loss(profit);//平仓盈亏
            detailsVo.setOffset_gain_and_loss(position_gain_and_loss);//持仓盈亏
        }

        return detailsVo;
    }

    public HoldOrderUserVo addHandListDetail(OrderUser orderUser) {
            HoldOrderUserVo holdOrderUserVo = new HoldOrderUserVo();
            holdOrderUserVo.setHandNumber(orderUser.getHandNumber());//手数
            holdOrderUserVo.setPlatFormCode(orderUser.getPlatFormCode()); //平台
            holdOrderUserVo.setProductCode(orderUser.getProductCode());//品种代码
            holdOrderUserVo.setHandNumber(orderUser.getHandNumber());//手数
            holdOrderUserVo.setOpenPrice(orderUser.getOpenPrice());//开仓价
            holdOrderUserVo.setStopProfit(orderUser.getStopProfit());//止盈
            holdOrderUserVo.setStopLoss(orderUser.getStopLoss());//止损
            holdOrderUserVo.setCreateDate(orderUser.getCreateDate());
            holdOrderUserVo.setCommission(orderUser.getCommission());//手续费
            holdOrderUserVo.setOpenPrice(orderUser.getOpenPrice());
            Prices prices = orderHongKongService.getMarketPrice(orderUser.getProductCode()); //价格
        //持仓盈亏
        if (orderUser.getCloseTime() == null || StringUtils.isEmpty(orderUser.getCloseTime())) {
            if (prices != null) {
                //多
                if (orderUser.getLongShort() == 0) {
                    holdOrderUserVo.setMarketPrice(prices.getAsk());
                    //（卖出价（Hk）  - 买入（orderUser）） * 持仓数（orderUser）
                    holdOrderUserVo.setGain_and_loss(DoubleUtil.mul(DoubleUtil.sub(prices.getAsk(), orderUser.getOpenPrice()), orderUser.getHandNumber()));
                }
                //空
                //（开仓价（orderUser）— 买入（HK））* 持仓数 （orderUser）
                if (orderUser.getLongShort() == 1) {
                    holdOrderUserVo.setMarketPrice(prices.getBid());
                    holdOrderUserVo.setGain_and_loss(DoubleUtil.mul(DoubleUtil.sub(orderUser.getOpenPrice(), prices.getBid()), orderUser.getHandNumber()));
                }
            }
        }
        return holdOrderUserVo;
    }

// 删除ArrayList中重复元素，保持顺序
    public static List<OrderUserVo>  removeDuplicateWithOrder(List<OrderUserVo> list) {
              Map<String, OrderUserVo> map = new HashMap<String, OrderUserVo>();
        for (OrderUserVo orderUserVo : list){
              map.put(orderUserVo.getUserCode(),orderUserVo);
        }
              list.clear();
         for (OrderUserVo orderUserVo2: map.values()){
                list.add(orderUserVo2);
         }
             return list;
    }

    /**
     * 计算最近下单时间
     * @param recentlyTime 时间Data 类型
     * @param orderUser1 orderUser对象
     * @return
     */
    public  Date countRecentlyTime (Date recentlyTime  ,OrderUser orderUser1 ){
        //计算最近下单时间
        if (recentlyTime == null) {
            try {
                recentlyTime = sdf.parse(orderUser1.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (recentlyTime.getTime() < sdf.parse(orderUser1.getCreateDate()).getTime()) {
                    recentlyTime = sdf.parse(orderUser1.getCreateDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("recentlyTime:"+DateUtil.longToStrDate(recentlyTime.getTime()));
                 return  recentlyTime;
    }

    /**
     * 计算持仓盈亏
     * @return
     */
    public double countPosition_gain_and_loss(OrderUser orderUser ,Double position_gain_and_loss,Prices prices ){
        //多
        if (orderUser.getLongShort() == 0) {
            //（卖出价（Hk）  - 买入（orderUser）） * 持仓数（orderUser）
            position_gain_and_loss = DoubleUtil.add(position_gain_and_loss, DoubleUtil.mul(DoubleUtil.sub(prices.getAsk(), orderUser.getOpenPrice()), orderUser.getHandNumber()));
        }
        //空
        //（开仓价（orderUser）— 买入（HK））* 持仓数 （orderUser）
        if (orderUser.getLongShort() == 1) {
            position_gain_and_loss =
                    DoubleUtil.add(position_gain_and_loss, DoubleUtil.mul(DoubleUtil.sub(orderUser.getOpenPrice(), prices.getBid()), orderUser.getHandNumber()));
        }
             return position_gain_and_loss;
    }
}
