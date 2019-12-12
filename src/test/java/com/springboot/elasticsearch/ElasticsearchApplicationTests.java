package com.springboot.elasticsearch;

import com.springboot.elasticsearch.bean.Article;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@RunWith(SpringRunner.class)
@SpringBootTest
 public  class ElasticsearchApplicationTests {
    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() {
        Article article = new Article();
        article.setId(1);
        article.setTitle("好消息");
        article.setAuthor("zhangsan");
        article.setContent("Hello World");
        Index index = new Index.Builder(article).index("ballshit").type("dickman").build();
        try {
            //执行
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
