package com.example.statshandler;



import com.example.models.kafka.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatsListener {

    private KafkaTemplate<String, KafkaMessage<String>> template;

    private int messageCome = 2;

    @Autowired
    public void setTemplate(KafkaTemplate<String, KafkaMessage<String>> template) {
        this.template = template;
    }

//    @KafkaListener(id = "myId1", topics = "stats") @SendTo()
//    public void listenVisitStats(String in) {
//        if (null != in && !in.isEmpty()) {
//            if(messageCome%2!=0){
//                return;
//            }
//            System.out.println("Data from domain project" + in);
//            try {
//                JSONParser parser = new JSONParser();
//                JSONObject jsonObject = (JSONObject) parser.parse(in);
////                System.out.println("visitStats : " + jsonObject.get("visitStats"));
////                System.out.println("productDTOList : " + jsonObject.get("productDTOList"));
//
////                List<VisitStats> visitStats = jsonObject.get("visitStats");
////                List<ProductDTO> productDTOList = jsonObject.get("productDTOList");
//                VisitStats visitStats = new VisitStats();
//                visitStats.setId(56L);
//                String serializedUser = JSONValue.toJSONString(visitStats);
//                System.out.println( "serializedUser" + serializedUser);
//                template.send("stats", serializedUser );
//                System.out.println("Send data to domain project");
//            } catch (ParseException exception) {
//                System.out.println(in);
//                System.out.println(exception);
//            }
//            messageCome++;
//        }
//    }

}
