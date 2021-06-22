package com.example.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExredisApplication.class)
public class SpringBootRedisApplicationTests {

    // String
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    // Hashes
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    // Hashes
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    // Set
    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    // Sorted Set
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Before
    public void init() {

        // list push
        // push는 leftPush, rightPush 두 종류.
        // Stack이나 Queue로 활용 가능.
        listOperations.rightPush("test:task", "자기소개");
        listOperations.rightPush("test:task", "취미소개");
        listOperations.rightPush("test:task", "소망소개");
        listOperations.rightPush("test:task", "선임이직");

        // hash put
        // Map 자료형과 사용이 거의 비슷
        // key-field-value를 작성하여 데이터를 삽입
        // 같은 key에 여러 value를 작성할 수 있다.
        hashOperations.put("test:user:joara", "name", "조아라");
        hashOperations.put("test:user:joara", "age", "30");

        // set put
        // value 안의 값들을 member라고 함.
        // 그래서 members를 통해 해당 key의 값들을 Set자료형으로 가져올 수 있음.
        setOperations.add("test:user:joara:hobby", "개발");
        setOperations.add("test:user:joara:hobby", "사진찍기");
        setOperations.add("test:user:joara:hobby", "음악감상");

        // zset
        // value에 score를 포함하여 데이터를 삽입.
        // range를 통해 정해진 rank 범위의 데이터를 가져올 수 있음.
        // score를 기준으로 데이터를 오름차순으로 정렬.
        zSetOperations.add("test:user:joara:wish", "부자가 되길", 1);
        zSetOperations.add("test:user:joara:wish", "건강하길", 2);
        zSetOperations.add("test:user:joara:wish", "행복하길", 3);
        zSetOperations.add("test:user:joara:wish", "웃을 일이 많길", 4);

    }

    @Test
    public void redisTest1() {
        String task = listOperations.leftPop("test:task"); // 큐처럼 사용
        StringBuilder stringBuilder = new StringBuilder();

        while (task != null) {
            switch (task) {
                case "자기소개":
                    Map<String, String> intro = hashOperations.entries("test:user:joara");
                    stringBuilder.append("\n******자기소개******");
                    stringBuilder.append("\n이름은 ");
                    stringBuilder.append(intro.get("name"));
                    stringBuilder.append("\n나이는 ");
                    stringBuilder.append(intro.get("age"));
                    break;
                case "취미소개":
                    Set<String> hobbys = setOperations.members("test:user:joara:hobby");
                    stringBuilder.append("\n******취미소개******");
                    stringBuilder.append("\n취미는 ");

                    for (String hobby : hobbys) {
                        stringBuilder.append("\n");
                        stringBuilder.append(hobby);
                    }

                    break;
                case "소망소개":
                    Set<String> wishes = zSetOperations.range("test:user:joara:wish", 0, 2);
                    stringBuilder.append("\n******소망소개******");

                    int rank = 1;

                    for (String wish : wishes) {
                        stringBuilder.append("\n");
                        stringBuilder.append(rank);
                        stringBuilder.append("등 ");
                        stringBuilder.append(wish);
                        rank++;
                    }
                    break;
                case "선임이직":
                    stringBuilder.append("\n!!! 믿었던 선임 이직");
                    // 선임이직이라는 이벤트 발생시,
                    // 경력직 채용의 score를 -1하여 rank를 높임.
                    zSetOperations.incrementScore("test:user:joara:wish", "경력직 채용", -1);
                    listOperations.rightPush("test:task", "소망소개");
                    break;
                default:
                    stringBuilder.append("nonone");
                    
            }

            task = listOperations.leftPop("test:task");

        }

        System.out.println(stringBuilder.toString());

    }

}
