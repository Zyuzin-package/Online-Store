package com.example.diplom.serviceHandler;


import com.example.diplom.domain.statistics.VisitStats;
import com.example.diplom.dto.ProductDTO;
import com.example.models.domain.Product;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


public class MessageHandler {

    private KafkaTemplate<String, String> template;

    public void setTemplate(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public boolean sendDataToStatsMicroService(List<VisitStats> visitStats,List<ProductDTO> productDTOList){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("visitStats",visitStats);
        jsonObject.put("productDTOList",productDTOList);

        String serializedData = JSONValue.toJSONString(jsonObject);
        System.out.println("Send data: " + serializedData);
        template.send("stats", serializedData);

        return true;
    }
    /*TODO:
        1) Correct data in json (toString at productDTO, VisitStats (domain) )
        2) On Micro Service side need:
            * realise logic from collectStats() from visitStatsServiceImpl
            * realise creating answer with changes data
        3) Put domain & dto models into library +
        4) create listener that must be wait into collectStats() while mirco-service send answer
    */
/*
{"productDTOList":[ProductDTO{id=11, title='Стейк ТОМАГАВК', price=450.0, image='/img/1.jpg', description=' Стейк Томагавк — это Рибай, подающийся на длинном, зачищенном ребре.'},ProductDTO{id=12, title='Стейк КОВБОЙ', price=550.0, image='/img/2.jpg', description='Стейк ковбой — это рибай с оставленной на нем короткой реберной костью.
        '},ProductDTO{id=13, title='Стейк T-Bone', price=600.0, image='/img/3.jpg', description=' Стейк Ти-бон — большой кусок мяса с Т-образной косточкой,
        ,нает сужаться.'},ProductDTO{id=14, title='Стейк ДЕНВЕР', price=412.0, image='/img/4.jpg', description='   Денвер — это внутренняя часть лопатки мраморного бычка.
       '},ProductDTO{id=15, title='Стейк ПИКАНЬЯ', price=780.0, image='/img/5.jpg', description='Пиканья-стейк — мягкое мясо из верхней части заднего отруба с узнаваемой текстурой,
       '},ProductDTO{id=16, title='Стейк ФЛАНК', price=328.0, image='/img/6.jpg', description=' Фланк стейк относится к группе альтернативных стейков.
        '},ProductDTO{id=17, title='Стейк ЧАК АЙ РОЛЛ', price=981.0, image='/img/7.jpg', description='Стейк Чак Ай Ролл – это альтернативный стейк из шейного отруба молодого бычка породы
        '}],"visitStats":[com.example.diplom.domain.statistics.VisitStats@15540aa,com.example.diplom.domain.statistics.VisitStats@5e5918ff,com.example.diplom.domain.statistics.VisitStats@7e24780d,com.example.diplom.domain.statistics.VisitStats@6c151adb,com.example.diplom.domain.statistics.VisitStats@47ee57e3,com.example.diplom.domain.statistics.VisitStats@51e63101,com.example.diplom.domain.statistics.VisitStats@cdfcf20,com.example.diplom.domain.statistics.VisitStats@48d8dfa2,com.example.diplom.domain.statistics.VisitStats@7d3cc815,com.example.diplom.domain.statistics.VisitStats@1d32808a,com.example.diplom.domain.statistics.VisitStats@6fe6fac3,com.example.diplom.domain.statistics.VisitStats@614d0d52,com.example.diplom.domain.statistics.VisitStats@7a2a1d0e,com.example.diplom.domain.statistics.VisitStats@7315f411,com.example.diplom.domain.statistics.VisitStats@2bfd2852,com.example.diplom.domain.statistics.VisitStats@22d387e2,com.example.diplom.domain.statistics.VisitStats@83b3cdb,com.example.diplom.domain.statistics.VisitStats@5452e4b1,com.example.diplom.domain.statistics.VisitStats@16afe9d1,com.example.diplom.domain.statistics.VisitStats@287d9071,com.example.diplom.domain.statistics.VisitStats@79f1ac41,com.example.diplom.domain.statistics.VisitStats@3a45b8a2,com.example.diplom.domain.statistics.VisitStats@53cd2f5a,com.example.diplom.domain.statistics.VisitStats@17f7d34a,com.example.diplom.domain.statistics.VisitStats@6390e651,com.example.diplom.domain.statistics.VisitStats@7487d229,com.example.diplom.domain.statistics.VisitStats@4e8a941c,com.example.diplom.domain.statistics.VisitStats@674e6304,com.example.diplom.domain.statistics.VisitStats@388a3d2,com.example.diplom.domain.statistics.VisitStats@49d42f10,com.example.diplom.domain.statistics.VisitStats@53647842,com.example.diplom.domain.statistics.VisitStats@47267769,com.example.diplom.domain.statistics.VisitStats@1f6ca6c,com.example.diplom.domain.statistics.VisitStats@69b2d9ed,com.example.diplom.domain.statistics.VisitStats@19e41af1,com.example.diplom.domain.statistics.VisitStats@6d8dab9b,com.example.diplom.domain.statistics.VisitStats@5a1fa9ec,com.example.diplom.domain.statistics.VisitStats@259d6ef3,com.example.diplom.domain.statistics.VisitStats@1c17db23,com.example.diplom.domain.statistics.VisitStats@1cc67a49,com.example.diplom.domain.statistics.VisitStats@3b31e0f4,com.example.diplom.domain.statistics.VisitStats@7595eb97,com.example.diplom.domain.statistics.VisitStats@57f9f8c5,com.example.diplom.domain.statistics.VisitStats@78c2d441,com.example.diplom.domain.statistics.VisitStats@4cc0aded]}
 */
}
