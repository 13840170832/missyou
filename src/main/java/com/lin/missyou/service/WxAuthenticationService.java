package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxAuthenticationService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;

    public String code2Session(String code){
        String url = MessageFormat.format(code2SessionUrl,this.appid,this.appsecret,code);
        RestTemplate rest = new RestTemplate();
        Map<String,String> session = new HashMap<>();
        String sessionText = rest.getForObject(url,String.class);
        try {
            session = mapper.readValue(sessionText,HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.registerUser(session);
    }

    private String registerUser(Map<String,String> session){
        String openid = (String)session.get("openid");
        if(null == openid){
            throw new ParameterException(20004);
        }
        Optional<User> userOptional = userRepository.findByOpenid(openid);
//        userOptional.ifPresentOrElse(Consummer, Runable)  JAVA9
        if(userOptional.isPresent()){
            //TODO 返回JWT令牌
            return JwtToken.makeToken(userOptional.get().getId());
        }
        User user = User.builder()
                .openid(openid)
                .build();
        userRepository.save(user);
        //TODO 返回JWT令牌
        return JwtToken.makeToken(user.getId());
    }
}
