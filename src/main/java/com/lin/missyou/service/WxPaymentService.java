package com.lin.missyou.service;

import com.github.wxpay.sdk.LinWxPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.exception.http.ServerErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.HttpRequestProxy;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxPaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;


    @Value("${missyou.order.pay-callback-host}")
    private String payCallbackHost;

    @Value("${missyou.order.pay-callback-path}")
    private String payCallbackPath;

    private static LinWxPayConfig linWxPayConfig = new LinWxPayConfig();

    public Map<String,String> preOrder(Long oid){
        Long uid = LocalUser.getUser().getId();
        Optional<Order> orderOptional = orderRepository.findFirstByUserIdAndId(uid,oid);
        Order order = orderOptional.orElseThrow(()-> new NotFoundException(50009));
        if(order.needCancel()){
            throw new ForbiddenException(50010);
        }
        WXPay wxPay = this.assembleWxPayConfig();
        Map<String,String> preOrderParams = this.makePreOrderParams(order.getFinalTotalPrice(),order.getOrderNo());
        Map<String,String> wxOrder;
        try {
            wxOrder = wxPay.unifiedOrder(preOrderParams);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        if(this.unifiedOrderSuccess(wxOrder)){
            this.orderService.updateOrderPrepayId(order.getId(),wxOrder.get("prepay_id"));
        }
        return this.makePaySignature(wxOrder);
    }

    private Map<String,String> makePaySignature(Map<String,String> wxOrder){
        Map<String,String> wxPayMap = new HashMap<>();
        String packages = "prepay_id" + wxOrder.get("prepay_id");

        wxPayMap.put("appId", WxPaymentService.linWxPayConfig.getAppID());

        wxPayMap.put("timeStamp", CommonUtil.timestamp10());
        wxPayMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        wxPayMap.put("package", packages);
        wxPayMap.put("signType", "HMAC-SHA256");

        String sign ;
        try {
            sign = WXPayUtil.generateSignature(wxPayMap,WxPaymentService.linWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        Map<String,String> miniPayParams = new HashMap<>();

        miniPayParams.put("paySign",sign);
        miniPayParams.putAll(wxPayMap);
        miniPayParams.remove("appId");
        return miniPayParams;
    }

    private boolean unifiedOrderSuccess(Map<String,String> wxOrder){
        if(!wxOrder.get("return_code").equals("SUCCESS")
                || !wxOrder.get("result_code").equals("SUCCESS")){
            throw new ParameterException(10007);
        }
        return true;
    }

    private Map<String,String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNo){
        String payCallbackUrl = this.payCallbackHost + this.payCallbackPath;
        Map<String,String> data = new HashMap<>();
        data.put("body","Sleeve");
        data.put("out_trade_no",orderNo);
        data.put("device_info","Sleeve");
        data.put("fee_type","CNY");
        data.put("trade_type","JSAPI");
        data.put("total_fee", CommonUtil.yuanToFenPlainString(serverFinalPrice));
        data.put("openid",LocalUser.getUser().getOpenid());
        data.put("spbill_create_ip", HttpRequestProxy.getRemoteRealIp());
        data.put("notify_url",payCallbackUrl);
        return data;
    }

    public WXPay assembleWxPayConfig(){
        WXPay wxPay ;
        try {
            wxPay = new WXPay(WxPaymentService.linWxPayConfig);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        return wxPay;
    }


}
